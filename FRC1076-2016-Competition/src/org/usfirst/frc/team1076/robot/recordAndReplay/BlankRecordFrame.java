package org.usfirst.frc.team1076.robot.recordAndReplay;

import org.usfirst.frc.team1076.robot.gamepad.IDriverInput.ControlSide;
import org.usfirst.frc.team1076.robot.gamepad.IOperatorInput.IntakeRaiseState;

public class BlankRecordFrame extends RecordFrame {

    private static final long serialVersionUID = 3904494175793828974L;
    DriverFrame driverFrame;
    OperatorFrame operatorFrame;
    public BlankRecordFrame() {
        super(new DriverFrame(), new OperatorFrame());
    }

}
