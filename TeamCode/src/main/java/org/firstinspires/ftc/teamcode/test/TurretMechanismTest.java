//package org.firstinspires.ftc.teamcode.test;
//
//import com.qualcomm.hardware.limelightvision.LLResult;
//import com.qualcomm.hardware.limelightvision.Limelight3A;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.robotcore.util.Range;
//
//import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
//
//public class TurretMechanismTest {
//    private DcMotorEx turret;
//    private Limelight3A limelight;
//
//
//    private double kP = 0.000001;
//    private double kD = 0.00000;
//    private double goalTx = 0;
//    private double lastError = 0;
//    private double angleTolerage = 0.2;
//    private final double MAX_POWER = 0.8;
//    private double power = 0;
//    private final ElapsedTime timer = new ElapsedTime();
//
//    public void init (HardwareMap hwMap) {
//        turret = hwMap.get(DcMotorEx.class, "turret");
//        turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//
//    }
//
//    public void setkP(double newKP) {
//        kP = newKP;
//
//    }
//    public double getkP() {
//        return kP;
//    }
//
//    public void setkD(double newKD) {
//        kD = newKD;
//
//    }
//    public double getkD() {
//        return kD;
//    }
//    public void resetTimer() {
//      timer.reset();
//    }
//
//    public void update(AprilTagDetection curID) {
//      double deltaTime = timer.seconds();
//      timer.reset();
//
//      if (curID == null && curID.id() == 20) {
//          turret.setPower(0);
//          lastError = 0;
//          return;
//      }
//
//      //start PID stuff
//        LLResult llResult = limelight.getLatestResult();
//
//        double error = goalTx - curID.ftcPose.llResult.getTx();
//      double pTerm = kP * error;
//      double dTerm = 0;
//      if (deltaTime > 0) {
//          dTerm = kD * (error - lastError) / deltaTime;
//      }
//
//      if(Math.abs(error) < angleTolerage) {
//            power = 0;
//        } else {
//          power = Range.clip(pTerm + dTerm, -MAX_POWER, MAX_POWER);
//      }
//
//      //safety stuff idk
//      turret.setPower(power);
//      lastError = error;
//
//
//
//    }
//
//}
