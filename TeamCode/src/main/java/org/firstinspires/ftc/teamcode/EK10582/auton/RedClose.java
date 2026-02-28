package org.firstinspires.ftc.teamcode.EK10582.auton; // make sure this aligns with class location


import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.createFollower;


import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


import org.firstinspires.ftc.teamcode.EK10582.auton.action.AutonIntake;
import org.firstinspires.ftc.teamcode.EK10582.auton.action.AutonShoot;
import org.firstinspires.ftc.teamcode.EK10582.subsystem.Robot;


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


    private final Pose scorePreloadPose = new Pose(112,112,Math.toRadians(45));


    private final Pose scorePose = new Pose (96,96,Math.toRadians(45));


    private final Pose collectTopPose = new Pose(98,85,Math.toRadians(0));


    private final Pose intakeTop = new Pose(113,85,Math.toRadians(0)); //119 -> 114


    private final Pose collectMidPose = new Pose(98,60, Math.toRadians(0));


    private final Pose intakeMiddle = new Pose(113,60,Math.toRadians(0));


    private final Pose collectBottomPose = new Pose(98,36,Math.toRadians(0));


    private final Pose intakeBottom = new Pose(113,36,Math.toRadians(0));


    private final Pose scoreBottomPose = new Pose(80,23,Math.toRadians(55));


    private final Pose parkPose = new Pose(90,120,Math.toRadians(90));


    /**CONTROL POINTS**/


    private final Pose controlPoint1 = new Pose(55,85, Math.toRadians(0)); //changed from 55,85 for straighter line
    private final Pose controlPoint2 = new Pose (70, 65, Math.toRadians(0));
    private final Pose controlPoint3 = new Pose(65,45, Math.toRadians(0));




    /**PATHS + PATHCHAINS**/


    private Path scorePreload,alignTop,collectTop,scoreTop,alignMiddle,collectMiddle,scoreMiddle,alignBottom,collectBottom,scoreBottom,park;


    public void buildPaths(){
        scorePreload = new Path(new BezierLine(startPose,scorePreloadPose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePreloadPose.getHeading());


        alignTop = new Path(new BezierCurve(scorePreloadPose,controlPoint1,collectTopPose));
        alignTop.setLinearHeadingInterpolation(scorePreloadPose.getHeading(),collectTopPose.getHeading());


        collectTop = new Path(new BezierLine(collectTopPose,intakeTop));
        collectTop.setConstantHeadingInterpolation(0);


        scoreTop = new Path(new BezierCurve(intakeTop,scorePose));
        scoreTop.setLinearHeadingInterpolation(intakeTop.getHeading(), scorePose.getHeading());


        alignMiddle = new Path(new BezierCurve(scorePose,controlPoint2,collectMidPose));
        alignMiddle.setLinearHeadingInterpolation(scorePose.getHeading(),collectMidPose.getHeading());


        collectMiddle = new Path(new BezierLine(collectMidPose,intakeMiddle));
        collectMiddle.setConstantHeadingInterpolation(0);


        scoreMiddle = new Path(new BezierCurve(intakeMiddle,scorePose));
        scoreMiddle.setLinearHeadingInterpolation(intakeMiddle.getHeading(), scorePose.getHeading());


        alignBottom = new Path(new BezierCurve(scorePose,controlPoint3,collectBottomPose));
        alignBottom.setLinearHeadingInterpolation(scorePose.getHeading(), collectBottomPose.getHeading());


        collectBottom = new Path(new BezierLine(collectBottomPose,intakeBottom));
        collectBottom.setConstantHeadingInterpolation(0);


        scoreBottom = new Path(new BezierCurve(intakeBottom,scoreBottomPose));
        scoreBottom.setLinearHeadingInterpolation(intakeBottom.getHeading(), scoreBottomPose.getHeading());


    }


    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(scorePreload, true);
                setPathState(1);
                break;
            case 1:
                if (!follower.isBusy()) {
                    robot.addAction(new AutonShoot());
                    setPathState(2);
                }
                break;
            case 2:
                if (robot.getActionLength() == 0) {
                    follower.followPath(alignTop,true);
                    setPathState(3);
                }
                break;
            case 3:
                if(!follower.isBusy()){
                    robot.addAction(new AutonIntake());
                    follower.followPath(collectTop);
                    setPathState(4);
                }
                break;
            case 4:
                if(!follower.isBusy() && robot.getActionLength() == 0){
                    follower.followPath(scoreTop);
                    setPathState(5);
                }
                break;
            case 5:
                if(!follower.isBusy()){
                    robot.addAction(new AutonShoot());
                    setPathState(6);
                }
                break;
            case 6:
                if(robot.getActionLength() == 0){
                    follower.followPath(alignMiddle, true);
                    setPathState(7);
                }
                break;
            case 7:
                if(!follower.isBusy()){
                    robot.addAction(new AutonIntake());
                    follower.followPath(collectMiddle);
                    setPathState(8);
                }
                break;
            case 8:
                if(!follower.isBusy() && robot.getActionLength()==0){
                    follower.followPath(scoreMiddle);
                    setPathState(9);
                }
                break;
            case 9:
                if(!follower.isBusy() && robot.getActionLength()==0){
                    robot.addAction(new AutonShoot());
                    setPathState(10);
                }
                break;
            case 10:
                if(robot.getActionLength() == 0) {
                    follower.followPath(alignBottom, true);
                    setPathState(11);
                }
                break;
            case 11:
                if(!follower.isBusy()){
                    robot.addAction(new AutonIntake());
                    follower.followPath(collectBottom);
                    setPathState(12);
                }
                break;
            case 12:
                if(!follower.isBusy() && robot.getActionLength()==0){
                    follower.followPath(scoreBottom);
                    setPathState(13);
                }
                break;
            case 13:
                if(!follower.isBusy() && robot.getActionLength()==0){
                    robot.addAction(new AutonShoot());
                    setPathState(14);
                }
                break;
            case 20:
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
        isAuton = true;


        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();




        waitForStart();
        follower = createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        buildPaths();


        setPathState(0);

        Robot.getInstance().limelight3A.pipelineSwitch(8);
        Robot.getInstance().limelight3A.start();


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
            telemetry.addData("path completed?", follower.isBusy());
            telemetry.addData("launch velocity", Robot.getInstance().launchMotor.getVelocity());
            telemetry.addData("limelight result:", robot.limelight3A.getLatestResult());
            telemetry.update();
        }
    }
}



