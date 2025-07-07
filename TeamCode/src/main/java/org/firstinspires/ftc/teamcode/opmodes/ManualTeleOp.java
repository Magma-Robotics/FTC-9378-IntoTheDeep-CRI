package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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
@TeleOp(name = "Manual TeleOp")
public class ManualTeleOp extends OpMode {
    private BoundBooleanSupplier rightBumper, rightTriggerTrue, rightTriggerFalse;

    @Override
    public void init() {
        rightBumper = Mercurial.gamepad2().rightBumper();
        rightTriggerTrue = Mercurial.gamepad2().rightTrigger().conditionalBindState().greaterThanEqualTo(0.01).bind();

        Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(true));
        Intake.INSTANCE.setDefaultCommand(Intake.INSTANCE.setWristControl());
        Mercurial.gamepad2().a()
                .onTrue(Slides.INSTANCE.retractSlides())
                .onFalse(Slides.INSTANCE.stopSlides());
        Mercurial.gamepad2().b()
                .onTrue(Slides.INSTANCE.extendSlides())
                .onFalse(Slides.INSTANCE.stopSlides());
        Mercurial.gamepad2().dpadUp()
                .onTrue(Arm.INSTANCE.armUp())
                .onFalse(Arm.INSTANCE.stopArm());
        Mercurial.gamepad2().dpadDown()
                .onTrue(Arm.INSTANCE.armDown())
                .onFalse(Arm.INSTANCE.stopArm());
        Mercurial.gamepad2().rightBumper()
                .onTrue(Intake.INSTANCE.setClawOpenAndClose());
        /*Mercurial.gamepad2().rightTrigger().conditionalBindState().greaterThanEqualTo(0.01).bind()
                .onTrue(GroupedCommands.INSTANCE.extendSlidesAndArm());*/
    }

    @Override
    public void loop() {
        if (rightBumper.onTrue()) {
            Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.slowDriveCommand(true));
        }
        if (rightBumper.onFalse()) {
            Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(true));
        }
    }
}
