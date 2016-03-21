/**
 * 
 */
package de.yadrone.base.recorder;

import java.io.PrintStream;

import de.yadrone.base.navdata.common.CommonNavdata;
import de.yadrone.base.navdata.common.CommonNavdataListener;

/**
 * @author Formicarufa (Tomas Prochazka)
 *12. 3. 2016
 */
class NavdataRecorder implements CommonNavdataListener {

	PrintStream stream;
	private char separator;
	
	/**
	 * @param stream
	 */
	public NavdataRecorder(PrintStream stream, char separator) {
		super();
		this.stream = stream;
		this.separator = separator;
		
	}

	/* (non-Javadoc)
	 * @see de.yadrone.base.navdata.common.CommonNavdataListener#navdataReceived(de.yadrone.base.navdata.common.CommonNavdata)
	 */
	@Override
	public void navdataReceived(CommonNavdata data) {
		stream.println(data.toString(separator));
	}

}
