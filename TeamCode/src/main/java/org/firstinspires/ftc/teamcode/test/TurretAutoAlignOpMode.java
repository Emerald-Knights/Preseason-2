package org.firstinspires.ftc.teamcode.test;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;



import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import org.firstinspires.ftc.teamcode.EK10582.subsystem.Robot;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.SubsystemConstants;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
@TeleOp(name = "Turret Turn PID", group = "Examples")


public class TurretAutoAlignOpMode extends OpMode {
    //    private AprilTagLimelightTest apriltaglimelight = new AprilTagLimelightTest();
    public DcMotorEx leftFront, leftBack, rightFront, rightBack;
    private Limelight3A limelight;
    private TurretMechanismTest turret = new TurretMechanismTest();

    private IMU imu;
    private final ElapsedTime imuTimer = new ElapsedTime();
    private double lastYawDeg = 0.0;
    private double lastImuTime = 0.0;
    private double yawRateDegPerSec = 0.0;


    private double[] stepSizes = {10.0, 1.0, 0.1, 0.01,0.001,0.0001,0.00001,0.000001,0.0000001};

    //P = 0.07, D= 0.0007

    int stepIndex = 2;

    public double ly,lx,rx;
    public double speed = SubsystemConstants.SPEED;
    public double ratio;
    public double slowMode;

    private double filterJoystick(double input) {
        //implements both deadzone and scaled drive
        if(Math.abs(input) < 0.01) return 0;
        if(input > 0) {
            return 0.1 * Math.pow((1/0.1), input);
        } else {
            input *= -1;
            return -0.1* Math.pow((1 /0.1), input);
        }
    }

    public double getLeftStickY() {
        return -filterJoystick(gamepad1.left_stick_y);
    }

    public double getLeftStickX() {
        return filterJoystick(gamepad1.left_stick_x);
    }

    public double getRightStickX() {
        return filterJoystick(gamepad1.right_stick_x);
    }

    private double getYawDeg() {
        YawPitchRollAngles a = imu.getRobotYawPitchRollAngles();
        return a.getYaw(AngleUnit.DEGREES);
    }


    @Override
    public void init() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(8);
        limelight.start();

        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        rightBack = hardwareMap.get(DcMotorEx.class, "rightBack");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        turret.init(hardwareMap);

        imu = hardwareMap.get(IMU.class, "imu"); // usually "imu" on Control Hub

        imuTimer.reset();
        lastImuTime = imuTimer.seconds();
        lastYawDeg = getYawDeg();


        telemetry.addLine("initialized all mechanisms");

        slowMode = 1;
    }


    @Override
    public void start() {
        turret.resetTimer();
    }


    @Override
    public void loop() {

        lx = getLeftStickX();
        ly = getLeftStickY();
        rx = getRightStickX();


        LLResult llResult = limelight.getLatestResult();

        if (llResult != null && llResult.isValid()) {
            Pose3D pose = llResult.getBotpose();     // MT1
            telemetry.addData("botpose", pose);      // toString() usually prints some    thing useful

            Pose3D pose2 = llResult.getBotpose_MT2(); // MT2 (needs robot orientation set)
            telemetry.addData("botposeMT2", pose2);
        } else {
            telemetry.addLine("no valid LLResult");
        }




        if(gamepad1.bWasPressed()){
            stepIndex = (stepIndex + 1) % stepSizes.length;
        }
        if(gamepad1.dpadLeftWasPressed()){
            turret.setkP(turret.getkP() - stepSizes[stepIndex]);
        }
        if(gamepad1.dpadRightWasPressed()){
            turret.setkP(turret.getkP() + stepSizes[stepIndex]);
        }
        if(gamepad1.dpadUpWasPressed()){
            turret.setkD(turret.getkD() + stepSizes[stepIndex]);
        }
        if(gamepad1.dpadDownWasPressed()){
            turret.setkD(turret.getkD() - stepSizes[stepIndex]);
        }
        if(gamepad1.yWasPressed()){
            turret.setkFF(turret.getkFF() + stepSizes[stepIndex]);
        }
        if(gamepad1.aWasPressed()){
            turret.setkFF(turret.getkFF() - stepSizes[stepIndex]);
        }

        double now = imuTimer.seconds();
        double dt = Math.max(now - lastImuTime, 1e-3);

        double yawDeg = getYawDeg();
        double dyaw = AngleUnit.normalizeDegrees(yawDeg - lastYawDeg);

        yawRateDegPerSec = dyaw / dt;

        lastYawDeg = yawDeg;
        lastImuTime = now;
        //turret.update(llResult);

        //chat gpt change
        boolean detection = llResult != null && llResult.isValid();
        if (detection) {
            turret.update(llResult,yawRateDegPerSec);
        } else {
            turret.stop();              // implement stop() = motor.setPower(0)
            turret.onNoTarget();        // optional: reset filters/lastError safely
        }


        if (detection) {
            telemetry.addLine("id detected");
        } else {
            telemetry.addLine("no tag detected, stopping turret motor");
        }
        telemetry.addData("error", turret.getError());



        telemetry.addLine("---------------------------");
        telemetry.addData("Tuning P", "%.7f (D-Pad L/R)", turret.getkP());
        telemetry.addData("Tuning D", "%.7f (D-Pad U/D)", turret.getkD());
        telemetry.addData("Tuning FF", "%.7f (Y up/A down)", turret.getkFF());
        telemetry.addData("Step Size", "%.7f (B Button)", stepSizes[stepIndex]);;

        telemetry.addData("tx", (llResult != null && llResult.isValid()) ? llResult.getTx() : 999);
        telemetry.addData("kP", turret.getkP());
        telemetry.addData("kD", turret.getkD());
        telemetry.addData("kFF", turret.getkFF());
        telemetry.addData("locked", turret.isLocked());
        telemetry.addData("cmdPower", turret.getLastPower());



        double lf = ly + lx + rx;
        double rf = ly - lx - rx;
        double lb = ly - lx + rx;
        double rb = ly + lx - rx;

        //Scales the values so none of them exceeds one while still maintaining max possible speed
        double max = Math.max(Math.max(Math.abs(lb), Math.abs(lf)), Math.max(Math.abs(rb), Math.abs(rf)));
        double magnitude = Math.sqrt((lx * lx) + (ly * ly) + (rx * rx));
        //Avoids dividing by 0
        if (max == 0) {
            ratio = 0;
        }
        //If magnitude is greater than 1 divide by max speed
        else if (magnitude > 1){
            ratio = 1/max;
        }
        //If magnitude is less than 1 scale by magnitude to maintain lower speeds
        else {
            ratio = magnitude / max * speed;
        }

        leftFront.setPower(lf*ratio*slowMode);
        rightFront.setPower(rf*ratio*slowMode);
        leftBack.setPower(lb*ratio*slowMode);
        rightBack.setPower(rb*ratio*slowMode);




    }
}
