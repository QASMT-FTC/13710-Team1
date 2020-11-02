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

        initHardware();

        //wait for start
        waitForStart();
        runtime.reset();

        //move forward a tiny bit

        while (opModeIsActive() && (runtime.seconds() < 5.0)) {

            openPipeline();

        }

        if (rings == 0) {
            //target zone A
            telemetry.addData("Zone: ", "A");



        } else if (rings == 1) {
            //target zone B
            telemetry.addData("Zone: ", "B");


        } else if (rings == 4) {
            //target zone C
            telemetry.addData("Zone: ", "C");


        } else {
            //error
            telemetry.addData("Zone: ", "Not found - error");
        }

        runToGoal();


        telemetry.update();

        sleep(30000);

    }

}
