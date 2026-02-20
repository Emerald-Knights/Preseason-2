package org.firstinspires.ftc.teamcode.EK10582.subsystem;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class Turret extends Subsystem{
        public static double P=0, I = 0, D=0, F=14;
        public double turretRotationInput;
        public double hoodAngleInput;
        public boolean activeLaunch;
        public boolean manualOverride;
        public double turretSpinPower;
        boolean latePress = false;

        public double targetLaunchVelocity;
        double hoodAngleTargetPos = 0.5;

        private double distance;

        public SubsystemConstants.TrackingGoal currentTargetGoal;

        public SubsystemConstants.TurretStates currentState = SubsystemConstants.TurretStates.MANUAL;


        public void init(boolean isAuton){
//            Robot.getInstance().limelight3A.pipelineSwitch(8); //april tag 24, **add feature for later to switch with button?**
            manualOverride = false;
            currentState = SubsystemConstants.TurretStates.MANUAL;
            activeLaunch = false;
            targetLaunchVelocity = SubsystemConstants.highVelocity;

            PIDFCoefficients pidfCoefficients =  new PIDFCoefficients(P,I,D,F);
            Robot.getInstance().launchMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
            Robot.getInstance().launchMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

//            Robot.getInstance().limelight3A.pipelineSwitch(8);
//            Robot.getInstance().limelight3A.start();


        }
        public void update(boolean isAuton){
            //motor power
            if(activeLaunch){
                Robot.getInstance().launchMotor.setVelocity(targetLaunchVelocity);
            }else{
                Robot.getInstance().launchMotor.setVelocity(0);
            }
            //manual hood + turret angle adjust
            if(currentState == SubsystemConstants.TurretStates.MANUAL){
                if(Math.abs(turretRotationInput) > 0.1){
                    turretSpinPower = turretRotationInput*0.5; //can change the increment value
                    Robot.getInstance().spinMotor.setPower(turretSpinPower);
                }else{
                    Robot.getInstance().spinMotor.setPower(0);
                }
                if(Math.abs(hoodAngleInput) > 0.05){
                    hoodAngleTargetPos += hoodAngleInput * 0.001;
                    Robot.getInstance().hoodAngleServo.setPosition(hoodAngleTargetPos);
                    if(hoodAngleTargetPos >= 1){
                        hoodAngleTargetPos = 1;
                    }
                    if(hoodAngleTargetPos <= 0){
                        hoodAngleTargetPos = 0;
                    }
                }
            }
            else if(currentState == SubsystemConstants.TurretStates.AUTO){

            }
        }

        public void stop(){

        }
        public void printToTelemetry(Telemetry telemetry){
            telemetry.addData("Turret State", currentState);
            telemetry.addData("Launch?", activeLaunch);
            telemetry.addData("Target Velocity", targetLaunchVelocity);
        }

//        public boolean isTargetVisible(){
//            LLResult llResult = Robot.getInstance().limelight3A.getLatestResult();
//            return llResult != null && llResult.isValid();
//        }


    }


