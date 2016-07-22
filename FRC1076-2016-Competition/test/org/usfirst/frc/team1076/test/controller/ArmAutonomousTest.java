package org.usfirst.frc.team1076.test.controller;

import static org.junit.Assert.*;

import org.junit.Test;
import org.usfirst.frc.team1076.robot.controllers.IRobotController.ArmPneumaticState;
import org.usfirst.frc.team1076.robot.gamepad.IDriverInput.MotorOutput;
import org.usfirst.frc.team1076.robot.statemachine.ArmAutonomous;
import org.usfirst.frc.team1076.robot.statemachine.ArmAutonomous.LiftDirection;
import org.usfirst.frc.team1076.robot.statemachine.AutoState;

public class ArmAutonomousTest {
	private static final double EPSILON = 1e-12;

	@Test
	public void testArmRaising() {
		AutoState auto = new ArmAutonomous(100, 1.0, LiftDirection.Up);
		auto.init();

		assertFalse("ArmAutonomous should not change before doing work!", auto.shouldChange());
		assertEquals(1.0, auto.armSpeed(), EPSILON);

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}

		assertTrue("ArmAutonomous should change after doing work!", auto.shouldChange());
		assertEquals(0.0, auto.armSpeed(), EPSILON);
	}

	public void testArmLowering() {
		AutoState auto = new ArmAutonomous(100, 1.0, LiftDirection.Down);
		auto.init();

		assertEquals(-1.0, auto.armSpeed(), EPSILON);
	}

	@Test
	public void testNoDriveTrainMotion() {
		AutoState auto = new ArmAutonomous(1, 1.0, LiftDirection.Up);
		MotorOutput motorOutput = auto.driveTrainSpeed();
		assertEquals(0.0, motorOutput.left, EPSILON);
		assertEquals(0.0, motorOutput.right, EPSILON);
	}
	
	@Test
	public void testPneumatic() {
	    AutoState auto = new ArmAutonomous(1, 1.0, LiftDirection.Up);
	    ArmPneumaticState state = auto.armPneumatic();
	    assertEquals(ArmPneumaticState.Off, auto.armPneumatic());
	}
}
