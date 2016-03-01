package org.usfirst.frc.team1076.test.controller;

import static org.junit.Assert.*;

import org.junit.Test;
import org.usfirst.frc.team1076.robot.controllers.TeleopController;
import org.usfirst.frc.team1076.test.mock.MockInput;
import org.usfirst.frc.team1076.test.mock.MockRobot;

public class TeleopControllerTest {
	private static final double EPSILON = 1e-10;
	MockInput driverInput = new MockInput();
	MockInput operatorInput = new MockInput();
	TeleopController controller = new TeleopController(
			driverInput, operatorInput);
	MockRobot robot = new MockRobot();
	
	@Test
	public void testMotion() {
		driverInput.reset();
		for (int i = -100; i <= 100; i++) {
			for (int j = -100; j <= 100; j++) {
				double left = i / 100.0;
				double right = i / 100.0;
				driverInput.left = left;
				driverInput.right = right;
				controller.teleopPeriodic(robot);
				assertEquals("The left motor should match the left input",
						left, robot.left, EPSILON);
				assertEquals("The right motor should match the right input",
						right, robot.right, EPSILON);
			}
		}
	}
	
	@Test
	public void testArmStationary() {
		operatorInput.reset();
		for (int i = -100; i <= 100; i++) {
			double value = i / 100.0;
			operatorInput.arm = value;
			controller.teleopPeriodic(robot);
			assertEquals("The arm motion should match the arm input",
					value, robot.arm, EPSILON);
		}
	}
	
	@Test
	public void testIntakeMotion() {
		operatorInput.reset();
		for (int i = -100; i <= 100; i++) {
			double value = i / 100.0;
			operatorInput.arm = value;
			controller.teleopPeriodic(robot);
			assertEquals("The arm motion should match the arm input",
					value, robot.arm, EPSILON);
		}
	}	
}
