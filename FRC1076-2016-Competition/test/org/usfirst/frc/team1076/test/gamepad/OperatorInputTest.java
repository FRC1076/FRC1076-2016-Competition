package org.usfirst.frc.team1076.test.gamepad;

import static org.junit.Assert.*;

import org.junit.Test;
import org.usfirst.frc.team1076.robot.gamepad.IOperatorInput.IntakeRaiseState;
import org.usfirst.frc.team1076.robot.gamepad.OperatorInput;
import org.usfirst.frc.team1076.test.mock.MockGamepad;

public class OperatorInputTest {
	private static final double EPSILON = 1e-10;
	MockGamepad gamepad = new MockGamepad();
	OperatorInput input = new OperatorInput(gamepad);
	
	@Test
	public void testIntakeZero() {
		gamepad.reset();
		assertEquals("No triggers should mean no intake motion",
				0, input.intakeSpeed(), EPSILON);
	}
	
	@Test
	public void testIntakeForward() {
		gamepad.reset();
		gamepad.lt = 1;
		assertEquals("The left trigger should run forwards",
				1, input.intakeSpeed(), EPSILON);
	}
	
	@Test
	public void testIntakeBackward() {
		gamepad.reset();
		gamepad.rt = 1;
		assertEquals("The right trigger should run backwards",
				-1, input.intakeSpeed(), EPSILON);
	}
	
	@Test
	public void testIntakeNeutral() {
		gamepad.reset();
		gamepad.lt = 1;
		gamepad.rt = 1;
		assertEquals("Both triggers pressed should produce no motion",
				0, input.intakeSpeed(), EPSILON);
	}
	
	@Test
	public void testArmExtension() {
		gamepad.reset();
		for (int i = -100; i <= 100; i++) {
			double ext = i / 100.0;
			gamepad.ly = ext;
			assertEquals("The arm extension should be driven by the left stick",
					-ext, input.armExtendSpeed(), EPSILON);
		}
	}
	
	@Test
	public void testArmMotion() {
		gamepad.reset();
		for (int i = -100; i <= 100; i++) {
			double ext = i / 100.0;
			gamepad.ry = ext;
			assertEquals("The arm rotation should be driven by the right stick",
					ext, input.armSpeed(), EPSILON);
			
		}
	}
	
	@Test
	public void testIntakeRaiseLower() {
		gamepad.reset();
		assertEquals("Neither trigger pressed means neutral state",
				IntakeRaiseState.Neutral, input.intakeRaiseState());
		gamepad.lb = true;
		assertEquals("The left trigger means ball intake up",
				IntakeRaiseState.Raised, input.intakeRaiseState());
		gamepad.rb = true;
		assertEquals("Both triggers pressed means neutral state",
				IntakeRaiseState.Neutral, input.intakeRaiseState());
		gamepad.lb = false;
		assertEquals("The right trigger means ball intake down",
				IntakeRaiseState.Lowered, input.intakeRaiseState());
		gamepad.rb = false;
		assertEquals("Neither trigger pressed means neutral state",
				IntakeRaiseState.Neutral, input.intakeRaiseState());
	}
}
