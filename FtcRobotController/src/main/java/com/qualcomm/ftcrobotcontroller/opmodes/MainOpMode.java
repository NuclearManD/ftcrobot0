package com.qualcomm.ftcrobotcontroller.opmodes;

import android.bluetooth.BluetoothA2dp;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.ftcrobotcontroller.opmodes.drivers.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by buric_000 on 11/6/2015.
 */
public class MainOpMode extends LinearOpMode {
    /*public enum teams{
        RED(0),
        BLUE(1);
        public int v;
        teams(int i) {
            this.v = i;
        }
    }
    private teams team;*/
    private MovementDriver drive;   // [id][team]
    private OrientationPoint targets[][]={{new OrientationPoint(1, 5.3f, 0),new OrientationPoint(5.3f, 1, 90)},{new OrientationPoint(4, -5.3f, 135),new OrientationPoint(-5.3f, 4, 315)}};
    Servo flapleft;
    Servo flapright;
    private Servo dropper;
    private float afps=0;
    DcMotor armdown0;
    DcMotor armdown1;
    DcMotor armup;
    public void runOpMode(){
        telemetry.addData("stage:", "FIRSTLINE");
        try {
            drive=new MovementDriver();
            drive.init(hardwareMap, targets[0][0],false);
            armup=hardwareMap.dcMotor.get("armup");
            armdown0=hardwareMap.dcMotor.get("armretractleft");
            armdown1=hardwareMap.dcMotor.get("armretractright");
            flapleft=hardwareMap.servo.get("flapleft");
            flapright=hardwareMap.servo.get("flapright");
            dropper=hardwareMap.servo.get("dropper");
        }catch (Exception e){
            e.printStackTrace();
        }
        telemetry.addData("stage:", "init");
        try {
            //waitForStart();
        }catch (Exception e){
            e.printStackTrace();
        }
        dropper.setPosition(0.1);
        flapleft.setPosition(0.25);
        flapright.setPosition(0.7);
        hardwareMap.servo.get("lock").setPosition(0.4);
        telemetry.addData("stage:", "done loading");
        try{
            //wait(30000);                   // wait for the end of autonomous mode.
        }catch(Exception e){

        }
        telemetry.addData("stage:","done waiting");
        while (opModeIsActive()) {
            drive.setRotspeed(gamepad1.left_stick_x);

            drive.setSpeed(-gamepad1.right_stick_y);

            //
            // Manage the arm motor.
            //
            if (gamepad2.x) {
                dropper.setPosition(1);
            }
            if (gamepad2.dpad_down)
                moveArm0(-1);
            else if (gamepad2.dpad_up)
                moveArm0(1);
            else
                moveArm0(0);
            if(gamepad2.right_bumper)//open
                flapright.setPosition(0.7);
            if(gamepad2.right_trigger>0.2)//close
                flapright.setPosition(0.09);
            if(gamepad2.left_bumper)//open
                flapleft.setPosition(0.25);
            if(gamepad2.left_trigger>0.2)//close
                flapleft.setPosition(0.82);
            //if (gamepad1.right_trigger)
            //    flapright.setPosition(0.5);
        }
        //telemetry.addData("stage:","done with program");
    }
    void moveArm0(float value){
        float power=0.1f;
        armdown0.setPower(value*power);
        armdown1.setPower(-value*power);

    }
    float scalejoystick (float p_power)
    {
        //
        // Assume no scaling.
        //
        float l_scale = 0.0f;

        //
        // Ensure the values are legal.
        //
        float l_power = Range.clip(p_power, -1, 1);

        float[] l_array =
                { 0.00f, 0.05f, 0.09f, 0.10f, 0.12f
                        , 0.15f, 0.18f, 0.24f, 0.30f, 0.36f
                        , 0.43f, 0.50f, 0.60f, 0.72f, 0.85f
                        , 1.00f, 1.00f
                };

        //
        // Get the corresponding index for the specified argument/parameter.
        //
        int l_index = (int)(l_power * 16.0);
        if (l_index < 0)
        {
            l_index = -l_index;
        }
        else if (l_index > 16)
        {
            l_index = 16;
        }

        if (l_power < 0)
        {
            l_scale = -l_array[l_index];
        }
        else
        {
            l_scale = l_array[l_index];
        }

        return l_scale;

    } // scale_motor_power
}