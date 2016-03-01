package org.usfirst.frc.team1076.test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.usfirst.frc.team1076.robot.gamepad.IInput.MotorOutput;
import org.usfirst.frc.team1076.robot.statemachine.AutoState;
import org.usfirst.frc.team1076.robot.statemachine.DistanceAutonomous;

public class DistanceAutonomousTest {

	private static final double EPSILON = 1e-12;
	
	@Test
	public void testNext() {
		AutoState auto = new DistanceAutonomous(1000, 10);
		assertSame(null, auto.next());
		auto.setNext(auto);
		assertSame(auto, auto.next());
	}
	
	@Test
	public void testShouldNotChange() {
		AutoState auto = new DistanceAutonomous(1000, 10);
		assertEquals(false, auto.shouldChange());
	}
	
	@Test
	public void testDistanceTraveled() {
		AutoState auto = new DistanceAutonomous(3, 0.8);
		MotorOutput motorOutput = auto.driveTrainSpeed();
		
		// We haven't driven far enough yet, so the robot should still be moving.
		assertEquals(false, auto.shouldChange());
		assertEquals(0.8, motorOutput.left, EPSILON);
		assertEquals(0.8, motorOutput.right, EPSILON);
		
		
		// Sleep for a few seconds to allow the robot to move forward.
		try {
		    Thread.sleep(4000);                
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}

		motorOutput = auto.driveTrainSpeed();
		
		// The robot has travel the distance and should stop.
		assertEquals(true, auto.shouldChange());
		assertEquals(0, motorOutput.left, EPSILON);
		assertEquals(0, motorOutput.right, EPSILON);
	}
	
	@Test
	public void testNoArmMotion() {
		AutoState auto = new DistanceAutonomous(100, 10);
		assertEquals(0, auto.armSpeed(), EPSILON);
	}
	
	@Test
	public void testNoIntakeMotion() {
		AutoState auto = new DistanceAutonomous(100, 10);
		assertEquals(0, auto.intakeSpeed(), EPSILON);
	}

}
