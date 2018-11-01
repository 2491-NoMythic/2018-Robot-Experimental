package com._2491nomythic.tempest.commands.autonomous;

import com._2491nomythic.tempest.commands._CommandBase;
import com._2491nomythic.tempest.commands.cubestorage.TransportCubeTime;
import com._2491nomythic.tempest.commands.drivetrain.DrivePath;
import com._2491nomythic.tempest.commands.drivetrain.DriveTime;
import com._2491nomythic.tempest.commands.shooter.RunShooterCustom;
import com._2491nomythic.tempest.commands.shooter.SetShooterSpeed;
import com._2491nomythic.tempest.settings.Constants;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 */
public class Automatic extends _CommandBase {
	private int mPathLength, currentStep;
	@SuppressWarnings("unused")
	private double mWaitTime;
	private DrivePath mPath;
	private DriveTime hitSwitch;
	private SetShooterSpeed mSetSwitchSpeed, mSetScaleSpeed;
	private TransportCubeTime mFireCubeScale, mFireCubeSwitch;
	private RunShooterCustom mRevShoot, mRevShoot2;
	private String mGameData;
	private boolean timerSafety;
	private Timer mTimer;
	
	public enum StartPosition {
		LEFT(true, true), 
		CENTER(false, false), 
		RIGHT(false, true), 
		CROSS_LINE(true, false), //after this line, values need verification 
		LEFT_NULL(false, false),
		RIGHT_NULL(false, false), 
		LEFT_CUBE(false, false), 
		RIGHT_CUBE(false, false), 
		LEFT_SWITCH(false, false), 
		RIGHT_SWITCH(false, false), 
		LEFT_BACKUP(false, false), 
		RIGHT_BACKUP(false, false), 
		LEFT_PYRAMID(false, false), 
		RIGHT_PYRAMID(false, false);

		private int mHeadingModifier;
		private int mDirectionModifier;
		private int mRightIndex;
		private int mLeftIndex;

		StartPosition(boolean left, boolean reversed) {
			mHeadingModifier = left ? -1 : 1;
			mLeftIndex = left ? 2 : 1;
			mRightIndex = left ? 1 : 2;
			mDirectionModifier = reversed ? -1 : 1;
		}

		public int getHeadingModifier() {
			return mHeadingModifier;
		}

		public int getDirectionModifier() {
			return mDirectionModifier;
		}

		public int getRightIndex() {
			return mRightIndex;
		}

		public int getLeftIndex() {
			return mLeftIndex;
		}
	}
	
	public enum EndPosition {
		CROSS_LINE(Constants.CROSS_LINE),
		SWITCH(Constants.SWITCH),
		LEFT_SWITCH(Constants.LEFT_SWITCH),
		RIGHT_SWITCH(Constants.RIGHT_SWITCH),
		OPPOSITE_SWTICH(Constants.OPPOSITE_SWTICH),
		SCALE(Constants.SCALE),
		OPPOSITE_SCALE(Constants.OPPOSITE_SCALE),
		BUMP_COUNTER(Constants.BUMP_COUNTER),
		MAX(Constants.MAX),
		CUBE(Constants.CUBE),
		NULL(Constants.NULL),
		BACKUP(Constants.BACKUP),
		LEFT_PYRAMID(Constants.LEFT_PYRAMID),
		RIGHT_PYRAMID(Constants.RIGHT_PYRAMID),
		SECOND_LEFT_SWITCH(Constants.SECOND_LEFT_SWITCH),
		SECOND_RIGHT_SWITCH(Constants.SECOND_RIGHT_SWITCH);

		private final double[][] calculatedPath;

		/**
		 *
		 * @param wayPointData an array of waypoints, timeSteps, and totalLengths
		 */
		EndPosition(double[][][] wayPointData) {
			if (wayPointData != null)
			{
				this.calculatedPath = pathPlanner.calculate(wayPointData[0], wayPointData[1][0][0], wayPointData[1][0][1], Constants.robotTrackWidth);
			} else {
				this.calculatedPath = null;
			}
		}

		public double headingStep(int step) {
			return calculatedPath[step][0];
		}
		/**
		 *
		 * @param timeStep destired time step
		 * @param index 1 for left, 2 for right. Pull this from the {@link StartPosition} getIndex method
		 * @return Velocity for the given side and timeStep
		 */
		public double velocityStep(int timeStep, int index) {
			return calculatedPath[timeStep][index];
		}

		public int pathLength() {
			return calculatedPath.length;
		}
	}
	
	public enum Priority {
		SCALE, SWITCH
	}
	
	public enum Crossing {
		OFF, ON, FORCE
	}
	
	private StartPosition mStartPosition;
	private Priority mPriority;
	private Crossing mCrossing;
	private EndPosition mEndPosition;
	
	/**
	 * 
	 * @param position
	 * @param priority
	 * @param crossing
	 */
    public Automatic(StartPosition position, Priority priority, Crossing crossing) {
    	this.mStartPosition = position;
        this.mPriority = priority;
    	this.mCrossing = crossing;
    	mSetSwitchSpeed = new SetShooterSpeed(0.2);
    	mSetScaleSpeed = new SetShooterSpeed(Constants.shooterMediumScaleSpeed);
    	mRevShoot = new RunShooterCustom();
    	mRevShoot2 = new RunShooterCustom();
    	mTimer = new Timer();
    	mWaitTime = 15;
    	
    	mFireCubeSwitch = new TransportCubeTime(-1, 2);
    	mFireCubeScale = new TransportCubeTime(1, 1);
    	hitSwitch = new DriveTime(0.2, 0.2, 1);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timerSafety = false;
    	
    	selectEndPosition(mStartPosition);
		mPath = new DrivePath(mStartPosition, mEndPosition, false);
		mPathLength = mEndPosition.pathLength();

		mTimer.reset();
		mTimer.stop();
		
		mPath.start();		
    }

    
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {	
	    currentStep = mPath.getCurrentStep();
		switch (mEndPosition) {
	    	case OPPOSITE_SCALE:
	    		if(currentStep == mPathLength - 12 || currentStep == mPathLength - 13) {
		   			intake.openArms();
		   			shooter.setScalePosition();
		   			mWaitTime = 0.1;
		   		} else if(currentStep == mPathLength - 17 || currentStep == mPathLength - 18) {
		   			mSetScaleSpeed.start();
		   			mRevShoot.start();
		   		}
		   		break;
		   	case SCALE:
		   		if(currentStep == mPathLength - 60 || currentStep == mPathLength - 61) {
		   			intake.openArms();
		   			shooter.setScalePosition();
		   			mWaitTime = 0.1;
		   		} else if(currentStep == mPathLength - 85 || currentStep == mPathLength - 86) {
		   			mSetScaleSpeed.start();
	    			mRevShoot.start();
	    		}
	    		break;
	    	case NULL:
	    		if(currentStep == mPathLength - 60 || currentStep == mPathLength - 61) {
	    			intake.openArms();
	    			shooter.setScalePosition();
	    			mWaitTime = 0.1;
	    		} else if(currentStep == mPathLength - 85 || currentStep == mPathLength - 86) {
	    			mSetScaleSpeed.start();
	    			mRevShoot.start();
	    		}
	    	default:
	    		break;
	   	}
    	
    	if(mPath.isCompleted() && !timerSafety) {    		
    		switch(mEndPosition) {
    		case LEFT_SWITCH:
    		case RIGHT_SWITCH:
    			mFireCubeSwitch.start();
    			hitSwitch.start();
    			break;
    		case CROSS_LINE:
    			break;
    		case SWITCH:
    			mSetSwitchSpeed.start();
    			mRevShoot2.start();
    			mFireCubeScale.start();
    			break;
    		case OPPOSITE_SCALE:
    		case SCALE:
    			mFireCubeScale.start();
    			break;
    		default:
    			break;    			
    		}
    		
			timerSafety = true;
			mTimer.start();

    	}
    }

    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {    	
    	return mTimer.get() > 2;
    }
    

    // Called once after isFinished returns true
    protected void end() {
    	System.out.println("AutomaticAuto ending");
    	mTimer.stop();
    	mTimer.reset();
    	mRevShoot.cancel();
    	mRevShoot2.cancel();
    	mFireCubeScale.cancel();
    	drivetrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
    
    private synchronized void selectEndPosition(StartPosition startPosition) {
    	getGameData();
    	switch(mStartPosition) {
    	case LEFT:
    		reverseGameData();
    		respondToARCADE(mGameData);
    		break;	
    	case CENTER:
    	case RIGHT:
    		respondToARCADE(mGameData);
    		break;
    	case CROSS_LINE:
    		mEndPosition = EndPosition.CROSS_LINE;
    		break;
    	default:
    		break;
    	}
    	System.out.println("Selected Start Position; " + mStartPosition);
    }
    
    private synchronized void respondToARCADE(String gameData) {
    	switch(mGameData.substring(0, 2)) {
    	case "LL":
			if (mStartPosition == StartPosition.CENTER) {
				mEndPosition = EndPosition.LEFT_SWITCH;
			}
			else if (mCrossing.equals(Crossing.OFF)) {
				mEndPosition = EndPosition.CROSS_LINE;
			}
			else {
				mEndPosition = EndPosition.OPPOSITE_SCALE;
			}
			break;
		case "LR":
			if (mStartPosition == StartPosition.CENTER) {
				mEndPosition = EndPosition.LEFT_SWITCH;
			}
			else {
				mEndPosition = EndPosition.SCALE;
			}
			break;
		case "RL":
			if (mStartPosition == StartPosition.CENTER) {
				mEndPosition = EndPosition.RIGHT_SWITCH;
			}
			else if (mCrossing.equals(Crossing.FORCE) && mPriority.equals(Priority.SCALE)) {
				mEndPosition = EndPosition.OPPOSITE_SCALE;
			}
			else {
				mEndPosition = EndPosition.SWITCH;
			}
			break;
		case "RR":
			if (mStartPosition == StartPosition.CENTER) {
				mEndPosition = EndPosition.RIGHT_SWITCH;
			}
			else if (mPriority == Priority.SCALE) {
				mEndPosition = EndPosition.SCALE;
			}
			else {
				mEndPosition = EndPosition.SWITCH;
			}
			break;
		default:
			System.out.println("Unexpected value for GameSpecificMessage: " + mGameData);
			end();
			break;
		}
    	System.out.println("Selected EndPosition: " + mEndPosition);
    }
    
    private synchronized void reverseGameData() {
       String temp = mGameData;
       temp = temp.replace("L", "G");
       temp = temp.replace("R", "L");
       temp = temp.replace("G", "R");
       mGameData = temp;
   	   System.out.println("Rev: " + mGameData);
    }
    
    private synchronized void getGameData() {
    	mGameData = DriverStation.getInstance().getGameSpecificMessage();  
	}
}
	
