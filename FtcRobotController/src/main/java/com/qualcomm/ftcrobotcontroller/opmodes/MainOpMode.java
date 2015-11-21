package com.qualcomm.ftcrobotcontroller.opmodes;

import android.bluetooth.BluetoothA2dp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.ftcrobotcontroller.opmodes.drivers.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by buric_000 on 11/6/2015.
 */
public class MainOpMode extends LinearOpMode {
    public enum teams{
        RED(0),
        BLUE(1);
        public int v;
        teams(int i) {
            this.v = i;
        }
    }
    private teams team;
    private MovementDriver drive;   // [id][team]
    private OrientationPoint targets[][]={{new OrientationPoint(1, 5.3f, 0),new OrientationPoint(5.3f, 1, 90)},{new OrientationPoint(4, -5.3f, 135),new OrientationPoint(-5.3f, 4, 315)}};
    private DcMotor arm;
    private Servo dropper;
    private float afps=0;
    public void runOpMode(){
        drive.init(hardwareMap,targets[0][team.v]);
        arm=hardwareMap.dcMotor.get("arm");
        dropper=hardwareMap.servo.get("dropper");
        try {
            waitForStart();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dropper.setPosition(270);
        long end=System.currentTimeMillis()+30000;
        boolean t=true;
        while(end<System.currentTimeMillis()){
            if(drive.update()&&t){
                drive.target=targets[1][drive.team.v];
                arm.setPower(0.5);
                long stoparm= (long) (System.currentTimeMillis()+(afps*2*0.5*1000));
                while(stoparm>System.currentTimeMillis());
                arm.setPower(0);
                stoparm= (long) (System.currentTimeMillis()+500);
                while(stoparm>System.currentTimeMillis());
                dropper.setPosition(0);
                arm.setPower(-0.5);
                stoparm= (long) (System.currentTimeMillis()+(afps*2*0.5*1000));
                while(stoparm>System.currentTimeMillis());
                t=false;
            }
            if(drive.knowsLocation&&!t){
                drive.target=targets[0][drive.team.v];
            }
        }

    }
}