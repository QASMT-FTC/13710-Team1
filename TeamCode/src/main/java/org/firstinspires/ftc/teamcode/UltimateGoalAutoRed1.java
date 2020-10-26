package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="UltimateGoalAutoRed1", group="Linear OpMode")
public class UltimateGoalAutoRed1 extends AutoController {

    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() {

        telemetry.addData("Status: ", "initialised");
        telemetry.update();

        initController();

        /* ROBOT HARDWARE */

        /* END ROBOT HARDWARE */

        //wait for start
        waitForStart();
        runtime.reset();

        //used for testing purposes currently
        while (opModeIsActive()) {

            openPipeline();

        }

    }

}
