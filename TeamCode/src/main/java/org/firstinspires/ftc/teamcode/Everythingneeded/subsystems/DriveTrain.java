package org.firstinspires.ftc.teamcode.Everythingneeded.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Everythingneeded.config.Constants;
import org.firstinspires.ftc.teamcode.Everythingneeded.hardware.RobotHardware;

/**
 * Mecanum drive controller. Teleop is robot-centric and does not use the IMU;
 * autonomous reads the IMU heading separately.
 *
 * Sign conventions: positive forward drives the robot forward, positive strafe
 * moves it to its right, positive turn rotates it clockwise viewed from above.
 *
 * Teleop calls {@link #driveSticks} (deadband, response curve, strafe
 * compensation) and is robot-centric. Autonomous code uses
 * {@link #driveRobotCentric} with raw powers.
 */
public class DriveTrain {

    private final RobotHardware robot;
    // Last powers sent to the wheels, kept for telemetry (as in the StarterBot sample).
    private double flPower, frPower, blPower, brPower;

    public DriveTrain(RobotHardware robot) {
        this.robot = robot;
    }

    /**
     * Teleop entry point. Takes raw stick values (forward positive, strafe right
     * positive, turn clockwise positive), cleans them up and drives in the selected mode.
     */
    public void driveSticks(double forward, double strafe, double turn) {
        forward = deadband(forward);
        strafe = deadband(strafe);
        turn = deadband(turn);

        // If the stick is mostly along one axis, drop the small cross-axis noise so a
        // straight push drives straight and uses all four wheels evenly.
        if (Math.abs(strafe) > 0 && Math.abs(forward) < Math.abs(strafe) * 0.25) {
            forward = 0;
        } else if (Math.abs(forward) > 0 && Math.abs(strafe) < Math.abs(forward) * 0.25) {
            strafe = 0;
        }
        drive(forward, strafe, turn);
    }

    /** Zero inside the deadband, rescaled outside so full stick is still 1.0. */
    private static double deadband(double v) {
        double dead = Constants.STICK_DEADBAND;
        if (Math.abs(v) < dead) {
            return 0.0;
        }
        return Math.signum(v) * (Math.abs(v) - dead) / (1.0 - dead);
    }

    /** Turn stick passthrough (raw, like the FTC sample). Do not use on vision-aim output. */
    public static double shapeTurn(double stick) {
        return stick;
    }

    /** Teleop driving: robot-centric, no IMU. X-pattern mecanum mix. */
    public void drive(double forward, double strafe, double turn) {
        // gm0 mecanum: every wheel gets a contribution from forward, strafe and turn.
        // Forward: all four wheels forward. Strafe right: FL+BR forward, FR+BL back.
        // Turn clockwise: left wheels forward, right wheels back.
        double adjStrafe = strafe * Constants.STRAFE_MULTIPLIER;

        // Divide by the sum of magnitudes (or 1) so the power ratios are preserved.
        double denominator = Math.max(
                Math.abs(forward) + Math.abs(adjStrafe) + Math.abs(turn), 1.0);
        double fl = (forward + adjStrafe + turn) / denominator;
        double bl = (forward - adjStrafe + turn) / denominator;
        double fr = (forward - adjStrafe - turn) / denominator;
        double br = (forward + adjStrafe - turn) / denominator;

        flPower = fl;
        frPower = fr;
        blPower = bl;
        brPower = br;

        setPower(robot.frontLeft, fl);
        setPower(robot.frontRight, fr);
        setPower(robot.backLeft, bl);
        setPower(robot.backRight, br);
    }

    public void driveRobotCentric(double forward, double strafe, double turn) {
        double fl = forward + strafe + turn;
        double fr = forward - strafe - turn;
        double bl = forward - strafe + turn;
        double br = forward + strafe - turn;

        double max = Math.max(
                Math.max(Math.abs(fl), Math.abs(fr)),
                Math.max(Math.abs(bl), Math.abs(br)));

        if (max > 1.0) {
            fl /= max;
            fr /= max;
            bl /= max;
            br /= max;
        }

        flPower = fl;
        frPower = fr;
        blPower = bl;
        brPower = br;

        setPower(robot.frontLeft, fl);
        setPower(robot.frontRight, fr);
        setPower(robot.backLeft, bl);
        setPower(robot.backRight, br);
    }

    /** Current IMU heading in degrees (autonomous only; call robot.initImu first). */
    public double getHeadingDegrees() {
        return robot.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }

    public void resetHeading() {
        robot.imu.resetYaw();
    }

    public double getFrontLeftPower() {
        return flPower;
    }

    public double getFrontRightPower() {
        return frPower;
    }

    public double getBackLeftPower() {
        return blPower;
    }

    public double getBackRightPower() {
        return brPower;
    }


    private static void setPower(DcMotorEx motor, double power) {
        motor.setPower(power);
    }
}
