package org.firstinspires.ftc.teamcode.EK10582.subsystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Transfer extends Subsystem{

    public boolean activeTransfer;

    public void init(boolean isAuton){
        activeTransfer = false;
    }
    public void update(boolean isAuton){
        if(activeTransfer){
            Robot.getInstance().inServo.setPosition(SubsystemConstants.TRANSFER_POSITION);
            Robot.getInstance().transferServo.setPower(1);
        }
        else{
            Robot.getInstance().inServo.setPosition(SubsystemConstants.TRANSFER_REST);
            Robot.getInstance().transferServo.setPower(0);
        }


    }

    public void stop(){

    }

    public void printToTelemetry(Telemetry telemetry){
        telemetry.addData("TransferState", activeTransfer);
    }
}
