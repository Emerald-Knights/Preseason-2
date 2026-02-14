package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

public class TurretAutoAlignOpMode extends OpMode {
    private AprilTagLimelightTest apriltaglimelight = new AprilTagLimelightTest();
    private TurretMechanismTest turret = new TurretMechanismTest();

    @Override
    public void init() {
        apriltaglimelight.init(hardwareMap, telemetry);
        turret.init(hardwareMap);

        telemetry.addLine("initialized all mechanisms");
    }

    @Override
    public void start() {
        turret.resetTimer();
    }

    @Override
    public void loop() {
        apriltaglimelight.update();
        AprilTagDetection id20 = apriltaglimelight.getTagBySpecificID(20);

        turret.update(id20);

        if(id20 != null) {
            telemetry.addData("cur ID", apriltaglimelight);
        } else {
            telemetry.addLine("no tag detected, stopping turret motor");
        }
    }
}
