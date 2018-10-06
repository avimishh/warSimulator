package Entities;
import java.util.concurrent.atomic.AtomicBoolean;

public class Missile extends DestructibleWeapon {

	private static final long serialVersionUID = -6237565596243452816L;
	private String destination;
	private double launchTime;
	private double flyTime;
	private double damage;
	private AtomicBoolean isDestructed = new AtomicBoolean(false);

	
	/**
	 * 
	 * @param id
	 * @param destination
	 * @param launchTime
	 * @param flyTime
	 * @param damage
	 */
	public Missile(String id, String destination, double launchTime, double flyTime, double damage) {
		super(id);
		setDestination(destination);
		setLaunchTime(launchTime);
		setFlyTime(flyTime);
		setDamage(damage);
	}

	/**
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * @return the launchTime
	 */
	public double getLaunchTime() {
		return launchTime;
	}

	/**
	 * @param launchTime the launchTime to set
	 */
	public void setLaunchTime(double launchTime) {
		this.launchTime = launchTime;
	}

	/**
	 * @return the flyTime
	 */
	public double getFlyTime() {
		return flyTime;
	}

	/**
	 * @param flyTime the flyTime to set
	 */
	public void setFlyTime(double flyTime) {
		this.flyTime = flyTime;
	}

	/**
	 * @return the damage
	 */
	public double getDamage() {
		return damage;
	}

	/**
	 * @param damage the damage to set
	 */
	public void setDamage(double damage) {
		this.damage = damage;
	}

	@Override
	public void destruct() {
		isDestructed.set(true);
		
	}

	@Override
	public boolean isDestructed() {
		
		return isDestructed();
	}
	
	@Override
	public String toString() {
		return super.toString() +
				" destination= " + destination +
				", launchTime=" + launchTime +
                ", flightTime=" + flyTime +
                ", damage=" + damage;
	}

}
