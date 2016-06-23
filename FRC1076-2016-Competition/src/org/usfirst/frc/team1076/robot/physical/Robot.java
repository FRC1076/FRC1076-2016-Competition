
package org.usfirst.frc.team1076.robot.physical;

import org.usfirst.frc.team1076.robot.ISolenoid;
import org.usfirst.frc.team1076.robot.IRobot;
import org.usfirst.frc.team1076.robot.controllers.AutoController;
import org.usfirst.frc.team1076.robot.controllers.IRobotController;
import org.usfirst.frc.team1076.robot.controllers.TeleopController;
import org.usfirst.frc.team1076.robot.controllers.TestController;
import org.usfirst.frc.team1076.robot.gamepad.ArcadeInput;
import org.usfirst.frc.team1076.robot.gamepad.IDriverInput;
import org.usfirst.frc.team1076.robot.gamepad.IDriverInput.MotorOutput;
import org.usfirst.frc.team1076.robot.gamepad.IGamepad;
import org.usfirst.frc.team1076.robot.gamepad.IOperatorInput;
import org.usfirst.frc.team1076.robot.gamepad.IOperatorInput.IntakeRaiseState;
import org.usfirst.frc.team1076.robot.gamepad.OperatorInput;
import org.usfirst.frc.team1076.robot.gamepad.TankInput;
import org.usfirst.frc.team1076.robot.sensors.DistanceEncoder;
import org.usfirst.frc.team1076.robot.sensors.IDistanceEncoder;
import org.usfirst.frc.team1076.robot.statemachine.AutoState;
import org.usfirst.frc.team1076.robot.statemachine.ForwardAutonomous;
import org.usfirst.frc.team1076.robot.statemachine.IntakeAutonomous;
import org.usfirst.frc.team1076.robot.statemachine.NothingAutonomous;
import org.usfirst.frc.team1076.robot.statemachine.RotateAutonomous;
import org.usfirst.frc.team1076.robot.statemachine.VisionAutonomous;
import org.usfirst.frc.team1076.robot.statemachine.StateMachineCompiler;
import org.usfirst.frc.team1076.udp.Channel;
import org.usfirst.frc.team1076.udp.IChannel;
import org.usfirst.frc.team1076.udp.SensorData;
import org.usfirst.frc.team1076.udp.SensorData.FieldPosition;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot implements IRobot {
	static final int LEFT_INDEX = 3;
	static final int LEFT_FOLLOWER_INDEX = 4;
	static final int RIGHT_INDEX = 1;
	static final int RIGHT_FOLLOWER_INDEX = 2;
	static final int INTAKE_INDEX = 5;
	// None of the below indexes are correct.
	static final int ARM_EXTEND_INDEX = 6;
	static final int ARM_EXTEND_FOLLOWER_INDEX = 9;
	static final int ARM_INDEX = 7;
	static final int ARM_FOLLOWER_INDEX = 8;
	double MOTOR_POWER_FACTOR = 0.9;
	
	CANTalon leftMotor = new CANTalon(LEFT_INDEX);
	CANTalon leftFollower = new CANTalon(LEFT_FOLLOWER_INDEX);
	CANTalon rightMotor = new CANTalon(RIGHT_INDEX);
	CANTalon rightFollower = new CANTalon(RIGHT_FOLLOWER_INDEX);
	CANTalon intakeMotor = new CANTalon(INTAKE_INDEX);
	CANTalon armExtendMotor = new CANTalon(ARM_EXTEND_INDEX);
	CANTalon armExtendFollower = new CANTalon(ARM_EXTEND_FOLLOWER_INDEX);
	CANTalon armMotor = new CANTalon(ARM_INDEX);
	CANTalon armFollower = new CANTalon(ARM_FOLLOWER_INDEX);
	Servo lidarServo = new Servo(0);
	
	Compressor compressor = new Compressor(0);
	ISolenoid intakePneumatic = new TwoSolenoid(1, 3);
	ISolenoid shifterPneumatic = new TwoSolenoid(0, 2);
	IDistanceEncoder encoder;
	
	IRobotController teleopController;
	IRobotController autoController;
	IRobotController testController;
	
	double robotSpeed = 1;
	double intakeSpeed = 1;
	double armSpeed = 0.5;
	double armExtendSpeed = 1;
	double upperGearThreshold = 0.6;
	double lowerGearThreshold = 0.4;
	
	double autoDriveDistance = 156;
	double initialLidarSpeed = 7;
	
	private final double RPM_MIN = 260;
	private final double RPM_MAX = 280;
    private double lidarMotorSpeed = 7;
	
	SensorData sensorData;
	GearShifter gearShifter = new GearShifter();
	
	@Override
	public void disabledInit() {
		setBrakes(true);
	}
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	@Override
    public void robotInit() {
		SmartDashboard.putBoolean("Low Bar", false);
		SmartDashboard.putBoolean("Backwards", false);		
    	SmartDashboard.putNumber("LIDAR Speed", 80);
    	SmartDashboard.putNumber("Motor Tweak", MOTOR_POWER_FACTOR);
    	SmartDashboard.putString("Enemy Color", "red");
		SmartDashboard.putNumber("Distance", autoDriveDistance);
		SmartDashboard.putNumber("Initial Lidar Speed", initialLidarSpeed);
    	SmartDashboard.putBoolean("Auto Program Enabled", false);
    	SmartDashboard.putString("Auto Program", "elevate up ; forward 3.9 0.65 ; elevate down ;"
    			+ "forward 0.6 0.5 ; rotate left 0.3 ; forward 3.5 ; rotate right 0.59 ;"
    			+ "vision 2.5 0.45 ; intake 1 out ; intake 0.5 in ; rotate right 0.05 ;"
    			+ "intake 1 out ; intake 0.5 in ; rotate right 0.05 ; intake 1 out");

		// Initialize the physical components before the controllers,
		// in case they depend on them.
		// rightFollower.changeControlMode(TalonControlMode.Follower);
		// rightFollower.set(RIGHT_INDEX);
		leftFollower.setInverted(true);
		leftMotor.setInverted(true);
		armMotor.enableBrakeMode(true);
		armFollower.enableBrakeMode(true);
		armExtendMotor.enableBrakeMode(true);
		armExtendFollower.enableBrakeMode(true);
		
		armMotor.ConfigFwdLimitSwitchNormallyOpen(true);
		armMotor.ConfigRevLimitSwitchNormallyOpen(true);
		armMotor.enableLimitSwitch(true, true);
        armFollower.ConfigFwdLimitSwitchNormallyOpen(true);
        armFollower.ConfigRevLimitSwitchNormallyOpen(true);
        armFollower.enableLimitSwitch(true, true);
        armExtendMotor.ConfigFwdLimitSwitchNormallyOpen(true);
        armExtendMotor.ConfigRevLimitSwitchNormallyOpen(true);
        armExtendMotor.enableLimitSwitch(true, true);
		armExtendFollower.ConfigFwdLimitSwitchNormallyOpen(true);
		armExtendFollower.ConfigRevLimitSwitchNormallyOpen(true);
		armExtendFollower.enableLimitSwitch(true, true);
		// leftFollower.changeControlMode(TalonControlMode.Follower);
		// leftFollower.set(LEFT_INDEX);

		// Soft Limits are limits which disable the motor if
		// the "Sensor Position" (the encoder) is outside a certain range
		// This is disabled in teleop.
		// TODO: Find true soft limits
		armMotor.setForwardSoftLimit(10000);
		armMotor.setReverseSoftLimit(-10000);
		armMotor.enableForwardSoftLimit(true);
		armMotor.enableReverseSoftLimit(true);
        armFollower.setForwardSoftLimit(10000);
        armFollower.setReverseSoftLimit(-10000);
        armFollower.enableForwardSoftLimit(true);
        armFollower.enableReverseSoftLimit(true);
		armExtendMotor.setForwardSoftLimit(10000);
		armExtendMotor.setReverseSoftLimit(-10000);
		armExtendMotor.enableForwardSoftLimit(true);
		armExtendMotor.enableReverseSoftLimit(true);
		armExtendFollower.setForwardSoftLimit(10000);
		armExtendFollower.setReverseSoftLimit(-10000);
		armExtendFollower.enableForwardSoftLimit(true);
		armExtendFollower.enableReverseSoftLimit(true);
		
		compressor.setClosedLoopControl(true);
		setIntakeElevation(IntakeRaiseState.Raised);
		gearShifter.shiftLow(this);
		
		IGamepad driverGamepad = new Gamepad(0);
		IGamepad operatorGamepad = new Gamepad(1);
		IDriverInput tank = new TankInput(driverGamepad);
		IDriverInput arcade = new ArcadeInput(driverGamepad);
		IOperatorInput operator = new OperatorInput(operatorGamepad);
		teleopController = new TeleopController(arcade, operator, tank, arcade);
		encoder = new DistanceEncoder(new MotorEncoder(leftMotor), gearShifter);
		autoController = new AutoController(new NothingAutonomous());
		testController = new TestController(driverGamepad);
		
		IChannel channel = new Channel(5880);
		sensorData = new SensorData(channel, FieldPosition.Right, new Gyro(new AnalogGyro(0)));
		// TODO: Figure out what analog input channel we'll be using.
	}
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
    public void autonomousInit() {
		sensorData.sendAttackColor("tegra-ubuntu:5888", SmartDashboard.getString("Enemy Color"));
		
		if (SmartDashboard.getBoolean("Auto Program Enabled")) {
			String source = SmartDashboard.getString("Auto Program");
			AutoState program = StateMachineCompiler.compile(source, sensorData);
			autoController = new AutoController(program);
		} else {
			autoDriveDistance = SmartDashboard.getNumber("Distance");
			lidarMotorSpeed = SmartDashboard.getNumber("Initial Lidar Speed");
			autoController = new AutoController(
					new ForwardAutonomous(600, -0.5)
					.addNext(new RotateAutonomous(320, -1, RotateAutonomous.TurnDirection.Left))
					.addNext(new ForwardAutonomous(4100, -0.5))
					.addNext(new RotateAutonomous(750, -1, RotateAutonomous.TurnDirection.Right))
					.addNext(new VisionAutonomous(1500, -0.7, sensorData))
					.addNext(new IntakeAutonomous(1500, -1))
					.addNext(new IntakeAutonomous(1000, 1))
					.addNext(new IntakeAutonomous(1500, -1)));
		}
		/*
		if (SmartDashboard.getBoolean("Low Bar")) {
			autoController = new AutoController(new IntakeElevationAutonomous(IntakeRaiseState.Lowered)
					.addNext(new ForwardAutonomous(6000, -0.6)));
		} else if (SmartDashboard.getBoolean("Backwards")) {
			autoController = new AutoController(new ForwardAutonomous(6000, 0.6));
		}
		*/
		
    	if (autoController != null) {
    		autoController.autonomousInit(this);
    	} else {
    		System.out.println("Autonomous Controller on Robot is null in autonomousInit()");
    	}
    }

    /**
     * This function is called periodically during autonomous
     */
	@Override
    public void autonomousPeriodic() {
		controlLidarMotor();
		commonPeriodic();
		
    	if (autoController != null) {
    		autoController.autonomousPeriodic(this);
    	} else {
    		System.out.println("Autonomous Controller on Robot is null in autonomousPeriodic()");
    	}
    }

    @Override
    public void teleopInit() {
    	lidarMotorSpeed = SmartDashboard.getNumber("Initial Lidar Speed");
    	// Disabling the soft limits still keeps the limit switch working.
        armMotor.enableForwardSoftLimit(false);
        armMotor.enableReverseSoftLimit(false);
        armFollower.enableForwardSoftLimit(false);
        armFollower.enableReverseSoftLimit(false);
        armExtendMotor.enableForwardSoftLimit(false);
        armExtendMotor.enableReverseSoftLimit(false);
        armExtendFollower.enableForwardSoftLimit(false);
        armExtendFollower.enableReverseSoftLimit(false);

    	if (teleopController != null) {
    		teleopController.teleopInit(this);
    	} else {
    		System.out.println("Teleop Controller on Robot is null in teleopInit()");
    	}
    }
    
    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
    	controlLidarMotor();
    	commonPeriodic();
    	
    	if (teleopController != null) {
        	teleopController.teleopPeriodic(this);
        } else {
    		System.err.println("Teleop Controller on Robot is null in teleopPeriodic()");
    	}
    }
    
    @Override
    public void testInit() {
    	if (testController != null) {
    		testController.testPeriodic(this);
    	} else {
    		System.err.println("Test Controller on Robot is null in testInit()");
    	}
    }
    
    @Override
    public void testPeriodic() {
    	commonPeriodic();
    	
    	if (testController != null) {
    		testController.testPeriodic(this);
    	} else {
    		System.err.println("Test Controller on Robot is null in testInit()");
    	}
    }

    public void commonPeriodic() {
    	sensorData.interpretData();
    	autoDriveDistance = SmartDashboard.getNumber("Distance");
    	SmartDashboard.putNumber("Distance", autoDriveDistance);
    	initialLidarSpeed = SmartDashboard.getNumber("Initial Lidar Speed");
    	SmartDashboard.putNumber("Initial Lidar Speed", initialLidarSpeed);
    	SmartDashboard.putNumber("Left Encoder", leftMotor.getEncPosition());
    	SmartDashboard.putNumber("Right Encoder", rightMotor.getEncPosition());
    	SmartDashboard.putNumber("Vision Heading", sensorData.getVisionHeading());
    }

    @Override
    public void disabledPeriodic() {
    	commonPeriodic();
    }
    
	@Override
	public void setLeftSpeed(double speed) {
		leftFollower.set(speed * MOTOR_POWER_FACTOR * robotSpeed);
		leftMotor.set(speed * MOTOR_POWER_FACTOR * robotSpeed);
	}

	@Override
	public void setRightSpeed(double speed) {
		rightMotor.set(speed * robotSpeed);
		rightFollower.set(speed * robotSpeed);
	}
	
	@Override
	public void setArmSpeed(double speed) {
		armMotor.set(speed * armSpeed);
		armFollower.set(speed * armSpeed);
	}
	
	@Override
	public void setArmExtendSpeed(double speed) {
		armExtendMotor.set(speed * armExtendSpeed);
		armExtendFollower.set(speed * armExtendSpeed);
	}
	
	@Override
	public void setIntakeSpeed(double speed) {
		intakeMotor.set(speed * intakeSpeed);
	}

	@Override
	public void setLidarSpeed(double speed) {
	    final double motorCenter = 92;
    	lidarServo.setAngle(motorCenter - speed);
	}
	
	@Override
	public void setBrakes(boolean enabled) {
		leftMotor.enableBrakeMode(enabled);
		leftFollower.enableBrakeMode(enabled);
		rightMotor.enableBrakeMode(enabled);
		rightFollower.enableBrakeMode(enabled);
	}

	@Override
	public SensorData getSensorData() {
		return sensorData;
	}

	@Override
	public void setGear(SolenoidValue value) {
		switch (value) {
		case Forward:
			// TODO: These functions may need to be swapped between
			// the practice and competition robots.
			// shifterPneumatic.setForward();
			shifterPneumatic.setReverse();
			break;
		case Reverse:
			// shifterPneumatic.setReverse();
			shifterPneumatic.setForward();
			break;
		case Off:
		default:
			shifterPneumatic.setNeutral();
			break;
		}
	}

	@Override
	public MotorOutput getMotorSpeed() {
		MotorOutput currentOutput = new MotorOutput(leftMotor.getSpeed(), rightMotor.getSpeed());
		return currentOutput;
	}

	@Override
	public void setIntakeElevation(IntakeRaiseState state) {
		switch (state) {
		case Lowered:
			intakePneumatic.setReverse();
			break;
		case Raised:
			intakePneumatic.setForward();
			break;
		case Neutral:
		default:
			intakePneumatic.setNeutral();
			break;
		}
	}
	
	private void controlLidarMotor() {
		if (getSensorData().getLidarRpm() < RPM_MIN) {
	    	lidarMotorSpeed *= 1.01;
	    } else if (getSensorData().getLidarRpm() > RPM_MAX) {
	    	lidarMotorSpeed *= 0.99;
	    }
	    setLidarSpeed(lidarMotorSpeed);
	}
}
