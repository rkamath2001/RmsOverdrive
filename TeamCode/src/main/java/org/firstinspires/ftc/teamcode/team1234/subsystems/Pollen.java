package org.firstinspires.ftc.teamcode.team1234.subsystems;

import org.firstinspires.ftc.teamcode.team1234.config.Constants;
import org.firstinspires.ftc.teamcode.team1234.hardware.RobotHardware;

/**
 * Pollen and nectar motors. Both always spin in the same direction at the same time.
 */
public class Pollen {

    private final RobotHardware robot;

    public Pollen(RobotHardware robot) {
        this.robot = robot;
    }

    public void clockwise() {
        setPower(Constants.POLLEN_POWER);
    }

    public void counterClockwise() {
        setPower(-Constants.POLLEN_POWER);
    }

    public void stop() {
        setPower(0.0);
    }

    private void setPower(double power) {
        robot.pollenMotor.setPower(power);
        robot.nectarMotor.setPower(power);
    }
}
