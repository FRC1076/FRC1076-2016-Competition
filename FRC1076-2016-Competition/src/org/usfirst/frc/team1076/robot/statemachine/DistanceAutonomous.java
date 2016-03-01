package org.usfirst.frc.team1076.robot.statemachine;

import org.usfirst.frc.team1076.robot.gamepad.IInput.MotorOutput;

// TODO: Actually use SensorData to change motor output
// import org.usfirst.frc.team1076.udp.SensorData;

/**
 * DistanceAutonomous takes a distance and speed parameter and outputs
 * MotorOutputs until the distance has been traveled. It currently assumes
 * that the motor travels at a constant speed. This should be changed to use an
 * encoder or accelerometer in the future.
 * */
public class DistanceAutonomous extends AutoState {
	double MOTOR_FACTOR = 1; // TODO: Find a reasonable value for this.
	
	double speed;
	double distanceTraveled = 0;
	double distance;
	
	public DistanceAutonomous(double distance, double speed) {
		this.distance = distance;
		this.speed = speed;
	}

	
	@Override
	public void init() { }

	@Override
	public boolean shouldChange() {
		return distanceTraveled > distance;
	}
	
	// This allows us to calculate how far the robot has traveled since a MotorOutput.
	private long lastFrameTime = 0;
	
	@Override
	public MotorOutput driveTrainSpeed() {
		if (lastFrameTime == 0) {
			lastFrameTime = System.nanoTime(); 
		}
		double deltaTime = (System.nanoTime() - lastFrameTime) / 1e9;
		lastFrameTime = System.nanoTime();
		
		distanceTraveled += speed * MOTOR_FACTOR * deltaTime;
		
		if (shouldChange()) {
			return new MotorOutput(0, 0);
		} else {
			return new MotorOutput(speed, speed);
		}
	}
	
	public double getDistanceTraveled(){
		return distanceTraveled;
	}
	

	@Override
	public double armSpeed() {
		return 0;
	}

	@Override
	public double intakeSpeed() {
		return 0;
	}

}
