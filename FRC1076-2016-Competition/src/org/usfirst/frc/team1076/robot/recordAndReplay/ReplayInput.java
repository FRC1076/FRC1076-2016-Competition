package org.usfirst.frc.team1076.robot.recordAndReplay;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.usfirst.frc.team1076.robot.gamepad.IDriverInput;
import org.usfirst.frc.team1076.robot.gamepad.IOperatorInput;

public class ReplayInput implements IOperatorInput, IDriverInput{
    /**
     * ReplayInput takes a saved replay file and outputs those to the robot.
     * A replay file consists of the various possible outputs (driveTrain, brakes
     * arm extension motor, etc). 
     * */
    boolean replaying = false;
    File file;
    ObjectInputStream ois;
    long time;
    LinkedList<RecordFrame> frames;
    int i = 0;
    DriverFrame driverFrame;
    OperatorFrame operatorFrame;

    public ReplayInput(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
        this.file = file;
        try {
            this.ois = new ObjectInputStream(new FileInputStream(file));
            frames = (LinkedList<RecordFrame>) ois.readObject();
            ois.close();
        } catch (EOFException e) {
            frames = new LinkedList<RecordFrame>();
            frames.add(new BlankRecordFrame());
        }
    }

    public void getFrame() throws NoSuchElementException {
        try {
            RecordFrame frame = frames.get(i);
            i++;
            replaying = true;
            driverFrame = frame.driverFrame;
            operatorFrame = frame.operatorFrame;
        } catch (IndexOutOfBoundsException e) {
            RecordFrame frame = new BlankRecordFrame();
            i = 0;
            replaying = false;
            driverFrame = frame.driverFrame;
            operatorFrame = frame.operatorFrame;

        }
    }

    public boolean isReplaying() {
        return replaying;
    }
    @Override
    public MotorOutput driveTrainSpeed() {
        double left = driverFrame.driveTrainSpeedLeft;
        double right = driverFrame.driveTrainSpeedRight;
        return new MotorOutput(left, right);
    }

    @Override
    public boolean brakesApplied() {
        return driverFrame.brakesApplied;
    }

    @Override
    public boolean shiftHigh() {
        return driverFrame.shiftHigh;
    }

    @Override
    public boolean shiftLow() {
        return driverFrame.shiftLow;
    }

    @Override
    public ControlSide controlSide() {
        return driverFrame.controlSide;
    }

    public boolean driverTurbo() {
        return driverFrame.turboArm;
    }

    @Override
    public double intakeSpeed() {
        return operatorFrame.intakeSpeed;
    }

    @Override
    public IntakeRaiseState intakeRaiseState() {
        return operatorFrame.intakeRaiseState;
    }

    @Override
    public double armSpeed() {
        return operatorFrame.armSpeed;
    }

    @Override
    public double armExtendSpeed() {
        return operatorFrame.armExtendSpeed;
    }

    public boolean operatorTurbo() {
        return operatorFrame.turboArm;
    }

    // Use driverTurbo and operatorTurbo instead.
    @Override @Deprecated
    public boolean turboArm() {
        return driverTurbo() || operatorTurbo();
    }

    // Never use this
    @Override @Deprecated
    public boolean replayButtonHeld() {
        return false;
    }
    
    public int numberOfFrames() {
        return frames.size();
    }
}
