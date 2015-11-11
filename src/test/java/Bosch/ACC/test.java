package Bosch.ACC;

public class test {

	private static void PrintPedalPosition(DummyAccSetGet dummySharedMemory)
	{
		System.out.println("GAS: "+dummySharedMemory.getGasPedalPositionPercent());
		System.out.println("BRAKE: "+dummySharedMemory.getBrakePedalPositionPercent());
	}
	
	private static void printIterate(double val, Acc acc,DummyAccSetGet dummySharedMemory)
	{
		acc.Iterate(val);
		PrintPedalPosition(dummySharedMemory);
	}
	
	public static void main(String[] args) {
		
		DummyAccSetGet dummySharedMemory = new DummyAccSetGet();
		
		dummySharedMemory.releaseBrakePedal(100);
		dummySharedMemory.releaseGasPedal(100);
		dummySharedMemory.DistanceOfClosestCarRadar = -1;
		dummySharedMemory.Speed = 0;
		dummySharedMemory.TargetSpeed = 20;
		dummySharedMemory.Curve = 0;
		Acc acc = new Acc(3.5, dummySharedMemory, dummySharedMemory);
		
		printIterate(1, acc, dummySharedMemory);
		
		dummySharedMemory.Speed = 15;
		printIterate(1, acc, dummySharedMemory);
		
		dummySharedMemory.Speed = 20;
		printIterate(1, acc, dummySharedMemory);
		
		dummySharedMemory.Speed = 21;
		printIterate(1, acc, dummySharedMemory);
	}

}
