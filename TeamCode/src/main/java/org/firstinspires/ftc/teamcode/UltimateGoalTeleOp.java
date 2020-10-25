
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

    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;

    private DcMotor wobbleLiftMotor = null;
    private DcMotor intakeDriveMotor = null;

    private double  G1LeftStickY;
    private double  G1LeftStickX;
    private double  G1RightStickX;
    private double  G2LeftStickY;
    private double  G2RightStickX;

    private double  angle;
    private double  magnitude;
    private double  powers[];


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

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //set direction of motors
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        wobbleLiftMotor.setDirection(DcMotor.Direction.FORWARD);
        intakeDriveMotor.setDirection(DcMotor.Direction.FORWARD);

        /* END ROBOT HARDWARE */


        //wait for start
        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            // setup the inputs
            G1LeftStickY  = gamepad1.left_stick_y;
            G1LeftStickX  = gamepad1.left_stick_x;
            G1RightStickX = gamepad1.right_stick_x;

            G2LeftStickY  = gamepad2.left_stick_y;
            G2RightStickX = gamepad2.left_stick_x;

            angle = Math.atan2(G1LeftStickX, -G1LeftStickY);
            magnitude = Math.sqrt(Math.pow(G1LeftStickY,2)+Math.pow(G1LeftStickX,2));

            powers[0] = Math.cos(angle + (1) * Math.PI/4) * magnitude;
            powers[1] = Math.cos(angle + (-1) * Math.PI/4) * magnitude;
            powers[2] = Math.cos(angle + (1) * Math.PI/4) * magnitude;
            powers[3] = Math.cos(angle + (-1) * Math.PI/4) * magnitude;

            frontLeftDrive.setPower(powers[0]);
            frontRightDrive.setPower(powers[1]);
            backLeftDrive.setPower(powers[2]);
            backRightDrive.setPower(powers[3]);

            wobbleLiftMotor.setPower(G2LeftStickY);
            intakeDriveMotor.setPower(G2RightStickX);

            telemetry.addData("Angle: ", angle);
            telemetry.addData("Magnitude: ", magnitude);
            for (int i = 0; i < 4; i ++ ) {
                telemetry.addData("Powers " + i + ":", powers[i]);
            }
            telemetry.update();

        }
    }

}

