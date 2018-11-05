package com._2491nomythic.tempest.commands.drivetrain;

import com._2491nomythic.tempest.commands._CommandBase;
import com._2491nomythic.tempest.commands.autonomous.Automatic.*;
import com._2491nomythic.tempest.settings.Constants;
import com._2491nomythic.tempest.settings.Variables;

/**
 * A command for streaming a selected TalonSRX Velocity mode path
 */
public class DrivePath extends _CommandBase {
	private int mCurrentStep, directionModifer, headingModifier, mLength, leftIndex, rightIndex;
	private EndPosition path;
	private double mInitialHeading;
    private double mAdjustedLeftVelocity;
    private double mAdjustedRightVelocity;
	private boolean reverse;
	/**
	 * 
	 * @param startPosition an robot {@linkplain StartPosition} with respect to the field  
	 * @param endPosition a robot {@linkplain EndPosition} with respect to the field
	 * @author Emilio Lobo
	 */
    public DrivePath(StartPosition startPosition, EndPosition endPosition, boolean reverse) {
    	
    	requires(drivetrain);
    	
		this.reverse = reverse;
		headingModifier = startPosition.getHeadingModifier();
		directionModifer = startPosition.getDirectionModifier();
		leftIndex = startPosition.getLeftIndex();
		rightIndex = startPosition.getRightIndex();
		path = endPosition;	
    }

    protected void initialize() {
    	Variables.isPathRunning = true;
    	resetVariables();
    }

    protected void execute() {
		adjustVelocities();

		drivetrain.driveVelocity(mAdjustedLeftVelocity , mAdjustedRightVelocity);

		if (reverse) {
			mCurrentStep--;
		} else {
			mCurrentStep++;
		}
    }

    protected boolean isFinished() {
        return mCurrentStep == mLength || mCurrentStep == 0; //TODO test this
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
        double mHeadingDifference = headingModifier * path.headingStep(mCurrentStep) + drivetrain.getRawGyroAngle() - mInitialHeading;
        double mTurnAdjustment = Constants.kVelocitykG * Constants.kVeloctiyUnitConversion * mHeadingDifference;
		
		mAdjustedLeftVelocity = directionModifer * path.velocityStep(mCurrentStep, leftIndex) - mTurnAdjustment;
		mAdjustedRightVelocity = directionModifer * path.velocityStep(mCurrentStep, rightIndex) + mTurnAdjustment;
    }
    
    public int getCurrentStep() {
    	return mCurrentStep;
    }
    
    private synchronized void resetVariables() {
		mInitialHeading = drivetrain.getRawGyroAngle();
		mLength = path.pathLength();
		if(reverse) {
			mCurrentStep = mLength;					
		} else {
			mCurrentStep = 0;
		}
    }
}
