package org.firstinspires.ftc.teamcode.EK10582.auton;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.EK10582.EKLinear;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.Robot;
import org.firstinspires.ftc.teamcode.EK10582.auton.action.Action;
import org.firstinspires.ftc.teamcode.EK10582.teleop.DriverStation;


public abstract class AutonBase extends EKLinear {

    public void runAction(Action action) { //only used in synchronous auton b/c while loop is blocking
        action.start();
        while(!action.isComplete) {
            action.update();
            Robot.getInstance().update();
        }
        action.end();
    }

    @Override
    public void waitForStart() {
        //initing things
        isAuton = true;
        allTelemetry = new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), this.telemetry);
        matchTimer = new ElapsedTime();
        driverStation = new DriverStation(gamepad1, gamepad2);
        robot.init(hardwareMap, this);

        while(!isStarted() && !isStopRequested()){
            telemetry.addData("Waiting for Start","");
            telemetry.update();
        }

        //after start
        matchTimer.reset();
    }

}
