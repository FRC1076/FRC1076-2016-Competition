package org.usfirst.frc.team1076.robot.gamepad;

import org.usfirst.frc.team1076.robot.gamepad.IGamepad.GamepadAxis;

public class TankInput implements IInput {
	IGamepad gamepad;

	public TankInput(IGamepad gamepad) {
		this.gamepad = gamepad;
	}
	
	@Override
	public MotorOutput driveTrainSpeed() {
		double left = gamepad.getAxis(GamepadAxis.LeftY);
		double right = gamepad.getAxis(GamepadAxis.RightY);
		return new MotorOutput(left, right);
	}

	@Override
	public double armSpeed() {
		return 0;
	}

	@Override
	public double intakeSpeed() {
		return 0;
	}

	@Override
	public double armExtendSpeed() {
		return 0;
	}

	@Override
	public IntakeRaiseState intakeRaiseState() {
		return IntakeRaiseState.Neutral;
	}
}
