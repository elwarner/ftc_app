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
        this.humanControl = humanControl;
    }
    //moves at constant power no matter what joystick value;
    public void moveArm() {
        if(humanControl.isArmUpDesired()) {
            armLeftMotor.setPower(0.3);
            armRightMotor.setPower(0.3);
        } else if(humanControl.isArmDownDesired()) {
            armLeftMotor.setPower(-0.3);
            armRightMotor.setPower(-0.3);
        }
    }

    public void update() {
        moveArm();
    }
}
