package org.usfirst.frc.team1076.test.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.usfirst.frc.team1076.robot.gamepad.IDriverInput.MotorOutput;
import org.usfirst.frc.team1076.robot.sensors.DistanceEncoder;
import org.usfirst.frc.team1076.robot.statemachine.AutoState;
import org.usfirst.frc.team1076.robot.statemachine.DistanceAutonomous;
import org.usfirst.frc.team1076.test.mock.MockEncoder;
import org.usfirst.frc.team1076.test.mock.MockGearShifter;
import org.usfirst.frc.team1076.test.mock.MockRobot;

public class DistanceAutonomousTest {
	private static final double EPSILON = 1e-12;
	
	MockEncoder encoder = new MockEncoder();
	MockRobot robot = new MockRobot();
	MockGearShifter gear = new MockGearShifter(0, 1);
	DistanceEncoder distanceEncoder = new DistanceEncoder(encoder, gear);
	
	private final double LOW_GEAR_COUNTS_PER_INCH = distanceEncoder.lowGearInchesToCounts(1);
	private final double HIGH_GEAR_COUNTS_PER_INCH = distanceEncoder.highGearInchesToCounts(1);
	

	
	@Before
	public void testSetUp() {
		encoder.reset();
		distanceEncoder.reset();
	}
	
	@Test
	public void testNext() {
		AutoState auto = new DistanceAutonomous(1000, 10, encoder);
		assertSame(null, auto.next());
		auto.addNext(auto);
		assertSame(auto, auto.next());
	}
	
	@Test
	public void testShouldNotChange() {
		AutoState auto = new DistanceAutonomous(1000, 10, encoder);
		assertEquals(false, auto.shouldChange());
	}
	
	@Test
	public void testFixedDistanceTraveled() {
		DistanceAutonomous auto = new DistanceAutonomous(1.5, 1, encoder);
		auto.init();
		MotorOutput motorOutput = auto.driveTrainSpeed();
		
		// We haven't driven far enough yet, so the robot should still be moving.
		assertEquals(false, auto.shouldChange());
		assertEquals(1, motorOutput.left, EPSILON);
		assertEquals(1, motorOutput.right, EPSILON);
		
		encoder.distance = 1.5;

		motorOutput = auto.driveTrainSpeed();
		
		// The robot should stop by now.
		assertEquals(true, auto.shouldChange());
		assertEquals(0, motorOutput.left, EPSILON);
		assertEquals(0, motorOutput.right, EPSILON);
		assertEquals(1.5, auto.getDistanceTraveled(), 0.1);
	}
	
	@Test
	public void testVaribleSpeed() {
		for (double speed = 0.0; speed < 1.0; speed += 0.3) {
			encoder.reset();
			DistanceAutonomous auto = new DistanceAutonomous(1, speed, encoder);
			auto.init();
			auto.driveTrainSpeed();
			
			encoder.distance = speed;

			auto.driveTrainSpeed();

			assertEquals(speed, auto.getDistanceTraveled(), 0.1);
		}
	}
	
	@Test
	public void testRepeatedMotion() {
		DistanceAutonomous auto = new DistanceAutonomous(1.5, 1, encoder);
		auto.init();
		auto.driveTrainSpeed();
		assertEquals(false, auto.shouldChange());
		encoder.distance += 1.5;
		assertEquals(true, auto.shouldChange());
		
		auto = new DistanceAutonomous(1.5, 1, encoder);
		auto.init();
		auto.driveTrainSpeed();
		assertEquals(false, auto.shouldChange());
		encoder.distance += 1.5;
		auto.driveTrainSpeed();
		assertEquals(true, auto.shouldChange());
	}
	
	@Test
	public void testNoArmMotion() {
		AutoState auto = new DistanceAutonomous(100, 10, encoder);
		assertEquals(0, auto.armSpeed(), EPSILON);
	}
	
	@Test
	public void testNoIntakeMotion() {
		AutoState auto = new DistanceAutonomous(100, 10, encoder);
		assertEquals(0, auto.intakeSpeed(), EPSILON);
	}
	
	// DistanceEncoder tests.
	
	@Test
	public void testHighGear() {
		AutoState auto = new DistanceAutonomous(120, 10, distanceEncoder);
		gear.shiftHigh(robot);
		assertFalse("Shouldn't change without moving!", auto.shouldChange());
		encoder.rawCount += 120 * HIGH_GEAR_COUNTS_PER_INCH;
		distanceEncoder.updateDistance();
		assertTrue("distance is:" + distanceEncoder.getDistance(), auto.shouldChange());

		auto.driveTrainSpeed();
		assertTrue("distance is:" + encoder.getDistance(), auto.shouldChange());
	}
	
	@Test
	public void testLowGear() {
		AutoState auto = new DistanceAutonomous(110, 10, distanceEncoder);
		
		gear.shiftLow(robot);
		assertFalse("Shouldn't change without moving!", auto.shouldChange());
		encoder.rawCount += 110 * LOW_GEAR_COUNTS_PER_INCH;
		assertTrue("Should change after incrementing encoder", auto.shouldChange());
		auto.driveTrainSpeed();
		assertTrue("Should change even after driveTrainSpeed()", auto.shouldChange());
	}
	
	@Test
	public void testLowToHighGear() {
		AutoState auto = new DistanceAutonomous(17, 10, distanceEncoder);
		
		gear.shiftLow(robot);
		assertFalse("Shouldn't change without moving!", auto.shouldChange());
		encoder.rawCount += 10 * LOW_GEAR_COUNTS_PER_INCH;
		
		assertFalse("Shouldn't change before done!", auto.shouldChange());
		gear.shiftHigh(robot);
		assertFalse("Shouldn't change because of shifting!", auto.shouldChange());
		encoder.rawCount += 7 * HIGH_GEAR_COUNTS_PER_INCH;
		assertTrue("Should change after incrementing encoder", auto.shouldChange());
		auto.driveTrainSpeed();
		assertTrue("Should change even after driveTrainSpeed()", auto.shouldChange());		
	}
	
	@Test
	public void testHighToLowGear() {
		AutoState auto = new DistanceAutonomous(17, 10, distanceEncoder);
		
		gear.shiftHigh(robot);
		assertFalse("Shouldn't change without moving!", auto.shouldChange());
		encoder.rawCount += 7 * HIGH_GEAR_COUNTS_PER_INCH;
		
		assertFalse("Shouldn't change before done!", auto.shouldChange());
		gear.shiftLow(robot);
		assertFalse("Shouldn't change because of shifting!", auto.shouldChange());
		encoder.rawCount += 10 * LOW_GEAR_COUNTS_PER_INCH;
		assertTrue("Should change but didn't.", auto.shouldChange());
		auto.driveTrainSpeed();
		assertTrue("Should change even after driveTrainSpeed()", auto.shouldChange());		
	}
}
