/******************************************************************
*
*	Copyright (C) Satoshi Konno 1999
*
*	File : MagellanInputDevice.java
*
******************************************************************/

import javax.media.j3d.*;
import javax.vecmath.*;

public class MagellanInputDevice implements InputDevice {
	
	private Magellan		magellan;
	private Sensor			magellanSensor			= new Sensor(this);
	private SensorRead	magellanSensorRead	= new SensorRead();
	
	private Transform3D	magellanTransform		= new Transform3D();
	private float			magellanTrans[]		= new float[3];
	private float			magellanRot[]			= new float[3];
	private int				data[]					= new int[3];
	
	private Transform3D	posTransform			= new Transform3D();
	private Transform3D	rotTransform			= new Transform3D();
	private Vector3f 		posVector				= new Vector3f();
	private Transform3D	trans						= new Transform3D();
	
	private float			sensitivity			= 1.0f;
	private float			angularRate			= 1.0f;
	private float			x, y, z;
	
	public MagellanInputDevice(Magellan magellan) {
		this.magellan = magellan;
		initialize();
		setSensitivity(1.0f);
		setAngularRate(1.0f);
		magellan.start();
	}

	public boolean initialize() {
		for (int i=0; i<3; i++) {
			magellanTrans[i]	= 0;
			magellanRot[i]		= 0;
		}
		return true;
	}

	public void close() {
		magellan.stop();
	}

	public int getProcessingMode() {
		return DEMAND_DRIVEN;
	}

	public int getSensorCount() {
		return 1;
	}

	public Sensor getSensor(int id)  {
		return magellanSensor;
	}

	public void setProcessingMode(int mode) {
	}

	public void getPositionTransform(Transform3D posTrans) {
		magellan.getTranslation(data);
		for (int n=0; n<3; n++)
			magellanTrans[n] += (float)data[n] * sensitivity;
		posVector.x = magellanTrans[0];
		posVector.y = magellanTrans[1];
		posVector.z = magellanTrans[2];
 		posTrans.setIdentity();
 		posTrans.setTranslation(posVector);
	}

	public void getRotationTransform(Transform3D rotTrans) {
		magellan.getRotation(data);

		if (data[1] <= data[0] && data[2] <= data[0])
			magellanRot[0] += (float)data[0] * angularRate;
		else if (data[0] <= data[1] && data[2] <= data[1])
			magellanRot[1] += (float)data[1] * angularRate;
		else
			magellanRot[2] += (float)data[2] * angularRate;

		for (int n=0; n<3; n++)
			magellanRot[n] = magellanRot[n] % 360.0f;

 		rotTrans.setIdentity();
 		
		trans.setIdentity();
		trans.rotX(Math.toRadians((double)magellanRot[0]));
		rotTrans.mul(trans);

		trans.setIdentity();
		trans.rotY(Math.toRadians((double)magellanRot[1]));
		rotTrans.mul(trans);

		trans.setIdentity();
		trans.rotZ(Math.toRadians((double)magellanRot[2]));
		rotTrans.mul(trans);
	}
	
	public void pollAndProcessInput() {
		int buttons = magellan.getButtons();
		if ((buttons & Magellan.BUTTONA) != 0) {
			setNominalPositionAndOrientation();
			return;
		}
		
		magellanSensorRead.setTime(System.currentTimeMillis());
			
		getPositionTransform(posTransform);
		getRotationTransform(rotTransform);
 			
 		magellanTransform.setIdentity();
		magellanTransform.mul(rotTransform);
		magellanTransform.mul(posTransform);

		magellanSensorRead.set(magellanTransform);
		magellanSensor.setNextSensorRead(magellanSensorRead);
	}
	
	public void processStreamInput() {
	}

	public void setNominalPositionAndOrientation() {
		initialize();
		magellanSensorRead.setTime(System.currentTimeMillis());
		magellanTransform.setIdentity();
		magellanSensorRead.set(magellanTransform);
		magellanSensor.setNextSensorRead(magellanSensorRead);
	}
	
	public void setSensitivity(float value) {
		sensitivity = value;
	}

	public float getSensitivity() {
		return sensitivity;
	}
	
	public void setAngularRate(float value) {
		angularRate = value;
	}

	public float getAngularRate() {
		return angularRate;
	}
}
