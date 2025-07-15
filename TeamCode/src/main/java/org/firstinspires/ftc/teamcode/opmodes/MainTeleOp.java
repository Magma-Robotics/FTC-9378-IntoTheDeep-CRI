package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.commands.GroupedCommands;
import org.firstinspires.ftc.teamcode.subsystems.Arm;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Slides;

import dev.frozenmilk.mercurial.Mercurial;

@Mercurial.Attach
@Arm.Attach
@Drive.Attach
@Intake.Attach
@Slides.Attach
@TeleOp(name = "Main TeleOp")
public class MainTeleOp extends OpMode {
    @Override
    public void init() {
        Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(true));
        Mercurial.gamepad1().b()
                .onTrue(GroupedCommands.INSTANCE.regripSpecimen());
        Mercurial.gamepad1().leftBumper()
                .onTrue(Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.ROTATED_INTAKE));

        Mercurial.gamepad2().dpadUp()
                .onTrue(GroupedCommands.INSTANCE.setHighScoringCommand());
        Mercurial.gamepad2().dpadLeft()
                .onTrue(GroupedCommands.INSTANCE.setSpecimenCommand());

        Mercurial.gamepad2().dpadDown()
                .onTrue(Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.ROTATED_INTAKE));
        Mercurial.gamepad2().dpadRight()
                .onTrue(Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.INTAKE));

        Mercurial.gamepad2().leftBumper()
                .onTrue(Intake.INSTANCE.setClawOpenAndClose());

        Mercurial.gamepad2().x()
                .onTrue(GroupedCommands.INSTANCE.specimenIntakeCommand());
        Mercurial.gamepad2().a()
                .onTrue(GroupedCommands.INSTANCE.scoringToHomeCommand());

        Mercurial.gamepad2().b()
                .onTrue(GroupedCommands.INSTANCE.extendIntakeCommand());
        Mercurial.gamepad2().y()
                .onTrue(GroupedCommands.INSTANCE.specimenPickupCommand());

        Mercurial.gamepad2().rightBumper()
                .onTrue(GroupedCommands.INSTANCE.grabSample());
    }

    @Override
    public void loop() {

    }
}
