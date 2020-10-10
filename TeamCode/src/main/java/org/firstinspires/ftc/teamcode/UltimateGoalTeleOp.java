
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="UltimateGoalTeleOp", group="Linear Opmode")

//@Disabled /*disables the class so it cannot be selected from the list*/
public class UltimateGoalTeleOp extends LinearOpMode {

    // Declare OpMode members
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;


    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        frontLeftDrive  = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backLeftDrive  = hardwareMap.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // setup the inputs
            double  G1LeftStickY  = gamepad1.left_stick_y;
//            double  G1RightoStickY = reverseControls * gamepad1.right_stick_y;
            double  G1LeftStickX  = gamepad1.left_stick_x;
            double  G1RightStickX = gamepad1.right_stick_x;

            // strafe Mode (allows sideways motion)
            frontLeftDrive.setPower(G1LeftStickY + G1RightStickX + G1LeftStickX);
            backLeftDrive.setPower(G1LeftStickY + G1RightStickX - G1LeftStickX);
            backRightDrive.setPower(G1LeftStickY - G1RightStickX + G1LeftStickX);
            frontRightDrive.setPower(G1LeftStickY - G1RightStickX - G1LeftStickX);


        }
    }

}
