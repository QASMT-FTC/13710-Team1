
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

    private double  gripPosition;
    private double  gripOpen, gripClose;
    private boolean gripCheck;

    private double  MIN_POSITION = 0, MAX_POSITION = 1;

    int armPosition = 0;
    int elbowPosition = 0;

    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        powers = new double[4];
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
        beltDriveMotor = hardwareMap.get(DcMotor.class, "beltDriveMotor");

        elbowDriveMotor = hardwareMap.get(DcMotor.class, "elbowDriveMotor");
        gripServo = hardwareMap.servo.get("gripServo");

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        wobbleLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elbowDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //set direction of motors
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        wobbleLiftMotor.setDirection(DcMotor.Direction.REVERSE);
        intakeDriveMotor.setDirection(DcMotor.Direction.REVERSE);
        beltDriveMotor.setDirection(DcMotor.Direction.REVERSE);
        elbowDriveMotor.setDirection(DcMotor.Direction.FORWARD);

        /* END ROBOT HARDWARE */

        wobbleLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbowDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //wait for start
        waitForStart();

        gripPosition  = MIN_POSITION; // or wherever is closed
        gripCheck = false;

        runtime.reset();

        while (opModeIsActive()) {

            //first gamepad
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

            //second gamepad
            wobbleLiftMotor.setPower(gamepad2.left_stick_y);
            intakeDriveMotor.setPower(gamepad2.right_stick_y);

//            if (gamepad2.a) {
//                if (gripCheck = false) {
//                    gripServo.setPosition(gripClose);
//                    gripCheck = true;
//                } else {
//                    gripServo.setPosition(gripOpen);
//                    gripCheck = false;
//                }
//            }

            gripServo.setPosition(gamepad2.left_stick_x);

            if (gamepad2.dpad_up) {
                elbowDriveMotor.setPower(0.05);
            } else if (gamepad2.dpad_down) {
                elbowDriveMotor.setPower(-0.05);
            }

            intakeDriveMotor.setPower(gamepad2.right_trigger);
            beltDriveMotor.setPower(gamepad2.left_trigger);

            double armPower, elbowPower;
            int sensitivity = 360; //360 will move from 0 to 90 degrees in joystick position 0 to 1.

            // YOU MAY NEED TO CHANGE THE DIRECTION OF THIS STICK. RIGHT NOW IT IS NEGATIVE.
            armPower = Range.clip(gamepad2.right_stick_y, -1.0, 1.0);
            elbowPower = Range.clip(gamepad2.left_stick_y, -1.0, 1.0);

            armPosition += (int)armPower*sensitivity;
            armPosition = Range.clip(armPosition, 0, 360);
            elbowPosition = Range.clip(elbowPosition, 0, 360);
            elbowPosition   

            // MOVES UP FROM POSITION 0 TO 90 DEGREES UP.
            wobbleLiftMotor.setTargetPosition(armPosition);
            wobbleLiftMotor.setPower(0.1);

            telemetry.addData("Arm Pos: ", wobbleLiftMotor.getCurrentPosition());
            telemetry.addData("Elbow Pos: ", elbowDriveMotor.getCurrentPosition());

            //telemetry
            for (int i = 0; i < 4; i ++ ) {
                telemetry.addData("Powers " + i + ":", powers[i]);
            }
            telemetry.update();

        }
    }

}

