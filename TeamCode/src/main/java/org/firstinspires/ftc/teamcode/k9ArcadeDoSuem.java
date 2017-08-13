/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwareK9bot;

/**
 * This OpMode uses the common HardwareK9bot class to define the devices on the robot.
 * All device access is managed through the HardwareK9bot class. (See this class for device names)
 * The code is structured as a LinearOpMode
 *
 * This particular OpMode executes a basic Tank Drive Teleop for the K9 bot
 * It raises and lowers the arm using the Gampad Y and A buttons respectively.
 * It also opens and closes the claw slowly using the X and B buttons.
 *
 * Note: the configuration of the servos is such that
 * as the arm servo approaches 0, the arm position moves up (away from the floor).
 * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="K9bot: Telop Tank", group="K9bot")
//@Disabled
public class k9ArcadeDoSuem extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareK9bot   robot           = new HardwareK9bot();              // Use a K9'shardware
    //double          armPosition     = robot.ARM_HOME;                   // Servo safe position
    double          clawPosition    = robot.CLAW_HOME;                  // Servo safe position
    final double    CLAW_SPEED      = 0.01 ;                            // sets rate to move servo
    final double    ARM_SPEED       = 0.01 ;                            // sets rate to move servo

    @Override
    public void runOpMode() {
        double moveValue;
        double rotateValue;
        double leftMotorOutput;
        double rightMotorOutput;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            //Arcade START
            // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
            moveValue = -gamepad1.left_stick_y;
            rotateValue = gamepad1.right_stick_x;
            if(moveValue > 1) {
                moveValue = 1;
            } else if(moveValue < -1) {
                moveValue = -1;
            }
            if(rotateValue > 1) {
                rotateValue = 1;
            } else if(moveValue < -1) {
                rotateValue = -1;
            }

            //Arcade Drive SQUARED_INPUTS = TRUE
            if (true) {
                // square the inputs (while preserving the sign) to increase fine control
                // while permitting full power
                if (moveValue >= 0.0) {
                    moveValue = (moveValue * moveValue);
                } else {
                    moveValue = -(moveValue * moveValue);
                }
                if (rotateValue >= 0.0) {
                    rotateValue = (rotateValue * rotateValue);
                } else {
                    rotateValue = -(rotateValue * rotateValue);
                }
            }

            if (moveValue > 0.0) {
                if (rotateValue > 0.0) {
                    leftMotorOutput = moveValue - rotateValue;
                    rightMotorOutput = Math.max(moveValue, rotateValue);
                } else {
                    leftMotorOutput = Math.max(moveValue, -rotateValue);
                    rightMotorOutput = moveValue + rotateValue;
                }
            } else {
                if (rotateValue > 0.0) {
                    leftMotorOutput = -Math.max(-moveValue, rotateValue);
                    rightMotorOutput = moveValue + rotateValue;
                } else {
                    leftMotorOutput = moveValue - rotateValue;
                    rightMotorOutput = -Math.max(-moveValue, -rotateValue);
                }
            }
            robot.leftMotor.setPower(leftMotorOutput);
            robot.rightMotor.setPower(rightMotorOutput);
            //ARCADE STOP

            // Use gamepad to control the claw
            if (gamepad1.a)

                clawPosition += CLAW_SPEED;
            else if (gamepad1.y)
                clawPosition -= CLAW_SPEED;

            // Move both servos to new position.
            if(-clawPosition >= -0.5 && clawPosition <= 0.5) {
                robot.clawA.setPosition(-clawPosition);
                robot.clawB.setPosition(clawPosition);
            }

            //USING dpad for arm
            if(gamepad1.dpad_down) {
                robot.arm.setPower(0.2);
            } else if(gamepad1.dpad_up) {
                robot.arm.setPower(-0.2);
            } else {
                robot.arm.setPower(0);
            }

            // Send telemetry message to signify robot running;
            //telemetry.addData("arm",   "%.2f", armPosition);
            telemetry.addData("left",  "%.2f", leftMotorOutput);
            telemetry.addData("right", "%.2f", rightMotorOutput);
            telemetry.addData("leftClaw", "%.2f",  -clawPosition);
            telemetry.addData("righttClaw", "%.2f",  clawPosition);
            telemetry.update();

            // Pause for metronome tick.  40 mS each cycle = update 25 times a second.
            robot.waitForTick(40);
        }
    }
}
