package Entities;
import java.io.Serializable;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

public class Launcher extends DestructibleWeapon implements Serializable {
	private static final long serialVersionUID = 853986302536976858L;
	private boolean isHidden;
	private Vector<Missile> missiles;
	private AtomicBoolean currentlyLaunching = new AtomicBoolean();

	public Launcher(String id) {
		super(id);
		missiles = new Vector<>();
	}
    public synchronized void addMissile(Missile missile){
        missiles.add(missile);
    }

    public Missile getMissile(String id){
        return missiles.get(missiles.indexOf(new Missile(id, "", 0, 0, 0)));
    }

    public int getNumOfLaunchedMissiles(){
        return missiles.size();
    }
	public boolean isHidden() {
		return false;
	}

	public Vector<Missile> getMissiles() {
		return missiles;
	}

	public void setMissiles(Vector<Missile> missiles) {
		this.missiles = missiles;
	}

	@Override
	public String toString() {
		return String.format("%s Hidden: %4s Launching: %4s", super.toString(), isHidden(), isCurrentlyLaunching());
	}

	public boolean isCurrentlyLaunching() {
		return currentlyLaunching.get();
	}

	public void setCurrentlyLaunching(boolean currentlyLaunching) {
		this.currentlyLaunching.set(currentlyLaunching);
	}
}
