package org.usfirst.frc.team1076.robot.sensors;

import org.usfirst.frc.team1076.robot.IRobot;
import org.usfirst.frc.team1076.robot.gamepad.IDriverInput.MotorOutput;

public abstract class GearShiftStateManager {
	public enum GearStates { High, Low; }
	private double shiftUpSpeed = 1;
	private double shiftDownSpeed = 0;
	private GearStates currentState;
	
	public GearShiftStateManager() { }
	
	public GearShiftStateManager(double upShift, double downShift) {
		this.shiftUpSpeed = upShift;
		this.shiftDownSpeed = downShift;
	}
	
	abstract protected void shiftGear(GearStates newState, IRobot robot);
	
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
	
	public void shiftTo(GearStates newState, IRobot robot) {
		currentState = newState;
		switch (newState) {
		case High:
			shiftHigh(robot);
			break;
		case Low:
			shiftLow(robot);
			break;
		}	
	}
	
	public void shiftHigh(IRobot robot) {
		currentState = GearStates.High;
		shiftGear(GearStates.High, robot);
	}
	
	public void shiftLow(IRobot robot) {
		currentState = GearStates.Low;
		shiftGear(GearStates.Low, robot);
	}
	
	public GearStates getGearState(){
		return currentState;
	}
}

	
