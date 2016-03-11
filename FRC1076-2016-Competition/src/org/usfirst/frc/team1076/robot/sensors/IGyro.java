package org.usfirst.frc.team1076.robot.sensors;

public interface IGyro {
	double getAngle();
	double getRate();
	void reset();
}
