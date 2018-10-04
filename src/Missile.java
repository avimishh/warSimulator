
public class Missile {

	private String id;
	private String destination;
	private double launchTime;
	private double flyTime;
	private double damage;

	
	/**
	 * 
	 * @param id
	 * @param destination
	 * @param launchTime
	 * @param flyTime
	 * @param damage
	 */
	public Missile(String id, String destination, double launchTime, double flyTime, double damage) {
		super();
		this.id = id;
		this.destination = destination;
		this.launchTime = launchTime;
		this.flyTime = flyTime;
		this.damage = damage;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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

}
