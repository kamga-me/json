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
public abstract class JSONReader<Str extends CharSequence, CC extends JSONReader<Str,?>> extends JSONStreamReader<Str,CC> {

	protected ICharReader charRdr;

	protected JSONReader(ICharReader charRdr, int strBufferInitCapacity, final short GREGORIAN_CUTOVER_YEAR) {
		super(strBufferInitCapacity, GREGORIAN_CUTOVER_YEAR);
		this.charRdr = charRdr;
	}

	protected JSONReader(ICharReader charRdr, int strBufferInitCapacity) {
		this(charRdr, strBufferInitCapacity, LiteralAccumulator.DEFAULT_GREGORIAN_CUTOVER_YEAR);
	}
	
	/**
	* {@inheritDoc}
	*/
	protected final boolean mayReadSupplCps() {
		return charRdr.mayReadSupplCps();
	}
	/**
	* {@inheritDoc}
	*/
	protected final byte asciiCharByteLen() {
		return charRdr.asciiCharByteLen();
	}
	/**
	* {@inheritDoc}
	*/
	protected final boolean readChar() {
		ch = charRdr.readChar();
		if (ch < 0) return false;
		offset += (ch >>> 24);
		ch &= 0xFFFFFF;
		return true;
	}
	

}