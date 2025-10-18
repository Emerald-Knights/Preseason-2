package org.firstinspires.ftc.teamcode.EK10582.subsystem;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.EK10582.EKLinear;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.SubsystemConstants.ShooterStates;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter extends Subsystem {

    public boolean dump = false;

    public SubsystemConstants.ShooterStates currentState = SubsystemConstants.ShooterStates.DOWN;

    @Override
    public void init(boolean isAuton){
        Robot.getInstance().shooterServo.setPosition(0);
        dump=false;
        currentState = SubsystemConstants.ShooterStates.DOWN;
    }
    @Override
    public void update(boolean isAuton){
        if(dump) {
            Robot.getInstance().shooterServo.setPosition(0.79);
            currentState = SubsystemConstants.ShooterStates.UP;
        }
        else{
            Robot.getInstance().shooterServo.setPosition(0); //down
            currentState = SubsystemConstants.ShooterStates.DOWN;
        }




    }
    @Override
    public void stop(){

    }
    @Override
    public void printToTelemetry(Telemetry telemetry){
        telemetry.addData("Shooter State", currentState);

    }

}

