package org.usfirst.frc.team1076.robot.physical;

import org.usfirst.frc.team1076.robot.AbstractSolenoid;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class TwoSolenoid extends AbstractSolenoid {
	DoubleSolenoid solenoid;
	
	public TwoSolenoid(DoubleSolenoid solenoid) {
		this.solenoid = solenoid;
	}
	
	public TwoSolenoid(int id1, int id2) {
		this(new DoubleSolenoid(id1, id2));
	}
	
	@Override
	public void setForward() {
		solenoid.set(DoubleSolenoid.Value.kForward);
	}

	@Override
	public void setReverse() {
		solenoid.set(DoubleSolenoid.Value.kReverse);
	}

	@Override
	public void setNeutral() {
		solenoid.set(DoubleSolenoid.Value.kOff);
	}

}
