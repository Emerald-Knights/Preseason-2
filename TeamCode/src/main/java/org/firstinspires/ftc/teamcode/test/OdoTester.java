package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.EK10582.EKLinear;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.Robot;
import com.pedropathing.ftc.localization.Encoder;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="Odo Test")
public class OdoTester extends LinearOpMode {

    DcMotorEx leftEncoder;
    DcMotorEx rightEncoder;
    DcMotorEx frontEncoder;


    double currentFront, currentLeft, currentRight;

    @Override
    public void runOpMode() {

        leftEncoder = hardwareMap.get(DcMotorEx.class, "odo1");
        rightEncoder = hardwareMap.get(DcMotorEx.class, "odo2");
        frontEncoder = hardwareMap.get(DcMotorEx.class, "odo3");

//        leftEncoder.setDirection(Encoder.REVERSE);
//        rightEncoder.setDirection(Encoder.REVERSE);
//        frontEncoder.setDirection(Encoder.REVERSE);
        waitForStart();

        currentRight = 0;
        currentFront = 0;
        currentLeft = 0;
        while(opModeIsActive()) {
//            currentFront += frontEncoder.getDeltaPosition();
//            currentLeft += leftEncoder.getDeltaPosition();
//            currentRight += rightEncoder.getDeltaPosition();


            telemetry.addData("backOdo", frontEncoder.getCurrentPosition());
            telemetry.addData("rightOdo", leftEncoder.getCurrentPosition());
            telemetry.addData("leftOdo", rightEncoder.getCurrentPosition());
            telemetry.update();
        }
    }
}