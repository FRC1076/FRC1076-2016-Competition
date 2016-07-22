package org.usfirst.frc.team1076.test.mock;

import org.usfirst.frc.team1076.robot.IRobot;
import org.usfirst.frc.team1076.robot.controllers.IRobotController.ArmPneumaticState;
import org.usfirst.frc.team1076.robot.gamepad.IDriverInput.MotorOutput;
import org.usfirst.frc.team1076.robot.gamepad.IOperatorInput.IntakeRaiseState;
import org.usfirst.frc.team1076.udp.ISensorData;

public class MockRobot implements IRobot {
	public double left, right, arm, intake, extend;
	public double lidarSpeed;
	public boolean brakes;
	public IntakeRaiseState intakeRaiseState = IntakeRaiseState.Neutral;
	public SolenoidValue gear = SolenoidValue.Off;
	public ArmPneumaticState armPneumaticState = ArmPneumaticState.On;
	public ISensorData sensorData;
	
	@Override
	public void setLeftSpeed(double speed) {
		left = speed;
	}

	@Override
	public void setRightSpeed(double speed) {
		right = speed;
	}

	@Override
	public void setArmSpeed(double speed) {
		arm = speed;
	}

	@Override
	public void setIntakeSpeed(double speed) {
		intake = speed;
	}

	@Override
	public void setGear(SolenoidValue value) {
		gear = value;
	}

	@Override
	public MotorOutput getMotorSpeed() {
		return new MotorOutput(left, right);
	}
	
	@Override
	public void setBrakes(boolean enabled) {
		brakes = enabled;
	}

	@Override
	public void setLidarSpeed(double speed) {
		lidarSpeed = speed;
	}
	
	@Override
	public ISensorData getSensorData() {
		return sensorData;
	}
	
	@Override
	public void setIntakeElevation(IntakeRaiseState state) {
		this.intakeRaiseState = state;
	}

	@Override
	public void setArmExtendSpeed(double speed) {
		this.extend = speed;
	}

    @Override
    public void setArmPneumatic(ArmPneumaticState state) {
        this.armPneumaticState = state;
    }
}
