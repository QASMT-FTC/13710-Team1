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

        pickGoal();

        move('x', -200, 0.2); //strafe slightly to get closer
        move('y', 1100, 0.4); //move forward to scan rings

        while (opModeIsActive() && (runtime.seconds() < 5.0)) {

            openPipeline();

        }

        if (rings == 0) {
            //target zone A
            telemetry.addData("Zone: ", "A");

            move('y', 730, 0.4); //move alongside goal
            move('x', -100, 0.4); //move out of goal slightly

            dropGoal();

            move('x', 500, 0.4);

        } else if (rings == 1) {
            //target zone B
            telemetry.addData("Zone: ", "B");

            move('y', 1300, 0.4); //move alongside goal
            move('x', 300, 0.4); //move into goal slightly

            dropGoal();

        } else if (rings == 4) {
            //target zone C
            telemetry.addData("Zone: ", "C");

            move('y', 1900, 0.4); //move alongside goal
            move('x', -100, 0.4); //move out of goal slightly

            dropGoal();

            move('x', 500, 0.4);

        } else {
            //error
            telemetry.addData("Zone: ", "Not found - error");
        }

        runToGoal();

        moveToLine(-1);

        telemetry.update();

        sleep(30000);

    }

}
