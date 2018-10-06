package Entities;
import java.util.HashMap;

public class MissileDestructor extends Destructor<Missile> {
	private static final long serialVersionUID = -8405969588734792859L;
//	private HashMap<Missile, Long> destructedMissiles = new HashMap<>();
	
	public MissileDestructor(String id) {
		super(id, new HashMap<Missile, Long>());
	}

}
