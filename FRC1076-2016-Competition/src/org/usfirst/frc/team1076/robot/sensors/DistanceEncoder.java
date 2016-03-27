package org.usfirst.frc.team1076.robot.sensors;

import org.usfirst.frc.team1076.robot.sensors.GearShiftStateManager.GearStates;

public class DistanceEncoder implements IDistanceEncoder {	
	IEncoder encoder;
	GearShiftStateManager gearShifter;
	double countAccumulator = 0; 
	double totalDistance = 0;
	
	public static final double MOTOR_PERIOD = 4096; // Number of encoder counts per motor rotation.
	// Number of motor rotations per wheel rotation.
	public static final double HIGH_GEAR_RATIO = 34.0/40.0;
	public static final double LOW_GEAR_RATIO = 14.0/60.0;
	public static final double WHEEL_CIRCUMFRENCE = 6 * Math.PI; // Value is in inches
	
	public DistanceEncoder(IEncoder encoder, GearShiftStateManager gearShifter) {
		this.encoder = encoder;
		this.gearShifter = gearShifter;
	}
	
	public void updateDistance() {
		// This function should be called often.
		double deltaCount = getRaw() - countAccumulator;
		double deltaDistance = deltaCount * MOTOR_PERIOD * WHEEL_CIRCUMFRENCE;
		GearStates currentGear = gearShifter.getGearState();
		countAccumulator = getRaw();
		
		// Add the distance traveled since last time we checked.
		switch (currentGear) {
		case High:
			totalDistance += deltaDistance * HIGH_GEAR_RATIO;
			break;
		case Low:
			totalDistance += deltaDistance * LOW_GEAR_RATIO;
			break;
		default:
			break;
		}
	}
	
	@Override
	public double getDistance() {
		return totalDistance;
	}
	
	@Override
	public void reset() {
		encoder.reset();
		countAccumulator = 0;
		totalDistance = 0;
	}

	@Override
	public double getRate() {
		return encoder.getRate();
	}

	@Override
	public double getRaw() {
		return encoder.getRaw();
	}
	
	public double getHighGearCountsPerInch() {
		return HIGH_GEAR_RATIO * MOTOR_PERIOD * WHEEL_CIRCUMFRENCE;
	}
	
	public double getLowGearCountsPerInch() {
		return LOW_GEAR_RATIO * MOTOR_PERIOD * WHEEL_CIRCUMFRENCE;
	}
}
