package json.stream;

import string.ICharSequence;
import string.CharBuffer;
import string.String;
import string.TemplateStringBuffer; //since 2018-04-24
import core.document.util.StrBufferToStrHelper; //since 2018-04-24

import core.IValueTypes;
import core.data.type.IValueTypesExt;
import core.Value;
import core.util.LiteralAccumulator;

import static core.util.CheckHexDigitUtil.checkHexDigit;
import static text.unicode.ICharSurrogates.*;
import static text.unicode.ICodePointConstants.MAX_CODE_POINT;
import static core.collection.util.ArrayCapacityIncrUtil.incrCapacityByHalf;

import static json.stream.JSONSreamEventTypes.*;

import core.io.IByteIterator;
import core.io.encoding.IStringDecoder;
import core.io.encoding.IStringDecoderProvider;
import java.io.InputStream;

import core.io.encoding.Encodings;

import core.logging.Logger;
import static core.util.GetInstanceHelper.*;

import static core.document.util.StrBufferToStrHelperAdapter.*; //since 2018-04-24

/**
*
* @author Marc E. KAMGA
* @version 1.0 
* @copyright Marc E. KAMGA
*/
public class JSONInputStreamReaderImpl extends JSONInputStreamReader<String,JSONInputStreamReaderImpl> { //since 2018-04-24

	StrBufferToStrHelper<? extends String, ? extends String, ? extends String> strBuf2StrHelper;//boolean dontAttemptToCreateCStrings; //NOTE: replaced dontAttemptToCreateCStrings on 2018-04-24

	
	/**
	* Constructor.<br>
	*/
	public JSONInputStreamReaderImpl(final IByteIterator inputStreamByteIter, final IStringDecoder strDecoder, final int strBufferInitCapacity, final StrBufferToStrHelper<? extends String, ? extends String, ? extends String> strBuf2StrHelper, final short GREGORIAN_CUTOVER_YEAR) {
		super(inputStreamByteIter, strDecoder, strBufferInitCapacity, GREGORIAN_CUTOVER_YEAR);
		this.strBuf2StrHelper = strBuf2StrHelper == null ? BASIC_CONV_2_STR : strBuf2StrHelper; //this.dontAttemptToCreateCStrings = dontAttemptToCreateCStrings;
	}
	/**
	* Constructor.<br>
	*/
	public JSONInputStreamReaderImpl(final IByteIterator inputStreamByteIter, final IStringDecoder strDecoder, final int strBufferInitCapacity, final short GREGORIAN_CUTOVER_YEAR) {
		this(inputStreamByteIter, strDecoder, strBufferInitCapacity, null, GREGORIAN_CUTOVER_YEAR);
	}
	/**
	* Constructor.<br>
	*/
	public JSONInputStreamReaderImpl(final IByteIterator inputStreamByteIter, final int strBufferInitCapacity, final short GREGORIAN_CUTOVER_YEAR) {
		this(inputStreamByteIter, null/*strDecoder*/, strBufferInitCapacity, GREGORIAN_CUTOVER_YEAR);
	}
	/**
	* Constructor.<br>
	*/
	public JSONInputStreamReaderImpl(final IByteIterator inputStreamByteIter, final IStringDecoder strDecoder, final int strBufferInitCapacity, final StrBufferToStrHelper<? extends String, ? extends String, ? extends String> strBuf2StrHelper) {
		super(inputStreamByteIter, strDecoder, strBufferInitCapacity);
		this.strBuf2StrHelper = strBuf2StrHelper == null ? BASIC_CONV_2_STR : strBuf2StrHelper; //this.dontAttemptToCreateCStrings = dontAttemptToCreateCStrings;
	}
	/**
	* Constructor.<br>
	*/
	public JSONInputStreamReaderImpl(final IByteIterator inputStreamByteIter, final IStringDecoder strDecoder, final int strBufferInitCapacity) {
		this(inputStreamByteIter, strDecoder, strBufferInitCapacity, null);
	}
	/**
	* Constructor.<br>
	*/
	public JSONInputStreamReaderImpl(final IByteIterator inputStreamByteIter, final int strBufferInitCapacity) {
		this(inputStreamByteIter, null/*strDecoder*/, strBufferInitCapacity);
	}
	
	protected final StrBufferToStrHelper<? extends String, ? extends String, ? extends String> _getToStrHelper() { //since 2018-04-24
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
		return super.toStr(strBuff); //strBuff.ToString(!dontAttemptToCreateCStrings); //NOTE: changed from strBuff.ToString(!dontAttemptToCreateCStrings) on 2018-04-24
	}

}