package cs131.pa4.CarsTunnels;

import cs131.pa4.Abstract.Direction;
import cs131.pa4.Abstract.Tunnel;
import cs131.pa4.Abstract.Vehicle;

/**
 * 
 * The class for the Basic Tunnel, extending Tunnel.
 * @author cs131a
 *
 */
public class BasicTunnel extends Tunnel{

	/**
	 * Creates a new instance of a basic tunnel with the given name
	 * @param name the name of the basic tunnel
	 */
	
	/**
	 * Number of cars inside the tunnel
	 */
	public int carInside = 0;
	/**
	 * Number of sleds inside the tunnel
	 */
	public int sledInside = 0;
	/**
	 * Number of cars checking to enter the tunnel
	 */
	int tempCar;
	/**
	 * Number of sleds checking to enter the tunnel
	 */
	int tempSled;
	/**
	 * Vehicles direction inside the tunnel
	 */
	public Direction direction=null;
	
	public BasicTunnel(String name) {
		super(name);
	}

	
	@Override
	/**
	 * Synchronized method checking if a vehicle is allowed to enter the tunnel
	 * @param vehicle try to enter
	 */
	protected synchronized boolean tryToEnterInner(Vehicle vehicle) {
		tempCar=0;
		tempSled=0;
		if(direction==null) {
			direction=vehicle.getDirection();
		}		
		if (vehicle instanceof Car) {tempCar=1;}
		if (vehicle instanceof Sled) {tempSled=1;}
		if(!vehicle.getDirection().equals(direction) || sledInside+tempSled>1 || carInside+tempCar>3 || ((sledInside+tempSled)>0&&carInside+tempCar>0)) { 
		//restriction for the tunnel at any time: all vehicles in the same direction; has three cars at most; has one sled at most; has no sleds with cars
			return false;
		}
		carInside+=tempCar;
		sledInside+=tempSled;
		return true;
	}

	@Override
	/**
	 * Synchronized method letting a vehicle exit the tunnel
	 * @param vehicle try to enter
	 */
	protected synchronized void exitTunnelInner(Vehicle vehicle) {
		 if (vehicle instanceof Car) {carInside--;}
		 if (vehicle instanceof Sled) {sledInside--;}
		 if (sledInside+carInside==0) {
			 direction=null;
		 }
	}
	
}
