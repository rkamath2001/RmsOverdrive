package org.firstinspires.ftc.teamcode.team1234.config;

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
}
