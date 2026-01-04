package org.firstinspires.ftc.teamcode.EK10582.auton; // make sure this aligns with class location

import android.graphics.Point;

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
import org.firstinspires.ftc.teamcode.EK10582.auton.actions.AutonIntake;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.SubsystemConstants;

@Autonomous(name = "BlueFar", group = "Examples")
public class BlueFar extends AutonBase {

    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;

    private int pathState;

    private final Pose startPose = new Pose(56,10, Math.toRadians(0));

    private final Pose scorePose = new Pose(24,120,Math.toRadians(-45));

    private final Pose collectTopPose = new Pose(41,83.5,Math.toRadians(-90));

    private final Pose intakeTopPose = new Pose(12,83.5,Math.toRadians(-90));

    private final Pose collectMidPose = new Pose(41,60, Math.toRadians(-90));

    private final Pose intakeMidPose = new Pose(12,60, Math.toRadians(-90));

    private final Pose collectBottomPose = new Pose(41,36,Math.toRadians(-90));

    private final Pose intakeBottomPose = new Pose(12,36,Math.toRadians(-90));

    private final Pose parkPose = new Pose(90,120,Math.toRadians(-90));

    /**CONTROL POINTS**/

    private final Pose controlPoint1 = new Pose(89,85, Math.toRadians(0));
    private final Pose controlPoint2 = new Pose (59, 85, Math.toRadians(0));
    private final Pose controlPoint3 = new Pose(85,65, Math.toRadians(0));
    private final Pose controlPoint4 = new Pose(84,60,Math.toRadians(0));
    private final Pose controlPoint5 = new Pose(88,54,Math.toRadians(0));
    private final Pose controlPoint6 = new Pose (99,50,Math.toRadians(0));


    /**PATHS + PATHCHAINS**/

    private Path scorePreload,pickupTop,intakeTop,shootTop,pickupMiddle,intakeMiddle,shootMiddle,pickupBottom,intakeBottom,shootBottom,park;

    public void buildPaths(){
        scorePreload = new Path(new BezierLine(startPose,scorePose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        pickupTop = new Path(new BezierCurve(scorePose,controlPoint1,collectTopPose));
        pickupTop.setLinearHeadingInterpolation(scorePose.getHeading(),collectTopPose.getHeading());

        intakeTop = new Path(new BezierLine(collectTopPose,intakeTopPose));
        intakeTop.setLinearHeadingInterpolation(collectTopPose.getHeading(),intakeTopPose.getHeading());

        shootTop = new Path(new BezierCurve(intakeTopPose,controlPoint2,scorePose));
        shootTop.setLinearHeadingInterpolation(intakeTopPose.getHeading(), scorePose.getHeading());

        pickupMiddle = new Path(new BezierCurve(scorePose,controlPoint3,collectMidPose));
        pickupMiddle.setLinearHeadingInterpolation(scorePose.getHeading(), collectMidPose.getHeading());

        intakeMiddle = new Path(new BezierCurve(collectMidPose,intakeMidPose));
        intakeMiddle.setLinearHeadingInterpolation(collectMidPose.getHeading(),intakeMidPose.getHeading());

        shootMiddle = new Path(new BezierCurve(intakeMidPose,controlPoint4,scorePose));
        shootMiddle.setLinearHeadingInterpolation(intakeMidPose.getHeading(), scorePose.getHeading());

        pickupBottom = new Path(new BezierCurve(scorePose,controlPoint5,collectBottomPose));
        pickupBottom.setLinearHeadingInterpolation(scorePose.getHeading(), collectBottomPose.getHeading());

        intakeBottom = new Path(new BezierLine(collectBottomPose,intakeBottomPose));
        intakeBottom.setLinearHeadingInterpolation(collectBottomPose.getHeading(), intakeBottomPose.getHeading());

        shootBottom = new Path(new BezierCurve(intakeBottomPose,controlPoint6,scorePose));
        shootBottom.setLinearHeadingInterpolation(intakeBottomPose.getHeading(), scorePose.getHeading());

        park = new Path(new BezierLine(scorePose,parkPose));
        park.setLinearHeadingInterpolation(scorePose.getHeading(),parkPose.getHeading());

    }
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(scorePreload, true);
                robot.addAction(new UpCatapult());
                setPathState(1);
                break;
            case 1:
                if (robot.getActionLength() == 0) {
                    robot.addAction(new DownCatapult());
                    follower.followPath(pickupTop, true);
                    setPathState(2);
                }
                break;
            case 2:
                if (robot.getActionLength() == 0) {
                    robot.addAction(new org.firstinspires.ftc.teamcode.EK10582.auton.actions.AutonIntake());
                    follower.followPath(intakeTop, true);
                    setPathState(3);
                }
                break;

            case 3:
                if (robot.getActionLength() == 0) {
                    follower.followPath(shootTop, true);
                    setPathState(4);
                }
                break;

            case 4:
                if (robot.getActionLength() == 0) {
                    robot.addAction(new UpCatapult());
                    setPathState(5);
                }
                break;

            case 5:
                if (robot.getActionLength() == 0) {
                    robot.addAction(new DownCatapult());
                    follower.followPath(pickupMiddle, true);
                    setPathState(6);
                }
                break;

            case 6:
                if (robot.getActionLength() == 0) {
                    robot.addAction(new org.firstinspires.ftc.teamcode.EK10582.auton.actions.AutonIntake());
                    follower.followPath(intakeMiddle, true);
                    setPathState(7);
                }
                break;

            case 7:
                if (robot.getActionLength() == 0) {
                    follower.followPath(shootMiddle, true);
                    setPathState(8);
                }
                break;

            case 8:
                if (robot.getActionLength()==0) {
                    robot.addAction(new UpCatapult());
                    setPathState(9);
                }
                break;

            case 9:
                if (robot.getActionLength() == 0) {
                    robot.addAction(new DownCatapult());
                    follower.followPath(pickupBottom, true);
                    setPathState(10);
                }
                break;

            case 10:
                if (robot.getActionLength() == 0) {
                    robot.addAction(new org.firstinspires.ftc.teamcode.EK10582.auton.actions.AutonIntake());
                    follower.followPath(intakeBottom, true);
                    setPathState(11);
                }
                break;

            case 11:
                if (robot.getActionLength() == 0) {
                    follower.followPath(shootBottom, true);
                    setPathState(12);
                }
                break;

            case 12:
                if (robot.getActionLength()==0) {
                    robot.addAction(new UpCatapult());
                    setPathState(13);
                }
                break;

            case 13:
                if (robot.getActionLength() == 0) {
                    robot.addAction(new DownCatapult());
                    follower.followPath(park, true);
                }
                break;

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
