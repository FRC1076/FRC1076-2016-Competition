package org.usfirst.frc.team1076.robot.gamepad;

import org.usfirst.frc.team1076.robot.gamepad.IGamepad.GamepadAxis;
import org.usfirst.frc.team1076.robot.gamepad.IGamepad.GamepadButton;

public class TankInput implements IDriverInput {
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
	public boolean brakesApplied() {
		return gamepad.getAxis(GamepadAxis.LeftTrigger) > 0.25;
	}

	@Override
	public boolean shiftHigh() {
		return gamepad.getButton(GamepadButton.LB);
	}

	@Override
	public boolean shiftLow() {
		return gamepad.getButton(GamepadButton.RB);
	}
	
	@Override
	public ControlSide controlSide() {
		if (gamepad.getButton(GamepadButton.Back)) {
			return ControlSide.Left;
		} else if (gamepad.getButton(GamepadButton.Start)) {
			return ControlSide.Right;
		} else {
			return ControlSide.Current;
		}
	}
}
