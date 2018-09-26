package json.stream;

import string.ICharSequence;
import string.String;

import core.io.IByteIterator;
import core.io.StreamableInput;
import core.io.CharInputStreamUtil;
import core.io.encoding.IStringDecoderProvider;
import core.document.IDocumentLoadContext;

import json.ByteIteratorFactory;

/**
* Utility class with static factory methods that are handy in creating JSON stream readers for reading JSON documents for JSON text files, be they located on local file system or accessible remotely through the web.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copytight Marc E. KAMGA
*/
public final class JSONStreamReaderFactory { //since 2018-04-24


	/**
	* Creates a new JSON stream reader for reading from the JSON text file/resource whose system identifier is supplied.<br>
	* @param systemId the system identifier of the JSON text file/resource from which the reader must read
	* @param loadContext the load context within which the created JSON stream reader is to be used - can/may be null
	*/
	public static final JSONStreamReader<? extends String, ?> newJSONSreamReader(final String systemId, final IDocumentLoadContext loadContext) {
		IStringDecoderProvider strDecoderPrvdr = loadContext == null ? null : loadContext.getStringDecoderProvider();
		final boolean preferFastRead = loadContext != null && loadContext.fastReadEvenIfAdditionalMemoryCost();
		String baseFileLocation = loadContext != null ? loadContext.getBaseFileLocation() : String.EMPTY;
		if (baseFileLocation == null) {
			baseFileLocation = String.EMPTY;
		}
		StreamableInput streamableInputForReuse = loadContext == null ? null : loadContext.getStreamableInputStructForReuse();
		if (streamableInputForReuse == null) {
			streamableInputForReuse = new StreamableInput();
		}
		int tokenBufferInitialCapacity = loadContext == null ? 40 : loadContext.getTokenBufferInitialCapacity();
		if (tokenBufferInitialCapacity < 1) {
			tokenBufferInitialCapacity = 40;
		}
		CharInputStreamUtil.openStream(systemId, preferFastRead, strDecoderPrvdr, ByteIteratorFactory.INSTANCE/*byteIterFactory*/, baseFileLocation, streamableInputForReuse/*output*/);
		return new JSONInputStreamReaderImpl(streamableInputForReuse.byteIterator/*inputStreamByteIter*/, streamableInputForReuse.stringDecoder, tokenBufferInitialCapacity, (loadContext != null ? null : loadContext.getStrBufferToStrHelper())/*strBuf2StrHelper*/);
	}
	/**
	* Creates a new JSON stream reader for reading from the JSON text file/resource whose system identifier is supplied.<br>
	* @param systemId the system identifier of the JSON text file/resource from which the reader must read
	* @param loadContext the load context within which the created JSON stream reader is to be used - can/may be null
	*/
	public static final JSONStreamReader<? extends String, ?> newJSONSreamReader(final java.lang.String systemId, final IDocumentLoadContext loadContext) {
		IStringDecoderProvider strDecoderPrvdr = loadContext == null ? null : loadContext.getStringDecoderProvider();
		final boolean preferFastRead = loadContext != null && loadContext.fastReadEvenIfAdditionalMemoryCost();
		String baseFileLocation = loadContext != null ? loadContext.getBaseFileLocation() : String.EMPTY;
		if (baseFileLocation == null) {
			baseFileLocation = String.EMPTY;
		}
		StreamableInput streamableInputForReuse = loadContext == null ? null : loadContext.getStreamableInputStructForReuse();
		if (streamableInputForReuse == null) {
			streamableInputForReuse = new StreamableInput();
		}
		int tokenBufferInitialCapacity = loadContext == null ? 40 : loadContext.getTokenBufferInitialCapacity();
		if (tokenBufferInitialCapacity < 1) {
			tokenBufferInitialCapacity = 40;
		}
		CharInputStreamUtil.openStream(systemId, preferFastRead, strDecoderPrvdr, ByteIteratorFactory.INSTANCE/*byteIterFactory*/, baseFileLocation, streamableInputForReuse/*output*/);
		return new JSONInputStreamReaderImpl(streamableInputForReuse.byteIterator/*inputStreamByteIter*/, streamableInputForReuse.stringDecoder, tokenBufferInitialCapacity, (loadContext == null ? null : loadContext.getStrBufferToStrHelper())/*strBuf2StrHelper*/);
	}
	/**
	* Creates a new JSON stream reader, given the streamable input.<br>
	*/
	public static final JSONStreamReader<? extends String, ?> newJSONSreamReader(final StreamableInput input, final IDocumentLoadContext loadContext) {
		int tokenBufferInitialCapacity = loadContext == null ? 40 : loadContext.getTokenBufferInitialCapacity();
		if (tokenBufferInitialCapacity < 1) {
			tokenBufferInitialCapacity = 40;
		}
		return new JSONInputStreamReaderImpl(input.byteIterator/*inputStreamByteIter*/, input.stringDecoder, tokenBufferInitialCapacity, (loadContext != null ? null : loadContext.getStrBufferToStrHelper())/*strBuf2StrHelper*/);
	}
	
	
	/**
	* Creates a new JSON stream reader for reading the JSON document using the supplied character reader.<br>
	* @param charRdr the character reader to use in reading the characters of the JSON data
	* @param loadContext the load context within which the created JSON stream reader is to be used - can/may be null
	*/
	public static final JSONStreamReader<? extends String, ?> newJSONSreamReader(final core.io.ICharReader charRdr, final IDocumentLoadContext loadContext) {
		int tokenBufferInitialCapacity = loadContext == null ? 40 : loadContext.getTokenBufferInitialCapacity();
		if (tokenBufferInitialCapacity < 1) {
			tokenBufferInitialCapacity = 40;
		}
		return new JSONStreamReaderImpl(charRdr, tokenBufferInitialCapacity,  (loadContext == null ? null : loadContext.getStrBufferToStrHelper())/*strBuf2StrHelper*/);
	}
	
}