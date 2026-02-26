package org.firstinspires.ftc.teamcode.EK10582.auton.action;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.EK10582.auton.action.Action;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.Robot;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.SubsystemConstants;

public class AutonShoot extends Action {
    ElapsedTime timer = new ElapsedTime();
    ElapsedTime servoTimer = new ElapsedTime();

    double P = 0.23;
    double F = 12.85;


    int shootCounter = 0;

    public void start(){
        timer.reset();
        PIDFCoefficients pidfCoefficients =  new PIDFCoefficients(P,0,0,F);
        Robot.getInstance().launchMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
    }

    public void update(){


        if(timer.milliseconds() >= 5000 || shootCounter < 3){
            Robot.getInstance().launchMotor.setVelocity(0);
            isComplete = true;
        }
        else if(timer.milliseconds() >= 600){
                if (servoTimer.milliseconds() >= 1000) {
                    Robot.getInstance().inServo.setPosition(SubsystemConstants.TRANSFER_REST);
                    servoTimer.reset();
                    shootCounter++;
                }
                else{
                    Robot.getInstance().inServo.setPosition(SubsystemConstants.TRANSFER_POSITION);
                }

        }
        else {
            Robot.getInstance().launchMotor.setVelocity(SubsystemConstants.mediumVelocity);

        }
    }

    public void end(){}
}