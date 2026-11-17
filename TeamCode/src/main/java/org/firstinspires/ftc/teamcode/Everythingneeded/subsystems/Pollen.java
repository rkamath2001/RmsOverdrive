package org.firstinspires.ftc.teamcode.Everythingneeded.subsystems;


import org.firstinspires.ftc.teamcode.Everythingneeded.config.Constants;
import org.firstinspires.ftc.teamcode.Everythingneeded.config.TuningConstants;
import org.firstinspires.ftc.teamcode.Everythingneeded.hardware.RobotHardware;

/**
 * Pollen and nectar motors. Both always spin in the same direction at the same time.
 */
public class Pollen {

    private final RobotHardware robot;

    private double pollenPower = Constants.POLLEN_POWER;
    private double nectarPower = Constants.POLLEN_POWER;

    private Double lockedPollenPower = null;
    private boolean voltageCompEnabled = true;
    private boolean singleMotorMode = false;
    private boolean nectarSelected = false;

    private boolean pollenFlipped = false;

    public Pollen(RobotHardware robot) {
        this.robot = robot;
    }

    /** Runs only the pollen motor opposite to the nectar motor. Off by default. */
    public void setPollenFlipped(boolean flipped) {
        pollenFlipped = flipped;
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
        return effectivePollenPower();
    }

    /**
     * Locks the pollen motor to a fixed power (0..1). While locked, nothing changes it:
     * dpad adjustments, distance-based power, shoot/reverse-at-power and voltage
     * compensation are all ignored for the pollen motor (the nectar motor is unaffected).
     */
    public void lockPollenPower(double power) {
        lockedPollenPower = Math.max(0.0, Math.min(1.0, power));
    }

    private double effectivePollenPower() {
        return lockedPollenPower != null ? lockedPollenPower : pollenPower;
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

    /** Turn battery compensation off to apply the commanded power directly (full power = full speed). */
    public void setVoltageCompensation(boolean enabled) {
        voltageCompEnabled = enabled;
    }


    /** Spins both motors clockwise at a fixed power, clamped to 0..1. */
    public void shootAtPower(double power) {
        double clamped = Math.max(0.0, Math.min(1.0, power));
        pollenPower = clamped;
        nectarPower = clamped;
        setPower(1.0);
    }

    /** Spins both motors counter-clockwise at a fixed power, clamped to 0..1. */
    public void reverseAtPower(double power) {
        double clamped = Math.max(0.0, Math.min(1.0, power));
        pollenPower = clamped;
        nectarPower = clamped;
        setPower(-1.0);
    }

    /**
     * When on, only the selected motor runs (see {@link #toggleSelected}); the other
     * stays stopped. Off by default so autonomous and other teleops run both.
     */
    public void setSingleMotorMode(boolean enabled) {
        singleMotorMode = enabled;
    }

    /** Switches the selected motor between pollen and nectar (single-motor mode). */
    public void toggleSelected() {
        nectarSelected = !nectarSelected;
    }

    public boolean isNectarSelected() {
        return nectarSelected;
    }

    /** direction is 1.0 (clockwise), -1.0 (counter-clockwise) or 0.0 (stop). */
    private void setPower(double direction) {
        // In single-motor mode only the selected motor runs, so with nectar selected
        // the (locked) pollen motor stays off and only nectar shoots.
        boolean runPollen = !singleMotorMode || !nectarSelected;
        boolean runNectar = !singleMotorMode || nectarSelected;
        double scale = direction == 0.0 ? 1.0 : voltageScale();
        double pollenDirection = pollenFlipped ? -direction : direction;
        if (lockedPollenPower != null) {
            // Locked: plain open-loop power, nothing else touches the pollen motor.
            robot.pollenMotor.setPower(runPollen ? pollenDirection * lockedPollenPower : 0.0);
        } else {
            robot.pollenMotor.setPower(runPollen
                    ? clamp(pollenDirection * pollenPower) : 0.0);
        }
        robot.nectarMotor.setPower(runNectar ? clamp(direction * nectarPower * scale) : 0.0);
    }

    /**
     * Battery compensation: nominal / actual voltage, so a sagging battery still
     * gives the same effective shooter speed. Output is clamped to +/-1, so it
     * cannot add power to a motor already commanded at full.
     */
    private double voltageScale() {
        if (!voltageCompEnabled || !TuningConstants.VOLTAGE_COMP_ENABLED) {
            return 1.0;
        }
        double scale = TuningConstants.NOMINAL_VOLTAGE / robot.getBatteryVoltage();
        return Math.max(0.8, Math.min(1.3, scale));
    }

    private static double clamp(double value) {
        return Math.max(-1.0, Math.min(1.0, value));
    }
}
