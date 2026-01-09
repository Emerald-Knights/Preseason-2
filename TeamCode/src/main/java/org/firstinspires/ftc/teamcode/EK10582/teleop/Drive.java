package org.firstinspires.ftc.teamcode.EK10582.teleop;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.EK10582.EKLinear;

@TeleOp(name="New Drive")
public class Drive extends EKLinear {

    @Override
    public void runOpMode() {
        waitForStart();

        while (opModeIsActive()) {
            //drive
            robot.mecanumDrive.lx = driverStation.getLeftStickX();
            robot.mecanumDrive.ly = driverStation.getLeftStickY();
            robot.mecanumDrive.rx = driverStation.getRightStickX();

            //slowmode
            if (driverStation.getRB1()) {
                if (robot.mecanumDrive.slowMode == 1)
                    robot.mecanumDrive.slowMode = 0.5;
                else {
                    robot.mecanumDrive.slowMode = 1;
                }
            }

            if(driverStation.getRightTrigger1()>0.2){
                if(robot.mecanumDrive.slowMode != 0.8){
                    robot.mecanumDrive.slowMode = 0.8;
                }
            }

            //driver 2 controls

            robot.turret.turretRotationInput = driverStation.gamepad2.left_stick_x; //controls the spin

            robot.turret.hoodAngleInput = driverStation.gamepad2.right_stick_y; //controls the angle of the hood

            robot.catapult.catapultUpButton=driverStation.gamepad2.right_bumper;

            robot.catapult.catapultDownButton=driverStation.gamepad2.right_trigger>0.2;

            robot.intake.activeIntake= driverStation.gamepad2.a;

            robot.intake.reverseIntake= driverStation.gamepad2.y;

            robot.transfer.activeTransfer = driverStation.gamepad2.y;





            telemetry.addData("Reset: ", driverStation.getReset());
            telemetry.addData("rawRB", gamepad2.right_bumper);
            robot.update();

        }
    }
}

