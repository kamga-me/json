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
import core.document.util.StrBufferToStrHelper; //since 2018-04-13
import static core.document.util.StrBufferToStrHelperAdapter.*; //since 2018-04-13

/**
* This is a concrete class for providing support for JSON stream readers backed.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public class JSONStreamReaderImpl extends JSONReader<String,JSONStreamReaderImpl> {

	protected StrBufferToStrHelper<? extends String, ? extends String, ? extends String> strBuf2StrHelper;//boolean dontAttemptToCreateCStrings; //NOTE: replaced dontAttemptToCreateCStrings on 2018-04-13

	/**
	* Constructor.<br>
	*/
	public JSONStreamReaderImpl(ICharReader charRdr, int strBufferInitCapacity, StrBufferToStrHelper<? extends String, ? extends String, ? extends String> strBuf2StrHelper/*final boolean dontAttemptToCreateCStrings*/, final short GREGORIAN_CUTOVER_YEAR) {
		super(charRdr, strBufferInitCapacity, GREGORIAN_CUTOVER_YEAR);
		this.strBuf2StrHelper = strBuf2StrHelper == null ? BASIC_CONV_2_STR : strBuf2StrHelper; //this.dontAttemptToCreateCStrings = dontAttemptToCreateCStrings;
	}
	/**
	* Constructor.<br>
	*/
	public JSONStreamReaderImpl(ICharReader charRdr, int strBufferInitCapacity, final boolean dontAttemptToCreateCStrings, final short GREGORIAN_CUTOVER_YEAR) {
		this(charRdr, strBufferInitCapacity, dontAttemptToCreateCStrings ? BASIC_CONV_2_STR_2 : BASIC_CONV_2_STR, GREGORIAN_CUTOVER_YEAR);
		//this.dontAttemptToCreateCStrings = dontAttemptToCreateCStrings;
	}
	/**
	* Constructor.<br>
	*/
	public JSONStreamReaderImpl(ICharReader charRdr, int strBufferInitCapacity, StrBufferToStrHelper<? extends String, ? extends String, ? extends String> strBuf2StrHelper/*final boolean dontAttemptToCreateCStrings*/) {
		super(charRdr, strBufferInitCapacity);
		this.strBuf2StrHelper = strBuf2StrHelper == null ? BASIC_CONV_2_STR : strBuf2StrHelper; //this.dontAttemptToCreateCStrings = dontAttemptToCreateCStrings;
	}
	/**
	* Constructor.<br>
	*/
	public JSONStreamReaderImpl(ICharReader charRdr, int strBufferInitCapacity, final boolean dontAttemptToCreateCStrings) {
		this(charRdr, strBufferInitCapacity, dontAttemptToCreateCStrings ? BASIC_CONV_2_STR_2 : BASIC_CONV_2_STR);
		//this.dontAttemptToCreateCStrings = dontAttemptToCreateCStrings;
	}
	
	/**
	* Constructor.<br>
	*/
	public JSONStreamReaderImpl(ICharReader charRdr, int strBufferInitCapacity) {
		this(charRdr, strBufferInitCapacity, false);
	}
	
	protected final StrBufferToStrHelper<? extends String, ? extends String, ? extends String> _getToStrHelper() { //since 2018-04-13
		return strBuf2StrHelper;
	}
	
	/**
	* {@inheritDoc}
	*/
	protected final boolean canConvertFieldNamesToFieldNumbers() {
		return strBuf2StrHelper.canConvertFieldNamesToFieldNumbers();
	}
	/**
	* {@inheritDoc}
	*/
	protected final void workoutPropertyName() {
		super.workoutPropertyName();
	}
	
	/**
	* {@inheritDoc}
	*/
	protected String toStr(final CharBuffer strBuff) {
		return super.toStr(strBuff); //strBuff.ToString(!dontAttemptToCreateCStrings); //NOTE: changed from strBuff.ToString(!dontAttemptToCreateCStrings) on 2018-04-13
	}

	

}