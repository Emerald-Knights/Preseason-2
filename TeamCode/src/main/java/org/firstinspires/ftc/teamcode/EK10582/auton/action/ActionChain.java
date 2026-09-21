package org.firstinspires.ftc.teamcode.EK10582.auton.action;

import org.firstinspires.ftc.teamcode.EK10582.subsystem.Robot;

public class ActionChain extends Action {
    Action[] actionList;
    int actionState;

    public ActionChain(Action[] actions){
        actionList = actions;
        actionState = 0;
    }

    public void start(){
        Robot.getInstance().addAction(actionList[0]);
    }

    public void update(){
        if(actionState < actionList.length-1){
            if (actionList[actionState].isComplete){
                Robot.getInstance().addAction(actionList[actionState + 1]);
                actionState++;
            }
        }
        else{
            isComplete = true;
        }
    }

    public void end(){
    }
}
