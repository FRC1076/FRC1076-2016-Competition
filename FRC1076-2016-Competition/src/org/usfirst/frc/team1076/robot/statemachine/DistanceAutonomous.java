package org.usfirst.frc.team1076.robot.statemachine;

import org.usfirst.frc.team1076.robot.gamepad.IDriverInput.MotorOutput;
import org.usfirst.frc.team1076.robot.sensors.IDistanceEncoder;

/**
 * DistanceAutonomous takes a distance and speed parameter and outputs
 * MotorOutputs until the distance has been traveled. It currently uses an
 * encoder to tell how far it has driven.
 * */
public class DistanceAutonomous extends AutoState {
	IDistanceEncoder encoder;
	double encoderZeroPoint;
	double speed;
	double distance;

	public DistanceAutonomous(double distance, double speed, IDistanceEncoder encoder) {
		this.distance = distance;
		this.speed = speed;
		this.encoder = encoder;
	}
	
	@Override
	public void init(){
		this.encoderZeroPoint = encoder.getDistance(); 		
	}
	
	@Override
	public boolean shouldChange() {
		return getDistanceTraveled() >= distance;
	}
		
	@Override
	public MotorOutput driveTrainSpeed() {
		if (shouldChange()) {
			return new MotorOutput(0, 0);
		} else {
			return new MotorOutput(speed, speed);
		}
	}
	
	public double getDistanceTraveled() {
		return encoder.getDistance() - encoderZeroPoint;
	}
}
