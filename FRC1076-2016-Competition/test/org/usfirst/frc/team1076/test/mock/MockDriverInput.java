package org.usfirst.frc.team1076.test.mock;

import org.usfirst.frc.team1076.robot.gamepad.IDriverInput;

public class MockDriverInput implements IDriverInput {
	public double left, right;
	public boolean brakes;
	public ControlSide controlSide = ControlSide.Current;
	
	public void reset() {
		left = right = 0;
		brakes = false;
	}
	
	@Override
	public MotorOutput driveTrainSpeed() {
		return new MotorOutput(left, right);
	}

	@Override
	public boolean brakesApplied() {
		return brakes;
	}

	@Override
	public boolean shiftHigh() {
		return false;
	}

	@Override
	public boolean shiftLow() {
		return false;
	}
	
	@Override
	public ControlSide controlSide() {
		return controlSide;
	}
}
