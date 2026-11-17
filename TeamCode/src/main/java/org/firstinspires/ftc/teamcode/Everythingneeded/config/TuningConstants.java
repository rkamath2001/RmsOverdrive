package org.firstinspires.ftc.teamcode.Everythingneeded.config;

import com.acmerobotics.dashboard.config.Config;

/**
 * Mutable coefficients tuned live during practice via FTC Dashboard.
 * Fields must stay public, static and non-final for the dashboard to edit them.
 */
@Config
public class TuningConstants {

    private TuningConstants() {
    }

    // ---------------------------------------------------------------------
    // Limelight Turn Alignment PID
    // ---------------------------------------------------------------------
    public static double VISION_ALIGN_KP = 0.035;
    public static double VISION_ALIGN_KI = 0.0;
    public static double VISION_ALIGN_KD = 0.002;

    // ---------------------------------------------------------------------
    // Shooter voltage compensation (power scaled by NOMINAL_VOLTAGE / battery voltage)
    // ---------------------------------------------------------------------
    public static boolean VOLTAGE_COMP_ENABLED = true;
    /** Battery voltage at which the tuned shot powers were measured. */
    public static double NOMINAL_VOLTAGE = 12.5;


    // ---------------------------------------------------------------------
    // Path auton (AutoRed). Legs are timed, so tune the seconds on the field.
    // ---------------------------------------------------------------------
    public static double PATH_DRIVE_POWER = 0.7;
    public static double PATH_LEG1_SECONDS = 1.3;
    public static double PATH_LEG2_SECONDS = 2.3;
    public static double PATH_LEG3_SECONDS = 2.2;
    public static double PATH_SHOOT_POWER = 0.90;
    public static double PATH_SHOOT_SECONDS = 2.0;
    /** Turn power per degree of heading error while following a path. */
    public static double PATH_HEADING_KP = 0.02;

    // ---------------------------------------------------------------------
    // AutoBlue (2 legs from the Pedro visualizer; timed, IMU heading). TUNE ON FIELD.
    // ---------------------------------------------------------------------
    public static double BLUE_DRIVE_POWER = 0.5;
    /** Backward part of leg 1 in seconds (~1 inch per 0.05 s at 0.5 power). */
    public static double BLUE_LEG1_FORWARD_SECONDS = 0.1;
    /** Left (strafe) part of leg 1 in seconds. Runs after the backward part. */
    public static double BLUE_LEG1_LEFT_SECONDS = 1.38;
    /** Leg 1 total seconds = backward part + left part. */
    public static double blueLeg1TotalSeconds() {
        return BLUE_LEG1_FORWARD_SECONDS + BLUE_LEG1_LEFT_SECONDS;
    }
    /** Leg 2 (after shooting): right (strafe) part in seconds. Runs first. */
    public static double BLUE_LEG2_LEFT_SECONDS = 1.3;
    /** Leg 2: backward part in seconds. Runs after the right part. */
    public static double BLUE_LEG2_FORWARD_SECONDS = 2.0;
    /** Leg 2 total seconds = right part + backward part. */
    public static double blueLeg2TotalSeconds() {
        return BLUE_LEG2_LEFT_SECONDS + BLUE_LEG2_FORWARD_SECONDS;
    }
    /** Max time to wait for the pollen motor to reach BLUE_SHOOT_POWER before opening anyway. */
    public static double BLUE_SPINUP_TIMEOUT_SECONDS = 3.0;
    /** Turn power per degree of heading error. */
    public static double BLUE_HEADING_KP = 0.02;
    /** Pollen shot power for the auto; the door opens once the motor reaches it. */
    public static double BLUE_SHOOT_POWER = 1.00;
    /** Degrees the door turns to open (negative to open the other way). */
    public static double BLUE_DOOR_OPEN_DEGREES = 60.0;
    /** How long the shooter runs with the door open. */
    public static double BLUE_SHOOT_SECONDS = 5.0;
}
