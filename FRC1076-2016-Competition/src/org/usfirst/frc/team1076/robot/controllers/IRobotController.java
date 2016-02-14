package org.usfirst.frc.team1076.robot.controllers;

import org.usfirst.frc.team1076.robot.IRobot;

public interface IRobotController {
    public void robotInit(IRobot robot);
    public void autonomousInit(IRobot robot);
    public void autonomousPeriodic(IRobot robot);
    public void teleopInit(IRobot robot);
    public void teleopPeriodic(IRobot robot);
}
