package org.firstinspires.ftc.teamcode.Everythingneeded.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Everythingneeded.subsystems.ServoDoor;

/**
 * Red Shooter: spins the pollen (and nectar) motors at the default 0.85 power, opens the
 * servo door, keeps shooting for 10 seconds, closes the door, then drives right for 3
 * seconds and backwards for 10 seconds.
 */
@Autonomous(name = "Red Shooter", group = "Competition")
public class RedShooter extends AutoBase {

    /** How far the door turns to open (it turns back the same amount to close). TUNE ON ROBOT. */
    private static final double DOOR_OPEN_DEGREES = 60.0;
    private static final double SHOOT_SECONDS = 7.0;
    private static final double POLLEN_SHOT_POWER = 0.78;

    private static final double DRIVE_POWER = 0.5;
    private static final double RIGHT_SECONDS = 1.2;
    private static final double BACKWARD_SECONDS = 3.0;

    private ServoDoor door;

    @Override
    public void runOpMode() {
        initialize();
        door = new ServoDoor(robot);

        waitForStart();
        if (!opModeIsActive()) {
            return;
        }

        // Pollen runs the whole time the door is open (pollen locked at 0.8, nectar default).
        pollen.lockPollenPower(POLLEN_SHOT_POWER); // pollen motor only
        pollen.setPollenFlipped(true); // pollen motor reversed (nectar unchanged)
        pollen.clockwise();

        turnDoor(DOOR_OPEN_DEGREES);
        holdFor(SHOOT_SECONDS);
        turnDoor(-DOOR_OPEN_DEGREES);

        pollen.stop();

        // Drive right (positive strafe), then backwards.
        driveFor(0.0, DRIVE_POWER, 0.0, RIGHT_SECONDS);
        driveFor(-DRIVE_POWER, 0.0, 0.0, BACKWARD_SECONDS);

        pollen.stop();
        door.stop();
    }

    /** Turns the door by the given degrees and waits until the servo pulse finishes. */
    private void turnDoor(double degrees) {
        door.rotateByDegrees(degrees);
        while (opModeIsActive() && door.isMoving()) {
            updateSubsystems();
            door.update();
        }
        door.stop();
    }
}
