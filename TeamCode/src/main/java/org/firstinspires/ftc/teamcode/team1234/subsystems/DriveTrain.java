package org.firstinspires.ftc.teamcode.team1234.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.team1234.hardware.RobotHardware;

/**
 * Mecanum drive controller supporting robot-centric and field-centric kinematics
 * and heading resets.
 *
 * Sign conventions: positive forward drives the robot forward, positive strafe
 * moves it to its right, positive turn rotates it clockwise viewed from above.
 */
public class DriveTrain {

    private final RobotHardware robot;
    private boolean fieldCentric = true;

    public DriveTrain(RobotHardware robot) {
        this.robot = robot;
    }

    /**
     * Drives in whichever mode is currently selected by the fieldCentric flag.
     */
    public void drive(double forward, double strafe, double turn) {
        if (fieldCentric) {
            driveFieldCentric(forward, strafe, turn);
        } else {
            driveRobotCentric(forward, strafe, turn);
        }
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

        setPower(robot.frontLeft, fl);
        setPower(robot.frontRight, fr);
        setPower(robot.backLeft, bl);
        setPower(robot.backRight, br);
    }

    public void driveFieldCentric(double forward, double strafe, double turn) {
        YawPitchRollAngles orientation = robot.imu.getRobotYawPitchRollAngles();
        double heading = orientation.getYaw(AngleUnit.RADIANS);

        // Rotates the field-frame command into the robot frame for this rotation
        // formulation. If the robot drives mirrored once it has turned away from
        // its reset heading, flip the sign here.
        double angle = heading;

        double rotatedForward = forward * Math.cos(angle) - strafe * Math.sin(angle);
        double rotatedStrafe = forward * Math.sin(angle) + strafe * Math.cos(angle);

        driveRobotCentric(rotatedForward, rotatedStrafe, turn);
    }

    public void resetHeading() {
        robot.imu.resetYaw();
    }

    public void setFieldCentric(boolean fieldCentric) {
        this.fieldCentric = fieldCentric;
    }

    public boolean isFieldCentric() {
        return fieldCentric;
    }

    private static void setPower(DcMotorEx motor, double power) {
        motor.setPower(power);
    }
}
