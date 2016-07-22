package org.usfirst.frc.team1076.robot.statemachine;

import java.util.concurrent.TimeUnit;

import org.usfirst.frc.team1076.robot.controllers.IRobotController.ArmPneumaticState;

public class ArmAutonomous extends AutoState {
	public enum LiftDirection {
		Up, Down
	};

	LiftDirection liftDirection;
	long timeStart;
	long timeLimit;
	boolean started = false;
	double speed;

	public ArmAutonomous(int millis, double speed, LiftDirection liftDirection) {
		this.speed = speed;
		this.timeLimit = millis;
		this.liftDirection = liftDirection;
	}

	public ArmAutonomous(int millis, LiftDirection liftDirection) {
		this(millis, 1, liftDirection);
	}

	@Override
	public void init() {
		started = true;
		timeStart = System.nanoTime();
	}

	@Override
	public boolean shouldChange() {
		return started && TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - timeStart) > timeLimit;
	}

	@Override
	public double armSpeed() {
	    if (shouldChange()) {
	        return 0;
	    }
	    switch (liftDirection) {
	    case Up:
	        return speed;
	    case Down:
	        return -speed;
	    default:
	        return 0;
	    }
	}
	
	@Override
	public ArmPneumaticState armPneumatic() {
	    return ArmPneumaticState.Off;
	}
}
