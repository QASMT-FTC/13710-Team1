
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
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

    private Servo   elbowServo;
    private Servo   gripServo;

    private double  angle;
    private double  robotAngle;
    private double  powers[];
    private double  rightX;

    private double  elbowPosition, gripPosition;
    private double  elbowOpen, elbowClose;
    private double  gripOpen, gripClose;
    private boolean elbowCheck, gripCheck;

    private double  MIN_POSITION = 0, MAX_POSITION = 1;


    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        powers = new double[4];
        elbowOpen = 1;
        elbowClose = 0;
        gripOpen = 1;
        gripClose = 0;

        /* ROBOT HARDWARE */

        //second parameter is used in config
        frontLeftDrive  = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backLeftDrive  = hardwareMap.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");

        wobbleLiftMotor = hardwareMap.get(DcMotor.class, "wobbleLiftMotor");
        intakeDriveMotor = hardwareMap.get(DcMotor.class, "intakeDriveMotor");

        gripServo = hardwareMap.servo.get("gripServo");
        elbowServo = hardwareMap.servo.get("elbowServo");

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //set direction of motors
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        wobbleLiftMotor.setDirection(DcMotor.Direction.REVERSE);
        intakeDriveMotor.setDirection(DcMotor.Direction.REVERSE);

        /* END ROBOT HARDWARE */


        //wait for start
        waitForStart();

        elbowPosition = 0.5; // whichever position is required to straighten out the elbow
        elbowCheck = true;
        gripPosition  = MIN_POSITION; // or wherever is closed
        gripCheck = false;

        runtime.reset();

        while (opModeIsActive()) {

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

            frontLeftDrive.setPower(powers[0]/largest);
            frontRightDrive.setPower(powers[1]/largest);
            backLeftDrive.setPower(powers[2]/largest);
            backRightDrive.setPower(powers[3]/largest);

            wobbleLiftMotor.setPower(gamepad2.left_stick_y);
            intakeDriveMotor.setPower(gamepad2.right_stick_y);

            if (gamepad2.a) {
                
            }

            for (int i = 0; i < 4; i ++ ) {
                telemetry.addData("Powers " + i + ":", powers[i]);
            }
            telemetry.update();

        }
    }

}

