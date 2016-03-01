package org.usfirst.frc.team1076.robot.gamepad;

import org.usfirst.frc.team1076.robot.gamepad.IGamepad.GamepadAxis;
import org.usfirst.frc.team1076.robot.gamepad.IGamepad.GamepadButton;

public class OperatorInput implements IInput {
	IGamepad gamepad;
	
	public OperatorInput(IGamepad gamepad) {
		this.gamepad = gamepad;
	}
	
	@Override
	public MotorOutput driveTrainSpeed() {
		return new MotorOutput(0, 0);
	}
	
	@Override
	public double armSpeed() {
		return gamepad.getAxis(GamepadAxis.RightY);
	}

	@Override
	public double armExtendSpeed() {
		return gamepad.getAxis(GamepadAxis.LeftY);
	}
	
	@Override
	public double intakeSpeed() {
		double in = gamepad.getAxis(GamepadAxis.LeftTrigger);
		double out = gamepad.getAxis(GamepadAxis.RightTrigger);
		return in - out;
	}	

	@Override
	public IntakeRaiseState intakeRaiseState() {
		boolean up = gamepad.getButton(GamepadButton.LB);
		boolean down = gamepad.getButton(GamepadButton.RB);
		if (up && down) {
			return IntakeRaiseState.Neutral;
		} else if (up) {
			return IntakeRaiseState.Raised;
		} else if (down) {
			return IntakeRaiseState.Lowered;
		}		
		return IntakeRaiseState.Neutral;
	}
}
