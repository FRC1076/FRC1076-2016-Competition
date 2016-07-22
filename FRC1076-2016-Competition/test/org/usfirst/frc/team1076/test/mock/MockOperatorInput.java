package org.usfirst.frc.team1076.test.mock;

import org.usfirst.frc.team1076.robot.gamepad.IOperatorInput;

public class MockOperatorInput implements IOperatorInput {

	public double arm, intake, extend;
	public boolean operatorTurbo, replayButton;

	public IntakeRaiseState raiseState = IntakeRaiseState.Neutral;
	
	public void reset() {
		arm = intake = extend = 0;
		raiseState = IntakeRaiseState.Neutral;
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

    @Override
    public boolean turboArm() {
        return operatorTurbo;
    }

    @Override
    public boolean replayButtonHeld() {
        return replayButton;
    }
}
