package org.usfirst.frc.team1076.robot.controllers;

import org.usfirst.frc.team1076.robot.IRobot;

public interface IRobotController {
    public enum ArmPneumaticState {On, Off};
    public void autonomousInit(IRobot robot);
    public void autonomousPeriodic(IRobot robot);
    public void teleopInit(IRobot robot);
    public void teleopPeriodic(IRobot robot);
    public void testInit(IRobot robot);
    public void testPeriodic(IRobot robot);
    public boolean replayActivated();
}
