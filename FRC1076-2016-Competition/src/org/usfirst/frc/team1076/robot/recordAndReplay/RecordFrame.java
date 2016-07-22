package org.usfirst.frc.team1076.robot.recordAndReplay;

import java.io.Serializable;

import org.usfirst.frc.team1076.robot.gamepad.IDriverInput.ControlSide;
import org.usfirst.frc.team1076.robot.gamepad.IOperatorInput.IntakeRaiseState;

public class RecordFrame implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 6006282172422543098L;
    public DriverFrame driverFrame;
    public OperatorFrame operatorFrame;
    
    public RecordFrame(DriverFrame driverFrame, OperatorFrame operatorFrame) {
        this.driverFrame = driverFrame;
        this.operatorFrame = operatorFrame;
    }
}