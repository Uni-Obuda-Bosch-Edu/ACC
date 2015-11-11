package SharedMemory;

public interface IACCGet {
	
	
	//public double getRelativeAcceleration();
	
	
	/* the gear is automatic
	public int getCurrentGear();
	public double getDownShiftRPM(int gear);
	public double getUPShiftRPM(int gear);
	*/
	
	
	public double getGasPedalPositionPercent();
	public double getBrakePedalPositionPercent();
	
	public double getDistanceOfClosestCarRadar();
	
	
	public double getCurve();
	public double getSpeed();
	
	public double getRelativeSpeed();
	
	public double getTargetSpeed();
	public boolean isAccIntact();
	
	
}
