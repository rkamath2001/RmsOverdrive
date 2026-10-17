package org.firstinspires.ftc.teamcode.team1234.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Audience-side autonomous route, run as a timed step trajectory:
 * leave the start line, confirm heading on an AprilTag, score the preloaded
 * sample in the high basket, then park in the observation zone.
 *
 * Every power and duration below is a starting value that has to be tuned on
 * the field for this robot and starting position.
 */
@Autonomous(name = "Auto Audience Side", group = "Competition")
public class AutoAudience extends AutoBase {

    /** Limelight pipeline configured for AprilTag detection. */
    private static final int APRILTAG_PIPELINE = 0;

    private static final double DRIVE_POWER = 0.4;
    private static final double LEAVE_START_SECONDS = 0.8;
    private static final double TAG_ALIGN_TIMEOUT_SECONDS = 1.5;
    private static final double PARK_REVERSE_SECONDS = 0.6;
    private static final double PARK_STRAFE_SECONDS = 1.8;

    @Override
    public void runOpMode() {
        initialize();

        limelight.setPipeline(APRILTAG_PIPELINE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        if (!opModeIsActive()) {
            return;
        }

        // 1. Drive forward off the start line.
        driveFor(DRIVE_POWER, 0.0, 0.0, LEAVE_START_SECONDS);

        // 2. Read the AprilTag to confirm heading and position.
        boolean tagConfirmed = alignToTarget(TAG_ALIGN_TIMEOUT_SECONDS);
        telemetry.addData("AprilTag confirmed", tagConfirmed);
        telemetry.addData("tx (deg)", "%.2f", limelight.getTx());
        telemetry.addData("ty (deg)", "%.2f", limelight.getTy());
        telemetry.addData("ta (%)", "%.2f", limelight.getTa());
        telemetry.update();

        // 3. Score the preloaded sample.
        scoreHighBasket();

        // 4. Park in the observation zone: back toward the wall, then strafe right.
        driveFor(-DRIVE_POWER, 0.0, 0.0, PARK_REVERSE_SECONDS);
        driveFor(0.0, DRIVE_POWER, 0.0, PARK_STRAFE_SECONDS);

        // Keep the subsystems updating until the period ends.
        while (opModeIsActive()) {
            updateSubsystems();
        }
    }
}
