
package org.usfirst.frc.team1076.robot.physical;

import org.usfirst.frc.team1076.robot.AbstractSolenoid;
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
import org.usfirst.frc.team1076.robot.statemachine.NothingAutonomous;
import org.usfirst.frc.team1076.udp.Channel;
import org.usfirst.frc.team1076.udp.IChannel;
import org.usfirst.frc.team1076.udp.SensorData;
import org.usfirst.frc.team1076.udp.SensorData.FieldPosition;

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
	static final int LEFT_SLAVE_INDEX = 4;
	static final int RIGHT_INDEX = 1;
	static final int RIGHT_SLAVE_INDEX = 2;
	static final int INTAKE_INDEX = 5;
	static final int ARM_INDEX = 6;
	
	double MOTOR_POWER_FACTOR = 1.11;
	
	CANTalon leftMotor = new CANTalon(LEFT_INDEX);
	CANTalon leftSlave = new CANTalon(LEFT_SLAVE_INDEX);
	CANTalon rightMotor = new CANTalon(RIGHT_INDEX);
	CANTalon rightSlave = new CANTalon(RIGHT_SLAVE_INDEX);
	CANTalon intakeMotor = new CANTalon(INTAKE_INDEX);
	CANTalon armMotor = new CANTalon(ARM_INDEX);
	Servo lidarServo = new Servo(0);
	
	Compressor compressor = new Compressor(0);
	AbstractSolenoid intakePneumatic = new OneSolenoid(1);
	AbstractSolenoid shifterPneumatic = new OneSolenoid(0);
	
	IRobotController teleopController;
	IRobotController autoController;
	IRobotController testController;
	
	double robotSpeed = 1;
	double armSpeed = 1;
	double intakeSpeed = 1;
	double upperGearThreshold = 0.6;
	double lowerGearThreshold = 0.4;
	
	SensorData sensorData;
	GearShifter gearShifter;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	@Override
    public void robotInit() {
    	SmartDashboard.putNumber("LIDAR Speed", 80);		
    	// SmartDashboard.putNumber("Motor Tweak", MOTOR_POWER_FACTOR);
		
		// Initialize the physical components before the controllers,
		// in case they depend on them.
		// rightSlave.changeControlMode(TalonControlMode.Follower);
		// rightSlave.set(RIGHT_INDEX);
		leftSlave.setInverted(true);
		leftMotor.setInverted(true);
		
		// leftSlave.changeControlMode(TalonControlMode.Follower);
		// leftSlave.set(LEFT_INDEX);
		
		compressor.setClosedLoopControl(true);
		intakePneumatic.setNeutral();
		
		IGamepad driverGamepad = new Gamepad(0);
		IGamepad operatorGamepad = new Gamepad(1);
		IDriverInput tank = new TankInput(driverGamepad);
		IDriverInput arcade = new ArcadeInput(driverGamepad);
		IOperatorInput operator = new OperatorInput(operatorGamepad);
		teleopController = new TeleopController(arcade, operator, tank, arcade);
		autoController = new AutoController(new NothingAutonomous());
		testController = new TestController(driverGamepad);

    	if (teleopController != null) {
    		teleopController.robotInit(this);
    	} else {
    		System.out.println("Teleop Controller on Robot is null in robotInit()");
    	}
    	
    	if (autoController != null) {
    		autoController.robotInit(this);
    	} else {
    		System.out.println("Autonomous Controller on Robot is null in robotInit()");
    	}
    	
    	if (testController != null) {
    		testController.robotInit(this);
    	} else {
    		System.out.println("Test Controller on Robot is null in robotInit()");
    	}
    	
    	
		IChannel channel = new Channel(5880);
		sensorData = new SensorData(channel, FieldPosition.Right);
		
		gearShifter = new GearShifter();
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
		commonPeriodic();
		
    	if (autoController != null) {
    		autoController.autonomousPeriodic(this);
    	} else {
    		System.out.println("Autonomous Controller on Robot is null in autonomousPeriodic()");
    	}
    }

    @Override
    public void teleopInit() {
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
    	// MOTOR_POWER_FACTOR = SmartDashboard.getNumber("Motor Tweak");

    	int left = leftMotor.getEncVelocity();
    	int right = rightMotor.getEncVelocity();
    	if (left != 0) {
        	System.out.println("Left motor " + left);
    	}
    	if (right != 0) {
    		System.out.println("Right motor " + right);
    	}
    }

	@Override
	public void setLeftSpeed(double speed) {
		leftSlave.set(speed * MOTOR_POWER_FACTOR * robotSpeed);
		leftMotor.set(speed * MOTOR_POWER_FACTOR * robotSpeed);
	}

	@Override
	public void setRightSpeed(double speed) {
		rightMotor.set(speed * robotSpeed);
		rightSlave.set(speed * robotSpeed);
	}
	
	@Override
	public void setArmSpeed(double speed) {
		armMotor.set(speed * armSpeed);
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
		leftSlave.enableBrakeMode(enabled);
		rightMotor.enableBrakeMode(enabled);
		rightSlave.enableBrakeMode(enabled);
	}

	@Override
	public SensorData getSensorData() {
		return sensorData;
	}

	@Override
	public void setGear(SolenoidValue value) {
		switch (value) {
		case Forward:
			shifterPneumatic.setForward();
			break;
		case Reverse:
			shifterPneumatic.setReverse();
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
}
