package org.firstinspires.ftc.teamcode.EK10582.auton.action;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.EK10582.auton.action.Action;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.Robot;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.SubsystemConstants;

public class AutonShoot extends Action {
    ElapsedTime timer = new ElapsedTime();
    ElapsedTime servoTimer = new ElapsedTime();

    int shootCounter = 0;

    public void start(){
        timer.reset();
    }

    public void update(){
        if(timer.milliseconds() >= 2000){
            Robot.getInstance().launchMotor.setPower(0);
            isComplete = true;
        }
        else if(timer.milliseconds() >= 600){

            while(shootCounter < 3){
                if (servoTimer.milliseconds() >= 1000) {
                    Robot.getInstance().inServo.setPosition(SubsystemConstants.TRANSFER_REST);
                    servoTimer.reset();
                    shootCounter++;
                }
                else{
                    Robot.getInstance().inServo.setPosition(SubsystemConstants.TRANSFER_POSITION);
                }
            }

        }
        else {
            Robot.getInstance().launchMotor.setPower(0.8);

        }
    }

    public void end(){}
}