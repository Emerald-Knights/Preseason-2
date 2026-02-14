package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp(name = "Flywheel PID Tuner")
public class FlywheelPIDTuner extends OpMode {
    public DcMotorEx flywheelMotor;

    public double highVelocity = 1500;

    public double lowVelocity = 900;

    double currentTargetVelocity = highVelocity;

    private double F = 0; //24
    private double P = 0;//200

    private double[] stepSizes = {10.0, 1.0, 0.1, 0.001,0.0001};

    int stepIndex = 1;
    @Override

    public void init(){
        flywheelMotor = hardwareMap.get(DcMotorEx.class,"flywheelMotor");
        flywheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheelMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        PIDFCoefficients pidfCoefficients =  new PIDFCoefficients(P,0,0,F);
        flywheelMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        telemetry.addLine("Init complete");
    }
    public void loop(){
        //get all our gamepad commands
        //set target velocity
        //update telemetry

        if(gamepad1.yWasPressed()){
            if(currentTargetVelocity == highVelocity){
                currentTargetVelocity = lowVelocity;
            }
            else{
                currentTargetVelocity = highVelocity;

            }
        }
        if(gamepad1.bWasPressed()){
            stepIndex = (stepIndex+1)%stepSizes.length;
        }
        if(gamepad1.dpadLeftWasPressed()){
            F += stepSizes[stepIndex];
        }
        if(gamepad1.dpadRightWasPressed()){
            F -= stepSizes[stepIndex];
        }
        if(gamepad1.dpadUpWasPressed()){
            P += stepSizes[stepIndex];
        }
        if(gamepad1.dpadDownWasPressed()){
            P -= stepSizes[stepIndex];
        }

        //set new PIDF coefficients
        PIDFCoefficients pidfCoefficients =  new PIDFCoefficients(P,0,0,F);
        flywheelMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        //set velocity
        flywheelMotor.setVelocity(currentTargetVelocity);

        double curVelocity = flywheelMotor.getVelocity();
        double error = currentTargetVelocity-curVelocity;

        //telemetry
        telemetry.addData("Target Velocity", currentTargetVelocity);
        telemetry.addData("Current Velocity","%.2f", curVelocity);
        telemetry.addData("Error", "%.2f",error);
        telemetry.addLine("---------------------------");
        telemetry.addData("Tuning P", "%.4f (D-Pad U/D", P);
        telemetry.addData("Tuning F", "%.4f (D-Pad L/R", F);
        telemetry.addData("Step Size", "%.4f (B Button)", stepSizes[stepIndex]);
        telemetry.update();

    }
}
