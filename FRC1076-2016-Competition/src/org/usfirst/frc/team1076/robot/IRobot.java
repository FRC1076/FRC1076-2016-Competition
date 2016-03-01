package org.usfirst.frc.team1076.robot;

import org.usfirst.frc.team1076.udp.SensorData;

public interface IRobot {
	void setLeftSpeed(double speed);
	void setRightSpeed(double speed);
	void setArmSpeed(double speed);
	void setIntakeSpeed(double speed);
	void setBreaks(boolean enabled);
	void setLidarSpeed(double speed);
	SensorData getSensorData(); 
}
