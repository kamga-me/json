package json;

import java.nio.MappedByteBuffer;
import static core.io.encoding.ByteOrderMarkUtil.*;

import java.io.InputStream;

/**
* Package private singleton provider class for byte iterators.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public final class ByteIteratorFactory extends core.io.ByteIteratorFactory { //NOTE: made public on 2018-04-24 for the class to be usable in package json.stream

	/**
	* The single instance of the class.<br>
	*/
	public static final ByteIteratorFactory INSTANCE = new ByteIteratorFactory();

	private ByteIteratorFactory() {}

	static class ByteArrayByteIterator extends core.io.ByteArrayByteIterator {
		
		/**
		* Constructor.<br>
		*/
		public ByteArrayByteIterator(final byte[] array, final int off, final int len) {
			super(array, off, len);
			this.array = array;
		}
		/**
		* Constructor.<br>
		*/
		public ByteArrayByteIterator(final int start, final int end, final byte[] array) {
			super(array, start, end);
		}
		/**
		* {@inheritDoc}
		*/
		protected final JSONIOException new_xcptn(final CharSequence errMsg, final boolean eof) {
			throw new JSONIOException(eof ? "EOF - " + errMsg.toString() : errMsg.toString());
		}
		
	}
	
	static class MappedFileByteIterator extends core.io.MappedFileByteIterator {
		
		/**
		* Constructor.<br>
		*/
		public MappedFileByteIterator(final MappedByteBuffer mappedFile) {
			super(mappedFile);
		}
		/**
		* Constructor.<br>
		* @param skipBOMIfAny flag telling if it must be checked if there is BOM for skip - ignored if argument <code>offset</code> is not equal to <code>0</code>; in case the check indicates the presence of a BOM, that BOM is skipped and corresponding encoding set.<br>
		*/
		public MappedFileByteIterator(final java.lang.String filePath, final long offset, final int size, final boolean skipBOMIfAny) {
			super(filePath, offset, size, skipBOMIfAny);
		}
		/**
		* Constructor.<br>
		* @param skipBOMIfAny flag telling if it must be checked if there is BOM for skip - in case the check indicates the presence of a BOM, that BOM is skipped and corresponding encoding set.<br>
		*/
		public MappedFileByteIterator(final java.lang.String filePath, final boolean skipBOMIfAny) {
			super(filePath, skipBOMIfAny);
		}
		/**
		* {@inheritDoc}
		*/
		protected final JSONIOException new_xcptn(final CharSequence errMsg, final boolean eof) {
			throw new JSONIOException(eof ? "EOF - " + errMsg.toString() : errMsg.toString());
		}	
		
		
	}
	
	static final class InputStreamByteIterator extends core.io.InputStreamByteIterator {
		
		/**
		* Constructor.<br>
		* @param is the input stream which is to back this the byte iterator
		* @param currentOffetPosition the known current offset position within <code>is</code>
		*/
		public InputStreamByteIterator(final InputStream is, final int currentOffetPosition) {
			super(is, currentOffetPosition);
		}
		
		/**
		* Constructor.<br>
		*/
		public InputStreamByteIterator(final InputStream is) {
			super(is);
		}
		/**
		* {@inheritDoc}
		*/
		protected final JSONIOException new_xcptn(final CharSequence errMsg, final boolean eof) {
			throw new JSONIOException(eof ? "EOF - " + errMsg.toString() : errMsg.toString());
		}
		
	}
	
	/**
	* {@inheritDoc}
	*/
	public final InputStreamByteIterator newInputStreamByteIterator(final InputStream is, final int currentOffetPosition) {
		return new InputStreamByteIterator(is, currentOffetPosition);
	}
	/**
	* {@inheritDoc}
	*/
	public final InputStreamByteIterator newInputStreamByteIterator(final InputStream is) {
		return new InputStreamByteIterator(is);
	}
	/**
	* {@inheritDoc}
	*/
	public final MappedFileByteIterator newMappedFileByteIterator(final java.lang.String filePath, final boolean skipBOMIfAny) {
		return new MappedFileByteIterator(filePath, skipBOMIfAny);
	}
	/**
	* {@inheritDoc}
	*/
	public final ByteArrayByteIterator newByteArrayByteIterator(final byte[] array, final int off, final int len) {
		return new ByteArrayByteIterator(array, off, len);
	}
	/**
	* {@inheritDoc}
	*/
	public final ByteArrayByteIterator newByteArrayByteIterator(final int start, final int end, final byte[] array) {
		return new ByteArrayByteIterator(start, end, array);
	}

}