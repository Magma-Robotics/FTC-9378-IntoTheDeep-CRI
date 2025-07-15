package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.Arm;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Slides;

import java.util.function.DoubleSupplier;

import dev.frozenmilk.mercurial.commands.groups.CommandGroup;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.Wait;

public class GroupedCommands {
    public static final GroupedCommands INSTANCE = new GroupedCommands();
    private GroupedCommands() {}

   /*public CommandGroup setHomeCommand() {
       if (Arm.armState == Arm.ArmState.HIGH_SCORING) {
           return new Parallel(
                   Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.HOME),
                   new Sequential(
                           new Wait(0.3),
                           Slides.INSTANCE.setSlidePosition(Slides.SlideState.HOME)
                   ),
                   new Sequential(
                           new Wait(1),
                           Arm.INSTANCE.setArmPosition(Arm.ArmState.HOME)
                   )
           );
       }
       else if (Arm.armState == Arm.ArmState.INTAKE){
           return new Parallel(
                   Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.HOME),
                   Slides.INSTANCE.setSlidePosition(Slides.SlideState.HOME)
           );
       }
       else if (Arm.armState == Arm.ArmState.SPECIMEN_SCORING) {
           return new Parallel(
                   Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.INTAKE),
                   new Sequential(
                           new Wait(2),
                           Intake.INSTANCE.setClawOpen(true)
                   ),
                   new Sequential(
                           new Wait(2.5),
                           Arm.INSTANCE.setArmPosition(Arm.ArmState.HOME)
                   ),
                   new Sequential(
                           new Wait(2.5),
                           Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.HOME)
                   )
           );
       }
       else {
           return new Parallel(
                   Arm.INSTANCE.setArmPosition(Arm.ArmState.HOME),
                   Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.HOME),
                   Slides.INSTANCE.setSlidePosition(Slides.SlideState.HOME)
           );
       }
   }*/

    public CommandGroup extendSlidesAndArm(DoubleSupplier triggerValue) {
        return new Parallel(
                Arm.INSTANCE.runToPosition(800 + triggerValue.getAsDouble()*500),
                Slides.INSTANCE.runToPosition(400 + triggerValue.getAsDouble()*1400)
        );
    }

    public CommandGroup intakeToHomeCommand() {
        return new Parallel(
                Arm.INSTANCE.setArmPosition(Arm.ArmState.HOME),
                Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.PROTECTED_HOME),
                Slides.INSTANCE.setSlidePosition(Slides.SlideState.HOME)
        );
    }

    public CommandGroup specimenIntakeCommand() {
        return new Parallel(
                Arm.INSTANCE.setArmPosition(Arm.ArmState.HOME),
                Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.HOME),
                Slides.INSTANCE.setSlidePosition(Slides.SlideState.HOME)
        );
    }

    public CommandGroup specimenPickupCommand() {
        return new Parallel(
                Arm.INSTANCE.setArmPosition(Arm.ArmState.SPECIMEN_SCORING),
                Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.PROTECTED_HOME)
        );
    }

    public CommandGroup scoringToHomeCommand() {
        return new Parallel(
                Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.HOME),
                new Sequential(
                        new Wait(0.4),
                        //Intake.INSTANCE.setWristPosition(),
                        Slides.INSTANCE.setSlidePosition(Slides.SlideState.HOME)
                ),
                new Sequential(
                        new Wait(1),
                        Arm.INSTANCE.setArmPosition(Arm.ArmState.HOME)
                ),
                Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.PROTECTED_HOME)
        );
    }

    public CommandGroup setSpecimenCommand() {
        return new Parallel(
                Arm.INSTANCE.setArmPosition(Arm.ArmState.SPECIMEN_SCORING),
                Slides.INSTANCE.setSlidePosition(Slides.SlideState.SPECIMEN_SCORING),
                Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.SPECIMEN_SCORING)
        );
    }

    public CommandGroup scoreSpecimenCommand() {
        return new Parallel(
                Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.INTAKE),
                new Sequential(
                        new Wait(1),
                        Intake.INSTANCE.setClawOpen(true)
                ),
                new Sequential(
                        new Wait(1.5),
                        Arm.INSTANCE.setArmPosition(Arm.ArmState.HOME)
                ),
                new Sequential(
                        new Wait(1.5),
                        Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.HOME)
                )
        );
    }

    public CommandGroup extendIntakeCommand() {
        return new Parallel(
                Slides.INSTANCE.setSlidePosition(Slides.SlideState.INTAKE),
                Intake.INSTANCE.setClawOpenSmaller(),
                new Sequential(
                        new Wait(0.25),
                        Arm.INSTANCE.setArmPosition(Arm.ArmState.INTAKE)
                ),
                new Sequential(
                        new Wait(0.5),
                        Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.INTAKE)
                )
        );
    }

    public CommandGroup grabSample() {
        return new Parallel(
                //move arm down
                Arm.INSTANCE.runToPosition(1150),
                //grab
                new Sequential(
                        new Wait(0.06),
                        Intake.INSTANCE.setClawOpen(false)
                ),

                //move arm back up
                new Sequential(
                        new Wait(0.23),
                        Arm.INSTANCE.setArmPosition(Arm.ArmState.INTAKE)
                )
        );
    }

    public CommandGroup setHighScoringCommand() {
        return new Parallel(
                Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.HOME),
                Arm.INSTANCE.setArmPosition(Arm.ArmState.HIGH_SCORING),
                new Sequential(
                        new Wait(1.3),
                        Slides.INSTANCE.setSlidePosition(Slides.SlideState.HIGH_SCORING)
                ),
                new Sequential(
                        new Wait(2),
                        Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.HIGH_SCORING)
                )
        );
    }

    public CommandGroup setMidScoringCommand() {
        return new Parallel(
                Arm.INSTANCE.setArmPosition(Arm.ArmState.MID_SCORING),
                new Sequential(
                        new Wait(0.7),
                        Slides.INSTANCE.setSlidePosition(Slides.SlideState.MID_SCORING)
                ),
                new Sequential(
                        new Wait(1.5),
                        Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.MID_SCORING)
                )
        );
    }


    public CommandGroup autoSetScoringCommand() {
        return new Parallel(
                Arm.INSTANCE.setArmPosition(Arm.ArmState.HIGH_SCORING),
                new Sequential(
                        new Wait(0.7),
                        Slides.INSTANCE.setSlidePosition(Slides.SlideState.HIGH_SCORING)
                ),
                new Sequential(
                        new Wait(1.5),
                        Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.HIGH_SCORING)
                )
        );
    }

    public CommandGroup regripSpecimen() {
        return new Sequential(
                Intake.INSTANCE.clawRegrip(),
                new Wait(0.5),
                Intake.INSTANCE.setClawOpen(false)
        );
    }

    public CommandGroup setSpecimenBackwardsCommand() {
        return new Parallel(
                Arm.INSTANCE.runToPosition(4000),
                Intake.INSTANCE.setIntakePivotPosition(0.55)
        );
    }

    public CommandGroup scoreSpecimenBackwardsCommand() {
        return new Parallel(
                Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.HIGH_SCORING),
                new Sequential(
                        new Wait(1),
                        Intake.INSTANCE.setClawOpen(false)
                ),
                new Sequential(
                        new Wait(1.3),
                        Arm.INSTANCE.setArmPosition(Arm.ArmState.HOME)
                ),
                new Sequential(
                        new Wait(1.5),
                        Intake.INSTANCE.setIntakePivot(Intake.IntakePivotState.HOME)
                )
        );
    }
}
