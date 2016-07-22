package org.usfirst.frc.team1076.test.recordAndReplay;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;

import org.junit.Test;
import org.usfirst.frc.team1076.robot.gamepad.IOperatorInput.IntakeRaiseState;
import org.usfirst.frc.team1076.robot.recordAndReplay.RecordController;
import org.usfirst.frc.team1076.robot.recordAndReplay.RecordFrame;
import org.usfirst.frc.team1076.test.mock.MockDriverInput;
import org.usfirst.frc.team1076.test.mock.MockOperatorInput;
import org.usfirst.frc.team1076.test.mock.MockRobot;

public class RecordTest {

    MockRobot robot;
    MockDriverInput driver = new MockDriverInput();
    MockOperatorInput operator = new MockOperatorInput();

    @Test
    public void createEmptyFileTest() {
        File file = new File("empty_test");
        RecordController record = new RecordController(file, null, null);
        record.startRecording();
        record.stopRecording();
        assertTrue("File should exist.", file.exists());
        assertTrue("Should be able to read the file.", file.canRead());
        assertTrue("Should be able to write to the file", file.canWrite());
        assertEquals("File name should be 'empty_test'", file.getName(), "empty_test");
        file.deleteOnExit();
    }
    
    @Test
    public void createSingleFrameTest() throws IOException, ClassNotFoundException {
        File file = new File("one_frame_test");
        RecordController record = new RecordController(file, driver, operator);
        record.startRecording();
        // Make some simple possible inputs
        operator.extend = 1;
        operator.raiseState = IntakeRaiseState.Raised;
        record.recordFrame();
        record.stopRecording();
        assertTrue("File should exist", file.exists());

        FileInputStream fis = new FileInputStream("one_frame_test");
        ObjectInputStream ois = new ObjectInputStream(fis);
        LinkedList<Object[]> frames = (LinkedList<Object[]>) ois.readObject();
        assertEquals("Exactly one frame should be in the output",
                1, frames.size());
        ois.close();
        file.deleteOnExit();
    }

    @Test 
    public void recorderThrowsExceptionTest() {
        File file = new File("exception_test");
        RecordController record = new RecordController(file, null, null);
        assertFalse(record.isRecording());
        try {
            record.recordFrame();
        } catch (RuntimeException e) {   }
        file.deleteOnExit();
    }

    @Test
    public void multiRecordingTest() throws FileNotFoundException, IOException, ClassNotFoundException {
        File file = new File("multi_test");
        RecordController record = new RecordController(file, driver, operator);
        record.startRecording();
        operator.extend = 1;
        driver.left = 0.4;
        record.recordFrame();
        record.stopRecording();
        
        ObjectInputStream ois;
        LinkedList<RecordFrame> frames;
        ois = new ObjectInputStream(new FileInputStream(file));
        frames = (LinkedList<RecordFrame>) ois.readObject();
        ois.close();

        assertEquals("There should only be one frame", frames.size(), 1);
        
        record = new RecordController(file, driver, operator);
        record.startRecording();
        operator.extend = 0.1;
        driver.left = 1;
        driver.right = 0.3;
        record.recordFrame();
        record.recordFrame();
        record.stopRecording();
        
        ois = new ObjectInputStream(new FileInputStream(file));
        frames = (LinkedList<RecordFrame>) ois.readObject();
        ois.close();
        
        assertEquals("There should only be two frames", frames.size(), 2);
        file.deleteOnExit();

    }
}
