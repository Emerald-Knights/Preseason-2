package org.firstinspires.ftc.teamcode.EK10582.subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Catapult extends Subsystem {

    public boolean catapultUpButton = false;
    public boolean catapultDownButton = false;

    private boolean isAutoDownActive = false;



    ElapsedTime catapultTime = new ElapsedTime();
    public static final double downTime = 1000;



    public SubsystemConstants.CatapultStates currentState = SubsystemConstants.CatapultStates.DOWN;

    @Override
    public void init(boolean isAuton){
        catapultUpButton=false;
        currentState = SubsystemConstants.CatapultStates.DOWN;
        Robot.getInstance().catapult1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Robot.getInstance().catapult2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Robot.getInstance().catapult1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Robot.getInstance().catapult2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Robot.getInstance().catapult1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Robot.getInstance().catapult2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



    }
    @Override
    public void update(boolean isAuton){
        if(catapultUpButton && catapultDownButton){
            catapultUpButton = false;

        }

        if(catapultUpButton && currentState == SubsystemConstants.CatapultStates.DOWN) {
            if(Robot.getInstance().catapult1.getCurrentPosition() >= SubsystemConstants.CATAPULT_UP_POSITION){
                Robot.getInstance().catapult1.setPower(0);
                Robot.getInstance().catapult2.setPower(0);
                currentState = SubsystemConstants.CatapultStates.UP;
                isAutoDownActive = true;
                catapultTime.reset();

            }
            else if(Robot.getInstance().catapult1.getCurrentPosition() >= SubsystemConstants.CATAPULT_UP_POSITION - SubsystemConstants.CATAPULT_TOLERANCE){
                Robot.getInstance().catapult1.setPower(0.1);
                Robot.getInstance().catapult2.setPower(0.1);
            }
            else{
                Robot.getInstance().catapult1.setPower(SubsystemConstants.CATAPULT_POWER);
                Robot.getInstance().catapult2.setPower(SubsystemConstants.CATAPULT_POWER);
            }
        }
        else if (isAutoDownActive){
            if(Robot.getInstance().catapult1.getCurrentPosition() >= SubsystemConstants.CATAPULT_TOLERANCE && catapultTime.milliseconds() < downTime){
                Robot.getInstance().catapult1.setPower(-SubsystemConstants.CATAPULT_POWER);
                Robot.getInstance().catapult2.setPower(-SubsystemConstants.CATAPULT_POWER);//down
            }else{
                Robot.getInstance().catapult1.setPower(0);
                Robot.getInstance().catapult2.setPower(0);
                Robot.getInstance().catapult1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                Robot.getInstance().catapult2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                Robot.getInstance().catapult1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                Robot.getInstance().catapult2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                isAutoDownActive = false;
                currentState = SubsystemConstants.CatapultStates.DOWN;
            }
            return;

        }
        else {
            Robot.getInstance().catapult1.setPower(0);
            Robot.getInstance().catapult2.setPower(0);

//
//            Robot.getInstance().catapult1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            Robot.getInstance().catapult2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            isAutoDownActive = false;
            currentState = SubsystemConstants.CatapultStates.DOWN;

        }






    }
    @Override
    public void stop(){
        Robot.getInstance().catapult1.setPower(0);
        Robot.getInstance().catapult2.setPower(0);
    }
    @Override
    public void printToTelemetry(Telemetry telemetry){
        telemetry.addData("Catapult State", currentState);
        telemetry.addData("Catapult1 Motor position", Robot.getInstance().catapult1.getCurrentPosition());
        telemetry.addData("Catapult AutoDown", isAutoDownActive);
        telemetry.addData("Pos", Robot.getInstance().catapult1.getCurrentPosition());
        telemetry.addData("Dir", Robot.getInstance().catapult1.getDirection());

    }

}

