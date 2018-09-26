package json.stream;

import string.ICharSequence;
import string.CharBuffer;
import string.String;

import core.IValueTypes;
import core.data.type.IValueTypesExt;
import static core.util.GetInstanceHelper.*;

//import core.io.encoding.IByteEncrypter;
import core.io.encoding.IStringEncoder;
import core.io.bdb.IStringEncoderProvider;

import core.util.SysPropUtil;
import core.logging.Logger;

import core.Value;

/**
* Base class for providing support for JSON buffers that buffer JSON documents in a stream fashion.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public abstract class JSONBuffer extends JSONStreamWriter {
	
	protected CharBuffer charBuffer;

	/**
	* Constructor.<br>
	*/
	protected JSONBuffer(int initialCapacity) {
		super();
		charBuffer = new CharBuffer(initialCapacity);
	}
	/**
	* Constructor.<br>
	*/
	protected JSONBuffer() {
		this(4096);
	}
	
	/**
	* returns the character buffer that holds the written JSON.<br>
	*/
	public final CharBuffer charBuffer() {
		return charBuffer;
	}
	
	
}