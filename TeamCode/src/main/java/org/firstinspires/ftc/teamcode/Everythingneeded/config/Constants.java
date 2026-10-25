package org.firstinspires.ftc.teamcode.Everythingneeded.config;

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

    public static final String POLLEN_MOTOR = "pollen";
    public static final String NECTAR_MOTOR = "nectar";

    public static final String INTAKE_MOTOR = "intake";

    public static final String LIMELIGHT_NAME = "limelight";

    // ---------------------------------------------------------------------
    // Physical Bounds & Settings
    // ---------------------------------------------------------------------
    public static final double POLLEN_POWER = 0.85;

    public static final double INTAKE_POWER = 0.85;

    /** Trigger travel needed before a trigger counts as pressed. */
    public static final double TRIGGER_THRESHOLD = 0.1;

    /** Pollen/nectar speed change per dpad press. */
    public static final double POWER_STEP = 0.05;

    // ---------------------------------------------------------------------
    // Hardware settings
    // ---------------------------------------------------------------------
    /** Default configuration name of the Control Hub's built-in IMU. */
    public static final String IMU_NAME = "imu";

    /**
     * The pollen and nectar motors face each other, so the same power spins them in
     * opposite directions. Nectar is reversed so both turn the same way.
     */
    public static final boolean POLLEN_REVERSED = false;
    public static final boolean NECTAR_REVERSED = true;
}
