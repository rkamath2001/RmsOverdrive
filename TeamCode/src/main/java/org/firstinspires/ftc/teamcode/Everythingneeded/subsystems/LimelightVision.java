package org.firstinspires.ftc.teamcode.Everythingneeded.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.Everythingneeded.config.Constants;
import org.firstinspires.ftc.teamcode.Everythingneeded.hardware.RobotHardware;

import java.util.List;

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

    /** First AprilTag in the latest result, or null if none is detected. */
    private LLResultTypes.FiducialResult getTag() {
        if (!hasValidTarget()) {
            return null;
        }
        List<LLResultTypes.FiducialResult> tags = latestResult.getFiducialResults();
        return tags.isEmpty() ? null : tags.get(0);
    }

    /** ID of the detected AprilTag, or -1 if none. */
    public int getTagId() {
        LLResultTypes.FiducialResult tag = getTag();
        return tag == null ? -1 : tag.getFiducialId();
    }

    /**
     * Straight-line distance from the camera to the tag in inches, from the
     * Limelight's 3D pose solve. Needs 3D enabled and the tag size set in the
     * pipeline. Returns NaN if no tag is seen.
     */
    public double getDistancePoseInches() {
        LLResultTypes.FiducialResult tag = getTag();
        if (tag == null) {
            return Double.NaN;
        }
        Position p = tag.getTargetPoseCameraSpace().getPosition();
        double x = DistanceUnit.INCH.fromUnit(p.unit, p.x);
        double y = DistanceUnit.INCH.fromUnit(p.unit, p.y);
        double z = DistanceUnit.INCH.fromUnit(p.unit, p.z);
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Floor distance to the tag in inches from the vertical offset and the
     * camera mounting constants. Works without 3D, but is only accurate when
     * the mount height and angle in Constants are correct. NaN if no tag.
     */
    public double getDistanceTrigInches() {
        if (getTag() == null) {
            return Double.NaN;
        }
        double angleRad = Math.toRadians(Constants.LIMELIGHT_MOUNT_ANGLE_DEG + getTy());
        return (Constants.APRILTAG_CENTER_HEIGHT_IN - Constants.LIMELIGHT_HEIGHT_IN) / Math.tan(angleRad);
    }

    /** Pose-based distance when available, otherwise the trig estimate. NaN if no tag. */
    public double getDistanceInches() {
        double pose = getDistancePoseInches();
        return Double.isNaN(pose) || pose <= 0.0 ? getDistanceTrigInches() : pose;
    }
}
