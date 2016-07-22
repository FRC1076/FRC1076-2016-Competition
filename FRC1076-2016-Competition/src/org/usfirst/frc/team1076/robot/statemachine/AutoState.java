package org.usfirst.frc.team1076.robot.statemachine;
import org.usfirst.frc.team1076.robot.controllers.IRobotController.ArmPneumaticState;
import org.usfirst.frc.team1076.robot.gamepad.IDriverInput.MotorOutput;
import org.usfirst.frc.team1076.robot.gamepad.IOperatorInput.IntakeRaiseState;

public abstract class AutoState {
	AutoState nextState = null;

	public AutoState addNext(AutoState nextState) {
		if (this.nextState == null) {
			this.nextState = nextState;
		} else {
			this.nextState.addNext(nextState);
		}
		return this;
	}
	
	public AutoState insertNext(AutoState nextState) {
		nextState.addNext(this.nextState);
		this.nextState = nextState;
		return this;
	}

	public AutoState next() {
		return nextState;
	}

	public void init() { }
	
	public abstract boolean shouldChange();
	
	public MotorOutput driveTrainSpeed() {
		return new MotorOutput(0, 0);
	}
	
	public double armSpeed() {
		return 0;
	}

	public double armExtendSpeed() {
		return 0;
	}

	public double intakeSpeed() {
		return 0;
	}
	
	public boolean setBrakes() {
		return false;
	}

	public IntakeRaiseState intakeRaiseState() {
		return IntakeRaiseState.Neutral;
	}

    public ArmPneumaticState armPneumatic() {
        return ArmPneumaticState.On;
    }
}