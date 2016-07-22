package org.usfirst.frc.team1076.robot.recordAndReplay;

import org.usfirst.frc.team1076.robot.IRobot;
import org.usfirst.frc.team1076.robot.controllers.TeleopController;
import org.usfirst.frc.team1076.robot.gamepad.IDriverInput.MotorOutput;
import org.usfirst.frc.team1076.robot.physical.GearShifter;

public class ReplayController {
    ReplayInput replayInput;
    GearShifter gearShifter;

    // TODO: Putting constants into the controllers is a bad idea.
    double driverTurboSpeed = 1;
    double operatorTurboSpeed = 0.75;
    double armUpSpeed = 0.4;
    double armDownSpeed = 0.23;
    // This is a fake teleop controller that responds only to replayInput 
    TeleopController teleop;

    public ReplayController(ReplayInput replayInput) {
        this.replayInput = replayInput;
        gearShifter = new GearShifter();
        teleop = new TeleopController(replayInput, replayInput, replayInput, replayInput);
    }

    public void replayInit(IRobot robot) {
        System.out.println("Num frames: " + replayInput.numberOfFrames());
    }

    public void replayPeriodic(IRobot robot) {
        replayInput.getFrame();
        teleop.teleopPeriodic(robot);
    }

    public boolean replaying() {
        return replayInput.isReplaying();
    }
}
