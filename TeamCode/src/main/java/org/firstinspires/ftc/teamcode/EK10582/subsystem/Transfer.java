package org.firstinspires.ftc.teamcode.EK10582.subsystem;

import static org.firstinspires.ftc.teamcode.EK10582.subsystem.SubsystemConstants.TRANSFER_POSITION;
import static org.firstinspires.ftc.teamcode.EK10582.subsystem.SubsystemConstants.TRANSFER_REST;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Transfer extends Subsystem{

    public boolean activeTransfer;

    public void init(boolean isAuton){
        activeTransfer = false;
    }

    public void update(boolean isAuton){
        if(activeTransfer){
            Robot.getInstance().inServo.setPosition(TRANSFER_POSITION);
        }
        else{
            Robot.getInstance().inServo.setPosition(TRANSFER_REST);
        }


    }

    public void stop(){

    }

    public void printToTelemetry(Telemetry telemetry){
        telemetry.addData("TransferState", activeTransfer);
    }
}
