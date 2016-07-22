package org.usfirst.frc.team1076.robot.recordAndReplay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import org.usfirst.frc.team1076.robot.gamepad.IDriverInput;
import org.usfirst.frc.team1076.robot.gamepad.IOperatorInput;

public class RecordController {
    /**
     * This class records inputs and outputs
     * them to a file. File is an ObjectOutputStream.
     *
     * Do not switch input methods during the recording. It will not record them.
     * Does not send motorOutputs, you must do this with teleopContoller.
     *
     */
    ObjectOutputStream ois;
    IDriverInput driverInput;
    IOperatorInput operatorInput;
    File file;
    // Contain all of the data about each controller.
    // driveTrain, brakes, shiftLow, shiftHigh, turboArm
    DriverFrame driverFrame;
    // intakeSpeed, intakeRaise, armSpeed, armExtendSpeed, operatorTurbo
    OperatorFrame operatorFrame;

    LinkedList<RecordFrame> frames = new LinkedList<RecordFrame>();

    boolean isRecording = false;
    public RecordController(File file, IDriverInput driverInput, IOperatorInput operatorInput) {
        this.driverInput = driverInput;
        this.operatorInput = operatorInput;
        if (file.exists() == false) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.file = file;
        try {
            this.ois = new ObjectOutputStream(new FileOutputStream(file));
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public void startRecording() {
        try {
            frames = new LinkedList<RecordFrame>();
            ois = new ObjectOutputStream(new FileOutputStream(file));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        isRecording = true;
    }

    public void recordFrame() {
        if(isRecording) {
            getDriverFrame();
            getOperatorFrame();
            RecordFrame frame = new RecordFrame(driverFrame, operatorFrame);
            frames.add(frame);
        } else {
            throw new RuntimeException("Not recording! (call startRecording())");
        }
    }

    public void getDriverFrame() {
        driverFrame = new DriverFrame(driverInput);
    }

    public void getOperatorFrame() {
        operatorFrame = new OperatorFrame(operatorInput);
    }

    public void stopRecording() {
        try {
            ois.writeObject(frames);
            ois.flush();
            ois.close();
            isRecording = false;
            System.out.println("End of Recording. Wrote " + frames.size() + " frames.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRecording() {
        return isRecording;
    }
}