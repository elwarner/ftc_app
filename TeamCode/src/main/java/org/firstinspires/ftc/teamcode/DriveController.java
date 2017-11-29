package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by peter on 11/22/17.
 */

public class DriveController {
    private DcMotor frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor;
    private HumanControl humanControl;
    int TICKS_PER_ROTATION = 1120;
    double TICKS_PER_DEGREE = (928/90) * .85;
    //int RIGHT_TICKS_PER_DEGREE = 769/90;


    public DriveController(DcMotor frontLeftMotor, DcMotor backLeftMotor, DcMotor frontRightMotor, DcMotor backRightMotor, HumanControl humanControl) {

        this.humanControl = humanControl;

        this.frontLeftMotor = frontLeftMotor;
        this.frontRightMotor = frontRightMotor;
        this.backLeftMotor = backLeftMotor;
        this.backRightMotor = backRightMotor;

        this.frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        this.backLeftMotor.setDirection(DcMotor.Direction.REVERSE);

        this.frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        this.backRightMotor.setDirection(DcMotor.Direction.FORWARD);
    }

    void resetEncoders() {
        //frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    void reset() {
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    void driveForward(double power) {
        frontLeftMotor.setPower(power);
        frontRightMotor.setPower(power);
        backLeftMotor.setPower(power);
        backRightMotor.setPower(power);
    }

    void rightPower(double power) {
        frontRightMotor.setPower(power);
        backRightMotor.setPower(power);
    }
    void leftPower(double power) {
        frontLeftMotor.setPower(power);
        backLeftMotor.setPower(power);
    }

    void stopDriving() {
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);
    }

    //distance and diameter in inches
    void driveDistance(double power, double diameter, double distance) {
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // rotations = (distance/circumference)
        int total_ticks = (int) Math.round((distance / (diameter*Math.PI) * TICKS_PER_ROTATION));
        //telemetry.addData("ticks_forward", total_ticks);
        //telemetry.update();
        //Fixing andymark tolerance 89%
        total_ticks = (int)Math.round(total_ticks * 0.89);
        backLeftMotor.setTargetPosition(total_ticks);
        backRightMotor.setTargetPosition(total_ticks);

        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        driveForward(power);

        while(backLeftMotor.isBusy() && backRightMotor.isBusy()) {


        }

        stopDriving();
        resetEncoders();

        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    void turnRight(double power, double degrees) {
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        int ticks = (int) Math.round(TICKS_PER_DEGREE * degrees);

        //Turn to the right
        backLeftMotor.setTargetPosition(ticks);
        backRightMotor.setTargetPosition(-ticks);

        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //turn right
        leftPower(power);
        rightPower(-power);
        while(backLeftMotor.isBusy() && backRightMotor.isBusy()) {

        }

        stopDriving();
        resetEncoders();

        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    void turnLeft(double power, double degrees) {
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        int ticks = (int) Math.round(TICKS_PER_DEGREE * degrees);

        //Turn to the left
        frontLeftMotor.setTargetPosition(-ticks);
        backRightMotor.setTargetPosition(ticks);

        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //turn left
        leftPower(-power);
        rightPower(power);

        while(backLeftMotor.isBusy() && backRightMotor.isBusy()) {

        }

        stopDriving();
        resetEncoders();

        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    void arcadeDrive(double moveValue, double rotateValue) {
        double left = moveValue + rotateValue;
        double right = moveValue - rotateValue;

        if(left > 1) {
            left = 1;
        } else if(left < -1) {
            left = -1;
        }

        if(right > 1) {
            right = 1;
        } else if(right < -1) {
            right = -1;
        }

        leftPower(left);
        rightPower(right);

    }
    void tankDrive(double left, double right) {
        leftPower(-left);
        rightPower(right);
    }
    void update() {
        arcadeDrive(humanControl.getDriverLeftJoyY(), humanControl.getDriverRightJoyX());
        //arcadeDrive(humanControl.getDriverRightJoyX(), humanControl.getDriverLeftJoyY());
    }

}
