package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Arm;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Slides;

import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.Lambda;

@Mercurial.Attach
@Arm.Attach
@Drive.Attach
@Intake.Attach
@Slides.Attach
@TeleOp(name = "CRI TeleOp")
public class CRI extends OpMode {
    @Override
    public void init() {
        Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(true));
       /* Mercurial.gamepad2().a()
                .onTrue(Slides.INSTANCE.retractSlides())
                .onFalse(Slides.INSTANCE.stopSlides());
        Mercurial.gamepad2().b()
                .onTrue(Slides.INSTANCE.extendSlides());*/
        Mercurial.gamepad2().dpadUp()
                .whileTrue(Arm.INSTANCE.armUp());
        Mercurial.gamepad2().dpadDown()
                .whileTrue(Arm.INSTANCE.armDown())
                .onFalse(Arm.INSTANCE.stopArm());
        Mercurial.gamepad2().x()
                .onTrue(Intake.INSTANCE.setClawOpenAndClose());
    }

    @Override
    public void loop() {
        if (Mercurial.gamepad1().rightBumper().onTrue()) {
            Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.slowDriveCommand(true));
        }
        if (Mercurial.gamepad1().rightBumper().onFalse()) {
            Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(true));
        }
        if (Mercurial.gamepad2().leftStickY().state() > 0.1 || Mercurial.gamepad2().rightStickY().state() > 0.1 || Mercurial.gamepad2().leftStickY().state() < -0.1 || Mercurial.gamepad2().rightStickY().state() < -0.1) {
            Intake.INSTANCE.setWristControl();
        }

        if (Mercurial.gamepad2().a().state()) {
            Slides.INSTANCE.retract();
        }
        else if (Mercurial.gamepad2().b().state()) {
            Slides.INSTANCE.extend();
        }
        else {
            Slides.INSTANCE.stop();
        }
    }
}
