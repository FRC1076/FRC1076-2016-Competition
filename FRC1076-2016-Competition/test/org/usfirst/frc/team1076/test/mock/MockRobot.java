package org.usfirst.frc.team1076.test.mock;

import org.usfirst.frc.team1076.robot.IRobot;
import org.usfirst.frc.team1076.udp.SensorData;

public class MockRobot implements IRobot {

	public double left, right, arm, intake;
	public double lidarSpeed;
	public boolean brakes;
	// TODO: Add SensorData
	
	@Override
	public void setLeftSpeed(double speed) {
		left = speed;
	}

	@Override
	public void setRightSpeed(double speed) {
		right = speed;
	}

	@Override
	public void setArmSpeed(double speed) {
		arm = speed;
	}

	@Override
	public void setIntakeSpeed(double speed) {
		intake = speed;
	}

	@Override
	public void setBreaks(boolean enabled) {
		brakes = enabled;
	}

	@Override
	public void setLidarSpeed(double speed) {
		lidarSpeed = speed;
	}

	public SensorData getSensorData() {
		// TODO Auto-generated method stub
		return null;
	}

}
