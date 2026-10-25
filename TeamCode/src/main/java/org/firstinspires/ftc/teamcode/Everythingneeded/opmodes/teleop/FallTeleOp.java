package org.firstinspires.ftc.teamcode.Everythingneeded.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Everythingneeded.config.Constants;
import org.firstinspires.ftc.teamcode.Everythingneeded.config.TuningConstants;
import org.firstinspires.ftc.teamcode.Everythingneeded.control.PIDFController;
import org.firstinspires.ftc.teamcode.Everythingneeded.hardware.RobotHardware;
import org.firstinspires.ftc.teamcode.Everythingneeded.hardware.RobotTelemetry;
import org.firstinspires.ftc.teamcode.Everythingneeded.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.Everythingneeded.subsystems.Intake;
import org.firstinspires.ftc.teamcode.Everythingneeded.subsystems.LimelightVision;
import org.firstinspires.ftc.teamcode.Everythingneeded.subsystems.Pollen;

/**
 * Fall TeleOp.
 *
 * Gamepad 1: left stick translates (field-centric), right stick turns,
 *            right bumper aims at the Limelight target, start resets heading.
 * Gamepad 2: left bumper spins the pollen and nectar motors clockwise, left
 *            trigger spins them counter-clockwise; right bumper spins the intake clockwise, right trigger
 *            spins it counter-clockwise.
 */
@TeleOp(name = "Fall TeleOp", group = "Competition")
public class FallTeleOp extends OpMode {

    private boolean lastDpadUp, lastDpadDown, lastDpadRight, lastDpadLeft;

    private RobotHardware robot;
    private DriveTrain drive;
    private Pollen pollen;
    private LimelightVision limelight;
    private Intake intake;
    private PIDFController visionAimController;

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
        robot.clearBulkCache();
        limelight.update();

        visionAimController.setGains(
                TuningConstants.VISION_ALIGN_KP,
                TuningConstants.VISION_ALIGN_KI,
                TuningConstants.VISION_ALIGN_KD,
                0.0);

        // Turn: aim assist while right bumper is held with a target in view.
        boolean targetLocked = gamepad1.right_bumper && limelight.hasValidTarget();
        double turnPower;
        if (targetLocked) {
            turnPower = visionAimController.calculate(limelight.getTx(), 0.0);
        } else {
            visionAimController.reset();
            turnPower = gamepad1.right_stick_x;
        }

        drive.driveFieldCentric(-gamepad1.left_stick_y, gamepad1.left_stick_x, turnPower);
        if (gamepad1.start) {
            drive.resetHeading();
        }

        adjustOutakePower();

        // Pollen + nectar: left bumper = clockwise, left trigger = counter-clockwise
        //Bumper to shoot trigger to eject ball
        processOutake();

        // Intake: right bumper = clockwise, right trigger = counter-clockwise
        processIntake();

        telemetry.addData("Target lock", targetLocked ? "LOCKED" : "off");
        telemetry.addData("Pollen set speed", pollen.getPollenPower());
        telemetry.addData("Nectar set speed", pollen.getNectarPower());
        RobotTelemetry.addMechanisms(telemetry, robot);
        RobotTelemetry.addDrive(telemetry, robot, drive);
        RobotTelemetry.addVision(telemetry, limelight);
    }

    private void processIntake() {
        if (gamepad1.right_bumper) {
            intake.clockwise();
        } else if (gamepad1.right_trigger > Constants.TRIGGER_THRESHOLD) {
            intake.counterClockwise();
        } else {
            intake.stop();
        }
    }

    private void processOutake() {
        if (gamepad1.left_bumper) {
            pollen.clockwise();
        } else if (gamepad1.left_trigger > Constants.TRIGGER_THRESHOLD) {
            pollen.counterClockwise();
        } else {
            pollen.stop();
        }
    }

    private void adjustOutakePower() {
        // Speed adjust (one step per press): dpad up/down = pollen, dpad right/left = nectar
        if (gamepad1.dpad_up && !lastDpadUp) {
            pollen.adjustPollenPower(Constants.POWER_STEP);
        }
        if (gamepad1.dpad_down && !lastDpadDown) {
            pollen.adjustPollenPower(-Constants.POWER_STEP);
        }
        if (gamepad1.dpad_right && !lastDpadRight) {
            pollen.adjustNectarPower(Constants.POWER_STEP);
        }
        if (gamepad1.dpad_left && !lastDpadLeft) {
            pollen.adjustNectarPower(-Constants.POWER_STEP);
        }
        lastDpadUp = gamepad1.dpad_up;
        lastDpadDown = gamepad1.dpad_down;
        lastDpadRight = gamepad1.dpad_right;
        lastDpadLeft = gamepad1.dpad_left;
    }
}
