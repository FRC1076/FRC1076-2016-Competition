package org.usfirst.frc.team1076.robot.gamepad;

import org.usfirst.frc.team1076.robot.gamepad.IGamepad.GamepadAxis;
import org.usfirst.frc.team1076.robot.gamepad.IGamepad.GamepadButton;

public class OperatorInput implements IOperatorInput {
	IGamepad gamepad;
	
	public OperatorInput(IGamepad gamepad) {
		this.gamepad = gamepad;
	}
	
	@Override
	public double armSpeed() {
		return gamepad.getAxis(GamepadAxis.RightY);
	}

	@Override
	public double armExtendSpeed() {
	    // You cannot remove this negative value
		return -gamepad.getAxis(GamepadAxis.LeftY);
	}
	
	@Override
	public double intakeSpeed() {
		double in = gamepad.getAxis(GamepadAxis.LeftTrigger);
		double out = gamepad.getAxis(GamepadAxis.RightTrigger);
		return in - out;
	}	

	@Override
	public IntakeRaiseState intakeRaiseState() {
		boolean down = gamepad.getButton(GamepadButton.LB);
		boolean up = gamepad.getButton(GamepadButton.RB);
		if (up && down) {
			return IntakeRaiseState.Neutral;
		} else if (up) {
			return IntakeRaiseState.Lowered;
		} else if (down) {
			return IntakeRaiseState.Raised;
		}		
		return IntakeRaiseState.Neutral;
	}

	@Override
	public boolean turboArm() {
		return gamepad.getButton(GamepadButton.A);
	}

	@Override
	public boolean replayButtonHeld() {
	    return gamepad.getButton(GamepadButton.X);
	}
}

