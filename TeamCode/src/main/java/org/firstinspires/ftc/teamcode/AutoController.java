
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import android.graphics.Color;

import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

@Disabled
public abstract class AutoController extends LinearOpMode {

    DcMotor frontLeftDrive;
    DcMotor frontRightDrive;
    DcMotor backLeftDrive;
    DcMotor backRightDrive;

    DcMotor wobbleLiftMotor;
    DcMotor intakeDriveMotor;
    DcMotor beltDriveMotor;

    DcMotor elbowDriveMotor;
    Servo   gripServo;

    DigitalChannel touch;
    ColorSensor color;

    double  angle;
    double  robotAngle;
    double  powers[];
    double  rightX;

    private double  gripPosition;
    private double  gripOpen, gripClose;
    private boolean gripCheck;

    private double  MIN_POSITION = 0, MAX_POSITION = 1;

    private static final String tflowModel = "UltimateGoal.tflite";
    private static final String labelFirst = "Quad";
    private static final String labelSecond = "Single";

    int rings = 0;
    boolean confident = false;

    private static final String vuforiaKey = "ASoWcFP/////AAABmcxv4D6UuE9nrun4533shIQSemlo5Sc0S1/AXz90Dom7PRfqkUqBvOyoaqKqaTWQHXDlhkOy3zcmgy/DnDaKJJU8z5MsbhUUSpJUWRV3knzpYD3KYvO8SySzXXgQits7LDJuGEmAmoqa/heAXwKyGlEKlAnCWr1icMXwrRjJPhP4xE74vXKcXmH7jgOtl0/dew8bo1bl2fZ0zxAPZ0QHNC3rQHoMASKwdwxnb/i7X3DQg544p12t7rpcZkv0HvHhM7CW0Aooal2lFDC/9PfRWknIOqPYYUGtJd7WJMf50UFJMfbMkcEPNxgLanI+ZtFfloMXzy1wx2okhsfN9AnFTwzkDd/L0cCvTwk/UUXoWVW8";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    public void runToGoal() {

        while (touch.getState() == true) {
            frontLeftDrive.setPower(0.3);
            frontRightDrive.setPower(0.3);
            backLeftDrive.setPower(0.3);
            backRightDrive.setPower(0.3);
        }

    }



    public void initController() {

        initVuforia();
        initTfod();

        if (tfod != null) {
            tfod.activate();
            //tfod.setZoom(2.5, 1.78); === the magnification/aspect ratio of the input images
        }

        telemetry.addData("Pipeline: ", "The Tensor is ready to flow.");
        telemetry.update();

    }

    public void initHardware() {

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

        touch = hardwareMap.get(DigitalChannel.class, "touch");

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        wobbleLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        beltDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elbowDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        touch.setMode(DigitalChannel.Mode.INPUT);

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


    }
    //1440 ticks per revolution
    //dist (mm) / 0.07055555535799998 = num pulses


    public void encoderReset() {
        frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }



    public void setPowers(double power) {
        frontLeftDrive.setPower(power);
        frontRightDrive.setPower(power);
        backLeftDrive.setPower(power);
        backRightDrive.setPower(power);
    }

    public int pulses(int distMM) {
        int pulses;
        pulses = (int) (mmDist / 0.07055555535799998);
        return pulses;
    }

    public void move(char axis, int dist) { //dist in mm

        encoderReset();
        if (axis == 'y') {
            forward(pulses(dist));
        } else if (axis == 'x') {
            strafe(pulses(dist));
        }

        while (frontLeftDrive.isBusy() || backLeftDrive().isBusy() && opModeIsActive()) {
            //wait until complete
        }

    }

    public String returnColor(int sensitivity)
    {
        telemetry.addData("Red", color.red());
        telemetry.addData("Green", color.green());
        telemetry.addData("Blue", color.blue());
        telemetry.update();

        if(color.red() + color.green() + color.blue() > sensitivity*3)
            return "white";

        if (color.blue() > sensitivity)
            return "blue";

        if (color.red() > sensitivity)
            return "red";

        return "black";
    }

    public void forward(int dist) {
        dist = (dist*52)/36;
        frontLeftDrive.setTargetPosition(dist);
        frontRightDrive.setTargetPosition(dist);
        backLeftDrive.setTargetPosition(dist);
        backRightDrive.setTargetPosition(dist);
    }

    public void strafe(int dist) {
        dist = (dist*52)/36;
        frontLeftDrive.setTargetPosition(dist);
        frontRightDrive.setTargetPosition(-dist);
        backLeftDrive.setTargetPosition(-dist);
        backRightDrive.setTargetPosition(dist);
    }

    public void openPipeline() {

        if (tfod != null) {

            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions(); //list will be null if input picture has not changed

            if (updatedRecognitions != null) {

                telemetry.addData("# Objects Detected: ", updatedRecognitions.size());

                int i = 0;
                for (Recognition recognition : updatedRecognitions) {

                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                    telemetry.addData(String.format(" left,top (%d) ", i), "%.03f , %.03f" , recognition.getLeft(), recognition.getTop());
                    telemetry.addData(String.format(" right,bottom (%d)", i), "%.03f , %.03f" , recognition.getRight(), recognition.getBottom());

                    if (recognition.getLabel() == "Single") {
                        rings = 1;
                    } else if (recognition.getLabel() == "Quad") {
                        rings = 4;
                    }

                }

                telemetry.addData("Number of rings: ", rings);
                telemetry.update();

            }

        }

    }

    private void initVuforia() {

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = vuforiaKey;
        parameters.cameraDirection = CameraDirection.BACK;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

    }

    private void initTfod() {

        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(tflowModel, labelFirst, labelSecond);

    }

}
