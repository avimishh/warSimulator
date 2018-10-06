package Entities;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class DestructibleWeapon extends Weapon implements Destructible {
	private static final long serialVersionUID = -5503396861749039239L;
	private AtomicBoolean isDestructed = new AtomicBoolean(false);
	
	public DestructibleWeapon(String id) {
		super(id);
		
	}
	
	@Override
	public void destruct() {
		isDestructed.set(true);

	}

	@Override
	public boolean isDestructed() {
		return isDestructed.get();
	}
	
	public String toStriing() {
		return String.format("%s Destructed: %4s", super.toString(),isDestructed.toString());
	}

}
