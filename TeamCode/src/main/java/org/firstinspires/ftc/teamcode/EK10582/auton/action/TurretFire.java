package org.firstinspires.ftc.teamcode.EK10582.auton.action;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.EK10582.auton.action.Action;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.Robot;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.SubsystemConstants;

public class TurretFire extends Action{

    ElapsedTime timer = new ElapsedTime();
    public void start() { timer.reset();}

    public void update(){
        if(timer.milliseconds() >= 2500){
            Robot.getInstance().launchMotor.setPower(0);
            isComplete = true;
        }
        else if(timer.milliseconds() >= 0){
            Robot.getInstance().launchMotor.setPower(1);

        }

    }

    public void end(){

    }

}
