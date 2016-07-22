
package org.usfirst.frc.team1076.robot.physical;

import org.usfirst.frc.team1076.robot.ISolenoid;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.usfirst.frc.team1076.robot.IRobot;
import org.usfirst.frc.team1076.robot.controllers.AutoController;
import org.usfirst.frc.team1076.robot.controllers.IRobotController;
import org.usfirst.frc.team1076.robot.controllers.IRobotController.ArmPneumaticState;
import org.usfirst.frc.team1076.robot.controllers.TeleopController;
import org.usfirst.frc.team1076.robot.controllers.TestController;
import org.usfirst.frc.team1076.robot.gamepad.ArcadeInput;
import org.usfirst.frc.team1076.robot.gamepad.IDriverInput;
import org.usfirst.frc.team1076.robot.gamepad.IDriverInput.MotorOutput;
import org.usfirst.frc.team1076.robot.gamepad.IGamepad;
import org.usfirst.frc.team1076.robot.gamepad.IOperatorInput;
import org.usfirst.frc.team1076.robot.gamepad.IOperatorInput.IntakeRaiseState;
import org.usfirst.frc.team1076.robot.recordAndReplay.RecordController;
import org.usfirst.frc.team1076.robot.recordAndReplay.ReplayController;
import org.usfirst.frc.team1076.robot.recordAndReplay.ReplayInput;
import org.usfirst.frc.team1076.robot.gamepad.OperatorInput;
import org.usfirst.frc.team1076.robot.gamepad.TankInput;
import org.usfirst.frc.team1076.robot.sensors.DistanceEncoder;
import org.usfirst.frc.team1076.robot.sensors.IDistanceEncoder;
import org.usfirst.frc.team1076.robot.statemachine.ArmAutonomous;
import org.usfirst.frc.team1076.robot.statemachine.ArmAutonomous.LiftDirection;
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
	static final int ARM_EXTEND_INDEX = 9;
	static final int ARM_EXTEND_FOLLOWER_INDEX = 6;
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
	ISolenoid brakePneumatic = new OneSolenoid(4);
	IDistanceEncoder encoder;
	
	IRobotController teleopController;
	IRobotController autoController;
	IRobotController testController;

	File file = new File(System.getProperty("user.home") + "/" + "recording");
	RecordController recordController;
	ReplayController replayController;
	boolean recordingInTest = true;
	boolean replayEnabled = true;
	boolean currentlyReplaying = false;
	double robotSpeed = 1;
	double intakeSpeed = 1;
//	double armUpSpeed = 0.4;
//	double armDownSpeed = 0.23;
	double armExtendSpeed = 1;
//	double driverTurboSpeed = 1;
//	double operatorTurboSpeed = 0.75;
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
		setArmPneumatic(ArmPneumaticState.Off);
		if (recordingInTest && recordController.isRecording()) {
		    System.out.println("End of recording.");
		    recordController.stopRecording();
		}
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
    	//SmartDashboard.putString("Auto Program", "elevate up ; forward 3.9 0.65 ; elevate down ;"
    	//		+ "forward 0.6 0.5 ; rotate left 0.3 ; forward 3.5 ; rotate right 0.59 ;"
    	//		+ "vision 2.5 0.45 ; intake 1 out ; intake 0.5 in ; rotate right 0.05 ;"
    	//		+ "intake 1 out ; intake 0.5 in ; rotate right 0.05 ; intake 1 out");
    	SmartDashboard.putString("Auto Program", "nothing");
		// Initialize the physical components before the controllers,
		// in case they depend on them.
		// rightFollower.changeControlMode(TalonControlMode.Follower);
		// rightFollower.set(RIGHT_INDEX);
		leftFollower.setInverted(true);
		leftMotor.setInverted(true);
//		armExtendMotor.setInverted(false);
		armMotor.enableBrakeMode(true);
		armFollower.enableBrakeMode(true);
		armExtendMotor.enableBrakeMode(true);
		armExtendFollower.enableBrakeMode(true);

		armExtendMotor.ConfigFwdLimitSwitchNormallyOpen(true);
		armExtendMotor.ConfigRevLimitSwitchNormallyOpen(true);
		armExtendMotor.enableLimitSwitch(true, true);
		armExtendFollower.ConfigFwdLimitSwitchNormallyOpen(true);
		armExtendFollower.ConfigRevLimitSwitchNormallyOpen(true);
		armExtendFollower.enableLimitSwitch(true, true);
		
		armExtendFollower.changeControlMode(CANTalon.TalonControlMode.Follower);
		armExtendFollower.set(ARM_EXTEND_INDEX);
		// leftFollower.changeControlMode(TalonControlMode.Follower);
		// leftFollower.set(LEFT_INDEX);
		
		compressor.setClosedLoopControl(true);

		setIntakeElevation(IntakeRaiseState.Raised);
		setArmPneumatic(ArmPneumaticState.On);
		
		IGamepad driverGamepad = new Gamepad(0);
		gearShifter.shiftLow(this);
		IGamepad operatorGamepad = new Gamepad(1);
		IDriverInput tank = new TankInput(driverGamepad);
		IDriverInput arcade = new ArcadeInput(driverGamepad);
		IOperatorInput operator = new OperatorInput(operatorGamepad);
		
		// Record and Replay System
		if (replayEnabled) {
		    try {
		        // Ensure the file exists
		        if (!file.exists()) {
		            file.createNewFile();
		        }
		    } catch (EOFException e) {
		        System.err.println("Recording file empty!");
		    } catch (IOException e) {
		        throw new RuntimeException(e.toString());
		    }

		    try {
		        ReplayInput replay = new ReplayInput(file);
		        recordController = new RecordController(file, tank, operator);
		        replayController = new ReplayController(replay);
		    } catch (Exception e) {
		        throw new RuntimeException(e.toString());
		    }
		}
		teleopController = new TeleopController(tank, operator, tank, arcade);
		autoController = new AutoController(new NothingAutonomous());
		testController = new TestController(driverGamepad);
		

		IChannel channel = new Channel(5880);
	    encoder = new DistanceEncoder(new MotorEncoder(leftMotor), gearShifter);
		sensorData = new SensorData(channel, FieldPosition.Right, new Gyro(new AnalogGyro(0)));
		// TODO: Figure out what analog input channel we'll be using. done??
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
	    // Setting the limit switch to be normally closed
	    // ensures that the motors will disable if the limit switches break.
//	    armMotor.ConfigFwdLimitSwitchNormallyOpen(false);
//	    armFollower.ConfigFwdLimitSwitchNormallyOpen(false);

		sensorData.sendAttackColor("tegra-ubuntu:5888", SmartDashboard.getString("Enemy Color"));
		
		if (false && SmartDashboard.getBoolean("Auto Program Enabled")) {
			String source = SmartDashboard.getString("Auto Program");
			AutoState program = StateMachineCompiler.compile(source, sensorData);
			autoController = new AutoController(program);
		} else {
			autoDriveDistance = SmartDashboard.getNumber("Distance");
			lidarMotorSpeed = SmartDashboard.getNumber("Initial Lidar Speed");
//			autoController = new AutoController(new ForwardAutonomous(2000, 0.75));
			
			autoController = new AutoController(new ArmAutonomous(750, -0.25, LiftDirection.Down)
												.addNext(new ForwardAutonomous(5000, 0.75))); 

												//TODO: investigate forwards backwards stuff.
//			autoController = new AutoController
//					new ForwardAutonomous(600, -0.5)
//					.addNext(new RotateAutonomous(320, -1, RotateAutonomous.TurnDirection.Left))
//					.addNext(new ForwardAutonomous(4100, -0.5))
//					.addNext(new RotateAutonomous(750, -1, RotateAutonomous.TurnDirection.Right))
//					.addNext(new VisionAutonomous(1500, -0.7, sensorData))
//					.addNext(new IntakeAutonomous(1500, -1))
//					.addNext(new IntakeAutonomous(1000, 1))
//					.addNext(new IntakeAutonomous(1500, -1)));
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
        // Setting the limit switches to be normally open
        // ensures the motor always works even when the limit switch
        // does not work. Note that this will cause a brief disabling of the arm.
//        armMotor.ConfigFwdLimitSwitchNormallyOpen(true);
//        armFollower.ConfigFwdLimitSwitchNormallyOpen(true);

    	lidarMotorSpeed = SmartDashboard.getNumber("Initial Lidar Speed");
        currentlyReplaying = false;
        if (replayEnabled) {
            ReplayInput replayInput = null;
            try {
                replayInput = new ReplayInput(file);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
            replayController = new ReplayController(replayInput);
            replayController.replayInit(this);
        }
    	if (teleopController != null) {
    		teleopController.teleopInit(this);
    	} else {
    		System.out.println("Teleop Controller on Robot is null in teleopInit()");
    	}
        setArmPneumatic(ArmPneumaticState.On);
    }
    
    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
    	controlLidarMotor();
    	commonPeriodic();
    	if (replayEnabled && teleopController.replayActivated() && !currentlyReplaying) {
    	    System.out.println("Replaying...");
    	    currentlyReplaying = true;
    	}
    	while (currentlyReplaying) {
    	    replayController.replayPeriodic(this);
    	    currentlyReplaying = replayController.replaying();
    	}
    	
    	if (teleopController != null) {
        	teleopController.teleopPeriodic(this);
        } else {
    		System.err.println("Teleop Controller on Robot is null in teleopPeriodic()");
    	}
    }
    
    @Override
    public void testInit() {

        if (recordingInTest) {
            recordInit();
        } else if (testController != null) {
    		testController.testPeriodic(this);
    	} else {
    		System.err.println("Test Controller on Robot is null in testInit()");
    	}
    }
    
    @Override
    public void testPeriodic() {
    	commonPeriodic();
    	if (recordingInTest) {
    	    recordPeriodic();
    	} else if (testController != null) {
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

    public void recordInit() {
        if (file.exists()) {
            boolean deleted = file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Deleted Previous File: " + deleted);
        }
        System.out.println("Now recording...");
        teleopInit();
        recordController.startRecording();
    }
    
    public void recordPeriodic() {
        teleopPeriodic();
        recordController.recordFrame();
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
		armMotor.set(speed);
		armFollower.set(speed);
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
			intakePneumatic.setForward();
			break;
		case Raised:
			intakePneumatic.setReverse();
			break;
		case Neutral:
		default:
			intakePneumatic.setNeutral();
			break;
		}
	}
	
	@Override
	public void setArmPneumatic(ArmPneumaticState state) {
	    switch (state) {
	    case On:
	        brakePneumatic.setForward();
	        break;
	    case Off:
	        brakePneumatic.setReverse();
	        break;
	    default:
	        brakePneumatic.setNeutral();
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
