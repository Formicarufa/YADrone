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
	
	/**
	 * @param stream
	 */
	public NavdataRecorder(PrintStream stream) {
		super();
		this.stream = stream;
	}

	/* (non-Javadoc)
	 * @see de.yadrone.base.navdata.common.CommonNavdataListener#navdataReceived(de.yadrone.base.navdata.common.CommonNavdata)
	 */
	@Override
	public void navdataReceived(CommonNavdata data) {
		stream.println(data.toString('\t'));
	}

}
