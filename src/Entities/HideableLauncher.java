package Entities;

public class HideableLauncher extends Launcher {
	private static final long serialVersionUID = 7080771447951370684L;

	public HideableLauncher(String id) {
		super(id);
	}
	
	@Override
	public boolean isHidden() {
		return !isCurrentlyLaunching();
		
	}
	
	@Override
	public String toString() {
		return String.format("%s Hideable", super.toString() );
	}

}
