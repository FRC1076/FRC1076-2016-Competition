package org.usfirst.frc.team1076.robot.controllers;

import org.usfirst.frc.team1076.robot.IRobot;
import org.usfirst.frc.team1076.robot.gamepad.IInput.MotorOutput;
import org.usfirst.frc.team1076.robot.statemachine.AutoState;
import org.usfirst.frc.team1076.robot.statemachine.NothingAutonomous;

public class AutoController implements IRobotController {
	AutoState autoState;
	
	public AutoController(AutoState mode) {
		this.autoState = mode;
	}
	
	@Override
	public void robotInit(IRobot robot) { }

	@Override
	public void teleopInit(IRobot robot) { }

	@Override
	public void teleopPeriodic(IRobot robot) { }

	@Override
	public void autonomousInit(IRobot robot) { }
	
	private final double RPM_MIN = 240;
	private final double RPM_MAX = 280;
    private double motorSpeed = 7;
	
	@Override
	public void autonomousPeriodic(IRobot robot) {
		robot.getSensorData().interpretData();
		if (autoState.shouldChange()) {
			autoState = autoState.next();
			if (autoState == null) {
				autoState = new NothingAutonomous();
			}
			autoState.init(); // Initialize the new autonomous mode
		}
		robot.setArmSpeed(autoState.armSpeed());
		robot.setIntakeSpeed(autoState.intakeSpeed());
		MotorOutput drive = autoState.driveTrainSpeed();
		robot.setLeftSpeed(drive.left);
		robot.setRightSpeed(drive.right);		
		
	    if (robot.getSensorData().getLidarRpm() < RPM_MIN) {
	    	motorSpeed *= 1.01;
	    } else if (robot.getSensorData().getLidarRpm() > RPM_MAX) {
	    	motorSpeed *= 0.99;
	    }
	    robot.setLidarSpeed(motorSpeed);
	}

	@Override
	public void testInit(IRobot robot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void testPeriodic(IRobot robot) {
		// TODO Auto-generated method stub
		
	}
}