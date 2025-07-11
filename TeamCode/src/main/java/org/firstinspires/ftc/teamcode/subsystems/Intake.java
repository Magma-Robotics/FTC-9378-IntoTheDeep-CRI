package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.Intake.clawClosedPos;
import static org.firstinspires.ftc.teamcode.Constants.Intake.clawOpenPos;
import static org.firstinspires.ftc.teamcode.Constants.Intake.clawOpenSmallerPos;
import static org.firstinspires.ftc.teamcode.Constants.Intake.clawRegripPos;
import static org.firstinspires.ftc.teamcode.Constants.Intake.homePos;
import static org.firstinspires.ftc.teamcode.Constants.Intake.intakePos;
import static org.firstinspires.ftc.teamcode.Constants.Intake.scoringPos;
import static org.firstinspires.ftc.teamcode.Constants.Intake.specimenScoringPos;
import static org.firstinspires.ftc.teamcode.Constants.Intake.startPos;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.cachinghardware.CachingServo;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;

public class Intake extends SDKSubsystem {
    public static final Intake INSTANCE = new Intake();
    private Intake() {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    public @interface Attach{}

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotation<>(Attach.class));

    @NonNull
    @Override
    public Dependency<?> getDependency() {
        return dependency;
    }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependency = dependency;
    }

    public enum IntakePivotState {
        START,
        SCORING,
        SPECIMEN_SCORING,
        INTAKE,
        HOME
    }

    public enum ClawState {
        OPEN,
        CLOSED
    }

    public boolean clawOpen;
    public static IntakePivotState intakePivotState = IntakePivotState.START;
    private static ClawState clawState = ClawState.CLOSED;

    //hardware
    private final Cell<CachingServo> intakePivotLeft = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, Constants.Intake.intakePivotLeft)));
    private final Cell<CachingServo> intakePivotRight = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, Constants.Intake.intakePivotRight)));
    private final Cell<CachingServo> intake = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, Constants.Intake.intake)));

    //set target method
    public void setPivotPosition(double position) {
        intakePivotLeft.get().setPosition(position);
        intakePivotRight.get().setPosition(position);
    }
    public double getPivotPosition() {
        return intakePivotLeft.get().getPosition();
    }
    public void setPivot(IntakePivotState intakePivotState) {
        switch (intakePivotState) {
            case SCORING:
                intakePivotLeft.get().setPosition(scoringPos);
                intakePivotRight.get().setPosition(scoringPos);
                break;
            case SPECIMEN_SCORING:
                intakePivotLeft.get().setPosition(specimenScoringPos);
                intakePivotRight.get().setPosition(specimenScoringPos);
                break;
            case INTAKE:
                intakePivotLeft.get().setPosition(intakePos);
                intakePivotRight.get().setPosition(intakePos);
                break;
            case HOME:
                intakePivotLeft.get().setPosition(homePos);
                intakePivotRight.get().setPosition(homePos);
                break;
            case START:
                intakePivotLeft.get().setPosition(startPos);
                intakePivotRight.get().setPosition(startPos);
        }
        Intake.intakePivotState = intakePivotState;
    }

    public void setClawPosition(double position) {
        intake.get().setPosition(position);
    }

    public void setWristPosition(double pitch, double roll) {
        if (pitch > 250) {
            pitch = 250;
        }
        else if (pitch < 50) {
            pitch = 50;
        }
        if (roll > 190) {
            roll = 190;
        }
        else if (roll < -220) {
            roll = -220;
        }
        double leftServoAngle = (pitch + (roll/2)) / 300;
        double rightServoAngle = (pitch - (roll/2)) / 300;
        intakePivotLeft.get().setPosition(leftServoAngle);
        intakePivotRight.get().setPosition(rightServoAngle);
    }

    public void setRotation(double rotation) {
        //rotatingIntake.get().setPosition(rotation);
    }

    public void clawOpen(boolean open) {

        if (open) {
            intake.get().setPosition(clawOpenPos);
            Intake.clawState = ClawState.OPEN;
        }
        else {
            intake.get().setPosition(clawClosedPos);
            Intake.clawState = ClawState.CLOSED;
        }
    }

    public void clawOpenAndClose() {
        switch (Intake.clawState) {
            case OPEN:
                intake.get().setPosition(clawClosedPos);
                Intake.clawState = ClawState.CLOSED;
                break;
            case CLOSED:
                intake.get().setPosition(clawOpenPos);
                Intake.clawState = ClawState.OPEN;
                break;
        }
    }

    public void clawOpenAndCloseSmaller() {
        switch (Intake.clawState) {
            case OPEN:
                intake.get().setPosition(clawClosedPos);
                Intake.clawState = ClawState.CLOSED;
                break;
            case CLOSED:
                intake.get().setPosition(clawOpenSmallerPos);
                Intake.clawState = ClawState.OPEN;
                break;
        }
    }

    public void clawOpenSmaller() {
            intake.get().setPosition(clawOpenSmallerPos);
            Intake.clawState = ClawState.OPEN;
    }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        intakePivotRight.get().setDirection(Servo.Direction.REVERSE);
        intake.get().setDirection(Servo.Direction.FORWARD);
    }

    public Lambda setWristControl() {
        BoundGamepad gamepad2 = Mercurial.gamepad2();
        return new Lambda("setWristControl")
                .setExecute(() -> {
                    setWristPosition((-gamepad2.leftStickY().state() + 1)/2*300, gamepad2.leftStickX().state()*220);
                });
    }

    public Lambda setIntakePivot(IntakePivotState intakePivotState) {
        return new Lambda("setIntakePivot")
                .setInit(() -> setPivot(intakePivotState));
    }

    public Lambda setClawOpen(boolean open) {
        return new Lambda("setClaw")
                .setInit(() -> clawOpen(open));
    }

    public Lambda setClawOpenAndClose() {
        return new Lambda("setClawOpenAndClose")
                .setInit(() -> clawOpenAndClose());
    }

    public Lambda setClawPos(double pos) {
        return new Lambda("SetClawPos")
                .setInit(() -> setClawPosition(pos));
    }

    public Lambda setIntakeRotation(double position) {
        return new Lambda("setIntakeRotation")
                .setInit(() -> setRotation(position));
    }

    public Lambda clawRegrip() {
        return new Lambda("clawRegrip")
                .setInit(() -> setClawPosition(clawRegripPos));
    }

    public Lambda setIntakePivotPosition(double position) {
        return new Lambda("setIntakePivotPosition")
                .setInit(() -> {
                    intakePivotLeft.get().setPosition(position);
                    intakePivotRight.get().setPosition(position);
                });
    }

    public Lambda setClawOpenSmaller() {
        return new Lambda("setClawOpenSmaller")
                .setInit(() -> clawOpenSmaller());
    }

    public Lambda setClawOpenAndCloseSmaller() {
        return new Lambda("setClawOpenSmaller")
                .setInit(() -> clawOpenAndCloseSmaller());
    }

}
