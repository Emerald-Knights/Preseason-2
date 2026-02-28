package org.firstinspires.ftc.teamcode.EK10582.subsystem;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;


public class Turret extends Subsystem{
        public static double P=0.23, I = 0, D=0, F=12.85;
        public static double kP=0.048001,kD = 0.0000001, kFF = 0.002;
        public double turretRotationInput;
        public boolean aimAssist;
        public boolean activeLaunch;
        public boolean manualOverride;
        public double turretSpinPower;
        boolean latePress = false;

        private double errorFilt = 0.0;
        private boolean errorFiltInit = false;
        private double lastErrorFilt = 0.0;
        private double derivFilt = 0.0;

    // Tune these
        public double alpha = 0.1;        // error filter (0.1–0.3)
        public double derivAlpha = 0.1;   // derivative filter (0.1–0.3)
        public double kS = 0.06;           // static friction feedforward (0.0–0.08)


        public double goalTx = 0;

        public boolean locked = false;
        public double lockInDeg = 0.8;   // enter lock
        public double lockOutDeg = 1.2;  // exit lock (must be > lockInDeg)
        public double lastPowerCmd = 0.0;

        private final ElapsedTime imuTimer = new ElapsedTime();
        private double lastYawDeg = 0.0;
        private double lastImuTime = 0.0;
        private double yawRateDegPerSec = 0.0;


    // ... other variables like kP, kD, etc.
        private double lastError = 0;

        public double targetLaunchVelocity;

        public Pose3D pose;

        public String invalid;

        public boolean detection;

        public double distance;

        public SubsystemConstants.TrackingGoal currentTargetGoal;

        public SubsystemConstants.TurretStates currentState = SubsystemConstants.TurretStates.MANUAL;

        public double getError() {
            return errorFiltInit ? errorFilt : lastError;
        }

        public void turretStop(){
            Robot.getInstance().spinMotor.setPower(0);
        }

        public void onNoTarget() {
            errorFiltInit = false;  // resets filter so we don't spike on reacquire
            derivFilt = 0;
            locked = false;
        }

        public boolean isLocked() { return locked; }
        public double getLastPower() { return power; }

        public void setkFF(double v) { kFF = v; }
        public double getkFF() { return kFF; }
        private double getYawDeg() {
            YawPitchRollAngles a = Robot.getInstance().imu.getRobotYawPitchRollAngles();
            return a.getYaw(AngleUnit.DEGREES);
        }


        private double angleTolerage = 0.8;
        private final double MAX_POWER = 0.2;
        private double power = 0;
        private final ElapsedTime timer = new ElapsedTime();

        public void setkP(double newKP) {
            kP = newKP;
        }
        public double getkP() {
            return kP;
        }


        public void setkD(double newKD) {
            kD = newKD;
        }
        public double getkD() {
            return kD;
        }
        public void resetTimer() {
            timer.reset();
        }

        public void turretUpdate(LLResult llResult,double robotYawRateDegPerSec) {
            double deltaTime = timer.seconds();
            timer.reset();
            deltaTime = Math.max(deltaTime, 1e-3);

            if (llResult == null || !llResult.isValid()) {
                stop();
                onNoTarget();
                return;
            }

            // Raw error in degrees (tx)
            double error = goalTx - llResult.getTx();


    // --- Low-pass filter the error ---
            if (!errorFiltInit) {
                errorFilt = error;
                lastErrorFilt = errorFilt;
                derivFilt = 0.0;
                errorFiltInit = true;
            } else {
                errorFilt = (1 - alpha) * errorFilt + alpha * error;
            }


    // --- Deadband in degrees ---
            double absErr = Math.abs(errorFilt);


            if (locked) {
                if (absErr > lockOutDeg) locked = false;
            } else {
                if (absErr < lockInDeg) locked = true;
            }


            if (locked) {
                power = 0;
                Robot.getInstance().spinMotor.setPower(0);


                derivFilt = 0;           // kill D chatter
                lastError = error;
                lastErrorFilt = errorFilt;


                return;
            }


    // --- Derivative using dt, and filter derivative ---
            double dErr = 0.0;
            if (deltaTime > 0) {
                dErr = (errorFilt - lastErrorFilt) / deltaTime;
            }
            derivFilt = (1 - derivAlpha) * derivFilt + derivAlpha * dErr;


    // --- PD output ---
            double output = (kP * errorFilt) + (kD * derivFilt);


            output += kFF * (-robotYawRateDegPerSec); // Feedforward: counteract robot rotation so tx doesn't "run away"


    // Optional static friction compensation (start with kS = 0)
            if (Math.abs(output) > 1e-6) {
                output += Math.signum(output) * kS;
            }


            if (Math.abs(output) < 0.005) output = 0;


    // Clamp
            power = Range.clip(output, -MAX_POWER, MAX_POWER);
            double maxDelta = 0.03; // tune 0.03–0.08
            power = Range.clip(power, lastPowerCmd - maxDelta, lastPowerCmd + maxDelta);
            lastPowerCmd = power;
            Robot.getInstance().spinMotor.setPower(power);


    // Update stored errors
            lastError = error;          // raw error for telemetry if you want
            lastErrorFilt = errorFilt;
        }

    public double getDistanceFromTags(double ta) {
        double scale = 176.9951;
        double power = -0.5621096;
        double distance = scale * Math.pow(ta, power);
        return distance;
    }


    public void init(boolean isAuton){
//            Robot.getInstance().limelight3A.pipelineSwitch(8); //april tag 24, **add feature for later to switch with button?**
            manualOverride = false;
            currentState = SubsystemConstants.TurretStates.MANUAL;
            activeLaunch = false;
            targetLaunchVelocity = SubsystemConstants.mediumVelocity;

            PIDFCoefficients pidfCoefficients =  new PIDFCoefficients(P,I,D,F);
            PIDFCoefficients turretpidf = new PIDFCoefficients(kP,0,kD,kFF);
            Robot.getInstance().launchMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
            Robot.getInstance().launchMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            Robot.getInstance().spinMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, turretpidf);
            Robot.getInstance().spinMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            Robot.getInstance().limelight3A.pipelineSwitch(8); //add functionality to switch targeting
            Robot.getInstance().limelight3A.start();

            imuTimer.reset();
            lastImuTime = imuTimer.seconds();
            lastYawDeg = getYawDeg();

        }

    public void update(boolean isAuton){
       LLResult llResult = Robot.getInstance().limelight3A.getLatestResult();

       if (llResult != null && llResult.isValid()) {
          pose = llResult.getBotpose();     // MT1
                      // toString() usually prints some    thing useful

       } else {
                invalid = "no valid LLResult";
       }

       double now = imuTimer.seconds();
       double dt = Math.max(now - lastImuTime, 1e-3);

       double yawDeg = getYawDeg();
       double dyaw = AngleUnit.normalizeDegrees(yawDeg - lastYawDeg);

       yawRateDegPerSec = dyaw / dt;

       lastYawDeg = yawDeg;
       lastImuTime = now;

       detection = llResult != null && llResult.isValid();
       if (detection) {
            Robot.getInstance().turret.turretUpdate(llResult,yawRateDegPerSec);
       } else {
            Robot.getInstance().turret.stop();              // implement stop() = motor.setPower(0)
            Robot.getInstance().turret.onNoTarget();        // optional: reset filters/lastError safely
        }


       //flywheel motor
        if (!isAuton) {
            if(activeLaunch){
                Robot.getInstance().launchMotor.setVelocity(targetLaunchVelocity);
            }else{
                Robot.getInstance().launchMotor.setVelocity(0);
            }
        }

        //manual hood + turret angle adjust
        if(aimAssist){
            if(currentState == SubsystemConstants.TurretStates.MANUAL){
                currentState = SubsystemConstants.TurretStates.AUTO;
                }
            else{
                currentState = SubsystemConstants.TurretStates.MANUAL;
            }
        }
        if(currentState == SubsystemConstants.TurretStates.MANUAL){
            if(Math.abs(turretRotationInput) > 0.1){
                turretSpinPower = turretRotationInput*0.4; //can change the increment value
                Robot.getInstance().spinMotor.setPower(-turretSpinPower);
            }else{
                Robot.getInstance().spinMotor.setPower(0);
            }
        }
        else if(currentState == SubsystemConstants.TurretStates.AUTO){

        }
        //distance scaling
        if (llResult != null && llResult.isValid()) {
            Pose3D botPose = llResult.getBotpose_MT2();
            distance = getDistanceFromTags(llResult.getTa());
        }
        if(distance <= 150){
            targetLaunchVelocity = SubsystemConstants.lowVelocity;
        }else if(distance <= 235){
            targetLaunchVelocity = SubsystemConstants.mediumVelocity;
        }else{
            targetLaunchVelocity = SubsystemConstants.highVelocity;
        }
    }

        public void stop(){

        }
        public void printToTelemetry(Telemetry telemetry){
            telemetry.addData("Turret State", currentState);
            telemetry.addData("Launch?", activeLaunch);
            telemetry.addData("Target Velocity", targetLaunchVelocity);
            telemetry.addData("botpose", pose);
            telemetry.addData("invalid", invalid);
            telemetry.addData("tag detected?", detection);
            telemetry.addData("error", Robot.getInstance().turret.getError());
        }
    }


