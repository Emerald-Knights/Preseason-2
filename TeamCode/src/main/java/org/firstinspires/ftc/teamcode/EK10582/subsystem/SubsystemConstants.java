package org.firstinspires.ftc.teamcode.EK10582.subsystem;

import com.qualcomm.robotcore.util.ElapsedTime;

public class SubsystemConstants {
    public static double SPEED = 0.8;

    //turret shi
    public enum TurretStates {
        AUTO, MANUAL;
    }

    //turret launch velocities
    public static double lowVelocity = 900;
    public static double highVelocity = 1500;

    //red or blue tracking
    public enum TrackingGoal{
        RED, BLUE;
    }

    //change values later



}