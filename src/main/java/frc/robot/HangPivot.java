package frc.robot;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.kauailabs.navx.frc.AHRS;

public class HangPivot {
    
    //MOTOR (MINI CIM)
    private MotorController hangPivot;
    
    //GYRO (NAVX)
    private AHRS navX;

    //VARIABLES [SUBJECT TO CHANGE]
    private final float tiltAngleForward = 0;
    private final float tiltAngleBackward = 0;
    private final float tiltAngleHome = 0;
    
    private final double forwardSpeed = 0.0;
    private final double backwardSpeed = 0.0;
    
    public HangPivot (MotorController pivotMotor, AHRS gyro){   //CONSTRUCTOR
        hangPivot = pivotMotor;
        navX = gyro;
    }

    private enum States{
        PIVOTFORWARD, PIVOTBACK, HOME, TESTING;
    }

    //SETTING STATES
    public States pivotState = States.TESTING;

    public void setPivForward(){
        pivotState = States.PIVOTFORWARD;
    }

    public void setPivBackward(){
        pivotState = States.PIVOTBACK;
    }

    public void setHome(){
        pivotState = States.HOME;
    }

    //METHODS
    private void pivotForward(){
        if(navX.getPitch() < tiltAngleForward){
            hangPivot.set(forwardSpeed);
        }
        
        else {
            hangPivot.set(0);
        }
    }

    private void pivotBackward(){
        if(navX.getPitch() < tiltAngleBackward){
            hangPivot.set(backwardSpeed);
        }

        else {
            hangPivot.set(0);
        }
    }

    private void nothing(){ //METHOD FOR TESTING CODE

    }

    public void run(){
        
        switch(pivotState){
            
            case TESTING:
            nothing();
            break;

            case PIVOTFORWARD:
            pivotForward();
            break;

            case PIVOTBACK:
            pivotBackward();
            break;

            case HOME:
            

        }
    }




}
