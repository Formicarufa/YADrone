package de.yadrone.apps.controlcenter;

import de.yadrone.base.ARDrone;


public class YADroneControlCenter
{	
	private ARDrone ardrone=null;
	
	public YADroneControlCenter(){
		initialize();
	}
	
	private void initialize(){
		try
		{
			ardrone = new ARDrone();
			System.out.println("Connect drone controller");
			ardrone.start();
			
			new CCFrame(ardrone);
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
			
			if (ardrone != null)
				ardrone.stop();
			//Do not disconnect the drone before actually sending the stop commands...
				try {
					Thread.sleep(80);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ardrone.disconnect();
			System.exit(-1);
		}
	}
		
	public static void main(String args[]){
		new YADroneControlCenter();
	}
}