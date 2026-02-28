package org.firstinspires.ftc.teamcode.EK10582.auton.action;

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
        if(timer.milliseconds() >= 2150){
            Robot.getInstance().intakeMotor.setPower(0);
            isComplete = true;
        }
        else if(timer.milliseconds() >= 2000){
            Robot.getInstance().intakeMotor.setPower(-0.4);
        }
        else{
            Robot.getInstance().intakeMotor.setPower(0.9);
        }
    }

    public void end(){}
}