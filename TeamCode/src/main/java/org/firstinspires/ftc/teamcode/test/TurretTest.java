package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

//@Disabled
@TeleOp(name="Turret Tester")
public class TurretTest extends LinearOpMode {
    @Override
    public void runOpMode() {

        waitForStart();

        DcMotorEx motor = hardwareMap.get(DcMotorEx.class, "catapult1");

        while(opModeIsActive()) {

            motor.setPower(gamepad1.left_trigger - gamepad1.right_trigger);

            telemetry.addData("motor speed: ", gamepad1.right_trigger-gamepad1.left_trigger);
//            telemetry.addData("position: ", motor.getCurrentPosition());
            telemetry.update();
        }
    }
};