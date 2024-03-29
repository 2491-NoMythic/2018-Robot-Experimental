package com._2491nomythic.tempest.commands.intake;

import com._2491nomythic.tempest.commands._CommandBase;


/**
 *Holds the intake open
 */
public class ToggleFingerWhileHeld extends _CommandBase {

    /**
     * Holds the intake open
     */
	public ToggleFingerWhileHeld() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	intake.openFingers();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	intake.retractFingers();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
