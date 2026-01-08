package org.firstinspires.ftc.teamcode.EK10582.auton.action;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.EK10582.auton.action.Action;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.Robot;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.SubsystemConstants;

public class TransferOut extends Action {
    ElapsedTime timer = new ElapsedTime();
    public void start(){
        timer.reset();
    }

    public void update(){
        if(timer.milliseconds() >= 1500){
            Robot.getInstance().inServo.setPosition(SubsystemConstants.TRANSFER_REST);
            isComplete = true;
        }
        else if(timer.milliseconds() >= 0){
            Robot.getInstance().transferServo.setPower(0);


        }
    }

    public void end(){}
}