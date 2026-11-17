package org.firstinspires.ftc.teamcode.Everythingneeded.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/** Auto Red: drives forward for 3 seconds, then stops. */
@Autonomous(name = "Auto Red", group = "Competition")
public class AutoRed extends AutoBase {

    private static final double DRIVE_POWER = 0.5;
    private static final double DRIVE_SECONDS = 3.0;

    @Override
    public void runOpMode() {
        initialize();

        waitForStart();
        if (!opModeIsActive()) {
            return;
        }

        driveFor(DRIVE_POWER, 0.0, 0.0, DRIVE_SECONDS);
    }
}
