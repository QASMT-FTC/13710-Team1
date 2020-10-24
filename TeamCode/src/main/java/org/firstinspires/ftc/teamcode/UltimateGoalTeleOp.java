
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

    // Declare OpMode members
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

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        frontLeftDrive  = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backLeftDrive  = hardwareMap.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");

        wobbleLiftMotor = hardwareMap.get(DcMotor.class, "wobbleLiftMotor");
        intakeDriveMotor = hardwareMap.get(DcMotor.class, "intakeDriveMotor");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        wobbleLiftMotor.setDirection(DcMotor.Direction.FORWARD);
        intakeDriveMotor.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // setup the inputs
            G1LeftStickY  = gamepad1.left_stick_y;
            G1LeftStickX  = gamepad1.left_stick_x;
            G1RightStickX = gamepad1.right_stick_x;

            G2LeftStickY  = gamepad2.left_stick_y;
            G2RightStickX = gamepad2.left_stick_x;


//            double angle = Math.atan(G1LeftStickY/G1LeftStickX); //using trig
//            frontRightDrive.setPower(Math.sin(angle-1/(4*Math.PI)));
//            backLeftDrive.setPower(Math.sin(angle-1/(4*Math.PI)));
//            frontLeftDrive.setPower(Math.sin(angle+1/(4*Math.PI)));
//            backRightDrive.setPower(Math.sin(angle+1/(4*Math.PI)));

//            if (gamepad1.a) {
//
//            } else {
//
//            }

            angle = Math.atan2(G1LeftStickY, G1LeftStickX);
            magnitude = Math.sqrt(Math.pow(G1LeftStickY,2)+Math.pow(G1LeftStickX,2));

            for (int i = 0; i < 4; i ++) {

                powers[i] = Math.sin(angle + Math.pow(-1, i) * Math.PI/4 * magnitude); //change i to i+1 or i-1

            }


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

