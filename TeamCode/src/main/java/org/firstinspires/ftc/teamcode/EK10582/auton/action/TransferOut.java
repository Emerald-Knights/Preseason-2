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
            isComplete = true;
        }
        else {
//            Robot.getInstance().inServo.setPosition(SubsystemConstants.TRANSFER_REST);

        }
    }

    public void end(){}
}