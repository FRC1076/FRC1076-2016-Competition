package org.usfirst.frc.team1076.test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.usfirst.frc.team1076.robot.gamepad.IDriverInput.MotorOutput;
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
	public void testFixedDistanceTraveled() {
		DistanceAutonomous auto = new DistanceAutonomous(.15, 1);
		MotorOutput motorOutput = auto.driveTrainSpeed();
		
		// We haven't driven far enough yet, so the robot should still be moving.
		assertEquals(false, auto.shouldChange());
		assertEquals(1, motorOutput.left, EPSILON);
		assertEquals(1, motorOutput.right, EPSILON);
		
		
		// Sleep in many small increments for
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(20);
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}

			motorOutput = auto.driveTrainSpeed();
		}
		
		// The robot should stop by now.
		assertEquals(true, auto.shouldChange());
		assertEquals(0, motorOutput.left, EPSILON);
		assertEquals(0, motorOutput.right, EPSILON);
		assertEquals(.15, auto.getDistanceTraveled(), 0.02);
	}
	
	@Test
	public void testVaribleSpeed() {
		for (double speed = 0.0; speed < 1.0; speed += 0.3) {
			DistanceAutonomous auto = new DistanceAutonomous(1, speed);
			auto.driveTrainSpeed();
			
			try {
			    Thread.sleep(100);                
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}

			auto.driveTrainSpeed();

			assertEquals(speed * 0.1, auto.getDistanceTraveled(), 0.1);
		}
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
