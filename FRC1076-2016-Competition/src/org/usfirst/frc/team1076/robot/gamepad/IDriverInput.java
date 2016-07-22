package org.usfirst.frc.team1076.robot.gamepad;

public interface IDriverInput {
	public class MotorOutput {
		public final double left, right;
		
		public MotorOutput(double left, double right) {
			this.left = left;
			this.right = right;
		}
	}
	
	MotorOutput driveTrainSpeed();
	boolean brakesApplied();
	boolean shiftHigh();
	boolean shiftLow();
	
	public enum ControlSide { Left, Right, Current }
	ControlSide controlSide();
	boolean turboArm();
}
