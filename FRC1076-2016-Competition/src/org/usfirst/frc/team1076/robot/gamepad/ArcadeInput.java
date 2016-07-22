package org.usfirst.frc.team1076.robot.gamepad;

import org.usfirst.frc.team1076.robot.gamepad.IGamepad.GamepadAxis;
import org.usfirst.frc.team1076.robot.gamepad.IGamepad.GamepadButton;

public class ArcadeInput implements IDriverInput {
	IGamepad gamepad;

	public ArcadeInput(IGamepad gamepad) {
		this.gamepad = gamepad;
	}
	
	@Override
	public MotorOutput driveTrainSpeed() {
		double speed = gamepad.getAxis(GamepadAxis.RightY);
		double turn = gamepad.getAxis(GamepadAxis.LeftX);
		double left = speed - (turn*0.75);
		double right = speed + (turn*0.75);
		// We multiply by the 1 - |speed|/2 so that we run full speed when it's only turning,
		// but when we're driving and turning we are centered around 0.5 (producing a 0 and 1).
		// This ensures we can drive fast and turn at full power.
		//		double left = (speed - turn) * (1 - 0.5 * Math.abs(speed));
		//		double right = (speed + turn) * (1 - 0.5 * Math.abs(speed));
		return new MotorOutput(left, right);
	}

	@Override
	public boolean brakesApplied() {
		return gamepad.getAxis(GamepadAxis.LeftTrigger) > 0.25;
	}

	@Override
	public boolean shiftHigh() {
		return gamepad.getButton(GamepadButton.RB);
	}

	@Override
	public boolean shiftLow() {
		return gamepad.getButton(GamepadButton.LB);
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

	@Override
	public boolean turboArm() {
		return gamepad.getButton(GamepadButton.A);
	}
}
