package org.firstinspires.ftc.teamcode;


import android.util.Pair;

public class Constants {
    public static final class Drive {
        public static final String leftFront = "leftFront";
        public static final String leftBack = "leftBack";
        public static final String rightFront = "rightFront";
        public static final String rightBack = "rightBack";
    }


    public static final class Arm {
        public static final String leftPivot = "leftPivot";
        public static final String rightPivot = "rightPivot";
        public static final double highScoringPos = 5700.0;
        public static final double midScoringPos = 5700.0;
        public static final double highSpecimenScoringPos = 2800.0;
        public static final double lowSpecimenScoringPos = 1200.0;
        public static final double intakePos = 1350.0;
        public static final double homePos = 1500.0;
    }


    public static final class Slides {
        public static final String slides = "slides";
        public static final double highScoringPos = 2000.0;
        public static final double midScoringPos = 500.0;
        public static final double highSpecimenScoringPos = 550;
        public static final double lowSpecimenScoringPos = 0.0;
        public static final double intakePos = 1800.0;
        public static final double homePos = 0.0;
    }



    public static final class Intake {
        public static final String intakePivotLeft = "intakePivotLeft";
        public static final String intakePivotRight = "intakePivotRight";
        public static final String intake = "intake";

        //ignore for now too lazy to remove
        public static final double startPos = 0.0;
        public static final double scoringPos = 0.33;
        public static final double highSpecimenScoringPos = 0.6;
        public static final double intakePos = 0.96;
        public static final double homePos = 0.68;
        public static final double rotation0Pos = 0.0;
        public static final double rotation90Pos = 0.3;
        public static final double rotation180Pos = 0.64;

        public static final double clawOpenSmallerPos = 0.87;
        public static final double clawOpenPos = 0.97;
        public static final double clawClosedPos = 0.75;
        public static final double clawRegripPos = 0.79;

        //pitch and roll
        public static final Pair<Double, Double> homeWristPos = new Pair<>(150.0, 0.0);
        public static final Pair<Double, Double> protectedHomeWristPos = new Pair<>(230.0, 0.0);
        public static final Pair<Double, Double> rotatedIntakeWristPos = new Pair<>(50.0, -200.0);
        public static final Pair<Double, Double> intakeWristPos = new Pair<>(70.0, 0.0);
        public static final Pair<Double, Double> highScoringWristPos = new Pair<>(240.0, 0.0);
        public static final Pair<Double, Double> midScoringWristPos = new Pair<>(240.0, 0.0);
        public static final Pair<Double, Double> startWristPos = new Pair<>(150.0, 0.0);
        public static final Pair<Double, Double> highSpecimenWristPos = new Pair<>(230.0, 0.0);
        public static final Pair<Double, Double> lowSpecimenWristPos = new Pair<>(240.0, 0.0);
    }




}
