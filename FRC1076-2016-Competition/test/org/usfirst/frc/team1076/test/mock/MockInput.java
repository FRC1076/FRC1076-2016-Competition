package org.usfirst.frc.team1076.test.mock;

import org.usfirst.frc.team1076.robot.gamepad.IInput;

public class MockInput implements IInput {

	public double left, right, arm, intake, extend;
	public IntakeRaiseState raiseState = IntakeRaiseState.Neutral;
	
	public void reset() {
		left = right = arm = intake = extend = 0;
		raiseState = IntakeRaiseState.Neutral;
	}
	
	@Override
	public MotorOutput driveTrainSpeed() {
		return new MotorOutput(left, right);
	}

	@Override
	public double armSpeed() {
		return arm;
	}

	@Override
	public double intakeSpeed() {
		return intake;
	}

	@Override
	public double armExtendSpeed() {
		return extend;
	}

	@Override
	public IntakeRaiseState intakeRaiseState() {
		return raiseState;
	}
}
