package com._2491nomythic.tempest.Controllers;

import edu.wpi.first.wpilibj.Joystick;

public class ArcadeDriver implements IDriveController {
    
    private static ArcadeDriver mInstance = null; 

    public static ArcadeDriver getInstance() {
        if (mInstance == null) {
            mInstance = new ArcadeDriver();
        }
        return mInstance;
    }

    private Joystick mJoystick;

    private ArcadeDriver() {
        mJoystick = new Joystick(0);
    }

    @Override
    public double getThrottle() {
        return mJoystick.getRawAxis(1);
    }

    @Override
    public double getTurn() {
        return mJoystick.getRawAxis(2);
    }

    @Override
    public boolean getKillSwitch() {
        return mJoystick.getRawButton(11);
    }

    @Override
    public boolean getTankTurnLeft() {
        return mJoystick.getRawButton(2);
    }

    @Override
    public boolean getTankTurnRight() {
        return mJoystick.getRawButton(3);
    }

    @Override
    public boolean getSlowSpeed() {
        return mJoystick.getRawButton(0);
    }


}