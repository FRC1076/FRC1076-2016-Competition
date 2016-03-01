package org.usfirst.frc.team1076.robot.physical;

import org.usfirst.frc.team1076.robot.sensors.IAutoSelector;
import org.usfirst.frc.team1076.robot.statemachine.ForwardAutonomous;
import org.usfirst.frc.team1076.robot.statemachine.AutoState;
import org.usfirst.frc.team1076.robot.statemachine.NothingAutonomous;
import org.usfirst.frc.team1076.robot.statemachine.UDPAutonomous;
import org.usfirst.frc.team1076.udp.Channel;
import org.usfirst.frc.team1076.udp.IChannel;
import org.usfirst.frc.team1076.udp.SensorData.FieldPosition;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoSelector implements IAutoSelector {
	//Button button;
	public AutoSelector() {
		//button = new Button();
		SmartDashboard.putNumber("Autonomous", 0);
	}
	public AutoState getState() {
		int chooser = (int) SmartDashboard.getNumber("Autonomous", 0);
		IChannel channel = new Channel(5880);
		switch(chooser) {
		// TODO: make the actual state chains
		case 1:
			return new UDPAutonomous(channel, FieldPosition.Left)
				.setNext(new ForwardAutonomous(100, 0.2));
		case 2:
			return new UDPAutonomous(channel, FieldPosition.Left);
		case 3:
			return new UDPAutonomous(channel, FieldPosition.Left);
		case 4:
			return new UDPAutonomous(channel, FieldPosition.Right);
		case 5:
			return new UDPAutonomous(channel, FieldPosition.Right);
		default:
			return new NothingAutonomous();
		}
	}
}
