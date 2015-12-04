package com.qualcomm.ftcrobotcontroller.opmodes.drivers;
import com.qualcomm.ftcrobotcontroller.opmodes.MainOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
/**
 * Created by buric_000 on 11/6/2015.
 */
public class MovementDriver {
    DcMotor fl;
    DcMotor fr;
    DcMotor bl;
    DcMotor br;
    ColorSensor csensor;
    DeviceInterfaceModule cdim;
    public OrientationPoint target;
    private OrientationPoint location;
    private float speed=0;
    private float rotspeed=0;
    public boolean knowsLocation=false;
    public boolean started = false;
    long lstime;
    float fpms=0;
    float dpms=0;
    public boolean reachedTarget=false;
    public MainOpMode.teams team;
    public void init(HardwareMap hardwareMap, OrientationPoint loc){
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
        csensor=hardwareMap.colorSensor.get("color");
        cdim = hardwareMap.deviceInterfaceModule.get("dim");
        cdim.setDigitalChannelMode(5, DigitalChannelController.Mode.OUTPUT);
        target=loc;
        lstime=System.currentTimeMillis();
    }
    public boolean update() {
        reachedTarget = false;
        calculatePosition();
        if (!knowsLocation) {
            if (!started) {
                setSpeed(0.4f);
                if (csensor.red() > 200) {
                    team = MainOpMode.teams.RED;
                    started = true;
                } else if (csensor.blue() > 200) {
                    team = MainOpMode.teams.BLUE;
                    started = true;
                } else
                    return false;
                setSpeed(0);
                location.x -= 5;
                location.y = location.x;
                knowsLocation = true;
            }
        }
        if (round(Math.atan2(location.y - target.y, location.x - target.x) * 57.2958f, 1) >= Math.round(360 % (180 + location.rotation))) {
            setRotspeed(-0.4f);
            return false;
        } else if (round(Math.atan2(location.y - target.y, location.x - target.x) * 57.2958f, 1) < Math.round(360 % (180 + location.rotation))) {
            setRotspeed(0.4f);
            return false;
        } else
            setRotspeed(0);
        if (round(location.distance(target), 1) > 0){
            setSpeed(1);
            return false;
        }else if(round(location.distance(target), 1) < 0) {
            setSpeed(-1);
            return false;
        }
        setSpeed(0);
        reachedTarget=true;
        return true;
    }
    private void calculatePosition(){
        long time=System.currentTimeMillis()-lstime;
        lstime=System.currentTimeMillis();
        float distanceMoved=time*speed*fpms;
        location.x+=(Math.sin(torad(location.rotation))*distanceMoved);
        location.y+=distanceMoved*Math.cos(location.rotation);
        location.rotation+=rotspeed*dpms;
    }
    private void setSpeed(float s){
        calculatePosition();
        fl.setPower(s);
        fr.setPower(s);
        bl.setPower(s);
        br.setPower(s);
        speed=s;
        rotspeed=0;
    }
    private void setRotspeed(float s){
        calculatePosition();
        fl.setPower(s);
        fr.setPower(-s);
        bl.setPower(s);
        br.setPower(-s);
        speed=0;
        rotspeed=s;
    }
    private float torad(float x){
        return x/57.2958f;
    }
    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}
