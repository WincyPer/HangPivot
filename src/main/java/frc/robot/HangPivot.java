package frc.robot;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Timer;

public class HangPivot {
    
    /////////////////////////////////////////////
    //                                         //
    //                VARIABLES                //
    //                                         //
    /////////////////////////////////////////////

    //  MOTOR (775) & VERSA-PLANETARY ENCODER  //
    private MotorController hangPivot;
    private TalonEncoder pivotEncoder;       

    //  LIMIT SWITCHES  //
    private DigitalInput frontSwitch;   
    private DigitalInput backSwitch;

    //  GYRO (NAVX)  //
    private AHRS navX;

    //  VARIABLES [SUBJECT TO CHANGE]  //
    private final double inwardPivotPos = -1100.0;
    private final double outwardPivotPos = -1500.0;
    private final double inwardPivotSpeed = 0.25;
    private final double outwardPivotSpeed = -0.25;

    private int pivotHangCounter = 0;
    private Timer timer;
    private int timerTest; 

    /////////////////////////////////////////////
    //                                         //
    //              CONSTRUCTOR                //
    //                                         //
    /////////////////////////////////////////////
    
    public HangPivot (MotorController pivotMotor, TalonEncoder hangPivotEncoder, AHRS gyro, DigitalInput frontLimitSwitch, DigitalInput backLimitSwitch, Timer hangTimer){  
        hangPivot = pivotMotor;
        pivotEncoder = hangPivotEncoder;
        frontSwitch = frontLimitSwitch;
        backSwitch = backLimitSwitch;
        navX = gyro;
        timer = new Timer();
    }

    /////////////////////////////////////////////
    //                                         //
    //               ENUMERATION               //
    //                                         //
    /////////////////////////////////////////////

    private enum States{
        PIVOTINWARD, PIVOTOUTWARD, PIVOTMID, STOP, TESTING;
    }

    //  SETTING STATES  //
    public States pivotState = States.STOP;

    public void setPivInward(){
        pivotState = States.PIVOTINWARD;
    }

    public void setPivOutward(){
        pivotState = States.PIVOTOUTWARD;
    }

    public void setPivotMid(){
        pivotState = States.PIVOTMID;
    }

    public void setTesting(){
        pivotState = States.TESTING;
    }

    public void setStop(){
        pivotState = States.STOP;
    }
    
    /////////////////////////////////////////////
    //                                         //
    //                 CHECKS                  //
    //                                         //
    /////////////////////////////////////////////

    private boolean backLimitTouched(){     //RETURNS VALUE OF BACK LIMIT SWITCH
        return backSwitch.get();
    }

    private boolean frontLimitTouched(){    //RETURNS VALUE OF FRONT LIMIT SWITCH
        return frontSwitch.get();
    }

    private boolean pivotIsOutward(){
        return timer.get() > 3;
    }

    /////////////////////////////////////////////
    //                                         //
    //                 METHODS                 //
    //                                         //
    /////////////////////////////////////////////

    public void resetEnc(){     //RESETS ENCODERS FOR THE PIVOT MOTOR
        pivotEncoder.reset();
    }

    private void pivotOutward(){    //PIVOTS OUTWARD FOR A CERTAIN AMOUNT OF ENCODER COUNTS [INWARD = TOWARDS ROBOT BASE, OUTWARD = TOWARDS ROBOT PERIMETER]
        if(backLimitTouched()){
            if(pivotEncoder.get() > outwardPivotPos){
                hangPivot.set(outwardPivotSpeed);
            }

            else{
                hangPivot.set(0);
            }
        }

        else{
            hangPivot.set(0);
        }
    }

    private void pivotInward(){     //PIVOTS INWARD FOR A CERTAIN AMOUNT OF ENCODER COUNTS
        if(frontLimitTouched()){   //IF THE FRONT LIMIT IS NOT TOUCHED
            if(pivotEncoder.get() < inwardPivotPos){    //IF THE PIVOT ENCODER IS LESS THAN ITS POSITION, PIVOT INWARD
                hangPivot.set(inwardPivotSpeed);
            }

            else{   //STOP IF POSITION IS REACHED
                hangPivot.set(0);
            }
        }

        else{
            hangPivot.set(0);
        }
    }

    private void manualPivotOutward(){      //MANUALLY PIVOT OUTWARD
        hangPivot.set(outwardPivotSpeed);
    }

    private void manualPivotInward(){       //MANUALLY PIVOT INWARD
        hangPivot.set(inwardPivotSpeed);       
    }

    public void manualPivot(double pivotSpeed){     //MANUALLY MOVE THE PIVOT MOTOR WITH JOYSTICK
        hangPivot.set(pivotSpeed);
    }

    private void stopPivot(){       //STOPS HANG PIVOT
        hangPivot.set(0);
    }
    

    /////////////////////////////////////////////
    //                                         //
    //                   RUN                   //
    //                                         //
    /////////////////////////////////////////////

    public void run(){      //RUN METHOD WITH SMART DASHBOARD DISPLAYS AND STATE SWITCHES

        SmartDashboard.putNumber("MOTOR SPEED", hangPivot.get());
        SmartDashboard.putString("HANG PIVOT STATE", pivotState.toString());
        SmartDashboard.putBoolean("BACK LIMIT", backSwitch.get());
        SmartDashboard.putBoolean("FRONT LIMIT", frontSwitch.get());
        SmartDashboard.putNumber("PIVOT ENCODER", pivotEncoder.get());
        SmartDashboard.putNumber("NAVX PITCH", navX.getPitch());
        SmartDashboard.putNumber("MID PIVOT CASE", timerTest);
        
        switch(pivotState){
            
            case TESTING:
            testing();
            break;

            case PIVOTOUTWARD:
            manualPivotOutward();
            break;

            case PIVOTINWARD:
            manualPivotInward();
            break;

            case PIVOTMID:
            //pivotOntoMid();
            pivotOutward3Secs(); 
            break;

            case STOP:
            stopPivot();
            break;

        }
    }




}
