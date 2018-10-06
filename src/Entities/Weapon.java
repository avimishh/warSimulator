package Entities;
import java.io.Serializable;

public abstract class Weapon implements Serializable {
	private static final long serialVersionUID = 9141116338022165364L;
	private String id;

	public Weapon(String id) {
		setId(id);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this==o) return true;
		if(o==null||this.getClass()!=o.getClass()) return false;
		Weapon oWeapon = (Weapon) o;
		return id.equals(oWeapon.id);
	}
	
	
	@Override
	public String toString() {
		return String.format("%-5s %-20s", getId(),getClass().getSimpleName());
	}
	
}
