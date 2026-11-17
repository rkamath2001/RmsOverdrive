package org.firstinspires.ftc.teamcode.Everythingneeded.config;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

/**
 * Global hardware string identifiers and fixed mechanical physical dimensions.
 */
public final class Constants {

    private Constants() {
    }

    // ---------------------------------------------------------------------
    // Hardware Device IDs (must match the names in the Robot Configuration)
    // ---------------------------------------------------------------------
    public static final String DRIVE_FL = "frontLeft";
    public static final String DRIVE_FR = "frontRight";
    public static final String DRIVE_BL = "backLeft";
    public static final String DRIVE_BR = "backRight";

    public static final String POLLEN_MOTOR = "pollen2";
    public static final String NECTAR_MOTOR = "nectar";

    public static final String INTAKE_MOTOR = "intake";

    public static final String SERVO_DOOR = "servoDoor";

    public static final String LIMELIGHT_NAME = "limelight";

    // ---------------------------------------------------------------------
    // Physical Bounds & Settings
    // ---------------------------------------------------------------------
    public static final double POLLEN_POWER = 0.85;

    public static final double INTAKE_POWER = 1.00;

    // Servo door
    public static final double SERVO_DOOR_STEP_DEGREES = 3.0;
    /** Continuous-rotation servo: power used during each pulse. */
    public static final double SERVO_DOOR_POWER = 0.3;
    /** CALIBRATE: measured degrees/second the servo turns at SERVO_DOOR_POWER. */
    public static final double SERVO_DOOR_DEGREES_PER_SECOND = 90.0;
    public static final boolean SERVO_DOOR_CLOCKWISE_IS_POSITIVE = true;

    /** Trigger travel needed before a trigger counts as pressed. */
    public static final double TRIGGER_THRESHOLD = 0.1;

    /** Pollen/nectar speed change per dpad press. */
    public static final double POWER_STEP = 0.05;

    // ---------------------------------------------------------------------
    // Limelight mounting (measure on the robot; used for the ty-based distance)
    // ---------------------------------------------------------------------
    /** Lens height above the floor, in inches (22 cm, measured). */
    public static final double LIMELIGHT_HEIGHT_IN = 8.6;

    /** Camera pitch above horizontal, in degrees (positive = tilted up). Measured. */
    public static final double LIMELIGHT_MOUNT_ANGLE_DEG = 10.0;

    /** Height of the AprilTag center above the floor, in inches. PLACEHOLDER: check the field manual. */
    public static final double APRILTAG_CENTER_HEIGHT_IN = 29.5;

    // ---------------------------------------------------------------------
    // Hardware settings
    // ---------------------------------------------------------------------
    /** Default configuration name of the Control Hub's built-in IMU. */
    public static final String IMU_NAME = "imu";

    /**
     * Pollen and nectar both run with the same motor direction, so the same power
     * spins them the same way. Set one to true to flip it.
     */
    public static final boolean POLLEN_REVERSED = false;
    public static final boolean NECTAR_REVERSED = false;

    // ---------------------------------------------------------------------
    // Drivetrain
    // ---------------------------------------------------------------------
    /**
     * Wheel directions. Pushing the left stick forward must roll every wheel forward.
     * If the robot drives backward, flip BOTH; if only one side runs backward, flip that side.
     */
    public static final boolean DRIVE_LEFT_REVERSED = true;
    public static final boolean DRIVE_RIGHT_REVERSED = false;

    /** Per-wheel flips applied on top of the side setting (true = invert that one wheel). */
    public static final boolean DRIVE_FL_FLIP = true;
    public static final boolean DRIVE_FR_FLIP = false;
    public static final boolean DRIVE_BL_FLIP = false;
    public static final boolean DRIVE_BR_FLIP = false;

    /** Stick values below this are treated as zero (stops drift when sticks are released). */
    public static final double STICK_DEADBAND = 0.1;

    /** Mecanum strafes less efficiently than it drives; scale strafe up to compensate. */
    public static final double STRAFE_MULTIPLIER = 1.1;

    /** Stick response exponent: 1 = linear, 2 = squared (finer control at low speed). */
    public static final double DRIVE_INPUT_EXPONENT = 2.0;
    public static final double TURN_INPUT_EXPONENT = 2.0;

    /**
     * Control Hub mounting for the IMU (field-centric only). Must match the robot:
     * which way the REV logo faces and which way the USB ports point.
     */
    public static final RevHubOrientationOnRobot.LogoFacingDirection HUB_LOGO_DIRECTION =
            RevHubOrientationOnRobot.LogoFacingDirection.UP;
    public static final RevHubOrientationOnRobot.UsbFacingDirection HUB_USB_DIRECTION =
            RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;
}
