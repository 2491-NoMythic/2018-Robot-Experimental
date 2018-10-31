package com._2491nomythic.tempest.commands.drivetrain;

import com._2491nomythic.tempest.commands._CommandBase;
import com._2491nomythic.tempest.settings.ControllerMap;
import com._2491nomythic.tempest.settings.Variables;

/**
 * Drives the robot with linear acceleration as according to input from a driver's controller
 */
public class Drive extends _CommandBase {
	private double turnSpeed, currentLeftSpeed, currentRightSpeed, lastLeftSpeed, lastRightSpeed;	
	
	/**
	 * Drives the robot with linear acceleration as according to input from a driver's controller
	 */
	public Drive() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(drivetrain);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		turnSpeed = 0.5 * oi.getAxisDeadzonedSquared(ControllerMap.driveController, ControllerMap.driveTurnAxis, 0.05);
		turnSpeed = 0.8 * oi.getAxisDeadzonedSquared(ControllerMap.driveController, ControllerMap.driveTurnAxis, 0.05);

		lastLeftSpeed = currentLeftSpeed;
		lastRightSpeed = currentRightSpeed;
		
		currentLeftSpeed = -oi.getAxisDeadzonedSquared(ControllerMap.driveController, ControllerMap.driveMainAxis, 0.1) + turnSpeed;
		currentRightSpeed = -oi.getAxisDeadzonedSquared(ControllerMap.driveController, ControllerMap.driveMainAxis, 0.1) - turnSpeed;
		
		if (Variables.useLinearAcceleration) {
			currentLeftSpeed = drivetrain.linearlyAccelerate(currentLeftSpeed, lastLeftSpeed, Variables.accelerationSpeed);
			currentRightSpeed = drivetrain.linearlyAccelerate(currentRightSpeed, lastRightSpeed, Variables.accelerationSpeed);
		}

		if (Variables.TankTurnBack){
            if (turnSpeed > 0) {
                drivetrain.drivePercentOutput(0, -turnSpeed);
            }
            else if (turnSpeed < 0) {
                drivetrain.drivePercentOutput(turnSpeed, 0);
            }
        } else if (Variables.TankTurnFore) {
            if (turnSpeed > 0) {
                drivetrain.drivePercentOutput(turnSpeed, 0);
            } else if (turnSpeed < 0) {
                drivetrain.drivePercentOutput(0, -turnSpeed);
            }
        } else {
            drivetrain.drivePercentOutput(currentLeftSpeed + turnSpeed, currentRightSpeed - turnSpeed);
        }
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		drivetrain.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
