package org.firstinspires.ftc.teamcode.EK10582.subsystem;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * A subsystem for aiming the robot chassis at a Limelight target.
 * This acts as a "virtual turret" where the entire robot turns to aim.
 */
public class Turret {

    private final Limelight3A limelight;

    // --- TUNING CONSTANTS ---
    // Proportional gain for aiming. Tune this value until the robot smoothly
    // centers on the target without oscillating.
    public static double AIM_KP = 0.025;

    // Proportional gain for ranging. This controls how fast the robot drives
    // towards the target based on its vertical offset (ty).
    public static double RANGE_KP = 0.05;

    // The desired vertical offset (ty) for the target when at the correct range.
    // This will depend on your camera's mounting angle and the target's height.
    // Tune this value by driving to the desired distance and recording the 'ty' value.
    public static double DESIRED_RANGE_TY = 0.0;

    public Turret(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
    }

    /**
     * Switches the Limelight to the specified pipeline.
     * @param pipelineID The ID of the pipeline to use (0-9).
     */
    public void setPipeline(int pipelineID) {
        limelight.pipelineSwitch(pipelineID);
    }

    /**
     * Checks if the Limelight currently sees a valid target.
     * @return true if a target is visible, false otherwise.
     */
    public boolean isTargetVisible() {
        LLResult result = limelight.getLatestResult();
        return result != null && result.isValid();
    }

    /**
     * Calculates the rotational power needed to aim at the target.
     * A positive output corresponds to a counter-clockwise rotation.
     * @return The rotational power, from -1.0 to 1.0. Returns 0 if no target is visible.
     */
    public double getAimingPower() {
        LLResult result = limelight.getLatestResult();
        if (!isTargetVisible()) {
            return 0.0;
        }

        // 'tx' is the horizontal offset from the crosshair in degrees.
        // A positive 'tx' means the target is to the right, requiring a clockwise (negative) rotation.
        double error = result.getTx();

        // The 'power' is proportional to the error. We invert it to match motor directions.
        double rotationalPower = -error * AIM_KP;

        return rotationalPower;
    }

    /**
     * Calculates the forward/backward power needed to move to the desired range.
     * A positive output corresponds to forward motion.
     * @return The forward power, from -1.0 to 1.0. Returns 0 if no target is visible.
     */
    public double getRangePower() {
        LLResult result = limelight.getLatestResult();
        if (!isTargetVisible()) {
            return 0.0;
        }

        // 'ty' is the vertical offset. We want to drive until 'ty' reaches our desired value.
        // If the camera is angled upwards, a target further away will have a smaller 'ty'.
        // Error = Desired Value - Actual Value
        double error = DESIRED_RANGE_TY - result.getTy();

        // The 'power' is proportional to the error.
        // If the target is too low (error > 0), we need to drive forward (positive power).
        double forwardPower = error * RANGE_KP;

        return forwardPower;
    }
}
