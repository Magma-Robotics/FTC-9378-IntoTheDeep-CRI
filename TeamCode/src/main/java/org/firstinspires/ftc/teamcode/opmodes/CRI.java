package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.commands.GroupedCommands;
import org.firstinspires.ftc.teamcode.subsystems.Arm;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Slides;

import dev.frozenmilk.dairy.core.util.supplier.logical.EnhancedBooleanSupplier;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundBooleanSupplier;

@Mercurial.Attach
@Arm.Attach
@Drive.Attach
@Intake.Attach
@Slides.Attach
@TeleOp(name = "CRI TeleOp")
public class CRI extends OpMode {
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
                .onTrue(GroupedCommands.INSTANCE.setScoringCommand());
        Mercurial.gamepad2().x()
                .onTrue(GroupedCommands.INSTANCE.extendIntakeCommand());
        Mercurial.gamepad2().y()
                .onTrue(GroupedCommands.INSTANCE.scoringToHomeCommand());
        Mercurial.gamepad2().dpadUp()
                .onTrue(Intake.INSTANCE.setClawOpenAndClose());
        Mercurial.gamepad2().dpadDown()
                .onTrue(Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.HOME));
        Mercurial.gamepad2().dpadLeft()
                .onTrue(Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.INTAKE));
        Mercurial.gamepad2().dpadRight()
                .onTrue(Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.SCORING));
        Mercurial.gamepad2().leftStickButton()
                .onTrue(Intake.INSTANCE.setIntakeRotation(Constants.Intake.rotation0Pos));
        Mercurial.gamepad2().rightStickButton()
                .onTrue(Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.ROTATED_INTAKE));
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
