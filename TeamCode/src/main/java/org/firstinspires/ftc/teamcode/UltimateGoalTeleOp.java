
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.lang.Math.*;

@TeleOp(name="UltimateGoalTeleOp", group="Linear Opmode")

//@Disabled /*disables the class so it cannot be selected from the list*/
public class UltimateGoalTeleOp extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor frontLeftDrive;
    private DcMotor frontRightDrive;
    private DcMotor backLeftDrive;
    private DcMotor backRightDrive;

    private DcMotor wobbleLiftMotor;
    private DcMotor intakeDriveMotor;
    private DcMotor beltDriveMotor;

    private DcMotor elbowDriveMotor;
    private Servo   gripServo;

    private double  angle;
    private double  robotAngle;
    private double  powers[];
    private double  rightX;

    int liftPosition = 0;
    int elbowPosition = 0;

    int sensitivity = 0;

    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        powers = new double[4];

        /* ROBOT HARDWARE */

        //second parameter is used in config
        frontLeftDrive  = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backLeftDrive  = hardwareMap.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");

        wobbleLiftMotor = hardwareMap.get(DcMotor.class, "wobbleLiftMotor");
        intakeDriveMotor = hardwareMap.get(DcMotor.class, "intakeDriveMotor");
        beltDriveMotor = hardwareMap.get(DcMotor.class, "beltDriveMotor");

        elbowDriveMotor = hardwareMap.get(DcMotor.class, "elbowDriveMotor");
        gripServo = hardwareMap.servo.get("gripServo");

        //set direction of motors
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        wobbleLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbowDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wobbleLiftMotor.setTargetPosition(0);
        elbowDriveMotor.setTargetPosition(0);
        wobbleLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elbowDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);




        /* END ROBOT HARDWARE */



        //wait for start
        waitForStart();

        runtime.reset();

        while (opModeIsActive()) {

            /*
            first gamepad
            */

            angle = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
            robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
            rightX = gamepad1.right_stick_x;
            powers[0] = angle * Math.cos(robotAngle) + rightX;
            powers[1] = angle * Math.sin(robotAngle) - rightX;
            powers[2] = angle * Math.sin(robotAngle) + rightX;
            powers[3] = angle * Math.cos(robotAngle) - rightX;
            double largest = 1.0;
            largest = Math.max(largest, Math.abs(powers[0]));
            largest = Math.max(largest, Math.abs(powers[1]));
            largest = Math.max(largest, Math.abs(powers[2]));
            largest = Math.max(largest, Math.abs(powers[3]));

            if (gamepad1.a) { //slow mode
                frontLeftDrive.setPower((powers[0]/largest)/2);
                frontRightDrive.setPower((powers[1]/largest)/2);
                backLeftDrive.setPower((powers[2]/largest)/2);
                backRightDrive.setPower((powers[3]/largest)/2);
            } else {
                frontLeftDrive.setPower(powers[0]/largest);
                frontRightDrive.setPower(powers[1]/largest);
                backLeftDrive.setPower(powers[2]/largest);
                backRightDrive.setPower(powers[3]/largest);
            }

            /*
            second gamepad
            */

            if (gamepad2.a) { //open
                gripServo.setPosition(1);
            } else if (gamepad2.b) { //close
                gripServo.setPosition(0.2);
            }

            intakeDriveMotor.setPower(gamepad2.right_trigger);
            beltDriveMotor.setPower(gamepad2.left_trigger);

            //wobble arm
            liftPosition += (int) gamepad2.left_stick_y*sensitivity;
            liftPosition = Range.clip(liftPosition, 0, 700);
            wobbleLiftMotor.setTargetPosition(liftPosition);
            wobbleLiftMotor.setPower(0.2);
            wobbleLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //wobble elbow
            elbowPosition += (int) gamepad2.right_stick_y*sensitivity;
            elbowPosition = Range.clip(elbowPosition, 0, 700);
            elbowDriveMotor.setTargetPosition(-elbowPosition);
            elbowDriveMotor.setPower(0.1);
            elbowDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            /*
            telemetry
             */

//            telemetry.addData("Arm Pos: ", wobbleLiftMotor.getCurrentPosition());
//            telemetry.addData("Elbow Pos: ", elbowDriveMotor.getCurrentPosition());

            for (int i = 0; i < 4; i ++ ) {
                telemetry.addData("Powers " + i + ":", powers[i]);
            }

            telemetry.addData("Run Time: ", runtime.toString());
            telemetry.update();

        }
    }

}

