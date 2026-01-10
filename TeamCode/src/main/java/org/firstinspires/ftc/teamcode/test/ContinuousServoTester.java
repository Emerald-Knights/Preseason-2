package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Continuous Servo Tester")
public class ContinuousServoTester extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();

       CRServo shooter = hardwareMap.get(CRServo.class, "shooter");

        double targetPos1 = 0.5;

        while(opModeIsActive()) {


            targetPos1 += gamepad2.left_stick_y;

            shooter.setPower(targetPos1);

            telemetry.addData("claw: ", targetPos1);
            telemetry.update();
        }
    }
}
