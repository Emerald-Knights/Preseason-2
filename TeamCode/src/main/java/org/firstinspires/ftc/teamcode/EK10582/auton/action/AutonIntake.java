package org.firstinspires.ftc.teamcode.EK10582.auton.actions;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.EK10582.auton.action.Action;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.Robot;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.SubsystemConstants;

public class AutonIntake extends Action {
    ElapsedTime timer = new ElapsedTime();
    public void start(){
        timer.reset();
    }

    public void update(){
        if(timer.milliseconds() >= 2000){
            Robot.getInstance().intakeMotor.setPower(0);
            isComplete = true;
        }
        else if(timer.milliseconds() >= 0){
            Robot.getInstance().intakeMotor.setPower(0.8);
            Robot.getInstance().leftFront.setPower(0.5);
            Robot.getInstance().leftBack.setPower(0.5);
            Robot.getInstance().rightBack.setPower(0.5);
            Robot.getInstance().rightFront.setPower(0.5);


        }
    }

    public void end(){}
}