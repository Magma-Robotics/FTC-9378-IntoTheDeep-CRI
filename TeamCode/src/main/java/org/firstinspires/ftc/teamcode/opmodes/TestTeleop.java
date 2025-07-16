package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.subsystems.Intake.IntakePivotState;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.commands.GroupedCommands;
import org.firstinspires.ftc.teamcode.subsystems.Arm;
import org.firstinspires.ftc.teamcode.subsystems.Arm.ArmState;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Slides;

import dev.frozenmilk.mercurial.Mercurial;

@Mercurial.Attach
@Arm.Attach
@Drive.Attach
@Intake.Attach
@Slides.Attach
@TeleOp(name = "Test Teleop")
@Disabled
public class TestTeleop extends OpMode {
    @Override
    public void init() {
        Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(true));
        Mercurial.gamepad2().leftBumper()
                .onTrue(Arm.INSTANCE.setArmPosition(ArmState.SPECIMEN_SCORING));
        Mercurial.gamepad2().rightBumper()
                .onTrue(Arm.INSTANCE.setArmPosition(ArmState.HIGH_SCORING));
        /*Mercurial.gamepad2().a()
                .onTrue(Arm.INSTANCE.setArmPosition(ArmState.SPECIMEN_SCORING));*/
        Mercurial.gamepad2().b()
                .onTrue(GroupedCommands.INSTANCE.setHighScoringCommand());
        Mercurial.gamepad2().x()
                .onTrue(GroupedCommands.INSTANCE.extendIntakeCommand());
        /*Mercurial.gamepad2().y()
                .onTrue(GroupedCommands.INSTANCE.setHomeCommand());*/
        Mercurial.gamepad2().dpadUp()
                .onTrue(Intake.INSTANCE.setClawOpenAndClose());
        Mercurial.gamepad2().dpadDown()
                .onTrue(Intake.INSTANCE.setIntakePivot(IntakePivotState.HOME));
        Mercurial.gamepad2().dpadLeft()
                .onTrue(Intake.INSTANCE.setIntakePivot(IntakePivotState.FAR_INTAKE));
        Mercurial.gamepad2().dpadRight()
                .onTrue(Intake.INSTANCE.setIntakePivot(IntakePivotState.HIGH_SCORING));
        Mercurial.gamepad2().leftStickButton()
                .onTrue(Intake.INSTANCE.setIntakeRotation(Constants.Intake.rotation0Pos));
        Mercurial.gamepad2().rightStickButton()
                .onTrue(Intake.INSTANCE.setIntakeRotation(Constants.Intake.rotation90Pos));

    }

    @Override
    public void loop() {

    }
}
