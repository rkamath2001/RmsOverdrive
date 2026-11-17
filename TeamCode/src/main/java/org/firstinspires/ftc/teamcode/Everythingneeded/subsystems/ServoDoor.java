package org.firstinspires.ftc.teamcode.Everythingneeded.subsystems;

import org.firstinspires.ftc.teamcode.Everythingneeded.config.Constants;
import org.firstinspires.ftc.teamcode.Everythingneeded.hardware.RobotHardware;

/**
 * Door servo (continuous rotation): each call pulses the servo for just long enough to turn
 * about Constants.SERVO_DOOR_STEP_DEGREES, then stops it. Call update() every loop.
 * Tune Constants.SERVO_DOOR_DEGREES_PER_SECOND until one press turns exactly 3 degrees,
 * and flip Constants.SERVO_DOOR_CLOCKWISE_IS_POSITIVE if it turns the wrong way.
 */
public class ServoDoor {

    private final RobotHardware robot;
    private long stopAtNanos = 0;
    private boolean moving = false;

    public ServoDoor(RobotHardware robot) {
        this.robot = robot;
        robot.servoDoor.setPower(0);
    }

    public void rotateClockwise() {
        pulse(Constants.SERVO_DOOR_CLOCKWISE_IS_POSITIVE ? 1 : -1);
    }

    public void rotateCounterClockwise() {
        pulse(Constants.SERVO_DOOR_CLOCKWISE_IS_POSITIVE ? -1 : 1);
    }

    /**
     * Turns the door by an arbitrary angle (positive = clockwise, negative = counter-clockwise).
     * Non-blocking: keep calling update() (or poll isMoving()) until it finishes.
     */
    public void rotateByDegrees(double degrees) {
        int sign = (degrees >= 0) == Constants.SERVO_DOOR_CLOCKWISE_IS_POSITIVE ? 1 : -1;
        pulse(sign, Math.abs(degrees));
    }

    public boolean isMoving() {
        return moving;
    }

    private void pulse(int sign) {
        pulse(sign, Constants.SERVO_DOOR_STEP_DEGREES);
    }

    private void pulse(int sign, double degrees) {
        double seconds = degrees / Constants.SERVO_DOOR_DEGREES_PER_SECOND;
        stopAtNanos = System.nanoTime() + (long) (seconds * 1e9);
        moving = true;
        robot.servoDoor.setPower(sign * Constants.SERVO_DOOR_POWER);
    }

    /** Call every loop; stops the servo once the pulse time has elapsed. */
    public void update() {
        if (moving && System.nanoTime() >= stopAtNanos) {
            robot.servoDoor.setPower(0);
            moving = false;
        }
    }

    public void stop() {
        robot.servoDoor.setPower(0);
        moving = false;
    }
}
