package org.firstinspires.ftc.teamcode.Everythingneeded.control;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Closed-loop controller producing Proportional, Integral, Derivative and constant
 * Feedforward output, with anti-windup clamping on the integral term.
 *
 *   error      = target - measured
 *   integral   = integral + error * deltaTime
 *   derivative = (error - previousError) / deltaTime
 *   output     = kP * error + kI * integral + kD * derivative + kF
 *
 * The output is clamped to [-1.0, 1.0] so it can be fed straight to a motor.
 */
public class PIDFController {

    public double kP, kI, kD, kF;

    private double maxIntegral = 1.0;
    private double integralSum = 0.0;
    private double lastError = 0.0;

    private final ElapsedTime timer;

    public PIDFController(double p, double i, double d, double f) {
        this.kP = p;
        this.kI = i;
        this.kD = d;
        this.kF = f;
        this.timer = new ElapsedTime();
    }

    public void setGains(double p, double i, double d, double f) {
        this.kP = p;
        this.kI = i;
        this.kD = d;
        this.kF = f;
    }

    public void reset() {
        integralSum = 0.0;
        lastError = 0.0;
        timer.reset();
    }

    public double calculate(double target, double measured) {
        double deltaTime = timer.seconds();
        timer.reset();

        double error = target - measured;

        integralSum = integralSum + error * deltaTime;
        integralSum = clamp(integralSum, -maxIntegral, maxIntegral);

        // Two calls inside the same timer tick would otherwise divide by zero.
        double derivative = deltaTime > 0.0 ? (error - lastError) / deltaTime : 0.0;
        lastError = error;

        double output = (kP * error) + (kI * integralSum) + (kD * derivative) + kF;
        return clamp(output, -1.0, 1.0);
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
