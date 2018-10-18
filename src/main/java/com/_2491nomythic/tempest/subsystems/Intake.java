package com._2491nomythic.tempest.subsystems;

import com._2491nomythic.tempest.settings.Constants;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * The subsystem that takes Power Cubes from the field.
 */
public class Intake extends Subsystem {
	private static Intake instance;
	private TalonSRX left, right;
	private DoubleSolenoid deplymentSolenoid, fingerSolenoid; 
	
	public static Intake getInstance() {
		if (instance == null) {
			instance = new Intake();
		}
		return instance;
	}
	
	/**
	 * The subsystem that takes Power Cubes from the field
	 */
	public Intake() {
		left = new TalonSRX(Constants.intakeTalonLeftChannel);
		left.setInverted(true);
		
		right = new TalonSRX(Constants.intakeTalonRightChannel);
		deplymentSolenoid = new DoubleSolenoid(Constants.intakeSolenoidActivateChannelForward, Constants.intakeSolenoidActivateChannelReverse);
		fingerSolenoid = new DoubleSolenoid(Constants.intakeSolenoidOpenChannelForward, Constants.intakeSolenoidOpenChannelReverse);
	}
	
	/**
	 * Runs both sides of the intake to capture Power Cubes.
	 * @param speed The speed that the motors will run at.
	 */
	public void run(double speed) {
		run(speed, speed);
	}
	
	public void run(double leftSpeed, double rightSpeed) {
		runLeft(leftSpeed);
		runRight(rightSpeed);
	}
	
	/**
	 * Runs the left side of the intake to capture Power Cubes.
	 * @param speed The speed that the motors will run at.
	 */
	public void runLeft(double speed) {
		left.set(ControlMode.PercentOutput, speed);
	}
	
	/**
	 * Runs the right side of the intake to capture Power Cubes.
	 * @param speed the speed that the motors will run at.
	 */
	public void runRight(double speed) {
		right.set(ControlMode.PercentOutput, speed);
	}
	
	/**
	 * Sets the intake out of the frame perimeter.
	 */
	public void openArms() {
		deplymentSolenoid.set(Value.kForward);
	}
	
	/**
	 * Sets the intake in the frame perimeter.
	 */
	public void retractArms() {
		deplymentSolenoid.set(Value.kReverse);
		retractFingers();
	}
	
	/**
	 * Sets the intake fingers to the open state.
	 */
	public void openFingers() {
		fingerSolenoid.set(Value.kForward);
	}
	
	/**
	 * Sets the intake fingers to the closed state.
	 */
	public void retractFingers() {
		fingerSolenoid.set(Value.kReverse);
	}
	
	/**
	 * Returns whether or not the intake arms are extended.
	 * @return Whether or not the intake arms are extended.
	 */
	public boolean armsRetracted() {
		return deplymentSolenoid.get() == Value.kReverse || deplymentSolenoid.get() == Value.kOff;
	}
	
	/**
	 * Returns whether or not the intake fingers are open.
	 * @return Whether or not the intake fingers are open.
	 */
	public boolean fingersOpened() {
		return fingerSolenoid.get() == Value.kForward;
	}
	
	/**
	 * Stops the intake motors.
	 */
	public void stop() {
		run(0);
	}
	
	
	

	public void initDefaultCommand() {
	}

	@Override
	public void periodic() {
		super.periodic();
	}
}

