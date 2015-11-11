package Bosch.ACC;

import SharedMemory.IACCGet;
import SharedMemory.IACCSet;

public class Acc {

	//3.5 m/s a maximum gyorsulás amit az acc létrehozhat (negatívban vagy pozitívban)
	private double maximumAcceleration = 3.5;
	
	IACCSet accSet;
	IACCGet accGet;
	double targetSpeed;
	
	double tempomatSpeed;
	
	public Acc(double maximumAcceleration,
			IACCSet accSet,
			IACCGet accGet
			) {
		this.maximumAcceleration = maximumAcceleration;
		this.accGet = accGet;
		this.accSet = accSet;
		this.targetSpeed = accGet.getTargetSpeed();
		this.tempomatSpeed = accGet.getTargetSpeed();
		if(accGet.getDistanceOfClosestCarRadar() == -1)
		{
			this.isCarAhead = false;
		}
		else
		{
			this.isCarAhead = true;
		}
	}
	
	
	double currentBrakePedalPosition;
	double currentGasPedalPosition;
	
	double lastRelativeSpeed;
	
	double currentDistanceOfClosestCar;
	double currentRelativeSpeed;
	
	double relativeAcceleration;
	
	double lastSpeed;
	double currentSpeed;
	double currentAcceleration;

	
	private void calculateAcceleration(double deltatime)
	{
		double deltaSpeed = currentSpeed - lastSpeed;
		this.currentAcceleration = deltaSpeed /  deltatime; 
	}
	
	private void calculateRelativeAcceleartion(double deltatime)
	{
		double deltaSpeed = currentRelativeSpeed - lastRelativeSpeed;
		this.relativeAcceleration = deltaSpeed / deltatime; 
	}
	
	public void Iterate(double timeLeft)
	{
		if(accGet.getDistanceOfClosestCarRadar() == -1)
		{
			this.isCarAhead = false;
		}
		else
		{
			this.isCarAhead = true;
		}
		
		lastRelativeSpeed = currentRelativeSpeed;
		lastSpeed = currentSpeed;
		
		this.currentBrakePedalPosition =
		accGet.getBrakePedalPositionPercent();
		
		this.currentGasPedalPosition =
	    accGet.getGasPedalPositionPercent();
		
		this.currentDistanceOfClosestCar =
		accGet.getDistanceOfClosestCarRadar();
		
		this.currentRelativeSpeed =
		accGet.getRelativeSpeed();
		
		this.currentAcceleration =
		(currentSpeed - lastSpeed) / timeLeft;
		
		this.currentSpeed =
	    accGet.getSpeed();
		
		calculateAcceleration(timeLeft);
		calculateRelativeAcceleartion(timeLeft);
		
		doControl();
	}
	
	boolean isCarAhead;
	
	private void decreaseSpeed()
	{
		if(currentGasPedalPosition > 0)
		{
			accSet.releaseGasPedal(1);;
		}
		else
		{
			if(currentBrakePedalPosition < 100)
			{
				accSet.pushBrakePedal(1);
			}
		}
	}
	
	private void IncreaseSpeed()
	{
		if(currentBrakePedalPosition != 0)
		{
			accSet.releaseBrakePedal(100);
		}
		else
		{
			if(currentGasPedalPosition != 100)
			{
				accSet.pushGasPedal(1);
			}						
		}
	}
	
	
	/*
	a				b
							
	t sec //mennyi idő alatt ütközünk
	a = relativeAcceleration
	
	v = distance / t
	a = v / t
	
			speedchangeneeded
			t alatt, vagyis az pont egy vizsgálati idő
			
    timelefttocollision = distance / v
	targetacc = v / timelefttocollision
	
    targetacc = v / (distance / v)
    
    targetacc - relativeAcceleration
    
    deltaaccneeded = targetacc - relativeAcceleration 
    
    instant ha elérjük akkor pont teljesítjük
    ha pedig timelefttocollision akkor pont nem koccoljuk meg
	 */
	
	private double CalcTargetAbsoluteAcc()
	{
		double v = currentRelativeSpeed;
		double a = relativeAcceleration;
		double s = currentDistanceOfClosestCar;
		
		double timelefttocollision = s / v;
		double targetacc = v / timelefttocollision;
		
		double deltaaccneeded = targetacc - a;
		double targetabsolute = deltaaccneeded + currentAcceleration;
		
		return targetabsolute;
	}
	
	double curvespeed = 25;
	double curve;
	
	private void doControl()
	{
		if(accGet.isAccIntact())
		{
			if(curve > 0)
			{
				double rateofslow = curve / 90;
				
				targetSpeed = curvespeed / rateofslow;
			}
			else
			{
				targetSpeed = tempomatSpeed;
			}
			
			if(!isCarAhead)
			{
				Tempomat();
			}
			else
			{
				Acc();
			}
		}
	}
	
	private void Acc()
	{
		double absTarget = CalcTargetAbsoluteAcc();
		
		if(Math.abs(absTarget) < maximumAcceleration)
		{
			//100% brake   ----  100% gas
			//currentBrakePedalPosition  ---- currentGasPedalPosition
			
			double getmagic = absTarget / maximumAcceleration;
			
			if(getmagic < 0)
			{
				//fék kell getmagic arányban
				//-1 - 0 ig skálát leképezzük a fék jelenlegi és maximum állása közötti intervallumra
				
				double scalegas = 100 - currentGasPedalPosition;
				double scalebrake = 100 - currentBrakePedalPosition;
				double sumscale = scalegas + scalebrake;
				double scaletarget = Math.abs(getmagic)*sumscale;
				
				if(scaletarget <= scalegas)
				{
					accSet.releaseGasPedal(scaletarget*100);
				}
				else
				{
					accSet.releaseGasPedal(scalegas);
					accSet.pushBrakePedal((scaletarget - scalegas)*100);
				}
			}
			else
			{
				//gáz kell getmagic arányban
				//0 - 1 ig skálát leképezzük a gázra jelenlegi és maximum állása közötti intervallumra
				
				double scalegas = 100 - currentGasPedalPosition;
				double scalebrake = 100 - currentBrakePedalPosition;
				double sumscale = scalegas + scalebrake;
				double scaletarget = Math.abs(getmagic)*sumscale;
				
				if(scaletarget <= scalegas)
				{
					accSet.releaseBrakePedal(scaletarget*100);
				}
				else
				{
					accSet.releaseBrakePedal(scalebrake);
					accSet.pushGasPedal((scaletarget - scalebrake)*100);
				}
			}
		}
		else
		{
			if(absTarget < 0)
			{
				//we are fucked
				//set alarm és satufék
				
				accSet.SetAlarm(true);
				accSet.releaseGasPedal(100);
				accSet.pushBrakePedal(100);
			}
			else
			{
				//elhajt tőlünk az autó
				Tempomat();
			}
		}
	}
	
	
	private void Tempomat()
	{
		//tempomat
		if(currentSpeed < targetSpeed)
		{
			if(currentAcceleration < maximumAcceleration)
			{
				//want to go faster, cause im on a highway
				
				//we release the brakes
				IncreaseSpeed();
			}
			else
			{
				decreaseSpeed();
			}
		}
		else if(currentSpeed > targetSpeed)
		{
			if(currentAcceleration < 0)
			{
				if(-currentAcceleration < maximumAcceleration)
				{
					decreaseSpeed();
				}
				else //we are deaccelerating too fast
				{
					IncreaseSpeed();
				}
			}
			else
			{
				decreaseSpeed();
			}
			
		}
	}
	
	private double timeLeftTillCollision()
	{
		return currentDistanceOfClosestCar / currentRelativeSpeed;
	}
	
	/*The following distance must be larger than this*/
	private double getMaxBrakingDistance()
	{
		return currentRelativeSpeed/maximumAcceleration;
	}
	
	private void StartBraking(double percent)
	{
		accSet.releaseGasPedal(100);
		accSet.pushBrakePedal(percent);
	}
	
}
