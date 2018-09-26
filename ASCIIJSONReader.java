package json.stream;

import string.ICharSequence;
import string.CharBuffer;
import string.String;

import core.IValueTypes;
import core.data.type.IValueTypesExt;
import core.Value;
import core.util.LiteralAccumulator;

import static core.collection.util.ArrayCapacityIncrUtil.incrCapacityByHalf;

import static json.stream.JSONSreamEventTypes.*;

import static time.ITimeZoneOffsetConstants.NULL_TZ_MINUTES_OFFSET;

import java.io.InputStream;
import core.io.ICharReader;

/**
* Almost concrete class for providing support for JSON stream readers backed.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public abstract class ASCIIJSONReader<Str extends CharSequence, CC extends ASCIIJSONReader<Str,?>> extends JSONStreamReader<Str,CC> {

	protected InputStream is;
	protected boolean iso88591CharsAccepted;

	protected ASCIIJSONReader(InputStream is, int strBufferInitCapacity, final short GREGORIAN_CUTOVER_YEAR, final boolean iso88591CharsAccepted) {
		super(strBufferInitCapacity, GREGORIAN_CUTOVER_YEAR);
		this.is = is;
		this.iso88591CharsAccepted = iso88591CharsAccepted;
	}

	protected ASCIIJSONReader(InputStream is, int strBufferInitCapacity, final boolean iso88591CharsAccepted) {
		this(is, strBufferInitCapacity, LiteralAccumulator.DEFAULT_GREGORIAN_CUTOVER_YEAR, iso88591CharsAccepted);
	}

	protected ASCIIJSONReader(InputStream is, int strBufferInitCapacity) {
		this(is, strBufferInitCapacity, false/*iso88591CharsAccepted*/);
	}
	
	/**
	* {@inheritDoc}
	*/
	protected final boolean mayReadSupplCps() {
		return false;
	}
	/**
	* {@inheritDoc}
	*/
	protected final byte asciiCharByteLen() {
		return 1;
	}
	/**
	* {@inheritDoc}
	*/
	protected final boolean readChar() {
		try 
		{
			ch = is.read();
		}
		catch(java.io.IOException ioe)
		{
			throw new JSONStreamException(ioe.getMessage() + core.IOperatingSystemConstants.LINE_TERMINATOR + 
			"ASCIIJSONReader::readChar-1: I/O error"
			, ioe 
			);
		}
		if (ch < 0) return false;
		else if (!iso88591CharsAccepted && ch > 0x7F) {
			throw new JSONStreamException(
			"ASCIIJSONReader::readChar-2: not a valid ASCII stream"
			);
		}
		offset++;
		return true;
	}
	

}