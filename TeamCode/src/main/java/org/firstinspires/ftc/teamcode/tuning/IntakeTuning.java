package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Intake;

import dev.frozenmilk.mercurial.Mercurial;

@Mercurial.Attach
@Intake.Attach
@Config
@TeleOp(name="IntakeTuning", group = "Tuning")
public class IntakeTuning extends OpMode {
    public static double pitch = 150;
    public static double intake = 0.9;
    public static double roll = 0;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    @Override
    public void loop() {
        Intake.INSTANCE.setWristPosition(pitch, roll);
        Intake.INSTANCE.setClawPosition(intake);
        telemetry.update();
    }
}