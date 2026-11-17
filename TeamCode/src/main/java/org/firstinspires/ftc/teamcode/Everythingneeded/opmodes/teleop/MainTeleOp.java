package org.firstinspires.ftc.teamcode.Everythingneeded.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Everythingneeded.config.*;
import org.firstinspires.ftc.teamcode.Everythingneeded.hardware.RobotHardware;
import org.firstinspires.ftc.teamcode.Everythingneeded.hardware.RobotTelemetry;
import org.firstinspires.ftc.teamcode.Everythingneeded.subsystems.*;

/**
 * Primary driver-controlled routine.
 *
 * Gamepad 1: left stick drives and strafes (robot-centric), right stick turns.
 * Gamepad 2: left bumper spins the pollen and nectar motors clockwise, left
 *            trigger spins them counter-clockwise; right bumper spins the intake clockwise, right trigger
 *            spins it counter-clockwise.
 */
@TeleOp(name = "Main TeleOp 1234", group = "Competition")
public class MainTeleOp extends OpMode {

    private RobotHardware robot;
    private DriveTrain drive;
    private Pollen pollen;
    private LimelightVision limelight;
    private Intake intake;

    private long lastLoopNanos = 0L;
    private boolean lastDpadRight, lastDpadLeft;

    @Override
    public void init() {
        robot = new RobotHardware();
        robot.init(hardwareMap);

        drive = new DriveTrain(robot);
        pollen = new Pollen(robot);
        pollen.setSingleMotorMode(true);
        pollen.lockPollenPower(1.0);
        limelight = new LimelightVision(robot);
        intake = new Intake(robot);
    }

    @Override
    public void loop() {
        // Bulk cache
        robot.clearBulkCache();

        // Limelight sensor update
        limelight.update();

        // Chassis driving: left stick drives/strafes, right stick turns.
        // Robot-centric (no IMU).
        drive.driveSticks(-gamepad1.left_stick_y, gamepad1.left_stick_x,
                DriveTrain.shapeTurn(gamepad1.right_stick_x));

        // Dpad left/right switches between pollen and nectar (only the selected one runs).
        if ((gamepad2.dpad_right && !lastDpadRight) || (gamepad2.dpad_left && !lastDpadLeft)) {
            pollen.toggleSelected();
        }
        lastDpadRight = gamepad2.dpad_right;
        lastDpadLeft = gamepad2.dpad_left;

        // Left bumper = clockwise, left trigger = counter-clockwise. Limelight never affects power.
        if (gamepad2.left_bumper) {
            pollen.clockwise();
        } else if (gamepad2.left_trigger > Constants.TRIGGER_THRESHOLD) {
            pollen.counterClockwise();
        } else {
            pollen.stop();
        }

        // Intake: right bumper = clockwise, right trigger = counter-clockwise
        if (gamepad2.right_bumper) {
            intake.clockwise();
        } else if (gamepad2.right_trigger > Constants.TRIGGER_THRESHOLD) {
            intake.counterClockwise();
        } else {
            intake.stop();
        }

        // Telemetry
        long now = System.nanoTime();
        double loopMillis = lastLoopNanos == 0L ? 0.0 : (now - lastLoopNanos) / 1.0e6;
        lastLoopNanos = now;

        String lockState;
        if (limelight.hasValidTarget()) {
            lockState = "target visible";
        } else {
            lockState = "no target";
        }

        telemetry.addData("Loop time (ms)", "%.1f", loopMillis);
        telemetry.addData("Target lock", lockState);
        RobotTelemetry.addMechanisms(telemetry, robot);
        RobotTelemetry.addDrive(telemetry, robot, drive);
        RobotTelemetry.addVision(telemetry, limelight);
    }
}
