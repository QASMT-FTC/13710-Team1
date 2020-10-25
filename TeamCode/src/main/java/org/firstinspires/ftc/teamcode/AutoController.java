
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

@Disabled
public abstract class AutoController extends LinearOpMode {

    private static final String tflowModel = "UltimateGoal.tflite";
    private static final String labelFirst = "Quad";
    private static final String labelSecond = "Single";

    private static final String vuforiaKey = " ";

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

                }

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
