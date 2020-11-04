package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Autonomous Testing", group="Linear OpMode")
public class AutoTest extends AutoController {

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

        //methods to choose from:

        pickGoal(); //grip the wobble goal

        dropGoal(); //drop the wobble goal

        while (opModeIsActive() && (runtime.seconds() < 5.0)) { openPipeline(); } //scan environment for a certain time

        move('x', 500); //move on an axis for a certain distance mm

        runToGoal(); //move the robot until reaching the far barrier

        moveToLine(); //move the robot until reaching the white line

    }

}