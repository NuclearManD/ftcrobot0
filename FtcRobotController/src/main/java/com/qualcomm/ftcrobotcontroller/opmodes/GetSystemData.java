package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by burickfamily on 12/3/2015.
 */
public class GetSystemData  extends LinearOpMode {
    private DcMotor arml;
    private DcMotor armrl;
    private DcMotor armrr;
    private Servo dropper;
    private float afps=0;
    DcMotor fl;
    DcMotor fr;
    DcMotor bl;
    DcMotor br;
    public void runOpMode(){
        arml=hardwareMap.dcMotor.get("armup");
        armrl=hardwareMap.dcMotor.get("armretractleft");
        armrr=hardwareMap.dcMotor.get("armretractright");
        dropper=hardwareMap.servo.get("dropper");
        try {
            waitForStart();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fl = hardwareMap.dcMotor.get("fl");
        fl.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        fr = hardwareMap.dcMotor.get("fr");
        fr.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        fr.setDirection(DcMotor.Direction.REVERSE);
        bl = hardwareMap.dcMotor.get("bl");
        bl.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        br = hardwareMap.dcMotor.get("br");
        br.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        br.setDirection(DcMotor.Direction.REVERSE);
        setSpeed(1);
        try{
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setSpeed(0);
    }
    private void setSpeed(float s){
        //calculatePosition();
        fl.setPower(s);
        fr.setPower(s);
        bl.setPower(s);
        br.setPower(s);
        //speed=s;
        //rotspeed=0;
    }
}
