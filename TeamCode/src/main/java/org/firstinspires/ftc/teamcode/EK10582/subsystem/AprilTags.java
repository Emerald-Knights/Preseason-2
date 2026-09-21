//package org.firstinspires.ftc.teamcode.EK10582.subsystem;
//
//import static org.firstinspires.ftc.robotcore.external.navigation.Orientation.getOrientation;
//
//import com.qualcomm.hardware.limelightvision.LLResult;
//import com.qualcomm.hardware.limelightvision.Limelight3A;
//import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
//import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
//import org.firstinspires.ftc.teamcode.EK10582.subsystem.Subsystem;
//
//import com.qualcomm.robotcore.hardware.IMU;
//
////.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
//
//import java.util.List;
//
//public class AprilTags extends Subsystem {
//
//    private LLResult llResult;
//    private Pose3D botPose;
//
//    @Override
//    public void init (boolean auton) {
//        RevHubOrientationOnRobot revHubOrientationOnRobot = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP,
//                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD); //change based on orientation of robot once assembled
//        Robot.getInstance().imu.initialize(new IMU.Parameters(revHubOrientationOnRobot));
//
//    }   // end method initAprilTag()
//
//    public void update (boolean auton){
//        YawPitchRollAngles orientation = Robot.getInstance().imu.getRobotYawPitchRollAngles();
//        Robot.getInstance().limelight3A.updateRobotOrientation(orientation.getYaw());
//
//
//        llResult = Robot.getInstance().limelight3A.getLatestResult();
//
//        if(llResult != null && llResult.isValid()){
//            botPose = llResult.getBotpose_MT2();
//        }
//
//
//    }
//
//    @Override
//    public void stop(){
//    }
//
//    @Override
//    public void printToTelemetry(Telemetry telemetry) {
//        if(llResult !=null && llResult.isValid()){
//            telemetry.addData("Target X", llResult.getTx());
//            telemetry.addData("Target Y", llResult.getTy());
//            telemetry.addData("Target Area", llResult.getTa());
//
//            if(botPose !=null){
//                telemetry.addData("BotPose", botPose.toString());
//                telemetry.addData("Heading/Yaw", botPose.getOrientation().getYaw());
//            }
//        }else{
//            telemetry.addData("Limelight", "No Target Detected");
//        }
//    }
//
//}   // end class