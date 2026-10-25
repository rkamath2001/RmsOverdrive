package org.firstinspires.ftc.teamcode.Everythingneeded.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.teamcode.Everythingneeded.hardware.RobotHardware;

/**
 * Wraps the Limelight 3A. Exposes targeting metrics (horizontal and vertical
 * offset, target area, detection flag) and handles pipeline switching.
 *
 * update() must be called once per loop cycle before reading any metric.
 */
public class LimelightVision {

    private final Limelight3A limelight;
    private LLResult latestResult;

    public LimelightVision(RobotHardware robot) {
        this.limelight = robot.limelight;
        this.limelight.start();
    }

    public void setPipeline(int pipelineIndex) {
        limelight.pipelineSwitch(pipelineIndex);
    }

    public void update() {
        latestResult = limelight.getLatestResult();
    }

    public boolean hasValidTarget() {
        return latestResult != null && latestResult.isValid();
    }

    public double getTx() {
        return hasValidTarget() ? latestResult.getTx() : 0.0;
    }

    public double getTy() {
        return hasValidTarget() ? latestResult.getTy() : 0.0;
    }

    public double getTa() {
        return hasValidTarget() ? latestResult.getTa() : 0.0;
    }
}
