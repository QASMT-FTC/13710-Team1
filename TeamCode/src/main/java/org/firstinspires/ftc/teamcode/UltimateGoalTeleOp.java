
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

        wobbleLiftMotor = hardwareMap.get(DcMotor.class, "wobbleLeftMotor");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        wobbleLiftMotor.setDirection(DcMotor.Direction.FORWARD);

        //create TrigDrive objects
        TrigDrive drive = new TrigDrive();
        double[] speeds = new double[4];

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // setup the inputs
            double  G1LeftStickY  = gamepad1.left_stick_y;
            double  G1LeftStickX  = gamepad1.left_stick_x;
            double  G1RightStickX = gamepad1.right_stick_x;
            double  G2LeftStickY = gamepad2.left_stick_y;

//            // strafe Mode (allows sideways motion)
//            frontLeftDrive.setPower(G1LeftStickY + G1RightStickX + G1LeftStickX);
//            backLeftDrive.setPower(G1LeftStickY + G1RightStickX - G1LeftStickX);
//            backRightDrive.setPower(G1LeftStickY + G1RightStickX + G1LeftStickX);
//            frontRightDrive.setPower(G1LeftStickY + G1RightStickX - G1LeftStickX);

            // trig drive
            speeds = drive.motorSpeeds(G1LeftStickY, G1RightStickX*(Math.PI/2), G1LeftStickX);
            frontLeftDrive.setPower(speeds[0]);
            backLeftDrive.setPower(speeds[1]);
            backRightDrive.setPower(speeds[2]);
            frontRightDrive.setPower(speeds[3]);

            wobbleLiftMotor.setPower(G2LeftStickY);

        }
    }

}

class TrigDrive {

    public static double calc1 (double Vd, double Td, double Vt) {

        double V;
        if (Vd == 0 && Td > 0) {
            Vd = Td/2;
        }
        V = Vd * Math.sin(Td + (Math.PI / 4)) + Vt;
        return V;

    }

    public static double calc2 (double Vd, double Td, double Vt) {

        double V;
        if (Vd == 0 && Td > 0) {
            Vd = Td/2;
        }
        V = Vd * Math.cos(Td + (Math.PI /4 )) + Vt;
        return V;

    }

    public static double[] motorSpeeds (double Vd, double Td, double Vt) {

        /*
            Vd: robot speed between -1 and 1
            Td: robot angle
                0 = straight on
                Pi/2 = 90 degrees -> so to the right
            Vt: directional speed (strafing) between -1 and 1
         */

        double[] vArr = new double[4];

        vArr[0] = calc1(Vd, Td, Vt);
        vArr[1] = calc2(Vd, Td, Vt);
        vArr[2] = calc2(Vd, Td, Vt);
        vArr[3] = calc1(Vd, Td, Vt);

        return vArr;

    }

}