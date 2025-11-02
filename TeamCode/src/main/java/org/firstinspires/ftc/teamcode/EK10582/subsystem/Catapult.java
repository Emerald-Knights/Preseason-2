package org.firstinspires.ftc.teamcode.EK10582.subsystem;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Catapult extends Subsystem {

    public boolean catapultUpButton = false;
    public boolean catapultDownButton = false;

    private boolean isAutoDownActive = false;

    private boolean hasRunOnce = false;



    ElapsedTime catapultTime = new ElapsedTime();
    public static final double downTime = 1000;



    public SubsystemConstants.CatapultStates currentState = SubsystemConstants.CatapultStates.DOWN;

    @Override
    public void init(boolean isAuton){
        catapultUpButton=false;
        currentState = SubsystemConstants.CatapultStates.DOWN;


    }
    @Override
    public void update(boolean isAuton){
        if(catapultUpButton && catapultDownButton){
            catapultUpButton = false;

        }

        if(catapultUpButton) {
            Robot.getInstance().catapult1.setPower(SubsystemConstants.UP_POWER);
            Robot.getInstance().catapult2.setPower(SubsystemConstants.UP_POWER);
            currentState = SubsystemConstants.CatapultStates.UP;

            isAutoDownActive = true;
            catapultTime.reset();
        }
        else if (isAutoDownActive && catapultTime.milliseconds() < downTime){
            Robot.getInstance().catapult1.setPower(SubsystemConstants.DOWN_POWER);
            Robot.getInstance().catapult2.setPower(SubsystemConstants.DOWN_POWER);//down
            currentState = SubsystemConstants.CatapultStates.DOWN;

        }
        else {
            Robot.getInstance().catapult1.setPower(0);
            Robot.getInstance().catapult2.setPower(0);
            isAutoDownActive = false;
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

