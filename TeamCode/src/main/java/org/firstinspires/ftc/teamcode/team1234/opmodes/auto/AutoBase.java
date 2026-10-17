package org.firstinspires.ftc.teamcode.team1234.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.team1234.config.TuningConstants;
import org.firstinspires.ftc.teamcode.team1234.control.PIDFController;
import org.firstinspires.ftc.teamcode.team1234.hardware.RobotHardware;
import org.firstinspires.ftc.teamcode.team1234.subsystems.*;

/**
 * Abstract base class for all 30-second autonomous routines. Provides lifecycle
 * setup and the macro actions that concrete routes are built from.
 *
 * Every helper here that takes time keeps the bulk cache and Limelight
 * updating, so routes should use holdFor() rather than sleep().
 */
public abstract class AutoBase extends LinearOpMode {

    /** Time the pollen and nectar motors run clockwise before ejecting. */
    protected static final double POLLEN_SECONDS = 1.0;
    /** Time the intake spins counter-clockwise to eject a sample into the basket. */
    protected static final double EJECT_SECONDS = 0.8;
    /** Heading is treated as aligned once the target's tx is within this many degrees. */
    protected static final double ALIGN_TOLERANCE_DEGREES = 1.5;
    /** Cap on turn power while aligning to a vision target. */
    protected static final double ALIGN_MAX_TURN_POWER = 0.3;

    protected RobotHardware robot;
    protected DriveTrain drive;
    protected Pollen pollen;
    protected LimelightVision limelight;
    protected Intake intake;

    protected void initialize() {
        robot = new RobotHardware();
        robot.init(hardwareMap);

        drive = new DriveTrain(robot);
        pollen = new Pollen(robot);
        limelight = new LimelightVision(robot);
        intake = new Intake(robot);
    }

    protected void scoreHighBasket() {
        pollen.clockwise();
        holdFor(POLLEN_SECONDS);
        pollen.stop();

        if (!opModeIsActive()) {
            return;
        }

        intake.counterClockwise();
        holdFor(EJECT_SECONDS);
        intake.stop();
    }

    /** One control cycle: refresh the bulk cache and the Limelight. */
    protected void updateSubsystems() {
        robot.clearBulkCache();
        limelight.update();
    }

    /** Waits for the given time while keeping the subsystems updating. */
    protected void holdFor(double seconds) {
        double deadline = getRuntime() + seconds;
        while (opModeIsActive() && getRuntime() < deadline) {
            updateSubsystems();
        }
    }

    /** Drives robot-centric at the given powers for a fixed time, then stops. */
    protected void driveFor(double forward, double strafe, double turn, double seconds) {
        double deadline = getRuntime() + seconds;
        while (opModeIsActive() && getRuntime() < deadline) {
            updateSubsystems();
            drive.driveRobotCentric(forward, strafe, turn);
        }
        stopDrive();
    }

    protected void stopDrive() {
        drive.driveRobotCentric(0.0, 0.0, 0.0);
    }

    /**
     * Turns in place until the Limelight target is centered, using the same
     * controller call as the TeleOp aim assist.
     *
     * @return true if a target was seen and centered within tolerance before the timeout
     */
    protected boolean alignToTarget(double timeoutSeconds) {
        PIDFController aimController = new PIDFController(
                TuningConstants.VISION_ALIGN_KP,
                TuningConstants.VISION_ALIGN_KI,
                TuningConstants.VISION_ALIGN_KD,
                0.0);

        boolean aligned = false;
        double deadline = getRuntime() + timeoutSeconds;
        while (opModeIsActive() && getRuntime() < deadline) {
            updateSubsystems();

            if (!limelight.hasValidTarget()) {
                stopDrive();
                continue;
            }

            double tx = limelight.getTx();
            if (Math.abs(tx) <= ALIGN_TOLERANCE_DEGREES) {
                aligned = true;
                break;
            }

            double turn = aimController.calculate(tx, 0.0);
            turn = Math.max(-ALIGN_MAX_TURN_POWER, Math.min(ALIGN_MAX_TURN_POWER, turn));
            drive.driveRobotCentric(0.0, 0.0, turn);
        }

        stopDrive();
        return aligned;
    }
}
