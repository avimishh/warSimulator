package Server;

import java.io.*;
import java.net.*;
import java.util.*;
import Entities.*;
import Controller.*;

public class Client extends Thread {
	private final String HOST = "localhost";
	private final int PORT = 7000;
	private final String CLIENT_NAME = "Client";
	
    private static Socket socket = null;
    private static ObjectInputStream fromNetInputStream;
    private static ObjectOutputStream toNetOutputStream;
    private Queue<Object> vec = new LinkedList<>();

    public Client() throws IOException {
    }
    
    public void createLauncherAndMissiles(Launcher launcher, List<Missile> missiles)
	    throws IOException {
    	this.vec.add(launcher);
    	for(Missile missile: missiles)
    		this.vec.add(missile);
    	toNetOutputStream.writeObject(vec);
    	this.vec.clear();
    	toNetOutputStream.reset();
    }
    
    public void createLauncher(Launcher launcher) throws IOException {
    	this.vec.add(launcher);
    	toNetOutputStream.writeObject(vec);
    	this.vec.clear();
    	toNetOutputStream.reset();
    }
    
    public void createMissileDestroyer(MissileDestructor missileDestructor) throws IOException {
    	this.vec.add(missileDestructor);
    	toNetOutputStream.writeObject(vec);
    	this.vec.clear();
    	toNetOutputStream.reset();
    }
    
    public void destructMissile(Missile missile) throws IOException {
    	this.vec.add(missile);
    	toNetOutputStream.writeObject(vec);
    	this.vec.clear();
    	toNetOutputStream.reset();
    }
    
    public void Close() throws IOException {
    	toNetOutputStream.close();
    	fromNetInputStream.close();
    	socket.close();
    }
    
    @Override
    public void run() {
    	try {
    		socket = new Socket(HOST, PORT);
    	} catch (UnknownHostException e1) {
    		e1.printStackTrace();
    	} catch (IOException e1) {
    		e1.printStackTrace();
    	}
    	System.out.println(CLIENT_NAME + ": " + new Date() + " Connected to server at "
    			+ socket.getLocalAddress() + ":" + socket.getLocalPort());

    	try {
    		toNetOutputStream = new ObjectOutputStream(socket.getOutputStream());
    		
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	try {
    		fromNetInputStream = new ObjectInputStream(socket.getInputStream());
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
}
