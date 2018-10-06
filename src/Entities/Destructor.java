package Entities;
import java.util.HashMap;

public abstract class Destructor<T extends DestructibleWeapon> extends Weapon {
	private static final long serialVersionUID = 7021319384117503206L;
	private HashMap<T,Long> destructedWeapons;
	
	public Destructor(String id,HashMap<T,Long> destructedWeapons) {
		super(id);
		this.destructedWeapons = destructedWeapons;
	}
	
    public void destruct(T destructibleWeapon){
        destructibleWeapon.destruct();
    }

    public void addDestructedWeapon(T destructedWeapon, long time){
        destructedWeapons.put(destructedWeapon, time);
    }

    @Override
    public String toString() {
        return String.format("%s Destructed Weapons: %2d",super.toString(), destructedWeapons.size());
    }
	

}
