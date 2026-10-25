package org.firstinspires.ftc.teamcode.Everythingneeded.subsystems;

import org.firstinspires.ftc.teamcode.Everythingneeded.config.Constants;
import org.firstinspires.ftc.teamcode.Everythingneeded.hardware.RobotHardware;

/**
 * Roller intake: spins clockwise or counter-clockwise, or stops.
 */
public class Intake {

    private final RobotHardware robot;

    public Intake(RobotHardware robot) {
        this.robot = robot;
    }

    public void clockwise() {
        robot.intakeRoller.setPower(Constants.INTAKE_POWER);
    }

    public void counterClockwise() {
        robot.intakeRoller.setPower(-Constants.INTAKE_POWER);
    }

    public void stop() {
        robot.intakeRoller.setPower(0.0);
    }
}
