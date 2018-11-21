package com._2491nomythic.tempest.settings;

/**
 * Hardcoded values for robot systems.
 */
public final class Constants {
	//Drive
	public static final int driveTalonLeft1Channel = 9;
	public static final int driveTalonLeft2Channel = 10;
	public static final int driveTalonRight1Channel = 4;
	public static final int driveTalonRight2Channel = 5;
	
	//Intake
	public static final int intakeTalonLeftChannel = 6;
	public static final int intakeTalonRightChannel = 2;
	public static final int intakeTalonBottomChannel = 1;
	public static final int intakeSolenoidActivateChannelForward = 4;
	public static final int intakeSolenoidActivateChannelReverse = 2;
	public static final int intakeSolenoidOpenChannelForward = 6;
	public static final int intakeSolenoidOpenChannelReverse = 3;

	//Shooter
	public static final int shooterTalonLeftAccelerateChannel = 13;
	public static final int shooterTalonRightAccelerateChannel = 14;
	public static final int shooterTalonLeftShootChannel = 15;
	public static final int shooterTalonRightShootChannel = 16;
	public static final int shooterElevatorChannelForward = 1;
	public static final int shooterElevatorChannelReverse = 5;
	public static final int timeForShooterToSpinUp = 2; //TODO time this
	public static final int timeForShooterToRaise = 4; //TODO time this
	public static final int timeForShooterToFire = 2; //TODO Time this
	public static final double shooterHighScaleSpeed = .75;
	public static final double shooterMediumScaleSpeed = .625;
	public static final double shooterLowScaleSpeed = .5;
	public static final double shooterSwitchSpeed = .4;
	public static double shooterHighScaleRPS = 93; // accelerate highscale rps = 28.5
	public static double shooterMediumScaleRPS = 75; // accelerate medscale rps = 24.75
	public static double shooterLowScaleRPS = 58; // accelerate lowscale rps = 19
	public static double shooterSwitchRPS = 48; //accelerate switch rps = 11.3
	public static final double shooterMaxSpeedRPS = 125; // Approximate function of power input to RPS output: f(x) = 52.83x^2 + 44.54 RPS input to power output = f(x) = sqrt((x - 44.54) / 52.83)
	
	public static final double shootFeedForwardHigh = .01;
	public static final double shootFeedForwardMed = .00433;
	public static final double shootFeedForwardLow = .0033;
	public static final double shootFeedForwardSwitch = .002;
	public static final double kJoystickThreshHold = 0.05;
	
	//Cyclone Shooter Constants
	//public static final double shootCLeft = .006;
	//public static final double shootCRight = .0025/20;
	//public static final double shootI1Left = .0045;
	//public static final double shootI1Right = .0085;
	//public static final double shootI2Left = .007;
	//public static final double shootI2Right = .0045;
	
	//Tempest Shooter Constants
	public static final double shootCLeft = .01;
	public static final double shootCRight = .02; //.0025/1.5;
	public static final double shootI1Left = .004;
	public static final double shootI1Right = .008;
	public static final double shootI2Left = .008;
	public static final double shootI2Right = .008; //.004;

	
	//Lights
	public static final int underglowPWM = 1;
	public static final int shooterLights = 2;
	
	//CubeStorage
	public static final int cubeStorageTalonLeftChannel = 11;
	public static final int cubeStorageTalonRightChannel = 12;
	public static final int ultrasonicPingChannel = 1;
	public static final int ultrasonicEchoChannel = 0;
	public static final double heldCubeRange = 5;
	
	//Sick Lights
	public static final int sickLightsSolenoidChannel = 7;
	
	/* Auto Paths */
	public static final double kVelocitykG = 0.05;

	//private static final double leftStartPos = 24.6-0.93; //feet
	private static final double rightStartPos = 2.4+0.93;
	private static final double centerStartPos = 14.5-0.99;
	private static final double robotLength = 3.16667;
	public static final double robotTrackWidth = 2; //distance between left and right wheels, feet

	private static final double kDefaultTimeStep = 0.02; // 20ms

	public static final double[][][] CROSS_LINE= {
		{
			//waypoints
			{0,rightStartPos},
			{8,rightStartPos}
		},
		{ 	//totalTime, timeStep (both in seconds)
			{10, kDefaultTimeStep}
		}
	};
	public static final double[][][] SWITCH = {
		{
			{0,rightStartPos},
			{8,rightStartPos},
			{10.5,7},
			{10.5,8}
		},
		{
			{10, kDefaultTimeStep}
		}
	};
	public static final double[][][] LEFT_SWITCH = {
		{
			{0,centerStartPos},
			{3,centerStartPos},
			{8,19},
			{14-3.82667,19} //-3.16667
		},
		{
			{6, kDefaultTimeStep}
		}
	};
	public static final double[][][] RIGHT_SWITCH = {
		{
			{0,centerStartPos},
			{3,centerStartPos},
			{8,9},
			{14-3.16667,9}
		},
		{
			{6,kDefaultTimeStep}
		}
	};
	public static final double[][][] OPPOSITE_SWTICH = null;
	public static final double[][][] SCALE = {
		{
			{0,rightStartPos},
			{5,rightStartPos+.5},
			{14+1,3.8+.5},
			{20,9.3}
		},
		{
			{10, kDefaultTimeStep}
		}
	};
	public static final double[][][] OPPOSITE_SCALE = {
		{
			{0,rightStartPos},
			{18-2.75,rightStartPos},
			{18-1.75,29},
			{24-robotLength,27}
		},
		{
			{10, kDefaultTimeStep}
		}
	};
	public static final double[][][] BUMP_COUNTER = null;
	public static final double[][][] MAX = null;
	public static final double[][][] CUBE = null;
	public static final double[][][] NULL = null;
	public static final double[][][] BACKUP = null;
	public static final double[][][] LEFT_PYRAMID = null;
	public static final double[][][] RIGHT_PYRAMID = null;
	public static final double[][][] SECOND_LEFT_SWITCH = null;
	public static final double[][][] SECOND_RIGHT_SWITCH = null;

	//Computation
	public static final double driveEncoderToInches = 1 / 6.0 * Math.PI / 4096.0 ;
	public static final double pathingTestEncoderCoversion= 4096.0 / Math.PI * 6.0; //Inches to encoder multiplication encoder to inches divisionS
	public static final double speedModeRPSToTalonOutput = 4096.0 / 10.0;
	public static final double driveEncoderVelocityToRPS = 1.0 / 4096.0 * 10;
	public static final double driveMaxSpeedRPS = 8.0; //approximately
	public static final double testEndcoderTicksToInches = 211.761452;
	public static final double shootEncoderVelocityToRPS = (4.0 * Math.PI) / 4096;
	
	//TalonSRX
	public static final int kTimeoutMs = 10;
	public static final int kVelocitySlotId = 0;

	/**
	 * Converts from Ft/Sec to NativeUnits/100Ms 
	 */
	public static final double kVeloctiyUnitConversion = 260.767149451;
	
	//Cyclone PIDF
	//public static final double kVelocitykF = 0.2960069444;
	//public static final double kVelocitykP = 1.5;
	//public static final double kVelocitykI = 0.0005;
	//public static final double kVelocitykD = 27.5;
	
	//Tempest PIDF
	public static final double kVelocitykF = .276486485;
	public static final double kVelocitykP = .8;
	public static final double kVelocitykI = 0.0005;
	public static final double kVelocitykD = 15;
}
