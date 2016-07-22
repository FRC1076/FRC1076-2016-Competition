package org.usfirst.frc.team1076.robot.gamepad;

public interface IOperatorInput {
	public enum IntakeRaiseState { Lowered, Raised, Neutral }

	IntakeRaiseState intakeRaiseState();
	double intakeSpeed();
	
	double armExtendSpeed();
	double armSpeed();
	boolean turboArm();
    boolean replayButtonHeld();
}
