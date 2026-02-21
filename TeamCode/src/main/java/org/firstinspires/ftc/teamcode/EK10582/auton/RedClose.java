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

//import org.firstinspires.ftc.teamcode.EK10582.auton.action.DownCatapult;
//import org.firstinspires.ftc.teamcode.EK10582.auton.action.UpCatapult;
//import org.firstinspires.ftc.teamcode.EK10582.auton.action.AutonIntake;
//import org.firstinspires.ftc.teamcode.EK10582.subsystem.Robot;
//import org.firstinspires.ftc.teamcode.EK10582.subsystem.SubsystemConstants;

@Autonomous(name = "RedClose", group = "Examples")
public class RedClose extends AutonBase {

    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;

    private int pathState;

    private final Pose startPose = new Pose(123,122, Math.toRadians(45));

    private final Pose scorePose = new Pose(120,120,Math.toRadians(45));

    private final Pose collectTopPose = new Pose(103,83.5,Math.toRadians(90));

    private final Pose intakeTopOne = new Pose(108,83.5,Math.toRadians(-90));

    private final Pose intakeTopTwo = new Pose(113,83.5,Math.toRadians(-90));

    private final Pose intakeTopThree = new Pose(132,83.5,Math.toRadians(-90));

    private final Pose collectMidPose = new Pose(103,60, Math.toRadians(90));

    private final Pose intakeMiddleOne = new Pose(108,60,Math.toRadians(-90));

    private final Pose intakeMiddleTwo = new Pose(113,60,Math.toRadians(-90));

    private final Pose intakeMiddleThree = new Pose(132,60,Math.toRadians(-90));

    private final Pose collectBottomPose = new Pose(103,36,Math.toRadians(90));

    private final Pose intakeBottomOne = new Pose(108,36,Math.toRadians(-90));

    private final Pose intakeBottomTwo = new Pose(113,36,Math.toRadians(-90));

    private final Pose intakeBottomThree = new Pose(132,36,Math.toRadians(-90));
    private final Pose parkPose = new Pose(90,120,Math.toRadians(90));

    /**CONTROL POINTS**/

    private final Pose controlPoint1 = new Pose(55,85, Math.toRadians(0));
    private final Pose controlPoint2 = new Pose (85, 85, Math.toRadians(0));
    private final Pose controlPoint3 = new Pose(59,65, Math.toRadians(0));
    private final Pose controlPoint4 = new Pose(60,60,Math.toRadians(0));
    private final Pose controlPoint5 = new Pose(56,54,Math.toRadians(0));
    private final Pose controlPoint6 = new Pose (45,50,Math.toRadians(0));


    /**PATHS + PATHCHAINS**/

    private Path scorePreload,pickupTop,intakeTop1Path,/*intakeTop2Path,*/intakeTop3Path,shootTop,pickupMiddle,intakeMiddle1Path,/*intakeMiddle2Path,*/intakeMiddle3Path,shootMiddle,pickupBottom,intakeBottom1Path,/*intakeBottom2Path,*/intakeBottom3Path,shootBottom,park;

    public void buildPaths(){
        scorePreload = new Path(new BezierLine(startPose,scorePose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        pickupTop = new Path(new BezierCurve(scorePose,controlPoint1,collectTopPose));
        pickupTop.setLinearHeadingInterpolation(scorePose.getHeading(),collectTopPose.getHeading());

        intakeTop1Path = new Path(new BezierLine(collectTopPose,intakeTopOne));
        intakeTop1Path.setLinearHeadingInterpolation(collectTopPose.getHeading(),intakeTopOne.getHeading());

//        intakeTop2Path = new Path(new BezierLine(intakeTopOne,intakeTopTwo));
//        intakeTop2Path.setLinearHeadingInterpolation(intakeTopOne.getHeading(),intakeTopTwo.getHeading());

        intakeTop3Path = new Path(new BezierLine(intakeTopTwo,intakeTopThree));
        intakeTop3Path.setLinearHeadingInterpolation(intakeTopTwo.getHeading(),intakeTopThree.getHeading());

        shootTop = new Path(new BezierCurve(intakeTopThree,controlPoint2,scorePose));
        shootTop.setLinearHeadingInterpolation(intakeTopThree.getHeading(), scorePose.getHeading());

        pickupMiddle = new Path(new BezierCurve(scorePose,controlPoint3,collectMidPose));
        pickupMiddle.setLinearHeadingInterpolation(scorePose.getHeading(), collectMidPose.getHeading());

        intakeMiddle1Path = new Path(new BezierLine(collectMidPose,intakeMiddleOne));
        intakeMiddle1Path.setLinearHeadingInterpolation(collectMidPose.getHeading(),intakeMiddleOne.getHeading());

//        intakeMiddle2Path = new Path(new BezierLine(intakeMiddleOne,intakeMiddleTwo));
//        intakeMiddle2Path.setLinearHeadingInterpolation(intakeMiddleOne.getHeading(),intakeMiddleTwo.getHeading());

        intakeMiddle3Path = new Path(new BezierLine(intakeMiddleTwo,intakeMiddleThree));
        intakeMiddle1Path.setLinearHeadingInterpolation(intakeMiddleTwo.getHeading(),intakeMiddleThree.getHeading());

        shootMiddle = new Path(new BezierCurve(intakeMiddleThree,controlPoint4,scorePose));
        shootMiddle.setLinearHeadingInterpolation(intakeMiddleThree.getHeading(), scorePose.getHeading());

        pickupBottom = new Path(new BezierCurve(scorePose,controlPoint5,collectBottomPose));
        pickupBottom.setLinearHeadingInterpolation(scorePose.getHeading(), collectBottomPose.getHeading());

        intakeBottom1Path = new Path(new BezierLine(collectBottomPose,intakeBottomOne));
        intakeBottom1Path.setLinearHeadingInterpolation(collectBottomPose.getHeading(), intakeBottomOne.getHeading());

//        intakeBottom2Path = new Path(new BezierLine(intakeBottomOne,intakeBottomTwo));
//        intakeBottom2Path.setLinearHeadingInterpolation(intakeBottomOne.getHeading(), intakeBottomTwo.getHeading());

        intakeBottom3Path = new Path(new BezierLine(intakeBottomTwo,intakeBottomThree));
        intakeBottom3Path.setLinearHeadingInterpolation(intakeBottomTwo.getHeading(), intakeBottomThree.getHeading());

        shootBottom = new Path(new BezierCurve(intakeBottomThree,controlPoint6,scorePose));
        shootBottom.setLinearHeadingInterpolation(intakeBottomThree.getHeading(), scorePose.getHeading());

        park = new Path(new BezierLine(scorePose,parkPose));
        park.setLinearHeadingInterpolation(scorePose.getHeading(),parkPose.getHeading());

    }

    public void autonomousPathUpdate() {
        switch (pathState) {

            case 0:
                follower.followPath(scorePreload, true);
                //robot.addAction(new TransferIn());
                setPathState(1);
                break;
//            case 1:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TurretFire());
//                    setPathState(2);
//                }
//                break;
//            case 2:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferOut());
//                    setPathState(3);
//                }
//                break;
//            case 3:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new Spin());
//                    setPathState(4);
//                }
//                break;
//            case 4:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferIn());
//                    setPathState(5);
//                }
//                break;
//            case 5:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TurretFire());
//                    setPathState(6);
//                }
//                break;
//            case 6:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferOut());
//                    setPathState(7);
//                }
//                break;
//            case 7:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new Spin);
//                    setPathState(8);
//                }
//                break;
//            case 8:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferIn());
//                    setPathState(9);
//                }
//                break;
//            case 9:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TurretFire());
//                    setPathState(10);
//                }
//                break;
//            case 10:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferOut());
//                    setPathState(11);
//                }
//                break;
            case 1:
                if (robot.getActionLength()==0) {
                    follower.followPath(pickupTop, true);
                    setPathState(2);
                }
                break;
            case 2:
                if (robot.getActionLength()==0) {
                    follower.followPath(intakeTop1Path, true);
                    //robot.addAction(new AutonIntake());
                    setPathState(3);
                }
                break;
//            case 13:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new Spin);
//                    setPathState(14);
//                }
//                break;
//            case 14:
//                if (robot.getActionLength()==0) {
//                    follower.followPath(intakeTop2Path, true);
//                    robot.addAction(new AutonIntake());
//                    setPathState(15);
//                }
//                break;
//            case 15:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new Spin);
//                    setPathState(16);
//                }
//                break;
            case 3:
                if (robot.getActionLength()==0) {
                    follower.followPath(intakeTop3Path, true);
                    //robot.addAction(new AutonIntake());
                    setPathState(4);
                }
                break;
            case 4:
                if (robot.getActionLength()==0) {
                    follower.followPath(shootTop, true);
                    setPathState(5);
                }
                break;
//            case 18:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferIn());
//                    setPathState(19);
//                }
//                break;
//            case 19:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TurretFire());
//                    setPathState(20);
//                }
//                break;
//            case 20:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferOut());
//                    setPathState(21);
//                }
//                break;
//            case 21:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new Spin());
//                    setPathState(22);
//                }
//                break;
//            case 22:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferIn());
//                    setPathState(23);
//                }
//                break;
//            case 23:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TurretFire());
//                    setPathState(24);
//                }
//                break;
//            case 24:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferOut());
//                    setPathState(25);
//                }
//                break;
//            case 25:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new Spin);
//                    setPathState(26);
//                }
//                break;
//            case 27:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferIn());
//                    setPathState(28);
//                }
//                break;
//            case 28:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TurretFire());
//                    setPathState(29);
//                }
//                break;
//            case 29:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferOut());
//                    setPathState(30);
//                }
//                break;
            case 5:
                if (robot.getActionLength()==0) {
                    follower.followPath(pickupMiddle, true);
                    setPathState(6);
                }
                break;
            case 6:
                if (robot.getActionLength()==0) {
                    follower.followPath(intakeMiddle1Path, true);
                    //robot.addAction(new AutonIntake());
                    setPathState(7);
                }
                break;
//            case 32:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new Spin);
//                    setPathState(33);
//                }
//                break;
//            case 33:
//                if (robot.getActionLength()==0) {
//                    follower.followPath(intakeMiddle2Path, true);
//                    robot.addAction(new AutonIntake());
//                    setPathState(34);
//                }
//                break;
//            case 34:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new Spin);
//                    setPathState(35);
//                }
//                break;
            case 7:
                if (robot.getActionLength()==0) {
                    follower.followPath(intakeMiddle3Path, true);
                    //robot.addAction(new AutonIntake());
                    setPathState(8);
                }
                break;
            case 8:
                if (robot.getActionLength()==0) {
                    follower.followPath(shootMiddle, true);
                    setPathState(9);
                }
                break;
//            case 37:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferIn());
//                    setPathState(38);
//                }
//                    break;
//            case 38:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TurretFire());
//                    setPathState(39);
//                }
//                break;
//            case 39:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferOut());
//                    setPathState(40);
//                }
//                break;
//            case 40:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new Spin());
//                    setPathState(41);
//                }
//                break;
//            case 41:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferIn());
//                    setPathState(42);
//                }
//                break;
//            case 42:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TurretFire());
//                    setPathState(43);
//                }
//                break;
//            case 43:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferOut());
//                    setPathState(44);
//                }
//                break;
//            case 44:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new Spin);
//                    setPathState(45);
//                }
//                break;
//            case 45:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferIn());
//                    setPathState(46);
//                }
//                break;
//            case 46:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TurretFire());
//                    setPathState(47);
//                }
//                break;
//            case 47:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferOut());
//                    setPathState(48);
//                }
//                break;
            case 9:
                if (robot.getActionLength()==0) {
                    follower.followPath(pickupBottom);
                    setPathState(10);
                }
                break;
            case 10:
                if (robot.getActionLength()==0) {
                    follower.followPath(intakeBottom1Path, true);
                    //robot.addAction(new AutonIntake());
                    setPathState(11);
                }
                break;
//            case 50:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new Spin);
//                    setPathState(51);
//                }
//                break;
//            case 51:
//                if (robot.getActionLength()==0) {
//                    follower.followPath(intakeBottom2Path, true);
//                    robot.addAction(new AutonIntake());
//                    setPathState(52);
//                }
//                break;
//            case 52:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new Spin);
//                    setPathState(53);
//                }
//                break;
            case 11:
                if (robot.getActionLength()==0) {
                    follower.followPath(intakeBottom3Path, true);
                    //robot.addAction(new AutonIntake());
                    setPathState(12);
                }
                break;
            case 12:
                if (robot.getActionLength()==0) {
                    follower.followPath(shootBottom, true);
                    setPathState(13);
                }
                break;
//            case 55:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferIn());
//                    setPathState(56);
//                }
//                    break;
//            case 56:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TurretFire());
//                    setPathState(57);
//                }
//                break;
//            case 57:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferOut());
//                    setPathState(58);
//                }
//                break;
//            case 58:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new Spin());
//                    setPathState(59);
//                }
//                break;
//            case 59:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferIn());
//                    setPathState(60);
//                }
//                break;
//            case 60:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TurretFire());
//                    setPathState(61);
//                }
//                break;
//            case 61:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferOut());
//                    setPathState(62);
//                }
//                break;
//            case 62:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new Spin);
//                    setPathState(63);
//                }
//                break;
//            case 63:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferIn());
//                    setPathState(64);
//                }
//                break;
//            case 64:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TurretFire());
//                    setPathState(65);
//                }
//                break;
//            case 65:
//                if (robot.getActionLength()==0) {
//                    robot.addAction(new TransferOut());
//                    setPathState(66);
//                }
//                break;
            case 13:
                if (robot.getActionLength()==0) {
                    follower.followPath(park, true);
                }
                break;

        }

    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    @Override
    public void runOpMode(){
        pathTimer = new Timer();
        opmodeTimer = new Timer();



        waitForStart();
//        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);
        buildPaths();

        opmodeTimer.resetTimer();
        setPathState(0);

        while (opModeIsActive()) {
            // These loop the movements of the robot
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

