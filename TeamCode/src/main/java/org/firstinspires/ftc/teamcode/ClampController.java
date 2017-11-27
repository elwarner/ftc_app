package org.firstinspires.ftc.teamcode;

/**
 * Created by peter on 11/22/17.
 */

import com.qualcomm.robotcore.hardware.Servo;


public class ClampController {
    private HumanControl humanControl;
    private Servo leftServo, rightServo;
    double OPEN_POSITION_LEFT, CLAMPED_POSITION_LEFT, OPEN_POSITION_RIGHT, CLAMPED_POSITION_RIGHT;

    //leftServo port 1 rightServo 0 Hub Top 10

    public ClampController(Servo leftServo, Servo rightServo, HumanControl humanControl) {
        this.leftServo = leftServo;
        this.rightServo = rightServo;
        this.humanControl = humanControl;

        OPEN_POSITION_LEFT = 0.5;
        CLAMPED_POSITION_LEFT = 1;

        OPEN_POSITION_RIGHT = 1;
        CLAMPED_POSITION_RIGHT = 0.5;
    }

    public void openClamp() {
        leftServo.setPosition(OPEN_POSITION_LEFT);
        rightServo.setPosition(OPEN_POSITION_RIGHT);
    }

    public void clampClamp() {
        leftServo.setPosition(CLAMPED_POSITION_LEFT);
        rightServo.setPosition(CLAMPED_POSITION_RIGHT);
    }

    public double getLeftPosition() {
        return leftServo.getPosition();
    }

    public double getRightPosition() {
        return rightServo.getPosition();
    }

    public void update() {
        if(humanControl.isClampClosedDesired()) {
            clampClamp();
        } else if(humanControl.isClampOpenDesired()) {
            openClamp();
        }
    }
}
