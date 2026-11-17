package org.firstinspires.ftc.teamcode.Everythingneeded.hardware;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit;
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
    public CRServo servoDoor;

    public Limelight3A limelight;
    public IMU imu;

    private static DcMotorSimple.Direction dir(boolean reversed) {
        return reversed ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD;
    }

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

        frontLeft.setDirection(dir(Constants.DRIVE_LEFT_REVERSED ^ Constants.DRIVE_FL_FLIP));
        backLeft.setDirection(dir(Constants.DRIVE_LEFT_REVERSED ^ Constants.DRIVE_BL_FLIP));
        frontRight.setDirection(dir(Constants.DRIVE_RIGHT_REVERSED ^ Constants.DRIVE_FR_FLIP));
        backRight.setDirection(dir(Constants.DRIVE_RIGHT_REVERSED ^ Constants.DRIVE_BR_FLIP));

        // Open-loop drive on raw power; encoders are read for telemetry only.
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Pollen and nectar motors
        pollenMotor = hwMap.get(DcMotorEx.class, Constants.POLLEN_MOTOR);
        nectarMotor = hwMap.get(DcMotorEx.class, Constants.NECTAR_MOTOR);

        pollenMotor.setDirection(Constants.POLLEN_REVERSED
                ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        nectarMotor.setDirection(Constants.NECTAR_REVERSED
                ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);

        // Raw open-loop power so no encoder velocity cap applies to the pollen motor.
        pollenMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pollenMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        nectarMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Intake
        intakeRoller = hwMap.get(DcMotorEx.class, Constants.INTAKE_MOTOR);

        // Servo door
        servoDoor = hwMap.get(CRServo.class, Constants.SERVO_DOOR);

        // Limelight
        limelight =hwMap.get(Limelight3A.class, Constants.LIMELIGHT_NAME);
    }

    /** IMU setup, used by autonomous only; teleop never touches the IMU. */
    public void initImu(HardwareMap hwMap) {
        imu = hwMap.get(IMU.class, Constants.IMU_NAME);
        // Hub mounting is set in Constants (HUB_LOGO_DIRECTION / HUB_USB_DIRECTION).
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                Constants.HUB_LOGO_DIRECTION,
                Constants.HUB_USB_DIRECTION)));
    }

    private double cachedVoltage = 12.0;
    private long lastVoltageNanos = 0L;

    /**
     * Battery voltage from the first hub. The read is a separate (slow) hub
     * command, so it is cached and refreshed at most every 200 ms.
     */
    public double getBatteryVoltage() {
        long now = System.nanoTime();
        if (lastVoltageNanos == 0L || now - lastVoltageNanos > 200_000_000L) {
            double v = allHubs.get(0).getInputVoltage(VoltageUnit.VOLTS);
            if (v > 1.0) {
                cachedVoltage = v;
            }
            lastVoltageNanos = now;
        }
        return cachedVoltage;
    }

    public void clearBulkCache() {
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }
    }
}
