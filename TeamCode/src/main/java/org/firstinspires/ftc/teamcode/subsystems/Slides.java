package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.Slides.closeIntakePos;
import static org.firstinspires.ftc.teamcode.Constants.Slides.highScoringPos;
import static org.firstinspires.ftc.teamcode.Constants.Slides.highSpecimenScoringPos;
import static org.firstinspires.ftc.teamcode.Constants.Slides.homePos;
import static org.firstinspires.ftc.teamcode.Constants.Slides.farIntakePos;
import static org.firstinspires.ftc.teamcode.Constants.Slides.midScoringPos;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.config.SlidesPIDConfig;
import org.firstinspires.ftc.teamcode.util.DoubleComponent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.DoubleSupplier;

import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.util.controller.implementation.DoubleController;
import dev.frozenmilk.dairy.core.util.supplier.numeric.CachedMotionComponentSupplier;
import dev.frozenmilk.dairy.core.util.supplier.numeric.EnhancedDoubleSupplier;
import dev.frozenmilk.dairy.core.util.supplier.numeric.MotionComponents;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;

public class Slides extends SDKSubsystem {
    public static final Slides INSTANCE = new Slides();
    private Slides() {}

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

    public enum SlideState {
        HIGH_SCORING,
        MID_SCORING,
        SPECIMEN_SCORING,
        FAR_INTAKE,
        CLOSE_INTAKE,
        HOME
    }

    public static SlideState slideState;

    //motors
    private final Cell<DcMotorEx> slides = subsystemCell(() -> getHardwareMap().get(DcMotorEx.class, Constants.Slides.slides));

    //encoder
    private final Cell<EnhancedDoubleSupplier> encoder = subsystemCell(() -> new EnhancedDoubleSupplier(() -> (double) slides.get().getCurrentPosition()));
    //current of motor
    private final Cell<EnhancedDoubleSupplier> current = subsystemCell(() -> new EnhancedDoubleSupplier(() -> slides.get().getCurrent(CurrentUnit.AMPS)));

    //controller
    private double targetPos = 0.0;
    private double targetVel = 0.0;
    private double posTolerance = 100.0;
    private double velTolerance = 1.0;
    private final CachedMotionComponentSupplier<Double> targetSupplier = new CachedMotionComponentSupplier<>(motionComponent -> {
        if (motionComponent == MotionComponents.STATE) {
            return targetPos;
        }/*
        else if (motionComponent == MotionComponents.VELOCITY) {
            return targetVel;
        }*/
        return Double.NaN;
    });
    private final CachedMotionComponentSupplier<Double> toleranceSupplier = new CachedMotionComponentSupplier<>(motionComponent -> {
        if (motionComponent == MotionComponents.STATE) {
            return posTolerance;
        }
        /*else if (motionComponent == MotionComponents.VELOCITY) {
            return velTolerance;
        }*/
        return Double.NaN;
    });
    private final Cell<DoubleController> controller = subsystemCell(() ->
            new DoubleController(
                    targetSupplier,
                    encoder.get(),
                    toleranceSupplier,
                    (Double power) -> {
                        slides.get().setPower(power);
                    },
                    new DoubleComponent.P(MotionComponents.STATE, () -> SlidesPIDConfig.SlidesP)
                            .plus(new DoubleComponent.I(MotionComponents.STATE, () -> SlidesPIDConfig.SlidesI))
                            .plus(new DoubleComponent.D(MotionComponents.STATE, () -> SlidesPIDConfig.SlidesD))
            )
    );


    //set Target method
    public void setTarget(double target) {
        controller.get().setEnabled(true);
        this.targetPos = target;
        targetSupplier.reset();
    }

    public void retract() {
        controller.get().setEnabled(false);
        slides.get().setPower(-1);
    }

    public void extend() {
        controller.get().setEnabled(false);
        if (encoder.get().state() < 2100) {
            slides.get().setPower(1);
        }
    }

    public void stop() {
        controller.get().setEnabled(false);
        slides.get().setPower(0);
        //setTarget(0);
    }

    public void slidePower(double power) {
        controller.get().setEnabled(false);
        slides.get().setPower(power);
    }

    public void setSlides(SlideState slideState) {
        switch (slideState) {
            case HIGH_SCORING:
                setTarget(highScoringPos);
                break;
            case MID_SCORING:
                setTarget(midScoringPos);
                break;
            case SPECIMEN_SCORING:
                setTarget(highSpecimenScoringPos);
                break;
            case FAR_INTAKE:
                setTarget(farIntakePos);
                break;
            case CLOSE_INTAKE:
                setTarget(closeIntakePos);
                break;
            case HOME:
                setTarget(homePos);
                break;
        }
        Slides.slideState = slideState;
    }

    public double getVelocity() {
        return (this.encoder.get().rawVelocity());
    }

    public double getCurrent() {
        return (current.get().state());
    }

    public double getCurrentChange() {
        return (current.get().rawVelocity());
    }

    public double getEncoder() {
        return (encoder.get().state());
    }

    public boolean getControllerFinished() {
        return (controller.get().finished());
    }

    public void resetEncoder() {
        slides.get().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        targetSupplier.reset();
    }

    //init hook
    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        slides.get().setDirection(DcMotorSimple.Direction.REVERSE);
        slides.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        controller.get().setEnabled(false);
    }

    //start hook
    @Override
    public void preUserStartHook(@NonNull Wrapper opMode) {
        controller.get().setEnabled(true);
    }

    public Lambda retractSlides() {
        return new Lambda("retractSlides")
                .setInit(() -> retract());
    }

    public Lambda extendSlides() {
        return new Lambda("extendSlides")
                .setInit(() -> extend());
    }

    public Lambda stopSlides() {
        return new Lambda("stopSlides")
                .setInit(() -> stop());
    }

    public Lambda setSlidePower(DoubleSupplier power) {
        return new Lambda("setSlidePower")
                .setExecute(() -> slidePower(power.getAsDouble()));
                //.setFinish(() -> false);
    }

    public Lambda runToPosition(double target) {
        return new Lambda("run_to_position-slides")
                .setExecute(() -> setTarget(target));
                //.setFinish(() -> false);
    }
    public Lambda setSlidePosition(SlideState slideState) {
        return new Lambda("setSlidePosition")
                .setInit(() -> setSlides(slideState));
    }
}
