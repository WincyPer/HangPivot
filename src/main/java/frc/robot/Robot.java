// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Joystick;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;


public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private Joystick joystick;

  //  HANG  //
  private WPI_TalonSRX hangPivotMotor;
  private TalonEncoder hangPivotEncoder;
  private DigitalInput backPivotSwitch;
  private DigitalInput frontPivotSwitch;
  private AHRS navX;
  private Timer pivotTimer;

  private HangPivot hangPivotClass;

  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    joystick = new Joystick(0);
    hangPivotMotor = new WPI_TalonSRX(3);   //PORTS ARE NOT FINAL, THESE PORTS ARE FROM OLD ROBOT
    hangPivotEncoder = new TalonEncoder(hangPivotMotor);    
    backPivotSwitch = new DigitalInput(3);
    frontPivotSwitch = new DigitalInput(4);
    navX = new AHRS (SPI.Port.kMXP);

    hangPivotClass = new HangPivot(hangPivotMotor, hangPivotEncoder, navX, frontPivotSwitch, backPivotSwitch, pivotTimer);

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        break;

      case kDefaultAuto:
      default:
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
   
    if(joystick.getRawButton(3)){   //MANUALLY RESET ENCODERS
      hangPivotClass.resetEnc();
    }

    else if(joystick.getRawButton(4)){    //MANUALLY MOVE HANG PIVOT
      hangPivotClass.setTesting();
      hangPivotClass.manualPivot(joystick.getY());
    }

    else if(joystick.getRawButton(5)){    //PIVOT OUTWARD UNTIL ENCODER LIMIT
      hangPivotClass.setPivOutward();
    }

    else if(joystick.getRawButton(6)){    //PIVOT INWARD UNTIL ENCODER LIMIT
      hangPivotClass.setPivInward();
    }

    else if(joystick.getRawButton(7)){
      
    }

    else{
      hangPivotClass.setStop();
    }

    hangPivotClass.run();
    
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
    hangPivotClass.setStop();
  }

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
