package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.EK10582.EKLinear;

@TeleOp(name="April Tag Tester")

public class AprilTagTester extends EKLinear {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();

        while(opModeIsActive()) {

            //when A is pressed, change target color from blue to yellow to red

            robot.update();
        }
    }
}

