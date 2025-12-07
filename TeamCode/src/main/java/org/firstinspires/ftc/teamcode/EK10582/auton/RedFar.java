package org.firstinspires.ftc.teamcode.EK10582.auton; // make sure this aligns with class location

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.EK10582.auton.action.DownCatapult;
import org.firstinspires.ftc.teamcode.EK10582.auton.action.UpCatapult;

@Autonomous(name = "RedFar", group = "Examples")
public class RedFar extends AutonBase {

    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;

    private int pathState;

    private final Pose startPose = new Pose(88,10, Math.toRadians(0));

    private final Pose scorePose = new Pose(120,120,Math.toRadians(45));

    private final Pose collectTopPose = new Pose(103,83.5,Math.toRadians(90));

    private final Pose collectMidPose = new Pose();

    private final Pose collectBottomPose = new Pose();

    private final Pose parkPose = new Pose();



    /** CONTROL POINTS**/
    private final Pose controlPointPreload = new Pose(58.8,47.7,Math.toRadians(0));
    private final Pose controlPoint1 = new Pose(55,85, Math.toRadians(0));


    /**PATHS + PATHCHAINS**/

    private Path scorePreload,pickupTop;

    public void buildPaths(){
        scorePreload = new Path(new BezierCurve(startPose,controlPointPreload,scorePose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        pickupTop = new Path(new BezierCurve(scorePose,controlPoint1,collectTopPose));
        pickupTop.setLinearHeadingInterpolation(scorePose.getHeading(),collectTopPose.getHeading());
    }

    public void autonomousPathUpdate() {
        switch(pathState){
            case 0:
                follower.followPath(scorePreload, true);
                robot.addAction(new UpCatapult());
                setPathState(1);
                break;
            case 1:
                if(robot.getActionLength()==0){
                    robot.addAction(new DownCatapult());
                    follower.followPath(pickupTop, true);
                    setPathState(2);
                }
                break;
            case 2:


            case 13:
        }
    }


    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }
    public void runOpMode(){
        while (opModeIsActive()) {

            // These loop the movements of the robot
            follower.update();
            robot.update();
            autonomousPathUpdate();

            // Feedback to Driver Hub
            telemetry.addData("path state", pathState);
            telemetry.addData("x", follower.getPose().getX());
            telemetry.addData("y", follower.getPose().getY());
            telemetry.addData("heading", follower.getPose().getHeading());
            telemetry.update();
        }
    }
}
