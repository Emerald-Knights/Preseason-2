package org.firstinspires.ftc.teamcode.EK10582.auton.action;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.EK10582.subsystem.Robot;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.SubsystemConstants;

public class DownCatapult extends Action {
    ElapsedTime timer = new ElapsedTime();

    public void start() {
        timer.reset();
    }

    public void update() {


        if (timer.milliseconds() >= 2500 || Robot.getInstance().catapult.currentState == SubsystemConstants.CatapultStates.DOWN ) {

            Robot.getInstance().catapult1.setPower(0);
            Robot.getInstance().catapult2.setPower(0);
            isComplete = true;

        } else if (Robot.getInstance().catapult1.getCurrentPosition() < SubsystemConstants.CATAPULT_TOLERANCE && timer.milliseconds() < 1000) {
            Robot.getInstance().catapult1.setPower(0);
            Robot.getInstance().catapult2.setPower(0);
            Robot.getInstance().catapult1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            Robot.getInstance().catapult2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            Robot.getInstance().catapult1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            Robot.getInstance().catapult2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            Robot.getInstance().catapult.currentState = SubsystemConstants.CatapultStates.DOWN;

        } else if (Robot.getInstance().catapult1.getCurrentPosition() >= SubsystemConstants.CATAPULT_TOLERANCE) {
            Robot.getInstance().catapult1.setPower(-SubsystemConstants.CATAPULT_POWER);
            Robot.getInstance().catapult2.setPower(-SubsystemConstants.CATAPULT_POWER);//down


        }
    }

    public void end() {
    }
}