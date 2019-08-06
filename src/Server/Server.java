package Server;

import java.io.*;
import java.net.*;
import java.util.*;
import Entities.*;
import Controller.*;

public class Server extends Thread {
	private final int PORT = 7000;
	private String name_server = "Server";
	
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private Queue<Object> vec;
	private ServerSocket server;
	private Socket socket = null;
	
	private ControllerFacade warController;
	
	private Launcher launcher = null;
	private Missile missile = null;
	private MissileDestructor missileDestructor = null;
	
	private static Server _instance;
	
	public static Server getInstance(ControllerFacade controllerFacade) throws IOException{
		if (_instance == null) {
			if (_instance == null)
	        _instance = new Server(controllerFacade);
	    }
	        
	    return _instance;
	}
	
	private Server(ControllerFacade controllerFacade) throws IOException{
		this.warController = controllerFacade;
	}

	public void Close() throws IOException
	{
		this.inputStream.close();
		this.outputStream.close();
		this.server.close();
		
	}
	
	@Override
	public void run() {
		try {
			server = new ServerSocket(PORT);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			System.out.println(name_server+ ": "  + new Date() + " Waiting for Client");
			socket = server.accept(); // blocking
			System.out.println(name_server+ ": " + new Date() + " Client connected from "
					+ socket.getInetAddress() + ":" + socket.getPort());
			this.vec = new LinkedList<>();
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
			while(true){
				this.vec = (Queue<Object>) inputStream.readObject();
				System.err.println(name_server + ": Busy");
				for (Object obj : vec) {
					if (obj instanceof Launcher) {
						this.launcher = (Launcher) obj;
						this.warController.addLauncher(launcher);
						System.err.println(name_server+ ": Added" + launcher.getClass().getSimpleName());
						
					}
					else if(obj instanceof MissileDestructor) {
						this.missileDestructor = (MissileDestructor) obj;
						this.warController.addMissileDestructor(missileDestructor);
						System.err.println(name_server + ": Added" + missileDestructor.getClass().getSimpleName());
					}
					else if (obj instanceof Missile && launcher != null) {
						this.missile = (Missile) obj;
						this.warController.launchMissile(launcher, missile);
						System.err.println(name_server + ": "+ missile + "launcher from" + launcher);
						
					}
					else if (obj instanceof Missile && launcher == null) {
						this.missile = (Missile) obj;
						if (!this.warController.retrieveMissileDestructors().isEmpty()) {
							this.warController.destructMissile(warController.retrieveMissileDestructors().iterator().next(), missile);
							System.err.println(name_server + ": Attempting to destruct " + missile);
						}
					}
				}
				
				vec.clear();
				missile = null;
				launcher = null;
				missileDestructor = null;
				
				System.out.println(name_server + ": Ready to recieve requests");
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {

		}
	}
}
