package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by peter on 11/22/17.
 */

public class ArmController {
    private DcMotor armLeftMotor;
    private DcMotor armRightMotor;
    private HumanControl humanControl;
    public ArmController(DcMotor armLeftMotor, DcMotor armRightMotor, HumanControl humanControl) {
        this.armLeftMotor = armLeftMotor;
        this.armLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        this.armRightMotor = armRightMotor;
        this.armRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        this.humanControl = humanControl;
    }
    //moves at constant power no matter what joystick value;
    public void moveArmUp() {
        armLeftMotor.setPower(-0.2);
        armRightMotor.setPower(-0.2);

    }
    public void moveArmDown() {
        armLeftMotor.setPower(0.1);
        armRightMotor.setPower(0.1);

    }
    public double getPosition() {
        armLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        return armLeftMotor.getCurrentPosition();
    }

    public void moveArmToPosition(int ticks) {
        armLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        armLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        armLeftMotor.setTargetPosition(ticks);
        armRightMotor.setTargetPosition(ticks);

        armLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        moveArmUp();

        while(armLeftMotor.isBusy() && armRightMotor.isBusy()) {


        }

        armLeftMotor.setPower(0);
        armRightMotor.setPower(0);

        armLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void update() {
        armLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        if(humanControl.isArmUpDesired()) {
            moveArmUp();
        } else if(humanControl.isArmDownDesired()) {
            moveArmDown();
        } else {
            armLeftMotor.setPower(0.0);
            armRightMotor.setPower(0.0);
        }
    }
}
