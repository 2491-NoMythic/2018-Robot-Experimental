package com._2491nomythic.tempest.commands.drivetrain;

import com._2491nomythic.tempest.commands._CommandBase;
import com._2491nomythic.tempest.commands.autonomous.Automatic.*;
import com._2491nomythic.tempest.settings.Constants;
import com._2491nomythic.tempest.settings.Variables;

/**
 * A command for streaming a selected TalonSRX Velocity mode path
 */
public class DrivePath extends _CommandBase {
	private int mCurrentStep, mTimeCounter, directionModifer, headingModifier, mLength, stepCounter, leftIndex, rightIndex;
	private EndPosition path;
	private double mInitialHeading, mHeadingDiffrence, mTurnAdjustment, mAdjustedLeftVelocity, mAdjustedRightVelocity;
	private String mSelectedPath;
	private boolean reverse;
	/**
	 * 
	 * @param startPosition an robot {@linkplain StartPosition} with respect to the field  
	 * @param endPosition a robot {@linkplain EndPosition} with respect to the field
	 * @author Emilio Lobo
	 */
    public DrivePath(StartPosition startPosition, EndPosition endPosition, int stepCounter, boolean reverse) {
    	
    	requires(drivetrain);
    	
		this.stepCounter = stepCounter;
		this.reverse = reverse;
		this.headingModifier = startPosition.getHeadingModifier();
		this.directionModifer = startPosition.getDirectionModifer();
		this.leftIndex = startPosition.getLeftIndex();
		this.rightIndex = startPosition.getRightIndex();
		this.path = endPosition;	
    }

    protected void initialize() {
    	Variables.isPathRunning = true;
    	resetVariables();
    }

    protected void execute() {
    	if(reverse) {
    		if(mTimeCounter == stepCounter) {
        		
    			adjustVelocities();
    		
    			drivetrain.driveVelocity(mAdjustedLeftVelocity , mAdjustedRightVelocity);
			        		
				mTimeCounter = 0;
				mCurrentStep++;
				mCurrentStep = mLength - mCurrentStep;
			
			} 
			else {
				mTimeCounter++;
			}
    		
    	} else {
    		
    		if(mTimeCounter == stepCounter) {
    		
    			adjustVelocities();
    		
    			drivetrain.driveVelocity(mAdjustedLeftVelocity , mAdjustedRightVelocity);
			        		
				mTimeCounter = 0;
				mCurrentStep++;
			
			} 
			else {
				mTimeCounter++;
			}
    	}
    }

    protected boolean isFinished() {
        return mCurrentStep == mLength;
    }

    protected void end() {
    	drivetrain.stop();
    	Variables.isPathRunning = false;
    }

    protected void interrupted() {
    	end();
    }
    
    /**
     * Adjusts the paths velocities at every step to account for drivetrain scrub. 
     * <p>
     * The heading difference in-between the path and the gyroscope is used to increase or decrease the speed of each drive rail proportionally
     */
    private synchronized void adjustVelocities() {
    	mHeadingDiffrence = headingModifier * path.headingStep(mCurrentStep) + drivetrain.getRawGyroAngle() - mInitialHeading;
		mTurnAdjustment = Constants.kVelocitykG * Constants.kVeloctiyUnitConversion * mHeadingDiffrence; 
		
		mAdjustedLeftVelocity = directionModifer * path.velocityStep(mCurrentStep, leftIndex) - mTurnAdjustment;
		mAdjustedRightVelocity = directionModifer * path.velocityStep(mCurrentStep, rightIndex) + mTurnAdjustment;
    }
    
    public int getCurrentStep() {
    	return mCurrentStep;
    }
    
    private synchronized void resetVariables() {
    	mInitialHeading = drivetrain.getRawGyroAngle();
    	mCurrentStep = 0;
		mTimeCounter = 4;
		mTimeCounter = 0;
		mLength = path.pathLength();
    }
}
