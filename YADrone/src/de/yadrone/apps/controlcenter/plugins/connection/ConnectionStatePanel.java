package de.yadrone.apps.controlcenter.plugins.connection;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.google.common.util.concurrent.Service.State;

import de.yadrone.apps.controlcenter.ICCPlugin;
import de.yadrone.base.IARDrone;
import de.yadrone.base.connection.ConnectionState;
import de.yadrone.base.connection.ConnectionStateListener;
import de.yadrone.base.exception.ARDroneException;
import de.yadrone.base.exception.CommandException;
import de.yadrone.base.exception.ConfigurationException;
import de.yadrone.base.exception.IExceptionListener;
import de.yadrone.base.exception.NavDataException;
import de.yadrone.base.exception.VideoException;

public class ConnectionStatePanel extends JPanel implements ICCPlugin
{
	private IARDrone drone;
	
	private static Icon greenIcon;
	private static Icon redIcon;
	
	private JLabel commandLabel;
	private JLabel configurationLabel;
	private JLabel videoLabel;
	private JLabel navdataLabel;
	
	private IExceptionListener exceptionListener;
	
	public ConnectionStatePanel()
	{
		super(new GridBagLayout());
		
		greenIcon = new ImageIcon(this.getClass().getResource("dot_green.png"));
		redIcon = new ImageIcon(this.getClass().getResource("dot_red.png"));
		
		commandLabel = new JLabel("Command Channel", greenIcon, SwingConstants.LEFT);
		configurationLabel = new JLabel("Configuration Channel", greenIcon, SwingConstants.LEFT);
		navdataLabel = new JLabel("Navdata Channel", greenIcon, SwingConstants.LEFT);
		videoLabel = new JLabel("Video Channel", greenIcon, SwingConstants.LEFT);
		
		exceptionListener = new IExceptionListener() {
			public void exeptionOccurred(ARDroneException exc)
			{
				if (exc instanceof ConfigurationException)
				{
					configurationLabel.setIcon(redIcon);
					configurationLabel.setToolTipText(exc+"");
				}
				else if (exc instanceof CommandException)
				{
					commandLabel.setIcon(redIcon);
					commandLabel.setToolTipText(exc+"");
				}
				else if (exc instanceof NavDataException)
				{
					navdataLabel.setIcon(redIcon);
					navdataLabel.setToolTipText(exc+"");
				}
				else if (exc instanceof VideoException)
				{
					videoLabel.setIcon(redIcon);
					videoLabel.setToolTipText(exc+"");
				}
			}
		};

		
		add(commandLabel, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0));
		add(navdataLabel, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0));
		//add(configurationLabel, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0));
		add(videoLabel, new GridBagConstraints(0, 3, 1, 1, 1, 1, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0));
	}
		
	public void activate(IARDrone drone)
	{
		this.drone = drone;
		drone.addExceptionListener(exceptionListener);
		drone.getNavDataManager().addConnectionStateListener(navdataListener);
		drone.getVideoManager().addConnectionStateListener(videoListener);
		drone.getCommandManager().addConnectionStateListener(commandsListener);
	}
	
	public void deactivate()
	{
		drone.removeExceptionListener(exceptionListener);
	}

	public String getTitle()
	{
		return "Connection State";
	}
	
	public String getDescription()
	{
		return "Shows the status of the current connections to the drone.";
	}

	public boolean isVisual()
	{
		return true;
	}

	public Dimension getScreenSize()
	{
		return new Dimension(150, 100);
	}
	
	public Point getScreenLocation()
	{
		return new Point(0, 330);
	}
	
	public JPanel getPanel()
	{
		return this;
	}
	private void setStateIcon(JLabel label, ConnectionState state) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				String connectedStr = "Connected.";
				if (state==ConnectionState.Connected) {
					label.setIcon(greenIcon);
					label.setToolTipText(connectedStr);
				} else {
					label.setIcon(redIcon);
					if(connectedStr.equals(label.getToolTipText())) {
						label.setToolTipText("Disconnected.");
					}
				}
			}
		});

	}
	ConnectionStateListener navdataListener = new ConnectionStateListener() {
		
		@Override
		public void stateChanged(ConnectionState newState) {
			setStateIcon(navdataLabel, newState);
			
		}
	};
	ConnectionStateListener commandsListener = new ConnectionStateListener() {
		
		@Override
		public void stateChanged(ConnectionState newState) {
			setStateIcon(commandLabel, newState);
			
		}
	};
	ConnectionStateListener videoListener = new ConnectionStateListener() {
		
		@Override
		public void stateChanged(ConnectionState newState) {
			setStateIcon(videoLabel, newState);
			
		}
	};
	
}
