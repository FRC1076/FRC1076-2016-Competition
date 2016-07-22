package org.usfirst.frc.team1076.robot;
import org.usfirst.frc.team1076.robot.controllers.IRobotController.ArmPneumaticState;
import org.usfirst.frc.team1076.robot.gamepad.IDriverInput.MotorOutput;
import org.usfirst.frc.team1076.robot.gamepad.IOperatorInput.IntakeRaiseState;
import org.usfirst.frc.team1076.udp.ISensorData;

public interface IRobot {
	enum SolenoidValue { Off, Forward, Reverse; }
	void setLeftSpeed(double speed);
	void setRightSpeed(double speed);
	void setArmSpeed(double speed);
	void setArmExtendSpeed(double speed);
	void setIntakeSpeed(double speed);
	void setIntakeElevation(IntakeRaiseState state);
	void setBrakes(boolean enabled);
	void setLidarSpeed(double speed);
	ISensorData getSensorData(); 
	void setGear(SolenoidValue value);
	MotorOutput getMotorSpeed();
    void setArmPneumatic(ArmPneumaticState state);
}
