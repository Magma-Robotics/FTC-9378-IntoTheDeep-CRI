package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Arm;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Slides;
import org.firstinspires.ftc.teamcode.util.MercurialAction;
import org.firstinspires.ftc.teamcode.util.SilkRoad;

import dev.frozenmilk.mercurial.Mercurial;

@Arm.Attach
@Drive.Attach
@Intake.Attach
@Slides.Attach
@SilkRoad.Attach
@Mercurial.Attach
@Config
@Autonomous(name = "DriveForwardAndPark", group = "Autonomous")

public class DriveForwardAndParkAuto extends OpMode {
    private final Pose2d initialPose = new Pose2d(0, -61.5, Math.toRadians(90));
    private Action park;
    private MecanumDrive drive;

    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap, initialPose);
        TrajectoryActionBuilder tab0 = drive.actionBuilder(initialPose)
                .lineToY(-25);

        park = tab0.build();
    }

    @Override
    public void start() {
        SilkRoad.RunAsync(
                new SequentialAction(
                        new MercurialAction(Intake.INSTANCE.setClawOpen(false)),
                        new SleepAction(24),
                        new MercurialAction(Arm.INSTANCE.runToPosition(2700)),
                        new MercurialAction(Slides.INSTANCE.runToPosition(700)),
                        new SleepAction(1.2),
                        park
                )
        );

    }

    @Override
    public void loop() {

    }
}
