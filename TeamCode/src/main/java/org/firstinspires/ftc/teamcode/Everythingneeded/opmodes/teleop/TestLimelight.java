package org.firstinspires.ftc.teamcode.Everythingneeded.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Everythingneeded.hardware.RobotHardware;
import org.firstinspires.ftc.teamcode.Everythingneeded.subsystems.LimelightVision;

/**
 * Bench test for Limelight distance. Telemetry only; no motors are driven.
 */
@TeleOp(name = "Test Limelight", group = "Test")
public class TestLimelight extends OpMode {

    private RobotHardware robot;
    private LimelightVision limelight;

    @Override
    public void init() {
        robot = new RobotHardware();
        robot.init(hardwareMap);

        limelight = new LimelightVision(robot);
    }

    @Override
    public void loop() {
        robot.clearBulkCache();
        limelight.update();

        double distance = limelight.getDistanceInches();
        boolean hasDistance = !Double.isNaN(distance);

        telemetry.addData("Tag seen", limelight.hasValidTarget() && hasDistance);
        telemetry.addData("Tag ID", limelight.getTagId());
        telemetry.addData("Distance (in)", hasDistance ? String.format("%.1f", distance) : "none");
        telemetry.addData("Distance pose (in)", "%.1f", limelight.getDistancePoseInches());
        telemetry.addData("Distance trig (in)", "%.1f", limelight.getDistanceTrigInches());
        telemetry.addData("Tx / Ty / Ta", "%.1f / %.1f / %.2f",
                limelight.getTx(), limelight.getTy(), limelight.getTa());
    }
}
