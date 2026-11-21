package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.opencv.core.Mat;

@Config
@TeleOp(name = "sdkteleop", group = "Linear OpMode")
public class SdkTeleop extends OpMode {

    Servo coupleServo;
    Servo leftTiltServo;
    Servo rightTiltServo;
    DcMotor intakeMotor;
    Servo rightGateServo;
    Servo leftGateServo;
    DcMotor frontLeftMotor;
    DcMotor backLeftMotor;
    DcMotor frontRightMotor;
    DcMotor backRightMotor;
    public double Coupled = 0.14;
    public double Decoupled = 0.25;
    boolean statec_oupled = false;
    boolean on_intake = false;
    boolean reverse = false;
    boolean tilt = false;
    double lOpen = 0.65;
    double lClosed = 0.38;
    double rOepen = 0.42;
    double rClosed = 0.65;
    long lastTime = 0;
    long interval = 500;
    boolean count = true;
    boolean gateopen = false;
    @Override
    public void init()
    {
        coupleServo = hardwareMap.get(Servo.class, "coupleServo");
        leftTiltServo = hardwareMap.get(Servo.class, "leftTiltServo");
        rightTiltServo = hardwareMap.get(Servo.class, "rightTiltServo");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        rightGateServo = hardwareMap.get(Servo.class,"rightGateServo");
        leftGateServo = hardwareMap.get(Servo.class,"leftGateServo");

        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeftMotor");
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRightMotor");

        frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {
        double x = gamepad1.left_stick_x;
        double y = -gamepad1.left_stick_y;
        double rx = gamepad1.right_stick_x;
        double powereFL = y + x + rx;
        double powereBL = y - x + rx;
        double powereFR = y - x - rx;
        double powereBR = y + x - rx;
        double absolut = Math.max(1, Math.max( Math.max(Math.abs(powereFL), Math.abs(powereBL)), Math.max( Math.abs(powereFR), Math.abs(powereBR) )));
        frontLeftMotor.setPower(powereFL/absolut);
        backLeftMotor.setPower(powereBL/absolut);
        frontRightMotor.setPower(powereFR/absolut);
        backRightMotor.setPower(powereBR/absolut);

        if (gamepad1.triangleWasPressed()) {
            tilt = !tilt;
        }
        if (gamepad1.circleWasPressed()) {
            on_intake = !on_intake;
        }
        if (gamepad1.crossWasPressed()) {
            statec_oupled = !statec_oupled;
        }
        if (gamepad1.squareWasPressed()) {
            reverse = !reverse;
        }

        if (tilt) {
            leftTiltServo.setPosition(0.2);
            rightTiltServo.setPosition(0.2);
        } else {
            leftTiltServo.setPosition(0.85);
            rightTiltServo.setPosition(0.85);
        }

        if (on_intake) {
            if (!reverse) {
                intakeMotor.setPower(1);
            } else {
                intakeMotor.setPower(-1);
            }
        } else intakeMotor.setPower(0);

        if (statec_oupled) {
            coupleServo.setPosition(Coupled);
        } else {
            coupleServo.setPosition(Decoupled);
        }

        if(gamepad1.rightBumperWasPressed()) {
            gateopen = !gateopen;
            lastTime = System.currentTimeMillis();
            count = !count;
        }
        long currentTime = System.currentTimeMillis();
        if (gateopen) {
            if (!count) {
                leftGateServo.setPosition(lClosed);
                if (currentTime - lastTime >= interval) {
                    rightGateServo.setPosition(rOepen);
                }
            } else {
                rightGateServo.setPosition(rClosed);
                if (currentTime - lastTime >= interval) {
                    leftGateServo.setPosition(lOpen);
                }
            }
        } else {
            leftGateServo.setPosition(lClosed);
            rightGateServo.setPosition(rClosed);
        }
    }
}
