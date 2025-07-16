package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.GroupedCommands;
import org.firstinspires.ftc.teamcode.subsystems.Arm;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Slides;

import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundBooleanSupplier;

@Mercurial.Attach
@Arm.Attach
@Drive.Attach
@Intake.Attach
@Slides.Attach
@TeleOp(name = "CRI TeleOp")
@Disabled
public class TestTeleop extends OpMode {
    private BoundBooleanSupplier rightBumper, rightTriggerTrue, rightTriggerFalse;

    @Override
    public void init() {
        rightBumper = Mercurial.gamepad2().rightBumper();
        rightTriggerTrue = Mercurial.gamepad2().rightTrigger().conditionalBindState().greaterThanEqualTo(0.01).bind();

        Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(true));
        Mercurial.gamepad1().b()
                .onTrue(GroupedCommands.INSTANCE.regripSpecimen());
        Mercurial.gamepad1().leftBumper()
                .onTrue(Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.ROTATED_INTAKE));
        Mercurial.gamepad2().leftBumper()
                .onTrue(GroupedCommands.INSTANCE.setSpecimenCommand());
        Mercurial.gamepad2().rightBumper()
                .onTrue(GroupedCommands.INSTANCE.grabSample());
        Mercurial.gamepad2().a()
                .onTrue(GroupedCommands.INSTANCE.intakeToHomeCommand());
        Mercurial.gamepad2().b()
                .onTrue(GroupedCommands.INSTANCE.setHighScoringCommand());
        Mercurial.gamepad2().x()
                .onTrue(GroupedCommands.INSTANCE.extendIntakeCommand());
        Mercurial.gamepad2().y()
                .onTrue(GroupedCommands.INSTANCE.scoringToHomeCommand());
        Mercurial.gamepad2().dpadUp()
                .onTrue(Intake.INSTANCE.setClawOpenAndClose());
        Mercurial.gamepad2().dpadDown()
                .onTrue(Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.HOME));
        Mercurial.gamepad2().dpadLeft()
                .onTrue(Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.FAR_INTAKE));
        Mercurial.gamepad2().dpadRight()
                .onTrue(Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.HIGH_SCORING));
        Mercurial.gamepad2().back()
                .onTrue(Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.ROTATED_INTAKE));

        Mercurial.gamepad2().rightTrigger().conditionalBindState().greaterThanEqualTo(0.01).bind()
                .whileTrue(GroupedCommands.INSTANCE.extendSlidesAndArm(() -> Mercurial.gamepad2().rightTrigger().state()));
        Mercurial.gamepad2().leftStickY().conditionalBindState().greaterThanEqualTo(0.01).lessThanEqualTo(-0.01).bind()
                .or(Mercurial.gamepad2().leftStickX().conditionalBindState().greaterThanEqualTo(0.01).lessThanEqualTo(-0.01).bind())
                .whileTrue(Intake.INSTANCE.setWristControl());
        Mercurial.gamepad2().rightStickY().conditionalBindState().greaterThanEqualTo(0.01).lessThanEqualTo(-0.01).bind()
                .whileTrue(Slides.INSTANCE.setSlidePower(() -> Mercurial.gamepad2().rightStickY().state()))
                .onFalse(Slides.INSTANCE.stopSlides());
    }

    @Override
    public void loop() {
        /*if (rightBumper.onTrue()) {
            Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.slowDriveCommand(true));
        }
        if (rightBumper.onFalse()) {
            Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(true));
        }*/
    }
}
