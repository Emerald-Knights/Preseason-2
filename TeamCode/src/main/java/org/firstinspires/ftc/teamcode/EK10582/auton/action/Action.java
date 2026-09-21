package org.firstinspires.ftc.teamcode.EK10582.auton.action;

public abstract class Action {
    //this is an abstract parent class for all of the actions the robot will do.
    //some examples of actions are: moveArm, collectPixel, dropPixel, etc.
    public boolean isComplete = false;

    public abstract void start();

    public abstract void update();

    public abstract void end();
}

