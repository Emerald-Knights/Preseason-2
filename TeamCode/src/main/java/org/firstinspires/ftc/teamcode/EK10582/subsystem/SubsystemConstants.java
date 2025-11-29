package org.firstinspires.ftc.teamcode.EK10582.subsystem;

import com.qualcomm.robotcore.util.ElapsedTime;

public class SubsystemConstants {
    public static double SPEED = 0.8;

    //catapult shi
    public enum CatapultStates {
        UP, DOWN;
    }
    public static double CATAPULT_POWER = 0.8;

    public static double CATAPULT_UP_POSITION = 20 ;

    //To prevent the catapult from slamming down or coasting over
    public static double CATAPULT_TOLERANCE = 0 ;

}