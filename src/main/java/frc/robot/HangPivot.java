package frc.robot;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.TalonEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.kauailabs.navx.frc.AHRS;

public class HangPivot {
    
    //MOTOR (775) & VERSA-PLANETARY ENCODER
    private MotorController hangPivot;
    private TalonEncoder pivotEncoder;       //MAY NOT BE THE RIGHT ENCODER CLASS

    //LIMIT SWITCHES
    private DigitalInput frontSwitch;   
    private DigitalInput backSwitch;

    //GYRO (NAVX)
    private AHRS navX;

    //VARIABLES [SUBJECT TO CHANGE]
    private final double inwardPivotPos = 0.0;
    private final double outwardPivotPos = 0.0;
    
    private final double inwardPivotSpeed = 0.0;
    private final double outwardPivotSpeed = 0.0;
    
    public HangPivot (MotorController pivotMotor, TalonEncoder hangPivotEncoder, AHRS gyro, DigitalInput frontLimitSwitch, DigitalInput backLimitSwitch){   //CONSTRUCTOR
        hangPivot = pivotMotor;
        pivotEncoder = hangPivotEncoder;
        frontSwitch = frontLimitSwitch;
        backSwitch = backLimitSwitch;
        navX = gyro;
    }

    private enum States{
        PIVOTFORWARD, PIVOTBACK, TESTING;
    }

    //SETTING STATES
    public States pivotState = States.TESTING;

    public void setPivForward(){
        pivotState = States.PIVOTFORWARD;
    }

    public void setPivBackward(){
        pivotState = States.PIVOTBACK;
    }

    //CHECKS
    private boolean backLimitTouched(){
        return backSwitch.get();
    }

    private boolean frontLimitTouched(){
        return frontSwitch.get();
    }

    public void resetEnc(){
        pivotEncoder.reset();
    }

    //METHODS
    private void pivotOutward(){
        if(!backLimitTouched()){
            if(pivotEncoder.get() > outwardPivotPos){
                hangPivot.set(outwardPivotSpeed);
            }
        }

        else{
            hangPivot.set(0);
        }
    }

    private void pivotInward(){
        if(!frontLimitTouched()){
            if(pivotEncoder.get() < inwardPivotPos){
                hangPivot.set(inwardPivotSpeed);
            }
        }

        else{
            hangPivot.set(0);
        }
    }
/*
    private void pivotHome(){
        if(pivotEncoder.get() > outwardPivotPos && pivotEncoder.get() < homePos){
            hangPivot.set(inwardPivotSpeed); 
        }

        else if(pivotEncoder.get() < inwardPivotSpeed && pivotEncoder.get() > homePos){
            hangPivot.set(outwardPivotPos);
        }

        else{
            hangPivot.set(0);
        }
    }
*/
    private void testing(){ //METHOD FOR TESTING CODE

    }

    public void run(){
        SmartDashboard.putString("HANG PIVOT STATE", pivotState.toString());
        SmartDashboard.putBoolean("BACK LIMIT", backSwitch.get());
        SmartDashboard.putBoolean("FRONT LIMIT", frontSwitch.get());
        SmartDashboard.putNumber("PIVOT ENCODER", pivotEncoder.get());
        SmartDashboard.putNumber("NAVX PITCH", navX.getPitch());
        
        switch(pivotState){
            
            case TESTING:
            testing();
            break;

            case PIVOTFORWARD:
            pivotOutward();
            break;

            case PIVOTBACK:
            pivotInward();
            break;

            default:
            testing();
            break;

        }
    }




}
