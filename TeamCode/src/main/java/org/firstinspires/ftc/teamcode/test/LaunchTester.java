package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

//@Disabled
@TeleOp(name="Launch Tester")
public class LaunchTester extends LinearOpMode {
    @Override
    public void runOpMode() {

        waitForStart();

        DcMotorEx motor = hardwareMap.get(DcMotorEx.class, "catapult1");
        DcMotorEx motor2 = hardwareMap.get(DcMotorEx.class, "catapult2");

        while(opModeIsActive()) {

            motor.setPower(gamepad1.left_trigger - gamepad1.right_trigger);
            motor2.setPower(-(gamepad1.left_trigger - gamepad1.right_trigger));

            telemetry.addData("motor speed: ", gamepad1.right_trigger-gamepad1.left_trigger);
//            telemetry.addData("position: ", motor.getCurrentPosition());
            telemetry.update();
        }
    }
};