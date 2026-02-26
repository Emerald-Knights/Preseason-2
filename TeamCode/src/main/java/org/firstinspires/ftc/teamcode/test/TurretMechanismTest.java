package org.firstinspires.ftc.teamcode.test;


import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


public class TurretMechanismTest {
    private DcMotorEx turret;
    private Limelight3A limelight;

    // Filtering + derivative bookkeeping
    private double errorFilt = 0.0;
    private boolean errorFiltInit = false;

    private double lastErrorFilt = 0.0;

    private double derivFilt = 0.0;

    // Tune these
    private double alpha = 0.1;        // error filter (0.1–0.3)
    private double derivAlpha = 0.1;   // derivative filter (0.1–0.3)
    private double deadbandDeg = 0.5;  // degrees (0.3–1.0)
    private double kS = 0.06;           // static friction feedforward (0.0–0.08)
    private double kP = 0.000001;
    private double kD = 0.00000;
    private double goalTx = 0;
    private double kFF = 0; //turret power per (deg/s)
    private boolean locked = false;
    private double lockInDeg = 0.7;   // enter lock
    private double lockOutDeg = 1.2;  // exit lock (must be > lockInDeg)
    private double lastPowerCmd = 0.0;



    // ... other variables like kP, kD, etc.
    private double lastError = 0;
    // ...


    // ADD THIS METHOD
    public double getError() {
//        return lastError;
        return errorFiltInit ? errorFilt : lastError;
    }

    public void stop(){
        turret.setPower(0);
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


    private double angleTolerage = 0.8;
    private final double MAX_POWER = 0.2;
    private double power = 0;
    private final ElapsedTime timer = new ElapsedTime();

    // init, update, etc.
    public void init (HardwareMap hwMap) {
        turret = hwMap.get(DcMotorEx.class, "spinMotor");
        turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


    }


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


    public void update(LLResult llResult,double robotYawRateDegPerSec) {
        double deltaTime = timer.seconds();
        timer.reset();
        deltaTime = Math.max(deltaTime, 1e-3);


        if (llResult == null || !llResult.isValid()) {
//            turret.setPower(0);
//            lastError = 0;
            stop();
            onNoTarget();
            return;
        }


        //start PID stuff
//        double error = goalTx - llResult.getTx();
//        double pTerm = kP * error;
//        double dTerm = 0;
//        if (deltaTime > 0) {
//            dTerm = kD * (error - lastError) / deltaTime;
//        }
//
//
//        if(Math.abs(error) < angleTolerage) {
//            power = 0;
//        } else {
//            power = Range.clip(pTerm + dTerm, -MAX_POWER, MAX_POWER);
//        }
//
//
//        //safety stuff idk
//        turret.setPower(power);
//        lastError = error;


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
            turret.setPower(0);

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
        turret.setPower(power);

// Update stored errors
        lastError = error;          // raw error for telemetry if you want
        lastErrorFilt = errorFilt;



    }


}
