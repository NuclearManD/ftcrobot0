package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by burickfamily on 1/5/2016.
 */
public class AutoMainOp extends LinearOpMode {
    DcMotor armup;
    Servo dropper;
    ColorSensor sensorRGB;
    MainOpMode driverC;
    public void runOpMode(){
        telemetry.addData("stage:", "FIRSTLINE0");
        try {
            armup=hardwareMap.dcMotor.get("armup");
            dropper=hardwareMap.servo.get("dropper");
            sensorRGB = hardwareMap.colorSensor.get("nxt");
            sensorRGB.enableLed(true);

        }catch (Exception e){
            e.printStackTrace();
        }
        telemetry.addData("stage:", "init0");
        try {
            //waitForStart();
        }catch (Exception e){
            e.printStackTrace();
        }
        long end=System.currentTimeMillis()+30000;
        long start=System.currentTimeMillis();


        driverC.runOpMode();
    }
}
