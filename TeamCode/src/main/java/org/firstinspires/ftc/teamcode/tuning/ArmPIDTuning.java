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
@TeleOp(name="ArmPIDTuning", group = "Tuning")
public class ArmPIDTuning extends OpMode {
    public static double target = 0;

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
                .onTrue(Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.FAR_INTAKE));
        gamepad2().dpadLeft()
                .onTrue(Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.HIGH_SCORING));
        gamepad2().dpadDown()
                .onTrue(Intake.INSTANCE.setClawOpen(true));
        gamepad2().y()
                .onTrue(Intake.INSTANCE.setClawOpen(false));
        gamepad2().b()
                .onTrue(Slides.INSTANCE.setSlidePosition(Slides.SlideState.FAR_INTAKE));
        gamepad2().a()
                .onTrue(Slides.INSTANCE.setSlidePosition(Slides.SlideState.HOME));
        gamepad2().x()
                .onTrue(Slides.INSTANCE.setSlidePosition(Slides.SlideState.HIGH_SCORING));
    }

    @Override
    public void loop() {
        Arm.INSTANCE.setTarget(target);
        telemetry.addData("Target: ", target);
        telemetry.addData("Position: ", Arm.INSTANCE.getEncoder());
        telemetry.update();
    }
}