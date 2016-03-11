package org.usfirst.frc.team1076.robot.physical;

import org.usfirst.frc.team1076.robot.AbstractSolenoid;

import edu.wpi.first.wpilibj.Solenoid;

public class OneSolenoid extends AbstractSolenoid {
	Solenoid solenoid;
	boolean state = false;
	
	public OneSolenoid(Solenoid solenoid) {
		this.solenoid = solenoid;
	}
	
	public OneSolenoid(int id) {
		this(new Solenoid(id));
	}
	
	@Override
	public void setForward() {
		state = true;
		solenoid.set(true);
	}

	@Override
	public void setReverse() {
		state = false;
		solenoid.set(false);
	}

	@Override
	public void setNeutral() {
		solenoid.set(state);
	}

}
