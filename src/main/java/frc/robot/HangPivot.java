package frc.robot;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.kauailabs.navx.frc.AHRS;

public class HangPivot {
    
    //MOTOR (775) & VERSA-PLANETARY ENCODER
    private MotorController hangPivot;
    private Encoder pivotEncoder;       //MAY NOT BE THE RIGHT ENCODER CLASS

    //LIMIT SWITCHES
    private DigitalInput frontSwitch;
    private DigitalInput backSwitch;

    //GYRO (NAVX)
    private AHRS navX;

    //VARIABLES [SUBJECT TO CHANGE]
    private final double forwardPivotPos = 0.0;
    private final double backwardPivotPos = 0.0;
    private final double homePos = 0.0;
    
    private final double forwardPivotSpeed = 0.0;
    private final double backwardPivotSpeed = 0.0;
    
    public HangPivot (MotorController pivotMotor, Encoder hangPivotEncoder, AHRS gyro, DigitalInput frontLimitSwitch, DigitalInput backLimitSwitch){   //CONSTRUCTOR
        hangPivot = pivotMotor;
        pivotEncoder = hangPivotEncoder;
        frontSwitch = frontLimitSwitch;
        backSwitch = backLimitSwitch;
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

    //CHECKS
    private boolean backLimitTouched(){
        return backSwitch.get();
    }

    private boolean frontLimitTouched(){
        return frontSwitch.get();
    }

    //METHODS
    private void pivotForward(){
        if(!backLimitTouched()){
            if(pivotEncoder.get() < forwardPivotPos){
                hangPivot.set(forwardPivotSpeed);
            }
        }

        else{
            hangPivot.set(0);
        }
    }

    private void pivotBackward(){
        if(!frontLimitTouched()){
            if(pivotEncoder.get() > backwardPivotPos){
                hangPivot.set(backwardPivotSpeed);
            }
        }

        else{
            hangPivot.set(0);
        }
    }

    private void pivotHome(){
        
    }

    private void testing(){ //METHOD FOR TESTING CODE

    }

    public void run(){
        SmartDashboard.putString("HANG PIVOT STATE", pivotState.toString());
        SmartDashboard.putBoolean("BACK LIMIT", backSwitch.get());
        SmartDashboard.putBoolean("FRONT LIMIT", frontSwitch.get());
        SmartDashboard.putNumber("PIVOT ENCODER", pivotEncoder.get());
        
        switch(pivotState){
            
            case TESTING:
            testing();
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
