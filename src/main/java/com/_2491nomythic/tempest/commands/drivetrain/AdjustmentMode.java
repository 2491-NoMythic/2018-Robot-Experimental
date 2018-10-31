package com._2491nomythic.tempest.commands.drivetrain;

import com._2491nomythic.tempest.commands._CommandBase;
import com._2491nomythic.tempest.settings.Variables;

/**
 *
 */
public class AdjustmentMode extends _CommandBase {

    private boolean tank,fore, adjust;

    public AdjustmentMode(boolean adjust, boolean tank, boolean fore) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        this.tank = tank;
        this.adjust = adjust;
        this.fore = fore;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        if (adjust) {
            Variables.driveAdjustmentCoefficient = .3; //experimental
        }

        if (tank) {
            if (fore) {
                Variables.TankTurnFore = true;
            } else {
                Variables.TankTurnBack = true;
            }
        }
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
    	Variables.driveAdjustmentCoefficient = 1;
    	Variables.TankTurnBack = false;
    	Variables.TankTurnFore = false;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
