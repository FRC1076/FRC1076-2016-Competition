package org.usfirst.frc.team1076.robot.recordAndReplay;

import java.io.Serializable;

import org.usfirst.frc.team1076.robot.gamepad.IOperatorInput;
import org.usfirst.frc.team1076.robot.gamepad.IOperatorInput.IntakeRaiseState;

public class OperatorFrame implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -4643772997532008924L;

    public double intakeSpeed;
    public double armSpeed;
    public double armExtendSpeed;
    public IntakeRaiseState intakeRaiseState;
    public boolean turboArm;
    public OperatorFrame(IOperatorInput operatorInput) {
        intakeSpeed = operatorInput.intakeSpeed();
        intakeRaiseState = operatorInput.intakeRaiseState();
        armSpeed = operatorInput.armSpeed();
        armExtendSpeed = operatorInput.armExtendSpeed();
        turboArm = operatorInput.turboArm();
    }
    
    public OperatorFrame() {
        armExtendSpeed = 0;
        armSpeed = 0;
        intakeRaiseState = IntakeRaiseState.Neutral;
        intakeSpeed = 0;
        turboArm = false;
    }

}
