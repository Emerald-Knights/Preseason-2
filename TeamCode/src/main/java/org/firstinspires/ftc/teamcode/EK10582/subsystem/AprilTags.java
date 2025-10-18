package org.firstinspires.ftc.teamcode.EK10582.subsystem;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class AprilTags extends Subsystem {

    boolean aprilTagsEnabled;

    private AprilTagProcessor aprilTag;
    public float decimation;

    private VisionPortal visionPortal;
    List<AprilTagDetection> currentDetections;

    @Override
    public void init (boolean auton) {

        aprilTagsEnabled = true;

        // Create the AprilTag processor.
        aprilTag = new AprilTagProcessor.Builder()

                // Vision processor default settings
                .setDrawAxes(true)
                .setDrawCubeProjection(false)
                .setDrawTagOutline(true)
                .setTagLibrary(AprilTagGameDatabase.getIntoTheDeepTagLibrary())
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .build();

        aprilTag.setDecimation(3);

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        //TODO: disabling camera viewport
        builder.setLiveViewContainerId(0);
        builder.enableLiveView(false);

        // Set the camera (webcam vs. built-in RC phone camera).
        builder.setCamera(Robot.getInstance().camera);

        // Set and enable the processor.
        builder.addProcessor(aprilTag);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Disable or re-enable the aprilTag processor at any time.
        visionPortal.setProcessorEnabled(aprilTag, aprilTagsEnabled);

    }   // end method initAprilTag()

    public void update (boolean auton){
        if(decimation < 1){
            decimation = 1;
        }
        else if (decimation > 5){
            decimation = 5;
        }
        aprilTag.setDecimation(decimation);
        currentDetections = aprilTag.getDetections();
    }

    @Override
    public void stop(){
    }

    @Override
    public void printToTelemetry(Telemetry telemetry) {
        telemetry.addData("# AprilTags Detected", currentDetections.size());

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", filterXDist(detection.ftcPose.x), filterYDist(detection.ftcPose.y), detection.ftcPose.z));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
                telemetry.addData("apriltags size: ", detection.metadata.tagsize);
            } else {
                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }   // end for() loop

        // Add "key" information to telemetry
        telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
        telemetry.addLine("RBE = Range, Bearing & Elevation");
        telemetry.addData("Decimation: ", decimation);

    }   // end method telemetryAprilTag()

    // Calibration data via physically tested values
    public double filterYDist(double dist){
        return dist;
    }

    public double filterXDist(double dist){
        return dist;
    }

}   // end class