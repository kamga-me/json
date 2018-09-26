package json.stream;

import string.ICharSequence;
import string.CharBuffer;
import string.String;

import core.IValueTypes;
import core.data.type.IValueTypesExt;

import core.util.SysPropUtil;
import core.logging.Logger;
import static core.util.GetInstanceHelper.*;

/**
* Base class for providing support for JSON stream writers.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public abstract class JSONStreamWriter implements IJSONStreamWriter, core.IUsualTinyNumbers, core.ITrinaryValues {
	
	protected static Logger getLogger() {
		return null;
	}

	/**
	* Constructor.<br>
	*/
	/*protected */JSONStreamWriter() {
		super();
	}
	

	/**
	* {@inheritDoc}
	* @return <code>0x80000000</code>
	*/
	public final int declare(String name, byte valueType, byte sharedStringType) { //since 2018-03-09
		return 0x80000000;
	}
	/**
	* {@inheritDoc}
	* @return <code>0x80000000</code>
	*/
	public final int declare(String name, byte valueType) { //since 2018-03-09
		return 0x80000000;
	}
	/**
	* {@inheritDoc}
	* @return <code>0x80000000</code>
	*/
	public final int declare(String name, boolean cstringField, String[] enumValues, int enumValsOff, int enumValsCount, final boolean unbounded) {
		return 0x80000000;
	}
	/**
	* {@inheritDoc}
	* @return <code>0x80000000</code>
	*/
	@Override
	public final int declare(String name, boolean cstringField, String[] enumValues, final boolean unbounded) {
		return 0x80000000;
	}
	
}