package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="UltimateGoalAutoDriveToLine", group="Linear OpMode")
public class UltimateGoalAutoDriveToLine extends AutoController {

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

        moveToLine(1);

    }

}
