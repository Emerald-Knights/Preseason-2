package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

//@Disabled
@TeleOp(name="Motor Tester + Encoder")
public class MotorTestWEncoder extends LinearOpMode {
    @Override
    public void runOpMode() {


        DcMotorEx motor = hardwareMap.get(DcMotorEx.class, "catapult1");
        DcMotorEx motor2 = hardwareMap.get(DcMotorEx.class, "catapult2");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();


        while(opModeIsActive()) {

            motor.setPower(0.5 *(gamepad1.left_trigger - gamepad1.right_trigger));
//            motor2.setPower(gamepad2.left_trigger - gamepad2.right_trigger);

            telemetry.addData("motor speed: ", gamepad1.right_trigger-gamepad1.left_trigger);
            telemetry.addData("position: ", motor.getCurrentPosition());
            telemetry.update();
        }
    }
};