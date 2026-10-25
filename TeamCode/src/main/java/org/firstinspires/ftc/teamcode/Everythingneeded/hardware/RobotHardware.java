package org.firstinspires.ftc.teamcode.Everythingneeded.hardware;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.Everythingneeded.config.Constants;

import java.util.List;

/**
 * Shared hardware container initialized in every OpMode. Fetches devices from the
 * HardwareMap, configures directions and zero-power behaviors, and sets up manual
 * hub bulk reads.
 *
 * Because the hubs are in MANUAL bulk caching mode, clearBulkCache() must be called
 * once at the top of every loop cycle or encoder readings will go stale.
 */
public class RobotHardware {

    public List<LynxModule> allHubs;

    public DcMotorEx frontLeft, frontRight, backLeft, backRight;
    public DcMotorEx pollenMotor, nectarMotor;
    public DcMotorEx intakeRoller;

    public Limelight3A limelight;
    public IMU imu;

    public void init(HardwareMap hwMap) {
        // Hubs: manual bulk caching
        allHubs = hwMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        // Drive motors
        frontLeft = hwMap.get(DcMotorEx.class, Constants.DRIVE_FL);
        frontRight = hwMap.get(DcMotorEx.class, Constants.DRIVE_FR);
        backLeft = hwMap.get(DcMotorEx.class, Constants.DRIVE_BL);
        backRight = hwMap.get(DcMotorEx.class, Constants.DRIVE_BR);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);

        // Pollen and nectar motors
        pollenMotor = hwMap.get(DcMotorEx.class, Constants.POLLEN_MOTOR);
        nectarMotor = hwMap.get(DcMotorEx.class, Constants.NECTAR_MOTOR);

        pollenMotor.setDirection(Constants.POLLEN_REVERSED
                ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        nectarMotor.setDirection(Constants.NECTAR_REVERSED
                ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);

        pollenMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        nectarMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        pollenMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        nectarMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Intake
        intakeRoller = hwMap.get(DcMotorEx.class, Constants.INTAKE_MOTOR);

        // Limelight
        limelight = hwMap.get(Limelight3A.class, Constants.LIMELIGHT_NAME);

        // IMU
        imu = hwMap.get(IMU.class, Constants.IMU_NAME);
        // Match these to how the Control Hub is mounted on the robot.
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)));
    }

    public void clearBulkCache() {
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }
    }
}
