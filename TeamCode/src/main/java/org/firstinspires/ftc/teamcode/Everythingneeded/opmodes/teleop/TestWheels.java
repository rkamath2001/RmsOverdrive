package org.firstinspires.ftc.teamcode.Everythingneeded.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Everythingneeded.hardware.RobotHardware;

/**
 * Bench test for wheel directions and names. Lift the robot off the ground.
 * Hold one button to spin that single wheel at a gentle power; it should roll
 * FORWARD (the top of the wheel moves toward the front of the robot).
 *
 * Gamepad 1: X = front left, B = front right, A = back left, Y = back right.
 */
@TeleOp(name = "Test Wheels", group = "Test")
public class TestWheels extends OpMode {

    private static final double TEST_POWER = 0.3;

    private RobotHardware robot;

    @Override
    public void init() {
        robot = new RobotHardware();
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        robot.frontLeft.setPower(gamepad1.x ? TEST_POWER : 0.0);
        robot.frontRight.setPower(gamepad1.b ? TEST_POWER : 0.0);
        robot.backLeft.setPower(gamepad1.a ? TEST_POWER : 0.0);
        robot.backRight.setPower(gamepad1.y ? TEST_POWER : 0.0);

        telemetry.addLine("X = front left, B = front right, A = back left, Y = back right");
        telemetry.addLine("Each wheel should roll FORWARD. Note any that don't.");
        telemetry.update();
    }

    @Override
    public void stop() {
        robot.frontLeft.setPower(0.0);
        robot.frontRight.setPower(0.0);
        robot.backLeft.setPower(0.0);
        robot.backRight.setPower(0.0);
    }
}
