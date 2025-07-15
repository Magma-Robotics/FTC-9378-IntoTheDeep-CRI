package org.firstinspires.ftc.teamcode.tuning;

import static dev.frozenmilk.mercurial.Mercurial.gamepad2;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Arm;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Slides;

import dev.frozenmilk.mercurial.Mercurial;

@Mercurial.Attach
@Arm.Attach
@Intake.Attach
@Drive.Attach
@Slides.Attach
@Config
@TeleOp(name="Tuning", group = "Tuning")
public class Tuning extends OpMode {
    public static double armTarget, slidesTarget;
    public static double pitch = 150;
    public static double roll = 0;
    public static double intake = 0.9;

    @Override
    public void init() {
        //just to make arm tuned in a similar environment to when everything is running
        //ignore everything in init
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(true));
        gamepad2().leftBumper()
                .onTrue(Arm.INSTANCE.setArmPosition(Arm.ArmState.HOME));
        gamepad2().rightBumper()
                .onTrue(Arm.INSTANCE.setArmPosition(Arm.ArmState.HIGH_SCORING));
        gamepad2().dpadUp()
                .onTrue(Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.HOME));
        gamepad2().dpadRight()
                .onTrue(Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.INTAKE));
        gamepad2().dpadLeft()
                .onTrue(Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.HIGH_SCORING));
        gamepad2().dpadDown()
                .onTrue(Intake.INSTANCE.setClawOpen(true));
        gamepad2().y()
                .onTrue(Intake.INSTANCE.setClawOpen(false));
        gamepad2().b()
                .onTrue(Slides.INSTANCE.setSlidePosition(Slides.SlideState.INTAKE));
        gamepad2().a()
                .onTrue(Slides.INSTANCE.setSlidePosition(Slides.SlideState.HOME));
        gamepad2().x()
                .onTrue(Slides.INSTANCE.setSlidePosition(Slides.SlideState.HIGH_SCORING));
    }

    @Override
    public void loop() {
        Arm.INSTANCE.setTarget(armTarget);
        Slides.INSTANCE.setTarget(slidesTarget);
        Intake.INSTANCE.setWristPosition(pitch, roll);
        Intake.INSTANCE.setClawPosition(intake);
        telemetry.addData("Arm Target: ", armTarget);
        telemetry.addData("Slides Target: ", slidesTarget);
        telemetry.addData("Arm Position: ", Arm.INSTANCE.getEncoder());
        telemetry.addData("Slides Position: ", Slides.INSTANCE.getEncoder());
        telemetry.update();
    }
}