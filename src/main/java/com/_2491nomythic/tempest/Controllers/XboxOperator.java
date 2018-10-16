package com._2491nomythic.tempest.Controllers;

import edu.wpi.first.wpilibj.Joystick;

public class XboxOperator implements IOperatorController {

    private static XboxOperator mInstance = null;

    public static XboxOperator getInstance() {
        if (mInstance == null) {
            mInstance = new XboxOperator();
        }
        return mInstance;
    }
    
    private final Joystick mDriveController;

    private XboxOperator() {
        mDriveController = new Joystick(0);
    }

    @Override
    public double getThrottle() {
        return mDriveController.getRawAxis(0);
    }

    @Override
    public double getTurn() {
        return mDriveController.getRawAxis(2);
    }

    @Override
    public boolean getKillSwitch() {
        return false;
    }

    @Override
    public boolean getTankTurnForward() {
        return false;
    }

    @Override
    public boolean getTankTurnReveres() {
        return false;
    }

    @Override
    public boolean getAdjustmentDrive() {
        return false;
    }

    @Override
    public boolean getToggleLights() {
        return false;
    }

}