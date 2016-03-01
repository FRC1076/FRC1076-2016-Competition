package org.usfirst.frc.team1076.test.controller;

import static org.junit.Assert.*;

import org.junit.Test;
import org.usfirst.frc.team1076.robot.controllers.TestController;
import org.usfirst.frc.team1076.test.mock.MockGamepad;
import org.usfirst.frc.team1076.test.mock.MockRobot;

public class TestControllerTest {

	private static final double EPSILON = 1e-10;

	MockGamepad gamepad;
	MockRobot robot;
	
	public TestControllerTest() {
		gamepad = new MockGamepad();
		robot = new MockRobot();
	}
	
	@Test
	public void testDriveForward() {
		TestController controller = new TestController(gamepad);
		
		gamepad.y = true;
		gamepad.a = false;
		controller.robotInit(robot);
		controller.testInit(robot);
		controller.testPeriodic(robot);
		assertEquals("The test periodic should run forward after the Y button is pressed",
				1, robot.left, EPSILON);
		assertEquals("The test periodic should run forward after the Y button is pressed",
				1, robot.right, EPSILON);
	}
	
	@Test
	public void testStop() {
		TestController controller = new TestController(gamepad);
		
		gamepad.y = true;
		gamepad.a = false;
		controller.robotInit(robot);
		controller.testInit(robot);
		controller.testPeriodic(robot);
		assertEquals(1, robot.left, EPSILON);
		
		gamepad.y = false;
		gamepad.a = true;
		controller.testPeriodic(robot);
		assertEquals(0, robot.left, EPSILON);
		assertEquals(0, robot.right, EPSILON);
	}
}
