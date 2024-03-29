/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com._2491nomythic.tempest;

import com._2491nomythic.tempest.OI.ControllerType;
import com._2491nomythic.tempest.commands._CommandBase;
import com._2491nomythic.tempest.commands.ResetSolenoids;
import com._2491nomythic.tempest.commands.SetCameraMode;
import com._2491nomythic.tempest.commands.autonomous.Automatic;
import com._2491nomythic.tempest.commands.autonomous.Automatic.Crossing;
import com._2491nomythic.tempest.commands.autonomous.Automatic.EndPosition;
import com._2491nomythic.tempest.commands.autonomous.Automatic.Priority;
import com._2491nomythic.tempest.commands.autonomous.Automatic.StartPosition;
import com._2491nomythic.tempest.commands.drivetrain.DrivePath;
import com._2491nomythic.tempest.commands.drivetrain.DriveTime;
import com._2491nomythic.tempest.commands.lights.SendAllianceColor;
import com._2491nomythic.tempest.commands.lights.SerialConnectivityTest;
import com._2491nomythic.tempest.commands.lights.UpdateLightsPattern;
import com._2491nomythic.tempest.settings.Variables;
import com._2491nomythic.tempest.subsystems.Drivetrain;
import com._2491nomythic.tempest.subsystems.Shooter;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {

	Command m_autonomousCommand;
	ResetSolenoids resetSolenoids;
	UpdateLightsPattern updateLights;
	SerialConnectivityTest staticPurple;
	SetCameraMode setCamera;
	SendAllianceColor sendColor;
	Preferences pref;
	
	SendableChooser<StartPosition> m_PositionSelector = new SendableChooser<>();
	SendableChooser<Priority> m_PrioritySelector = new SendableChooser<>();
	SendableChooser<Crossing> m_CrossingSelector = new SendableChooser<>();
	SendableChooser<ControllerType> m_ControllerType = new SendableChooser<>();

	public static boolean isTeleop;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() { 
		
		_CommandBase.init();
		//pref.getDouble(key, backup);
		
		setCamera = new SetCameraMode();
		staticPurple = new SerialConnectivityTest();
		sendColor = new SendAllianceColor();
		updateLights = new UpdateLightsPattern();
		resetSolenoids = new ResetSolenoids();
		
		
		updateLights.start();
		
		m_PositionSelector.addObject("LEFT", StartPosition.LEFT);
		m_PositionSelector.addObject("CENTER", StartPosition.CENTER);
		m_PositionSelector.addObject("RIGHT", StartPosition.RIGHT);
		m_PositionSelector.addDefault("CROSS LINE", StartPosition.CROSS_LINE);
		
		m_PrioritySelector.addDefault("SWITCH", Priority.SWITCH);
		m_PrioritySelector.addObject("SCALE", Priority.SCALE);
		
		m_CrossingSelector.addDefault("OFF", Crossing.OFF);
		m_CrossingSelector.addObject("ON", Crossing.ON);
		m_CrossingSelector.addObject("FORCE", Crossing.FORCE);
		
		m_ControllerType.addDefault("PS4", ControllerType.PS4);
		m_ControllerType.addObject("F310", ControllerType.F310);
		m_ControllerType.addObject("Xbox", ControllerType.Xbox);
		m_ControllerType.addObject("Button Board", ControllerType.ButtonBoard);
		
		SmartDashboard.putData("Position", m_PositionSelector);
		SmartDashboard.putData("Priority", m_PrioritySelector);
		SmartDashboard.putData("Crossing", m_CrossingSelector);
		SmartDashboard.putData("Controller Type", m_ControllerType);
		
		SmartDashboard.putData("HitSwitch", new DriveTime(-0.3, 0.4, 0.8));

		SmartDashboard.putNumber("AutoDelay", Variables.autoDelay);
		SmartDashboard.putData("Backup", new DrivePath(StartPosition.LEFT_SWITCH, EndPosition.BACKUP, false));
		System.out.println("Boot Successful");
	}

	@Override
	public void robotPeriodic() {
		Scheduler.getInstance().run();
		outputToSmartDashboard();
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		isTeleop = false;
		updateLights.cancel();
		staticPurple.start();
		setCamera.start();
	}

	@Override
	public void disabledPeriodic() {
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		Variables.autoDelay = SmartDashboard.getNumber("AutoDelay", 0);
		sendColor.start();
		
		m_autonomousCommand = new Automatic(m_PositionSelector.getSelected(), m_PrioritySelector.getSelected(), m_CrossingSelector.getSelected());
		//m_autonomousCommand = new VelocityTestAuto();
		//m_autonomousCommand = new AutomaticTwoCube(m_PositionSelector.getSelected(), m_PrioritySelector.getSelected(), m_CrossingSelector.getSelected(), m_SecondCubeSelector.getSelected());
		//updateLights.start();
		//sendColor.start();

		// schedule the autonomous command (example)
		if (m_autonomousCommand != null) {
			m_autonomousCommand.start();
		}

		isTeleop = false;
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}
		
		//m_ControllerType.getSelected();
		
		sendColor.start();
		isTeleop = true;
		setCamera.start();
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}

	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Gyro Angle", Drivetrain.getInstance().getGyroAngle());
		SmartDashboard.putBoolean("ShooterReadyToFire", Variables.readyToFire);
		SmartDashboard.putNumber("LeftShootRPS", Shooter.getInstance().getLeftShootVelocity());
		SmartDashboard.putNumber("RightShootRPS", Shooter.getInstance().getRightShootVelocity());
		SmartDashboard.putNumber("Pathing Gyro", -Drivetrain.getInstance().getRawGyroAngle());
		SmartDashboard.putNumber("GyroPitchMeasure", Drivetrain.getInstance().getPitchAngle());
	}
}
