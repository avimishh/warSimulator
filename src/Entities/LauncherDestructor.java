package Entities;
import java.util.HashMap;

public class LauncherDestructor extends Destructor<Launcher> {
	private static final long serialVersionUID = -314020625974934091L;
	public enum DestractorType {PLANE, SHIP};
	private DestractorType type;
	
	public LauncherDestructor(String id) {
		super(id, new HashMap<Launcher, Long>());
	}

	public DestractorType getType() {
		return type;
	}

	public void setType(DestractorType type) {
		this.type = type;
	}
	
	@Override
    public String toString() {
        return String.format("%s [%6s]",super.toString(),type.toString());
    }

}
