package com._2491nomythic.tempest.Controllers;

public interface IDriveController {
    
    public double getThrottle();

    public double getTurn();

    public boolean getKillSwitch();

    public boolean getTankTurnLeft();

    public boolean getTankTurnRight();

    public boolean getSlowSpeed();
}