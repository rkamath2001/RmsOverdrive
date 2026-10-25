package org.firstinspires.ftc.teamcode.Everythingneeded.subsystems;

import org.firstinspires.ftc.teamcode.Everythingneeded.config.Constants;
import org.firstinspires.ftc.teamcode.Everythingneeded.hardware.RobotHardware;

/**
 * Pollen and nectar motors. Both always spin in the same direction at the same time.
 */
public class Pollen {

    private final RobotHardware robot;

    private double pollenPower = Constants.POLLEN_POWER;
    private double nectarPower = Constants.POLLEN_POWER;

    public Pollen(RobotHardware robot) {
        this.robot = robot;
    }

    public void clockwise() {
        setPower(1.0);
    }

    public void counterClockwise() {
        setPower(-1.0);
    }

    public void stop() {
        setPower(0.0);
    }

    public double getPollenPower() {
        return pollenPower;
    }

    public double getNectarPower() {
        return nectarPower;
    }

    /** Adjusts the pollen motor speed by delta, clamped to 0..1. */
    public void adjustPollenPower(double delta) {
        pollenPower = Math.max(0.0, Math.min(1.0, pollenPower + delta));
    }

    /** Adjusts the nectar motor speed by delta, clamped to 0..1. */
    public void adjustNectarPower(double delta) {
        nectarPower = Math.max(0.0, Math.min(1.0, nectarPower + delta));
    }

    /** direction is 1.0 (clockwise), -1.0 (counter-clockwise) or 0.0 (stop). */
    private void setPower(double direction) {
        robot.pollenMotor.setPower(direction * pollenPower);
        robot.nectarMotor.setPower(direction * nectarPower);
    }
}
