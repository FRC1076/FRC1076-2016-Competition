package org.usfirst.frc.team1076.robot.controllers;

import org.usfirst.frc.team1076.robot.IRobot;
import org.usfirst.frc.team1076.robot.gamepad.IDriverInput;
import org.usfirst.frc.team1076.robot.gamepad.IDriverInput.MotorOutput;
import org.usfirst.frc.team1076.robot.gamepad.IOperatorInput;
import org.usfirst.frc.team1076.robot.physical.GearShifter;

public class TeleopController implements IRobotController {
	IDriverInput tankInput, arcadeInput;
	IDriverInput driverInput;
	IOperatorInput operatorInput;
	
	double driverTurboSpeed = 1;
	double operatorTurboSpeed = 0.75;
	double armUpSpeed = 0.4;
	double armDownSpeed = 0.23;
	
	GearShifter gearShifter;
	
	public TeleopController(IDriverInput driverInput, IOperatorInput operatorInput,
			IDriverInput tankInput, IDriverInput arcadeInput) {
		this.driverInput = driverInput;
		this.operatorInput = operatorInput;
		this.tankInput = tankInput;
		this.arcadeInput = arcadeInput;
		gearShifter = new GearShifter();
	}

	@Override
	public void autonomousInit(IRobot robot) { }

	@Override
	public void autonomousPeriodic(IRobot robot) { }

	@Override
	public void teleopInit(IRobot robot) {
		gearShifter.shiftLow(robot);
		// Default the input to tank when enabling the robot.
		driverInput = tankInput;
	}
	
	@Override
	public void teleopPeriodic(IRobot robot) {
	    /*
        Turbo mode increases the power going to the lift arm. This allows
        the arm to recover from moving too far forwards. The driver has a full
        power turbo while the operator has a 75% turbo. The driver takes precedence.
        The turbo mode only applies when lowering the lift arm.

        Note that the raising and lowering of the arm differ in speed even
        when not in any turbo mode. Lowering is less powerful due to gravity
        helping. This difference can not be turned off.
        */
	    double armSpeed = operatorInput.armSpeed();
	    // armSpeed between -0.1 and 0.1
	    if (Math.abs(armSpeed) < 0.1) {
	        robot.setArmPneumatic(ArmPneumaticState.On);
	    } else {
	        robot.setArmPneumatic(ArmPneumaticState.Off);
	    }

		if (armSpeed < 0) {
		    robot.setArmSpeed(armSpeed * armUpSpeed);
		} else {
		    if (driverInput.turboArm()) {
                robot.setArmSpeed(armSpeed * driverTurboSpeed);
            } else if (operatorInput.turboArm()) {
                robot.setArmSpeed(armSpeed * operatorTurboSpeed);
            } else {
                robot.setArmSpeed(armSpeed * armDownSpeed);
            }
        }
        robot.setArmExtendSpeed(operatorInput.armExtendSpeed());
		robot.setIntakeSpeed(operatorInput.intakeSpeed());
		robot.setIntakeElevation(operatorInput.intakeRaiseState());
		MotorOutput drive = driverInput.driveTrainSpeed();
		robot.setLeftSpeed(drive.left);
		robot.setRightSpeed(drive.right);
		robot.setBrakes(driverInput.brakesApplied());
		
		switch (driverInput.controlSide()) {
		case Left:
			driverInput = tankInput;
			break;
		case Right:
			driverInput = arcadeInput;
			break;
		case Current:
		default:
			break;
		}
    	if (driverInput.shiftHigh()) {
    		gearShifter.shiftHigh(robot);
    	} else if (driverInput.shiftLow()) {
    		gearShifter.shiftLow(robot);
    	} else {
    		// gearShifter.shiftAuto(robot);
    	}
	}

	@Override
	public void testInit(IRobot robot) { }

	@Override
	public void testPeriodic(IRobot robot) { }

    public double getDriverTurboSpeed() {
        return driverTurboSpeed;
    }

    public double getOperatorTurboSpeed() {
        return operatorTurboSpeed;
    }

    public double getArmUpSpeed() {
        return armUpSpeed;
    }

    public double getArmDownSpeed() {
        return armDownSpeed;
    }

    public boolean replayActivated() {
        return operatorInput.replayButtonHeld();
    }
    
    public IDriverInput getDriverInput() {
        return driverInput;
    }
}
