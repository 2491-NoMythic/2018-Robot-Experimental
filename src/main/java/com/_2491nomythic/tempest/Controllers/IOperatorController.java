package com._2491nomythic.tempest.Controllers;

public interface IOperatorController {
    double getThrottle();
    
    double getTurn();

    boolean getKillSwitch();

    boolean getTankTurnForward();

    boolean getTankTurnReveres();

    boolean getAdjustmentDrive();

    boolean getToggleLights();
}