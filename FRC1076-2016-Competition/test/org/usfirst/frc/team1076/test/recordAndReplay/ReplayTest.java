package org.usfirst.frc.team1076.test.recordAndReplay;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;

import org.junit.Test;
import org.usfirst.frc.team1076.robot.gamepad.IDriverInput.MotorOutput;
import org.usfirst.frc.team1076.robot.gamepad.IOperatorInput.IntakeRaiseState;
import org.usfirst.frc.team1076.robot.recordAndReplay.RecordController;
import org.usfirst.frc.team1076.robot.recordAndReplay.RecordFrame;
import org.usfirst.frc.team1076.robot.recordAndReplay.ReplayController;
import org.usfirst.frc.team1076.robot.recordAndReplay.ReplayInput;
import org.usfirst.frc.team1076.test.mock.MockDriverInput;
import org.usfirst.frc.team1076.test.mock.MockOperatorInput;
import org.usfirst.frc.team1076.test.mock.MockRobot;

public class ReplayTest {
    private static final double EPSILON = 1e-12;
    MockRobot robot = new MockRobot();
    MockDriverInput driver = new MockDriverInput();
    MockOperatorInput operator = new MockOperatorInput();

    public void init() {
        driver.reset();
        operator.reset();
    }
    @Test
    public void ReadNothingTest() throws IOException, ClassNotFoundException {
        // Setup
        init();
        File file = new File("nothing_test");
        file.createNewFile();
        ReplayInput input = new ReplayInput(file);
        ReplayController controller = new ReplayController(input);
        assertFalse(input.isReplaying());
        assertFalse(controller.replaying());
        controller.replayPeriodic(robot);
        file.deleteOnExit();
    }
    

    @Test
    public void RecordFrameReadTest() throws FileNotFoundException, ClassNotFoundException, IOException {
        // Setup
        init();
        File file = new File("read_test");
        RecordController controller = new RecordController(file, driver, operator);
        controller.startRecording();
        driver.left = 1;
        driver.shift_high = true;
        operator.operatorTurbo = true;
        controller.recordFrame();
        controller.stopRecording();
        // Read
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        LinkedList<RecordFrame> frames = (LinkedList<RecordFrame>) ois.readObject();
        ois.close();
        RecordFrame frame = frames.getFirst();
        // Test
        assertTrue(frame.driverFrame.shiftHigh);
        assertTrue(frame.operatorFrame.turboArm);
        assertEquals(frame.driverFrame.driveTrainSpeedLeft, 1, EPSILON);
        
        file.deleteOnExit();
    }
    
    @Test
    public void ManyFramesTest() throws IOException, ClassNotFoundException {
        init();
        // Setup
        File file = new File("many_frames");
        RecordController controller = new RecordController(file, driver, operator);
        controller.startRecording();
        // Record
        for (double i = 0; i < 100; i++) {
            driver.left = i/100;
            driver.right = i/200;
            controller.recordFrame();
        }
        controller.stopRecording();
        // Read
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        LinkedList<RecordFrame> frames = (LinkedList<RecordFrame>) ois.readObject();
        ois.close();
        // Test
        for (double i = 100; i < 100; i++) {
            RecordFrame frame = frames.get((int) i);
            assertEquals(frame.driverFrame.driveTrainSpeedLeft, i/100, EPSILON);
            assertEquals(frame.driverFrame.driveTrainSpeedRight, i/200, EPSILON);
        }
        
        file.deleteOnExit();
    }
    
    @Test
    public void ReplayInputTest() throws IOException, ClassNotFoundException {
        init();
        // Setup
        File file = new File("replay_input");
        RecordController controller = new RecordController(file, driver, operator);
        controller.startRecording();
        // Record
        for (double i = 0; i < 100; i++) {
            driver.left = i/100;
            driver.right = i/200;
            controller.recordFrame();
        }
        controller.stopRecording();
        // Test
        ReplayInput replayInput = new ReplayInput(file);

        for (double i = 0; i < 100; i++) {
            replayInput.getFrame();
            MotorOutput output = replayInput.driveTrainSpeed();
            assertEquals(output.left, i/100, EPSILON);
            assertEquals(output.right, i/200, EPSILON);
        }
        file.deleteOnExit();
    }
    
    @Test
    public void ReplayControllerTest() throws IOException, ClassNotFoundException {
        init();
        // Setup
        File file = new File("replay_controller");
        RecordController controller = new RecordController(file, driver, operator);
        controller.startRecording();
        // Record
        for (double i = 0; i < 100; i++) {
            driver.left = i/100;
            driver.right = i/200;
            controller.recordFrame();
        }
        controller.stopRecording();
        // Test
        ReplayInput replayInput = new ReplayInput(file);
        ReplayController replayController = new ReplayController(replayInput);
        
        for (double i = 0; i < 100; i++) {
            replayController.replayPeriodic(robot);
            assertEquals(robot.left, i/100, EPSILON);
            assertEquals(robot.right, i/200, EPSILON);
        }
        file.deleteOnExit();
    }
}
