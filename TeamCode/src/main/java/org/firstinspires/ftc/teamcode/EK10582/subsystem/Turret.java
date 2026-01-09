package org.firstinspires.ftc.teamcode.EK10582.subsystem;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Turret extends AprilTags{
        public static double p, i = 0, d=0, f;
        public double turretRotationInput;
        public double hoodAngleInput;
        public boolean manualOverride;
        boolean latePress = false;

        double lowerLimit, upperLimit;

        double turretTargetPosition = 0.5;
        double hoodAngleTargetPos = 0.5;

        public SubsystemConstants.TurretStates currentState = SubsystemConstants.TurretStates.MANUAL;


        public void init(boolean isAuton){
            Robot.getInstance().limelight3A.pipelineSwitch(8); //april tag 24, **add feature for later to switch with button?**
            manualOverride = false;
            currentState = SubsystemConstants.TurretStates.MANUAL;


        }
        public void update(boolean isAuton){
            if(currentState == SubsystemConstants.TurretStates.MANUAL){
                if(Math.abs(turretRotationInput) > 0.05){
                    turretTargetPosition += turretRotationInput* 0.001; //can change the increment value
                    Robot.getInstance().turretServo.setPosition(turretTargetPosition);
                    if(turretTargetPosition >= 1){
                        turretTargetPosition = 1;
                    }
                    if(turretTargetPosition <= 0){
                        turretTargetPosition = 0;
                    }
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

        }

        public boolean isTargetVisible(){
            LLResult llResult = Robot.getInstance().limelight3A.getLatestResult();
            return llResult != null && llResult.isValid();
        }


    }


