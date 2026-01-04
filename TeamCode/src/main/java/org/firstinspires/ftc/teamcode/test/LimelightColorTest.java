package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name = "LimelightColorTest", group = "Examples")
public class LimelightColorTest extends OpMode {
    private Limelight3A limelight3A;

    private LLResult greenResult;
    private LLResult purpleResult;

    private int pipelineCheckState = 0;
    private final int GREEN_PIPELINE = 9;
    private final int PURPLE_PIPELINE = 7;


    @Override
    public void init() {
        limelight3A = hardwareMap.get(Limelight3A.class, "limelight");
        limelight3A.pipelineSwitch(GREEN_PIPELINE);
    }

    @Override
    public void start() {
        limelight3A.start();
    }

    @Override
    public void loop() {
        switch (pipelineCheckState) {
            case 0:
                // Get the result from the current pipeline (green)
                greenResult = limelight3A.getLatestResult();

                // Switch to the next pipeline (purple) for the next loop iteration
                limelight3A.pipelineSwitch(PURPLE_PIPELINE);

                // Move to the next state
                pipelineCheckState = 1;
                break;

            case 1:
                // Get the result from the current pipeline (purple)
                purpleResult = limelight3A.getLatestResult();

                // Switch back to the first pipeline (green) for the next loop iteration
                limelight3A.pipelineSwitch(GREEN_PIPELINE);

                // Go back to the first state
                pipelineCheckState = 0;
                break;
        }

        telemetry.addLine("Green Pipeline (9)");
        if (greenResult != null && greenResult.isValid()) {
            telemetry.addData("Target X Offset", greenResult.getTx());
            telemetry.addData("Target Y Offset", greenResult.getTy());
            telemetry.addData("Target Area", greenResult.getTa());
        } else {
            telemetry.addLine("No valid target found");
        }

        telemetry.addLine("\nPurple Pipeline (7)");
        if (purpleResult != null && purpleResult.isValid()) {
            telemetry.addData("Target X Offset", purpleResult.getTx());
            telemetry.addData("Target Y Offset", purpleResult.getTy());
            telemetry.addData("Target Area", purpleResult.getTa());
        } else {
            telemetry.addLine("No valid target found");
        }
        telemetry.update();
    }
}