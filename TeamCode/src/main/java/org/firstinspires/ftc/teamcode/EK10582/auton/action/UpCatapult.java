package org.firstinspires.ftc.teamcode.EK10582.auton.action;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.EK10582.subsystem.Robot;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.SubsystemConstants;

public class UpCatapult extends Action {
    ElapsedTime timer = new ElapsedTime();

    public void start() {
        timer.reset();
    }

    public void update() {


        if (timer.milliseconds() >= 2500 || Robot.getInstance().catapult1.getCurrentPosition() >= SubsystemConstants.CATAPULT_UP_POSITION) {

            Robot.getInstance().catapult1.setPower(0);
            Robot.getInstance().catapult2.setPower(0);
            Robot.getInstance().catapult.currentState = SubsystemConstants.CatapultStates.UP;
            isComplete = true;

        } else if (Robot.getInstance().catapult1.getCurrentPosition() >= SubsystemConstants.CATAPULT_UP_POSITION - SubsystemConstants.CATAPULT_TOLERANCE) {
            Robot.getInstance().catapult1.setPower(0.1);
            Robot.getInstance().catapult2.setPower(0.1);

        } else if (timer.milliseconds() >= 0) {
            Robot.getInstance().catapult1.setPower(SubsystemConstants.CATAPULT_POWER);
            Robot.getInstance().catapult2.setPower(SubsystemConstants.CATAPULT_POWER);


        }
    }

    public void end() {
    }
}