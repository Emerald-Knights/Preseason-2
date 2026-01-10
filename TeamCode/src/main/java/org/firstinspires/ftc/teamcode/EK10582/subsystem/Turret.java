package org.firstinspires.ftc.teamcode.EK10582.subsystem;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Turret extends Subsystem{
        public static double p, i = 0, d=0, f;
        public double turretRotationInput;
        public double hoodAngleInput;
        public boolean sortLeft;
        public boolean sortRight;
        public boolean activeLaunch;
        public boolean manualOverride;
        boolean latePress = false;

        double lowerLimit, upperLimit;

        double turretSpinPower;
        double hoodAngleTargetPos = 0.5;

        public SubsystemConstants.TurretStates currentState = SubsystemConstants.TurretStates.MANUAL;


        public void init(boolean isAuton){
//            Robot.getInstance().limelight3A.pipelineSwitch(8); //april tag 24, **add feature for later to switch with button?**
            manualOverride = false;
            currentState = SubsystemConstants.TurretStates.MANUAL;
            activeLaunch = false;


        }
        public void update(boolean isAuton){
            if(activeLaunch){
                Robot.getInstance().launchMotor.setPower(1);
            }else{
                Robot.getInstance().launchMotor.setPower(0);
            }
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
                // Cleaner, more robust version
                if (sortLeft) {
                    // Math.signum returns 1.0 for positive, -1.0 for negative
                    Robot.getInstance().sortServo.setPower(0.7);
                } else if (sortRight){
                    Robot.getInstance().sortServo.setPower(-0.7);
                }else{
                    Robot.getInstance().sortServo.setPower(0);
                }

            }
            else if(currentState == SubsystemConstants.TurretStates.AUTO){

            }
        }

        public void stop(){

        }
        public void printToTelemetry(Telemetry telemetry){
            telemetry.addData("Turret State", currentState);
            telemetry.addData("Sort Left", sortLeft);
            telemetry.addData("Launch?", activeLaunch);
        }

//        public boolean isTargetVisible(){
//            LLResult llResult = Robot.getInstance().limelight3A.getLatestResult();
//            return llResult != null && llResult.isValid();
//        }


    }


