/**
 * 
 */
package de.yadrone.apps.controlcenter.plugins.recorder;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.yadrone.apps.controlcenter.ICCPlugin;
import de.yadrone.base.IARDrone;
import de.yadrone.base.recorder.Recorder;

/**
 * @author Formicarufa (Tomas Prochazka)
 *14. 3. 2016
 */
public class RecorderPanel extends JPanel implements ICCPlugin {
	private JTextField textField;
	private IARDrone drone;
	boolean recording = false;
	private Recorder recorder;
	private JLabel labelError;
	private FileOutputStream navdataout;
	private FileOutputStream commandsout;
	private JButton buttonRecord;

	/**
	 * Create the panel.
	 */
	public RecorderPanel() {
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblContent = new JLabel("Recording name");
		add(lblContent);
		
		textField = new JTextField();
		add(textField);
		textField.setColumns(10);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Package to .zip");
		add(chckbxNewCheckBox);
		
		buttonRecord = new JButton("Start recording");
		buttonRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				labelError.setText("");
				if (!recording) {
					startRecording();
				} else {
					stopRecording();
				}
			}
		});
		add(buttonRecord);
		
		labelError = new JLabel("");
		add(labelError);

	}

	/**
	 * 
	 */
	protected void startRecording() {
		String dir = textField.getText();
		if (dir.isEmpty()) {
			labelError.setText("Please, insert a name.");
			return;		
		}
		File f = new File(dir);
		if (f.exists()) {
			labelError.setText("Sorry. Select a unique name.");
			return;			
		}
		f.mkdir();
		if (!f.exists()) {
			labelError.setText("Sorry. Unable to create a directory.");
		}
		String navdataFile = dir + "/navdata.tsv";
		String commandsFile = dir + "/commands.tsv";
		try {
			navdataout = new FileOutputStream(navdataFile);
			commandsout = new FileOutputStream(commandsFile);
		} catch (IOException e) {
			labelError.setText("Sorry. Can not open files for writing.");
			e.printStackTrace();
			return;
		}
		recording=true;
		buttonRecord.setText("Stop recording.");
		recorder.startRecordingNavdata(new PrintStream(navdataout));
		recorder.startRecordingCommands(new PrintStream(commandsout));
		
	}

	/**
	 * 
	 */
	protected void stopRecording() {
		recorder.stopRecordingCommands();
		recorder.stopRecordingNavdata();
		recording=false;
		try {
			navdataout.close();
			commandsout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		buttonRecord .setText("Start recording");
		
	}

	@Override
	public void activate(IARDrone drone) {
		this.drone = drone;
		recorder = new Recorder(drone);
		
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Navdata recorder";
	}

	/* (non-Javadoc)
	 * @see de.yadrone.apps.controlcenter.ICCPlugin#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Saves navdata and commands to a text file.";
	}

	/* (non-Javadoc)
	 * @see de.yadrone.apps.controlcenter.ICCPlugin#isVisual()
	 */
	@Override
	public boolean isVisual() {
		return true;
	}

	/* (non-Javadoc)
	 * @see de.yadrone.apps.controlcenter.ICCPlugin#getScreenSize()
	 */
	@Override
	public Dimension getScreenSize() {
		return new Dimension(330, 250);
	}

	/* (non-Javadoc)
	 * @see de.yadrone.apps.controlcenter.ICCPlugin#getScreenLocation()
	 */
	@Override
	public Point getScreenLocation() {
		return new Point(330, 390);
	}

	/* (non-Javadoc)
	 * @see de.yadrone.apps.controlcenter.ICCPlugin#getPanel()
	 */
	@Override
	public JPanel getPanel() {
		return this;
	}
}
