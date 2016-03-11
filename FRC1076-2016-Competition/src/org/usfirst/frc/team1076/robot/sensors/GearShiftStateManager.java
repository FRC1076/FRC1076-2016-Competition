package org.usfirst.frc.team1076.robot.sensors;

import org.usfirst.frc.team1076.robot.IRobot;
import org.usfirst.frc.team1076.robot.gamepad.IDriverInput.MotorOutput;

public abstract class GearShiftStateManager {
	public enum GearStates { High, Low; }
	private double shiftUpSpeed = 1;
	private double shiftDownSpeed = 0;

	public GearShiftStateManager() { }
	public GearShiftStateManager(double upShift, double downShift) {
		this.shiftUpSpeed = upShift;
		this.shiftDownSpeed = downShift;
	}
	
	abstract public void shiftGear(GearStates newState, IRobot robot);
	
	public void shiftAuto(IRobot robot) {
		MotorOutput motorSpeeds = robot.getMotorSpeed();
		boolean shiftUp = motorSpeeds.left > shiftUpSpeed && motorSpeeds.right > shiftUpSpeed; // Check if we should try to shift up
		boolean shiftDown = motorSpeeds.left < shiftDownSpeed && motorSpeeds.right < shiftDownSpeed; // Shift down check
		if (shiftUp) {
			shiftGear(GearStates.High, robot);
		} else if(shiftDown) {
			shiftGear(GearStates.Low, robot);
		}
	}
	
	public void shiftHigh(IRobot robot) {
		shiftGear(GearStates.High, robot);
	}
	
	public void shiftLow(IRobot robot) {
		shiftGear(GearStates.Low, robot);
	}
}
