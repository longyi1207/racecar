package cs131.pa4.CarsTunnels;

import java.util.Comparator;

import cs131.pa4.Abstract.Vehicle;

public class CarComparator implements Comparator<Vehicle>{
	

	@Override
	public int compare(Vehicle v1, Vehicle v2) {
		return v1.getPriority()<v2.getPriority()?1:-1;
	}
}
