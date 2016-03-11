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
	
	GearShifter gearShifter;
	
	public TeleopController(IDriverInput driverInput, IOperatorInput operatorInput,
			IDriverInput tankInput, IDriverInput arcadeInput) {
		this.driverInput = driverInput;
		this.operatorInput = operatorInput;
		this.tankInput = tankInput;
		this.driverInput = driverInput;
		gearShifter = new GearShifter();
	}
	
	@Override
	public void robotInit(IRobot robot) { }

	@Override
	public void autonomousInit(IRobot robot) { }

	@Override
	public void autonomousPeriodic(IRobot robot) { }

	@Override
	public void teleopInit(IRobot robot) { }
	
	@Override
	public void teleopPeriodic(IRobot robot) {
		robot.setArmSpeed(operatorInput.armSpeed());
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
    		gearShifter.shiftAuto(robot);
    	}
	}

	@Override
	public void testInit(IRobot robot) { }

	@Override
	public void testPeriodic(IRobot robot) { }
}
