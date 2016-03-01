package org.usfirst.frc.team1076.robot.statemachine;

import org.usfirst.frc.team1076.robot.gamepad.IInput.MotorOutput;
import org.usfirst.frc.team1076.robot.sensors.IAccelerometer;
import org.usfirst.frc.team1076.udp.IChannel;
import org.usfirst.frc.team1076.udp.SensorData;
import org.usfirst.frc.team1076.udp.SensorData.FieldPosition;

public class UDPAutonomous extends AutoState {
	private IChannel channel;
	private SensorData sensors;
	private IAccelerometer accel;
	
	public UDPAutonomous(IChannel channel, FieldPosition position) {
		this.channel = channel;
		sensors = new SensorData(channel, position); 
	}

	public void init() { }
	
	public boolean shouldChange() {
		return false;
	}
	
	public MotorOutput driveTrainSpeed() {
		sensors.interpretData();
		// TODO: Decide motion based on sensors
		return new MotorOutput(1, 1);
	}
	
	public double armSpeed() {
		return 0;
	}
	
	public double intakeSpeed() {
		return 0;
	}
}
