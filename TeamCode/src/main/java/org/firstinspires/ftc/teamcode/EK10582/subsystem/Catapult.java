package org.firstinspires.ftc.teamcode.EK10582.subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Catapult extends Subsystem {

    public boolean catapultUpButton = false;
    public boolean catapultDownButton = false;

    private boolean isAutoDownActive = false;

    private boolean hasRunOnce = false;

    public SubsystemConstants.CatapultStates currentState;



    ElapsedTime catapultTime = new ElapsedTime();
    public static final double downTime = 1000;




    @Override
    public void init(boolean isAuton){
        catapultUpButton=false;
        currentState = SubsystemConstants.CatapultStates.DOWN;
        Robot.getInstance().catapult1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Robot.getInstance().catapult2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }
    @Override
    public void update(boolean isAuton){
        if(catapultUpButton && catapultDownButton){
            catapultUpButton = false;

        }

        if(catapultUpButton) {
            Robot.getInstance().catapult1.setPower(SubsystemConstants.CATAPULT_POWER);
            Robot.getInstance().catapult2.setPower(SubsystemConstants.CATAPULT_POWER);
            currentState = SubsystemConstants.CatapultStates.UP;
            catapultTime.reset();
        }
        else if (catapultDownButton){
            Robot.getInstance().catapult1.setPower(-0.5);
            Robot.getInstance().catapult2.setPower(-0.5);//down
            currentState = SubsystemConstants.CatapultStates.DOWN;

        }
        else {
            Robot.getInstance().catapult1.setPower(-0.4);
            Robot.getInstance().catapult2.setPower(-0.4);
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
        telemetry.addData("Catapult AutoDown", isAutoDownActive);
    }

}
