/**
 * 
 */
package de.yadrone.base.navdata.common;

import de.yadrone.base.navdata.AcceleroListener;
import de.yadrone.base.navdata.AcceleroPhysData;
import de.yadrone.base.navdata.AcceleroRawData;
import de.yadrone.base.navdata.Altitude;
import de.yadrone.base.navdata.AltitudeListener;
import de.yadrone.base.navdata.AttitudeListener;
import de.yadrone.base.navdata.BatteryListener;
import de.yadrone.base.navdata.ControlState;
import de.yadrone.base.navdata.DroneState;
import de.yadrone.base.navdata.KalmanPressureData;
import de.yadrone.base.navdata.MagnetoData;
import de.yadrone.base.navdata.MagnetoListener;
import de.yadrone.base.navdata.NavDataManager;
import de.yadrone.base.navdata.PWMData;
import de.yadrone.base.navdata.PWMlistener;
import de.yadrone.base.navdata.Pressure;
import de.yadrone.base.navdata.PressureListener;
import de.yadrone.base.navdata.StateListener;
import de.yadrone.base.navdata.Temperature;
import de.yadrone.base.navdata.TemperatureListener;
import de.yadrone.base.navdata.TimeListener;
import de.yadrone.base.navdata.VelocityListener;
import de.yadrone.base.navdata.WindEstimationData;
import de.yadrone.base.navdata.WindListener;

/**
 * @author Formicarufa (Tomas Prochazka)
 *12. 3. 2016
 */
public class NavdataCollector implements TimeListener, PWMlistener, AcceleroListener, VelocityListener, AltitudeListener, AttitudeListener, WindListener, TemperatureListener, PressureListener, MagnetoListener, BatteryListener, StateListener {
	
	CommonNavdata result;
	/**
	 * Navdata collector listens to changes of individual
	 * values in the navdata and fills an instance of 
	 * CommonNavdata with the values
	 */
	public NavdataCollector() {
		result = new CommonNavdata();
	}
	private boolean activated=false;
	public void activate(NavDataManager manager){
		if (activated) return;
		activated=true;
		manager.addStateListener(this);
		manager.addBatteryListener(this);
		manager.addMagnetoListener(this);
		manager.addPressureListener(this);
		manager.addTemperatureListener(this);
		manager.addWindListener(this);
		manager.addAttitudeListener(this);
		manager.addAltitudeListener(this);
		manager.addVelocityListener(this);
		manager.addAcceleroListener(this);
		manager.addPWMlistener(this);
		manager.addTimeListener(this);
		
			
	}
	public void deactivate(NavDataManager manager) {
		if (!activated) return;
		activated=false;
		manager.removeStateListener(this);
		manager.removeBatteryListener(this);
		manager.removeMagnetoListener(this);
		manager.removePressureListener(this);
		manager.removeTemperatureListener(this);
		manager.removeWindListener(this);
		manager.removeAttitudeListener(this);
		manager.removeAltitudeListener(this);
		manager.removeVelocityListener(this);
		manager.removeAcceleroListener(this);
		manager.removePWMlistener(this);
		manager.removeTimeListener(this);
	}
	/**
	 * Gets the filled CommonNavdata objects
	 * and starts filling the values to a new one.
	 * To be called after all navdata are parsed.
	 * @return
	 */
	public CommonNavdata getNavdata() {
		result.time= System.currentTimeMillis();
		CommonNavdata ret = result;
		result=new CommonNavdata();
		return ret;
	}
	@Override
	public void stateChanged(DroneState state) {
		// Skipping.
		
	}
	@Override
	public void controlStateChanged(ControlState state) {
		result.state=state.ordinal();
		
	}
	@Override
	public void batteryLevelChanged(int percentage) {
		result.battery=percentage;
		
	}
	@Override
	public void voltageChanged(int vbat_raw) {
		//nothing
		
	}
	@Override
	public void received(MagnetoData d) {
		short[] mag = d.getM();
		result.magX=mag[0];
		result.magY=mag[1];
		result.magZ=mag[2];
		
	}
	@Override
	public void receivedKalmanPressure(KalmanPressureData d) {
	}
	@Override
	public void receivedPressure(Pressure d) {
		result.pressure= d.getMeasurement();
		
	}
	@Override
	public void receivedTemperature(Temperature d) {
		result.temperature=d.getMeasurement();
		
	}
	@Override
	public void receivedEstimation(WindEstimationData d) {
		result.windSpeed=d.getEstimatedSpeed();
		result.windAngle = d.getEstimatedAngle();
	}
	@Override
	public void attitudeUpdated(float pitch, float roll, float yaw) {
		result.rotY=pitch;
		result.rotX=roll;
		result.rotZ=yaw;
		
	}
	@Override
	public void attitudeUpdated(float pitch, float roll) {
		//Already captured.
	}
	@Override
	public void windCompensation(float pitch, float roll) {
		result.windCompAngleTheta=pitch; 
		result.windCompAnglePhi=roll;
	}

	@Override
	public void receivedAltitude(int altitude) {
		result.altitude=altitude;
		
	}

	@Override
	public void receivedExtendedAltitude(Altitude d) {
		// Lets skip this.
		
	}

	@Override
	public void velocityChanged(float vx, float vy, float vz) {
		result.vx=vx;
		result.vy=vy;
		result.vz=vz;
	}

	@Override
	public void receivedRawData(AcceleroRawData d) {
		int[] rawAccs = d.getRawAccs();
		result.ax= rawAccs[0];
		result.ay= rawAccs[1];
		result.az= rawAccs[2];
	}

	@Override
	public void receivedPhysData(AcceleroPhysData d) {
		//No, thanks.
		
	}

	@Override
	public void received(PWMData d) {
		short[] motor = d.getMotor();
		result.motor1=motor[0];
		result.motor2=motor[1];
		result.motor3=motor[2];
		result.motor4=motor[3];
		
	}
	/* (non-Javadoc)
	 * @see de.yadrone.base.navdata.TimeListener#timeReceived(int, int)
	 */
	@Override
	public void timeReceived(int seconds, int useconds) {
		result.boardTime= seconds* 1000L*1000+useconds;
	}
}