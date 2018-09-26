package json.stream;

import string.ICharSequence;
import string.CharBuffer;
import string.String;
import string.TemplateStringBuffer; //since 2018-04-13
import core.document.util.StrBufferToStrHelper; //since 2018-04-13

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

/**
* Base class for providing support for JSON stream readers backed by input streams.br>
*
* @author Marc E. KAMGA
* @version 1.0 
* @copyright Marc E. KAMGA
*/
public abstract class JSONInputStreamReader<Str extends CharSequence, CC extends JSONInputStreamReader<Str,?>> extends JSONStreamReader<Str,CC> {

	protected static final IStringDecoderProvider STRING_DECODER_PRVDR = getInstance("json.stream.stringDecoder.provider.factory.class", "json.stream.stringDecoder.provider.factory.staticMethod", core.io.encoding.BDBStringDecoderProvider.INSTANCE/*defaultInst*/, true/*namesAreSysPropNames*/, getLogger()/*ERROR_LOGGER*/);
	
	protected IByteIterator inputStreamByteIter;
	protected IStringDecoder strDecoder;
	
	/**
	* Constructor.<br>
	*/
	protected JSONInputStreamReader(final IByteIterator inputStreamByteIter, final IStringDecoder strDecoder, final int strBufferInitCapacity, final short GREGORIAN_CUTOVER_YEAR) {
		super(strBufferInitCapacity, GREGORIAN_CUTOVER_YEAR);
		this.inputStreamByteIter = inputStreamByteIter;
		this.strDecoder = strDecoder == null ? STRING_DECODER_PRVDR.get(Encodings.UTF_8) : strDecoder;
	}
	/**
	* Constructor.<br>
	*/
	protected JSONInputStreamReader(final IByteIterator inputStreamByteIter, final int strBufferInitCapacity, final short GREGORIAN_CUTOVER_YEAR) {
		this(inputStreamByteIter, null/*strDecoder*/, strBufferInitCapacity, GREGORIAN_CUTOVER_YEAR);
	}
	/**
	* Constructor.<br>
	*/
	protected JSONInputStreamReader(final IByteIterator inputStreamByteIter, final IStringDecoder strDecoder, final int strBufferInitCapacity) {
		super(strBufferInitCapacity);
		this.inputStreamByteIter = inputStreamByteIter;
		this.strDecoder = strDecoder == null ? STRING_DECODER_PRVDR.get(Encodings.UTF_8) : strDecoder;
	}
	/**
	* Constructor.<br>
	*/
	protected JSONInputStreamReader(final IByteIterator inputStreamByteIter, final int strBufferInitCapacity) {
		this(inputStreamByteIter, null/*strDecoder*/, strBufferInitCapacity);
	}
	
	/**
	* {@inheritDoc}
	*/
	protected final boolean mayReadSupplCps() {
		return Encodings.get_SupplCpSupport(strDecoder.getEncodingNumber()) != Encodings.N;
	}
	/**
	* {@inheritDoc}
	*/
	protected final byte asciiCharByteLen() {
		//Question: is there any of the supported encodings for which strDecoder.getCharFixedByteFactor() does not gie proper byte length? I DO NOT THING SO!
		return strDecoder.getCharFixedByteFactor(); //1 for US-ASCII, UTF-8, all the ISO-8959-x, the windows-125x,..., 2 for the UTF-16, 4 for the UTF-32, ...
	}
	/**
	* {@inheritDoc}
	*/
	protected final boolean readChar() {
		ch = strDecoder.readCp(inputStreamByteIter);
		if (ch < 0) return false;
		offset = inputStreamByteIter.offset(); //update offset
		return true;
	}
	
}
