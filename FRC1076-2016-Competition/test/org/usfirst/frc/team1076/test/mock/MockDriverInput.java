package org.usfirst.frc.team1076.test.mock;

import org.usfirst.frc.team1076.robot.gamepad.IDriverInput;

public class MockDriverInput implements IDriverInput {
	public double left, right;
	public boolean brakes;
	public ControlSide controlSide = ControlSide.Current;
	public boolean turboArm;
	public boolean shift_low, shift_high;
	
	public void reset() {
		left = right = 0;
		turboArm = shift_low = shift_high = brakes = false;
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
		return shift_high;
	}

	@Override
	public boolean shiftLow() {
		return shift_low;
	}
	
	@Override
	public ControlSide controlSide() {
		return controlSide;
	}

    @Override
    public boolean turboArm() {
        return turboArm;
    }
}
