package cs131.pa4.CarsTunnels;

import java.util.Collection;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.locks.*;

import cs131.pa4.Abstract.Scheduler;
import cs131.pa4.Abstract.Tunnel;
import cs131.pa4.Abstract.Vehicle;

/**
 * The priority scheduler assigns vehicles to tunnels based on their priority
 * It extends the Scheduler class.
 * @author cs131a
 *
 */
public class PriorityScheduler extends Scheduler{
	
	/**
	 * collection of tunnels
	 */
	protected Collection<Tunnel> tunnels;
	/**
	 * lock
	 */
	private Lock lock = new ReentrantLock(); 
	/**
	 * conditional variable
	 */
	private Condition condVar = lock.newCondition(); 
	/**
	 * queque of waiting vehicles
	 */
	private PriorityQueue<Vehicle> waiting_vehicles=new PriorityQueue<Vehicle>(new CarComparator());
	/**
	 * map of inside vehicles and corresponding tunnels
	 */
	private HashMap<Vehicle,Tunnel> inside_vehicles=new HashMap<Vehicle,Tunnel>();
	/**
	 * tunnel
	 */
	private Tunnel temp;
	/**
	 * Creates an instance of a priority scheduler with the given name and tunnels
	 * @param name the name of the priority scheduler
	 * @param tunnels the tunnels where the vehicles will be scheduled to
	 */
	public PriorityScheduler(String name, Collection<Tunnel> tunnels) {
		super(name,tunnels);
		this.tunnels = super.getTunnels();
	}
	
	/**
	 * Checks if a vehicle is qualified to enter a tunnel
	 * @param v a given vehicle
	 * @return tunnel that the vehicle enter
	 */
	public Tunnel checkPriorityEnter(Vehicle v) {
		if(v.equals(waiting_vehicles.peek())){
			for(Tunnel t:tunnels) {
				if(t.tryToEnter(v)) {
					temp=t;
					return t;
				}
			}
		}
		return null;
	}
	
	@Override
	public Tunnel admit(Vehicle vehicle) {
		lock.lock();
		waiting_vehicles.add(vehicle);
		while(checkPriorityEnter(vehicle)==null) {
			try {
				condVar.await(); //waiting if not qualified to enter a tunnel
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		inside_vehicles.put(waiting_vehicles.poll(),temp);
		lock.unlock();
		return temp;
	}
	
	@Override
	public void exit(Vehicle vehicle) {
		lock.lock();
		inside_vehicles.get(vehicle).exitTunnel(vehicle);
		inside_vehicles.remove(vehicle);
		condVar.signalAll(); //signals waiting vehicles to check entering tunnel
		lock.unlock();
	}
	
}
