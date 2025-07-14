package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.Arm.highScoringPos;
import static org.firstinspires.ftc.teamcode.Constants.Arm.highSpecimenScoringPos;
import static org.firstinspires.ftc.teamcode.Constants.Arm.homePos;
import static org.firstinspires.ftc.teamcode.Constants.Arm.intakePos;
import static org.firstinspires.ftc.teamcode.Constants.Arm.midScoringPos;
import static org.firstinspires.ftc.teamcode.Constants.Arm.highSpecimenScoringPos;
import static org.firstinspires.ftc.teamcode.config.ArmPIDConfig.ArmD;
import static org.firstinspires.ftc.teamcode.config.ArmPIDConfig.ArmI;
import static org.firstinspires.ftc.teamcode.config.ArmPIDConfig.ArmP;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.DoubleComponent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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

public class Arm extends SDKSubsystem {
    public static final Arm INSTANCE = new Arm();
    private Arm() {}

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

    public enum ArmState {
        HIGH_SCORING,
        MID_SCORING,
        SPECIMEN_SCORING,
        INTAKE,
        HOME
    }

    public static ArmState armState = ArmState.HOME;

    //motors
    private final Cell<DcMotorEx> leftPivot = subsystemCell(() -> getHardwareMap().get(DcMotorEx.class, Constants.Arm.leftPivot));
    private final Cell<DcMotorEx> rightPivot = subsystemCell(() -> getHardwareMap().get(DcMotorEx.class, Constants.Arm.rightPivot));


    //encoder
    private final Cell<EnhancedDoubleSupplier> encoder = subsystemCell(() -> new EnhancedDoubleSupplier(() -> (double) leftPivot.get().getCurrentPosition()));
    //current of motor
    private final Cell<EnhancedDoubleSupplier> current = subsystemCell(() -> new EnhancedDoubleSupplier(() -> leftPivot.get().getCurrent(CurrentUnit.AMPS)));

    //controller
    private double targetPos = 0.0;
    private double targetVel = 0.0;
    private double posTolerance = 50.0;
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
        }/*
        else if (motionComponent == MotionComponents.VELOCITY) {
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
                        leftPivot.get().setPower(power);
                        rightPivot.get().setPower(power);
                    },
                    new DoubleComponent.P(MotionComponents.STATE, () -> ArmP)
                            .plus(new DoubleComponent.I(MotionComponents.STATE, () -> ArmI))
                            .plus(new DoubleComponent.D(MotionComponents.STATE, () -> ArmD))
            )
    );


    public void up() {
        controller.get().setEnabled(false);
        leftPivot.get().setPower(1);
        rightPivot.get().setPower(1);
    }

    public void down() {
        controller.get().setEnabled(false);
        leftPivot.get().setPower(-1);
        rightPivot.get().setPower(-1);
    }

    public void stop() {
        controller.get().setEnabled(false);
        leftPivot.get().setPower(0);
        rightPivot.get().setPower(0);
    }


    //set Target method
    public void setTarget(double target) {
        controller.get().setEnabled(true);
        this.targetPos = target;
        targetSupplier.reset();
    }

    public void setArm(ArmState armState) {
        switch (armState) {
            case HIGH_SCORING:
                setTarget(highScoringPos);
                break;
            case MID_SCORING:
                setTarget(midScoringPos);
                break;
            case SPECIMEN_SCORING:
                setTarget(highSpecimenScoringPos);
                break;
            case INTAKE:
                setTarget(intakePos);
                break;
            case HOME:
                setTarget(homePos);
                break;
        }
        Arm.armState = armState;
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
        leftPivot.get().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightPivot.get().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        targetSupplier.reset();
    }

    //init hook
    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        leftPivot.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightPivot.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftPivot.get().setDirection(DcMotorSimple.Direction.REVERSE);
        controller.get().setEnabled(false);
    }

    //start hook
    @Override
    public void preUserStartHook(@NonNull Wrapper opMode) {
        controller.get().setEnabled(true);
    }

    public Lambda armUp() {
        return new Lambda("armUp")
                .setInit(() -> up());
    }

    public Lambda armDown() {
        return new Lambda("armDown")
                .setInit(() -> down());
    }

    public Lambda stopArm() {
        return new Lambda("stopArm")
                .setInit(() -> stop());
    }

    public Lambda runToPosition(double target) {
        return new Lambda("run_to_position-arm")
                .setInit(() -> setTarget(target))
                .setFinish(() -> controller.get().finished());
    }
    public Lambda setArmPosition(ArmState armState) {
        return new Lambda("setArmPosition")
                .setInit(() -> setArm(armState));
    }
}
