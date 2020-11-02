
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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

//            int rings;
//            if (updatedRecognitions == null) {
//                rings = 0;
//            } else {
//                rings = updatedRecognitions.size();
//            }

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
