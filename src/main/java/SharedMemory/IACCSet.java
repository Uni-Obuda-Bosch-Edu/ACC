package SharedMemory;

public interface IACCSet {
	
	/*the gear is automatic
	public void SetGear(int gear);
	*/
	
	public void SetAlarm(boolean state);
	
	public void pushGasPedal(double percent);
	public void releaseGasPedal(double percent);
	public void pushBrakePedal(double percent);
	public void releaseBrakePedal(double percent);
}
