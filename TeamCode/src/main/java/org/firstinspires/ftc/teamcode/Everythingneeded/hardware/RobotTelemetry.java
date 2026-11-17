package org.firstinspires.ftc.teamcode.Everythingneeded.hardware;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Everythingneeded.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.Everythingneeded.subsystems.LimelightVision;

/**
 * Shared telemetry readouts for exact values: motor velocities (ticks/sec),
 * encoder positions, heading and Limelight targeting offsets.
 *
 * Reads come from the bulk cache, so call after robot.clearBulkCache().
 */
public final class RobotTelemetry {

    private RobotTelemetry() {
    }

    public static void addMechanisms(Telemetry telemetry, RobotHardware robot) {
        telemetry.addData("Battery (V)", "%.2f", robot.getBatteryVoltage());
        telemetry.addData("Pollen power", "%.2f", robot.pollenMotor.getPower());
        telemetry.addData("Nectar power", "%.2f", robot.nectarMotor.getPower());
        telemetry.addData("Intake power", "%.2f", robot.intakeRoller.getPower());
    }

    public static void addDrive(Telemetry telemetry, RobotHardware robot, DriveTrain drive) {
        telemetry.addData("Wheel power FL/FR", "%.2f / %.2f",
                drive.getFrontLeftPower(), drive.getFrontRightPower());
        telemetry.addData("Wheel power BL/BR", "%.2f / %.2f",
                drive.getBackLeftPower(), drive.getBackRightPower());
        telemetry.addData("Drive pos FL/FR", "%d / %d",
                robot.frontLeft.getCurrentPosition(), robot.frontRight.getCurrentPosition());
        telemetry.addData("Drive pos BL/BR", "%d / %d",
                robot.backLeft.getCurrentPosition(), robot.backRight.getCurrentPosition());
    }

    public static void addVision(Telemetry telemetry, LimelightVision limelight) {
        telemetry.addData("Limelight target", limelight.hasValidTarget() ? "yes" : "no");
        telemetry.addData("Limelight tx / ty (deg)", "%.2f / %.2f",
                limelight.getTx(), limelight.getTy());
        telemetry.addData("Limelight area (%)", "%.2f", limelight.getTa());
        telemetry.addData("AprilTag ID", limelight.getTagId());
        telemetry.addData("Tag distance (in)", "%.1f", limelight.getDistanceInches());
        telemetry.addData("  pose / trig (in)", "%.1f / %.1f",
                limelight.getDistancePoseInches(), limelight.getDistanceTrigInches());
    }
}
