package org.firstinspires.ftc.teamcode.Everythingneeded.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Everythingneeded.config.*;
import org.firstinspires.ftc.teamcode.Everythingneeded.control.PIDFController;
import org.firstinspires.ftc.teamcode.Everythingneeded.hardware.RobotHardware;
import org.firstinspires.ftc.teamcode.Everythingneeded.hardware.RobotTelemetry;
import org.firstinspires.ftc.teamcode.Everythingneeded.subsystems.*;

/**
 * Primary driver-controlled routine.
 *
 * Gamepad 1: left stick translates (field-centric), right stick turns,
 *            right bumper aims at the Limelight target, start resets heading.
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
    private PIDFController visionAimController;

    private long lastLoopNanos = 0L;

    @Override
    public void init() {
        robot = new RobotHardware();
        robot.init(hardwareMap);

        drive = new DriveTrain(robot);
        pollen = new Pollen(robot);
        limelight = new LimelightVision(robot);
        intake = new Intake(robot);

        visionAimController = new PIDFController(
                TuningConstants.VISION_ALIGN_KP,
                TuningConstants.VISION_ALIGN_KI,
                TuningConstants.VISION_ALIGN_KD,
                0.0);
    }

    @Override
    public void loop() {
        // Bulk cache
        robot.clearBulkCache();

        // Limelight sensor update
        limelight.update();

        // Pick up any vision gains edited live on FTC Dashboard.
        visionAimController.setGains(
                TuningConstants.VISION_ALIGN_KP,
                TuningConstants.VISION_ALIGN_KI,
                TuningConstants.VISION_ALIGN_KD,
                0.0);

        // Turn calculation
        boolean targetLocked = gamepad1.right_bumper && limelight.hasValidTarget();
        double turnPower;
        if (targetLocked) {
            turnPower = visionAimController.calculate(limelight.getTx(), 0.0);
        } else {
            visionAimController.reset();
            turnPower = gamepad1.right_stick_x;
        }

        // Chassis driving
        drive.driveFieldCentric(-gamepad1.left_stick_y, gamepad1.left_stick_x, turnPower);
        if (gamepad1.start) {
            drive.resetHeading();
        }

        // Pollen + nectar: left bumper = clockwise, left trigger = counter-clockwise
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
        if (targetLocked) {
            lockState = "LOCKED";
        } else if (limelight.hasValidTarget()) {
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
