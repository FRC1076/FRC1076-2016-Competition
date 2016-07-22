package org.usfirst.frc.team1076.robot.recordAndReplay;

import java.io.Serializable;

import org.usfirst.frc.team1076.robot.gamepad.IDriverInput;
import org.usfirst.frc.team1076.robot.gamepad.IDriverInput.ControlSide;
import org.usfirst.frc.team1076.robot.gamepad.IOperatorInput.IntakeRaiseState;

public class DriverFrame implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5887518044847225176L;
    public double driveTrainSpeedLeft;
    public double driveTrainSpeedRight;
    public boolean brakesApplied;
    public boolean shiftLow;
    public boolean shiftHigh;
    public boolean turboArm;
    public ControlSide controlSide;
    public DriverFrame(IDriverInput driverInput) {
        driveTrainSpeedLeft = driverInput.driveTrainSpeed().left;
        driveTrainSpeedRight = driverInput.driveTrainSpeed().right;
        brakesApplied = driverInput.brakesApplied();
        shiftLow = driverInput.shiftLow();
        shiftHigh = driverInput.shiftHigh();
        turboArm = driverInput.turboArm();
        controlSide = driverInput.controlSide();
    }
    public DriverFrame() {
        brakesApplied = false;
        driveTrainSpeedLeft = 0;
        driveTrainSpeedRight = 0;
        controlSide = ControlSide.Current;
        shiftHigh = false;
        shiftLow = false;
        turboArm = false;
    }
}
