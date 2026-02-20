package org.firstinspires.ftc.teamcode.EK10582.auton.action;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.EK10582.auton.action.Action;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.Robot;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.SubsystemConstants;

public class AutonShoot extends Action {
    ElapsedTime timer = new ElapsedTime();
    public void start(){
        timer.reset();
    }

    public void update(){
        if(timer.milliseconds() >= 2000){
            Robot.getInstance().launchMotor.setPower(0);
            Robot.getInstance().inServo.setPosition(SubsystemConstants.TRANSFER_REST);
            isComplete = true;
        }
        else if(timer.milliseconds() >= 600){
            Robot.getInstance().launchMotor.setPower(0.8);
        }
        else {
            Robot.getInstance().inServo.setPosition(SubsystemConstants.TRANSFER_POSITION);
        }
    }

    public void end(){}
}