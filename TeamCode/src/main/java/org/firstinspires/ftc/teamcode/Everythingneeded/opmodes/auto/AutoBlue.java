package org.firstinspires.ftc.teamcode.Everythingneeded.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/** Auto Blue: drives forward 4 inches, then strafes left 8 inches. Timed, so tune on the field. */
@Autonomous(name = "Auto Blue", group = "Competition")
public class AutoBlue extends AutoBase {

    private static final double DRIVE_POWER = 0.5;

    /** Seconds per inch at DRIVE_POWER (~0.05 s per inch at 0.5 power). TUNE ON FIELD. */
    private static final double FORWARD_SECONDS_PER_INCH = 0.05;
    /** Strafing is slower than driving forward, so it needs more time per inch. TUNE ON FIELD. */
    private static final double STRAFE_SECONDS_PER_INCH = 0.07;

    private static final double FORWARD_INCHES = 4.4;
    private static final double STRAFE_LEFT_INCHES = 8.0;

    @Override
    public void runOpMode() {
        initialize();

        waitForStart();
        if (!opModeIsActive()) {
            return;
        }

        // Forward 4 inches.
        driveFor(DRIVE_POWER, 0.0, 0.0, FORWARD_INCHES * FORWARD_SECONDS_PER_INCH);

        // Strafe left 8 inches (negative strafe = left).
        driveFor(0.0, -DRIVE_POWER, 0.0, STRAFE_LEFT_INCHES * STRAFE_SECONDS_PER_INCH);
    }
}
