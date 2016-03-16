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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

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
	/**
	 * 
	 */
	private static final long serialVersionUID = -2069639302901417264L;
	private JTextField textField;
	private IARDrone drone;
	boolean recording = false;
	private Recorder recorder;
	private JLabel labelError;
	private FileOutputStream navdataout;
	private FileOutputStream commandsout;
	private JButton buttonRecord;
	private JCheckBox checkBoxZip;
	private String recordingName;

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
		
		checkBoxZip = new JCheckBox("Package to .zip");
		add(checkBoxZip);
		
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
		recordingName = textField.getText();
		if (recordingName.isEmpty()) {
			labelError.setText("Please, insert a name.");
			return;		
		}
		File f = new File(recordingName);
		if (f.exists()) {
			labelError.setText("Sorry. Select a unique name.");
			return;			
		}
		f.mkdir();
		if (!f.exists()) {
			labelError.setText("Sorry. Unable to create a directory.");
		}
		String navdataFile = recordingName + "/navdata.tsv";
		String commandsFile = recordingName + "/commands.tsv";
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
			return;
		}
		copyContentsInfoXml();
		if (checkBoxZip.isSelected()) {
			try {
				ZipFolder.pack(recordingName, recordingName+".zip");
			} catch (IOException e) {
				labelError.setText("Sorry. Unable to save .zip.");
				e.printStackTrace();
			}
		}
		buttonRecord .setText("Start recording");
		
	}

	/**
	 * 
	 */
	private void copyContentsInfoXml() {
		InputStream filesInfo = getClass().getResourceAsStream("/de/yadrone/base/recorder/description.xml");
		if (filesInfo==null) {
			labelError.setText("Sorry. Content description file missing");
		} else  {
			try {
				Files.copy(filesInfo, Paths.get(recordingName + "/description.xml"));
			}  catch (IOException e) {
				labelError.setText("Sorry. Unable to add description.");
				e.printStackTrace();
			}
		}
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
