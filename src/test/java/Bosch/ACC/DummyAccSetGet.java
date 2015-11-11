package Bosch.ACC;

import SharedMemory.IACCGet;
import SharedMemory.IACCSet;

public class DummyAccSetGet implements IACCSet, IACCGet{

	public boolean alarm;
	
	@Override
	public void SetAlarm(boolean state) {
		this.alarm = state;
	}

	double MaxPedalPushValue = 100;
	double GasPedalPosition;
	
	@Override
	public void pushGasPedal(double percent) {
		double currentPosition = getGasPedalPositionPercent();
		currentPosition += Math.min(Math.abs(percent), MaxPedalPushValue - currentPosition);
		GasPedalPosition = currentPosition; 
	}

	@Override
	public void releaseGasPedal(double percent) {
		double currentPosition = getGasPedalPositionPercent();
		currentPosition -= Math.min(Math.abs(percent), currentPosition);
		GasPedalPosition = currentPosition;
	}

	double BrakePedalPosition;
	
	@Override
	public void pushBrakePedal(double percent) {
		double currentPosition = getBrakePedalPositionPercent();
		currentPosition += Math.min(Math.abs(percent), MaxPedalPushValue - currentPosition);
		BrakePedalPosition = currentPosition;
	}

	@Override
	public void releaseBrakePedal(double percent) {
		double currentPosition = getBrakePedalPositionPercent();
		currentPosition -= Math.min(Math.abs(percent), currentPosition);
		BrakePedalPosition = currentPosition;
	}

	@Override
	public double getGasPedalPositionPercent() {
		return GasPedalPosition;
	}

	@Override
	public double getBrakePedalPositionPercent() {
		return BrakePedalPosition;
	}

	double DistanceOfClosestCarRadar = -1;
	@Override
	public double getDistanceOfClosestCarRadar() {
		return DistanceOfClosestCarRadar;
	}

	public double Speed = 0;
	
	@Override
	public double getSpeed() {
		return Speed;
	}

	public double RelativeSpeed = 0;
	
	@Override
	public double getRelativeSpeed() {
		return RelativeSpeed;
	}

	
	public double TargetSpeed = 25;
	
	@Override
	public double getTargetSpeed() {
		return TargetSpeed;
	}

	public boolean isACCIntact = true;
	
	@Override
	public boolean isAccIntact() {
		return isACCIntact;
	}

	double Curve = 0;
	
	@Override
	public double getCurve() {
		return Curve;
	}

}
