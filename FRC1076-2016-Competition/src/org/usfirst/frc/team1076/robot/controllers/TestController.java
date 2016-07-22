package org.usfirst.frc.team1076.robot.controllers;

import org.usfirst.frc.team1076.robot.IRobot;
import org.usfirst.frc.team1076.robot.gamepad.IGamepad;
import org.usfirst.frc.team1076.robot.gamepad.IGamepad.GamepadButton;

public class TestController implements IRobotController {

	IGamepad gamepad;
	boolean waitingForAction = true;
	enum Action { None, DriveForward, TurnRight, TurnLeft; }
	Action currentAction = Action.None;
	long actionProgress = 0;
	
	public TestController(IGamepad gamepad) {
		this.gamepad = gamepad;
	}

	@Override
	public void autonomousInit(IRobot robot) {
	}

	@Override
	public void autonomousPeriodic(IRobot robot) {
	}

	@Override
	public void teleopInit(IRobot robot) {
	}

	@Override
	public void teleopPeriodic(IRobot robot) {
	}

	@Override
	public void testInit(IRobot robot) {
	}

	@Override
	public void testPeriodic(IRobot robot) {
		if (waitingForAction) {
			if (gamepad.getButton(GamepadButton.Y)) {
				waitingForAction = false;
				currentAction = Action.DriveForward;
				actionProgress = 0;
			} else if (gamepad.getButton(GamepadButton.B)) {
				waitingForAction = false;
				currentAction = Action.TurnRight;
				actionProgress = 0;
			} else if (gamepad.getButton(GamepadButton.X)) {
				waitingForAction = false;
				currentAction = Action.TurnLeft;
				actionProgress = 0;
			}
		}
		
		if (gamepad.getButton(GamepadButton.A)) {
			waitingForAction = true;
			currentAction = Action.None;
		}
		
		switch (currentAction) {
		case DriveForward:
			driveForward(robot);
			break;
		case TurnLeft:
			turnLeft(robot);
			break;
		case TurnRight:
			turnRight(robot);
			break;
		case None:
		default:
			robot.setLeftSpeed(0);
			robot.setRightSpeed(0);
			break;
		}
	}
	
	public void driveForward(IRobot robot) {
		robot.setLeftSpeed(1);
		robot.setRightSpeed(1);
		actionProgress += 300;
		if (actionProgress > 30000) {
			currentAction = Action.None;
			waitingForAction = true;
		}
	}
	
	public void turnLeft(IRobot robot) {
		robot.setLeftSpeed(-1);
		robot.setRightSpeed(1);
		actionProgress += 300;
		if (actionProgress > 30000) {
			currentAction = Action.None;
			waitingForAction = true;
		}
	}
	
	public void turnRight(IRobot robot) {
		robot.setLeftSpeed(1);
		robot.setRightSpeed(-1);
		actionProgress += 300;
		if (actionProgress > 30000) {
			currentAction = Action.None;
			waitingForAction = true;
		}
	}

    @Override
    public boolean replayActivated() {
        // TODO Auto-generated method stub
        return false;
    }

}
