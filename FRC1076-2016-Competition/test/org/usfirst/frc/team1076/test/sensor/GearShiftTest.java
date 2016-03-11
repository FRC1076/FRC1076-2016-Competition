package org.usfirst.frc.team1076.test.sensor;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.usfirst.frc.team1076.robot.IRobot.SolenoidValue;
import org.usfirst.frc.team1076.test.mock.MockGearShifter;
import org.usfirst.frc.team1076.test.mock.MockRobot;

public class GearShiftTest {
	MockGearShifter gear;
	MockRobot robot;
	
	@Before
	public void testSetUp() {
		gear = new MockGearShifter(0.2, 0.8);
		robot = new MockRobot();
	}
	
	@Test
	public void testManualHigh() {
		gear.shiftHigh(robot);
		assertEquals(SolenoidValue.Forward, robot.gear);
	}
	
	@Test
	public void testManualLow() {
		gear.shiftLow(robot);
		assertEquals(SolenoidValue.Reverse, robot.gear);
	}
	
	@Test
	public void testAutoHigh() {
		robot.setLeftSpeed(0.9);
		robot.setRightSpeed(0.9);
		gear.shiftAuto(robot);
		assertEquals(SolenoidValue.Forward, robot.gear);
	}

	@Test
	public void testAutoLow() {
		robot.setLeftSpeed(0.1);
		robot.setRightSpeed(0.1);
		gear.shiftAuto(robot);
		assertEquals(SolenoidValue.Reverse, robot.gear);
	}
}