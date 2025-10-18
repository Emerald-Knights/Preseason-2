package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.EK10582.EKLinear;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.Robot;

@TeleOp(name="Servo Tester")
public class ServoTester extends EKLinear {

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();

        Servo shooter = hardwareMap.get(Servo.class, "shooter");

        double targetPos1 = 0.5;

        while(opModeIsActive()) {


            targetPos1 += gamepad2.left_stick_y * 0.001;
            if(targetPos1 >= 1){
                targetPos1 = 1;
            }
            if(targetPos1 <= 0){
                targetPos1 = 0;
            }

            shooter.setPosition(targetPos1);

            telemetry.addData("claw: ", targetPos1);
            telemetry.update();
        }
    }
}
