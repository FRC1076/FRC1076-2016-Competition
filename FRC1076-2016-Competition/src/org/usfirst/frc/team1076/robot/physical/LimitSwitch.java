package org.usfirst.frc.team1076.robot.physical;

import edu.wpi.first.wpilibj.DigitalInput;

public class LimitSwitch {
	DigitalInput limitSwitch;
	// True means that the circuit is completed
	// False means that the circuit is broken
	// When not pressed, the limit switch can be either.
	boolean defaultOutput = false;

	public LimitSwitch(int channel, boolean defaultOutput) {
		limitSwitch = new DigitalInput(channel);
		this.defaultOutput = defaultOutput;
	}
	
	public boolean isPressed() {
		if (limitSwitch.get() != defaultOutput) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean getOutput() {
		return limitSwitch.get();
	}
	
	public boolean getDefaultOutput() {
		return getDefaultOutput();
	}
}
