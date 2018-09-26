package json.stream;

import string.ICharSequence;
import string.CharBuffer;
import string.String;

import core.IValueTypes;
import core.data.type.IValueTypesExt;

import core.io.encoding.IByteEncrypter;
import core.io.encoding.IStringEncoder;
import core.io.encoding.IStringEncoderProvider;

import core.util.SysPropUtil;
import core.logging.Logger;
import static core.util.GetInstanceHelper.*;

import core.io.AppendableByteBuffer;
import static core.io.AppendableByteBufferInternals.*;
import static core.io.encoding.Encodings.*;

import core.Value;


/**
* Base class for providing support for JSON stream writers.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public abstract class JSONWriter extends JSONStreamWriter {
	
	protected static final IStringEncoderProvider STRING_ENCODER_PRVDR = getInstance("core.io.encoding.stringEncoder.provider.factory.class", "core.io.encoding.stringEncoder.provider.factory.staticMethod", core.io.encoding.BDBStringEncoderSupplier.INSTANCE/*defaultInst*/, true/*namesAreSysPropNames*/, getLogger()/*ERROR_LOGGER*/);
	
	protected IStringEncoder stringEncoder;
	protected AppendableByteBuffer byteBuffer;
	protected int autoflushSize;

	/**
	* Constructor.<br>
	*/
	protected JSONWriter(int bufferInitialCapacity, int autoflushSize, IStringEncoder stringEncoder) {
		super();
		byteBuffer = new AppendableByteBuffer(bufferInitialCapacity);
		this.autoflushSize = autoflushSize;
		this.stringEncoder = stringEncoder;
	}
	/**
	* Constructor.<br>
	*/
	protected JSONWriter(int bufferInitialCapacity, int autoflushSize) {
		this(bufferInitialCapacity, autoflushSize, STRING_ENCODER_PRVDR.get(UTF_8));
	}
	/**
	* Constructor.<br>
	*/
	protected JSONWriter(int bufferInitialCapacity) {
		this(bufferInitialCapacity, -1);
	}
	/**
	* Constructor.<br>
	*/
	protected JSONWriter() {
		this(4096);
	}
	
	
}