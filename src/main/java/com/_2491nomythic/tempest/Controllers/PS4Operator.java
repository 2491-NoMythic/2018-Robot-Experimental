package com._2491nomythic.tempest.Controllers;

import edu.wpi.first.wpilibj.Joystick;

public class PS4Operator implements IOperatorController {

    private static PS4Operator mInstance = null;

    public static PS4Operator getInstance() {
        if (mInstance == null) {
            mInstance = new PS4Operator();
        }
        return mInstance;
    }
    
    private final Joystick mJoystick;

    private PS4Operator() {
        mJoystick = new Joystick(1);
    }
}