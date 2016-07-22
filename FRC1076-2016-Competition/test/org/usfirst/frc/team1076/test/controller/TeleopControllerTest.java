package org.usfirst.frc.team1076.test.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.usfirst.frc.team1076.robot.controllers.TeleopController;
import org.usfirst.frc.team1076.robot.controllers.IRobotController.ArmPneumaticState;
import org.usfirst.frc.team1076.test.mock.MockDriverInput;
import org.usfirst.frc.team1076.test.mock.MockOperatorInput;
import org.usfirst.frc.team1076.test.mock.MockRobot;

public class TeleopControllerTest {
	private static final double EPSILON = 1e-10;
	MockDriverInput driverInput = new MockDriverInput();
	MockOperatorInput operatorInput = new MockOperatorInput();
	TeleopController controller = new TeleopController(driverInput, operatorInput, driverInput, driverInput);
	MockRobot robot = new MockRobot();
	
	@Before
	public void reset() {
		driverInput.reset();
	}
	
	@Test
	public void testMotion() {
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
	

	@Test @Ignore
	public void testUpArmMotion() {
	    for (int i = 0; i <= 100; i++) {
	        double armUpSpeed = controller.getArmUpSpeed();
	        double value = i / 100.0;
	        double expected =  value * armUpSpeed;

	        operatorInput.arm = value;
	        controller.teleopPeriodic(robot);
	        assertEquals("The arm motion should be the arm input " +
	                     "reduced by a factor of " + armUpSpeed,
	                     expected, robot.arm, EPSILON);
	    }
	}
	
	@Test @Ignore
	public void testDownArmMotion() {
	    for (int i = -100; i <= 0; i++) {
	        double armDownSpeed = controller.getArmDownSpeed();
	        double value = i / 100.0;
            double expected =  value * armDownSpeed;

	        operatorInput.arm = value;
	        controller.teleopPeriodic(robot);
	        assertEquals("The arm motion should be the arm input "
	                + "reduced by a factor of " + armDownSpeed,
	                expected, robot.arm, EPSILON);
	    }
	}

	@Test @Ignore
	public void testDriverTurboArmMotion() {
	    for (int i = -100; i <= 0; i++) {
	        double driverTurboSpeed = controller.getDriverTurboSpeed();
	        double value = i / 100.0;
            double expected =  value * driverTurboSpeed;

	        operatorInput.arm = value;
	        driverInput.turboArm = true;
	        controller.teleopPeriodic(robot);
	        assertEquals("The arm motion should be the arm input "
	                + "multiplied by a factor of " + driverTurboSpeed,
	                expected, robot.arm, EPSILON);
	    }
	}

    @Test @Ignore
    public void testOperatorTurboArmMotion() {
        for (int i = -100; i <= 0; i++) {
            double operatorTurboSpeed = controller.getOperatorTurboSpeed();
            double value = i / 100.0;
            double expected =  value * operatorTurboSpeed;

            operatorInput.arm = value;
            operatorInput.operatorTurbo = true;
            controller.teleopPeriodic(robot);
            assertEquals("The arm motion should be the arm input "
                    + "multiplied by a factor of " + operatorTurboSpeed,
                    expected, robot.arm, EPSILON);
        }
    }

	@Test
	public void testArmExtension() {
		for (int i = -100; i <= 100; i++) {
			double value = i / 100.0;
			operatorInput.extend = value;
			controller.teleopPeriodic(robot);
			assertEquals("The arm extension should match the extension input",
					value, robot.extend, EPSILON);
		}
	}
	
	@Test
	public void testIntakeMotion() {
		for (int i = -100; i <= 100; i++) {
			double value = i / 100.0;
			operatorInput.intake = value;
			controller.teleopPeriodic(robot);
			assertEquals("The arm motion should match the arm input",
					value, robot.intake, EPSILON);
		}
	}
	
	@Test
	public void testBrakes() {
		driverInput.brakes = true;
		controller.teleopPeriodic(robot);
		assertEquals(true, robot.brakes);
		
		driverInput.brakes = false;
		controller.teleopPeriodic(robot);
		assertEquals(false, robot.brakes);
	}
	
	@Test
	public void testArmPneumatic() {
	    assertEquals("Robot should start with the pneumatic active", 
	            ArmPneumaticState.On, robot.armPneumaticState);
	    
	    operatorInput.arm = 1;
	    controller.teleopPeriodic(robot);
	    assertEquals("Arm motion means pneumatic",
	            ArmPneumaticState.Off, robot.armPneumaticState);
	    
	    operatorInput.arm = -1;
	        controller.teleopPeriodic(robot);
	        assertEquals("Arm motion means pneumatic",
	                ArmPneumaticState.Off, robot.armPneumaticState);
	    
	    operatorInput.arm = 0;
        controller.teleopPeriodic(robot);
        assertEquals("No arm motion means no pneumatic",
                ArmPneumaticState.On, robot.armPneumaticState);

	}
}
