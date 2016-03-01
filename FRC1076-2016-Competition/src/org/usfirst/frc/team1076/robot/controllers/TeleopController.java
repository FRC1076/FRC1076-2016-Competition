package org.usfirst.frc.team1076.robot.controllers;

import org.usfirst.frc.team1076.robot.IRobot;
import org.usfirst.frc.team1076.robot.gamepad.IInput;
import org.usfirst.frc.team1076.robot.gamepad.IInput.MotorOutput;

public class TeleopController implements IRobotController {

	IInput driverInput, operatorInput;
	
	public TeleopController(IInput driverInput, IInput operatorInput) {
		this.driverInput = driverInput;
		this.operatorInput = operatorInput;
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
		MotorOutput drive = driverInput.driveTrainSpeed();
		robot.setLeftSpeed(drive.left);
		robot.setRightSpeed(drive.right);		
	}

	@Override
	public void testInit(IRobot robot) {
	}

	@Override
	public void testPeriodic(IRobot robot) {
	}

}
