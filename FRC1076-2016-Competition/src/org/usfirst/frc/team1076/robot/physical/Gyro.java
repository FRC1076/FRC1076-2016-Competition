package org.usfirst.frc.team1076.robot.physical;

import org.usfirst.frc.team1076.robot.sensors.IGyro;
import edu.wpi.first.wpilibj.GyroBase;

public class Gyro implements IGyro {
	GyroBase gyro;
	
	public Gyro(GyroBase gyro) {
		this.gyro = gyro;
	}
	
	@Override
	public double getAngle() {
		return gyro.getAngle();
	}
	
	@Override
	public double getRate() {
		return gyro.getRate();
	}

	@Override
	public void reset() {
		gyro.reset();
	}
}
