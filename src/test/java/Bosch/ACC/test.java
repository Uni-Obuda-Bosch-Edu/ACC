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
		
		//Nothing in front
		System.out.println("Nothing in front");
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
		
		//Car in front - increasing distance
		System.out.println("Car in front - increasing distance");
		dummySharedMemory.Speed = 15;
		dummySharedMemory.DistanceOfClosestCarRadar = 20;
		printIterate(1, acc, dummySharedMemory);
		
		dummySharedMemory.DistanceOfClosestCarRadar = 21;
		printIterate(1, acc, dummySharedMemory);
		
		dummySharedMemory.DistanceOfClosestCarRadar = 25;
		printIterate(1, acc, dummySharedMemory);
		
		//Car in front - same speed (distance = 25)
		System.out.println("Car in front - same speed (distance = 25)");
		printIterate(1, acc, dummySharedMemory);
		printIterate(1, acc, dummySharedMemory);
		printIterate(1, acc, dummySharedMemory);
		
		//Car closing in distance
		System.out.println("Car closing in distance");
		dummySharedMemory.DistanceOfClosestCarRadar = 24;
		printIterate(1, acc, dummySharedMemory);
		
		dummySharedMemory.DistanceOfClosestCarRadar = 22;
		printIterate(1, acc, dummySharedMemory);
		
		dummySharedMemory.DistanceOfClosestCarRadar = 19;
		printIterate(1, acc, dummySharedMemory);
		
		//Curve
		System.out.println("Curve = 45°");
		dummySharedMemory.Curve = 45;
		dummySharedMemory.Speed = 90;
		printIterate(1, acc, dummySharedMemory);
		printIterate(1, acc, dummySharedMemory);
		printIterate(1, acc, dummySharedMemory);
		printIterate(1, acc, dummySharedMemory);

	}

}
