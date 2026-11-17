package org.firstinspires.ftc.teamcode.Everythingneeded.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Everythingneeded.config.Constants;
import org.firstinspires.ftc.teamcode.Everythingneeded.hardware.RobotHardware;
import org.firstinspires.ftc.teamcode.Everythingneeded.hardware.RobotTelemetry;
import org.firstinspires.ftc.teamcode.Everythingneeded.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.Everythingneeded.subsystems.Intake;
import org.firstinspires.ftc.teamcode.Everythingneeded.subsystems.LimelightVision;
import org.firstinspires.ftc.teamcode.Everythingneeded.subsystems.Pollen;
import org.firstinspires.ftc.teamcode.Everythingneeded.subsystems.ServoDoor;

/**
 * Fall TeleOp.
 *
 * Gamepad 1: left stick translates (robot-centric), right stick turns.
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
    private ServoDoor servoDoor;
    private boolean lastX, lastY;

    @Override
    public void init() {
        robot = new RobotHardware();
        robot.init(hardwareMap);

        drive = new DriveTrain(robot);
        pollen = new Pollen(robot);
        pollen.setSingleMotorMode(true);
        limelight = new LimelightVision(robot);
        intake = new Intake(robot);
        servoDoor = new ServoDoor(robot);
    }

    @Override
    public void loop() {
        robot.clearBulkCache();
        limelight.update();

        drive.driveSticks(-gamepad1.left_stick_y, gamepad1.left_stick_x,
                DriveTrain.shapeTurn(gamepad1.right_stick_x));

        adjustOutakePower();

        // Pollen + nectar: left bumper = clockwise, left trigger = counter-clockwise
        //Bumper to shoot trigger to eject ball
        processOutake();

        // Intake: right bumper = clockwise, right trigger = counter-clockwise
        processIntake();

        servoDoor.update();
        // Servo door: X = 3 degrees clockwise, Y = 3 degrees counter-clockwise (one step per press)
        if (gamepad1.x && !lastX) {
            servoDoor.rotateClockwise();
        }
        if (gamepad1.y && !lastY) {
            servoDoor.rotateCounterClockwise();
        }
        lastX = gamepad1.x;
        lastY = gamepad1.y;

        telemetry.addData("Selected motor", pollen.isNectarSelected() ? "NECTAR" : "POLLEN");
        telemetry.addData("Pollen set speed", pollen.getPollenPower());
        telemetry.addData("Nectar set speed", pollen.getNectarPower());
        telemetry.addData("Tag distance (in)", "%.1f", limelight.getDistanceInches());
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
            // Limelight does not affect shooter power; pollen is locked at full power.
            pollen.clockwise();
        } else if (gamepad1.left_trigger > Constants.TRIGGER_THRESHOLD) {
            pollen.counterClockwise();
        } else {
            pollen.stop();
        }
    }

    private void adjustOutakePower() {
        // Dpad left/right switches between the pollen and nectar motor (only the
        // selected one runs). Dpad up/down changes the selected motor's power,
        // one step per press.
        if ((gamepad1.dpad_right && !lastDpadRight) || (gamepad1.dpad_left && !lastDpadLeft)) {
            pollen.toggleSelected();
        }
        if (gamepad1.dpad_up && !lastDpadUp) {
            if (pollen.isNectarSelected()) {
                pollen.adjustNectarPower(Constants.POWER_STEP);
            } else {
                pollen.adjustPollenPower(Constants.POWER_STEP);
            }
        }
        if (gamepad1.dpad_down && !lastDpadDown) {
            if (pollen.isNectarSelected()) {
                pollen.adjustNectarPower(-Constants.POWER_STEP);
            } else {
                pollen.adjustPollenPower(-Constants.POWER_STEP);
            }
        }
        lastDpadUp = gamepad1.dpad_up;
        lastDpadDown = gamepad1.dpad_down;
        lastDpadRight = gamepad1.dpad_right;
        lastDpadLeft = gamepad1.dpad_left;
    }
}
