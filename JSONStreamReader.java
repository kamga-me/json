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

import core.net.IPv4;
import static time.ITimeZoneOffsetConstants.NULL_TZ_MINUTES_OFFSET;

import core.logging.Logger;
import static core.util.GetInstanceHelper.*;

/**
* Base class for providing support for JSON stream readers.<br>
*
* <br><b>TODO - VERY URGENT</b>: have a method toName and use it instead of toStr when the string value is a property key - StrBufferToStrHelper will surely help.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public abstract class JSONStreamReader<Str extends CharSequence, CC extends JSONStreamReader<Str,?>> implements IJSONStreamReader<Str>, json.IJSONContentForms, json.IJSONTokenTypes, core.ITrinaryValues, core.IUsualTinyNumbers {
	
	protected static Logger getLogger() { //since 2018-04-24
		return null;
	}
	
	public static final byte HAS_ESCAPED_BMP_CHAR = 1;
	public static final byte HAS_ESCAPED_SUPPL_CP = 2;
	
	protected byte eventType;
	
	protected transient CharBuffer strToken;
	protected transient CharBuffer innerSpaces;
	protected transient LiteralAccumulator literalAcc;
	protected transient Value literalValue;
	protected transient Str strValue;
	protected transient byte tokenType/*isLiteral*/; //no ==> is a string, yes ==> value contained in literalValue and maybe ==> value is a primitive value
	protected transient byte joinerChar;
	protected transient int lineNumber;
	protected transient int offset;
	protected transient int lineStartOffset;
//	protected transient int lastStructStartOffset;
	
	protected transient int ch;
	
//	protected transient byte b;
	
	protected byte documentForm;
	
	protected transient boolean nextCharMaybeLf;
	protected transient char quoteChar;
	
	protected transient byte tokenFlags;
	protected transient byte valueType;
	
	protected short defaultTzRefNum;
	protected boolean preferIPv4To4PartVer;
	/**
	* Tells if single quote and double quote characters are not tolerated within non quoted strings.
	*/
	protected boolean noQuoteInNonQuotedStr; //since 2018-01-02
	
	protected transient byte currentCmplxEventType;
	protected transient byte[] stack;
	protected transient int stackSzM1;
	
	/**
	* Tells if string values must be post-decoded for other value types (GUID, objectID, RFC datetime, ...) - for quoted string values, applies if field {@link #forceDecodeQuotedStrValues forceDecodeQuotedStrValues} is equal to <code>true</code>
	*/
	protected boolean postDecodeOtherValueTypes;
	/**
	* Tells if the decode of quoted string values must be forced, once they are fully fetched.<br>
	*/
	protected boolean forceDecodeQuotedStrValues;
	
	protected int propertyNum; //since 2018-04-24

	/**
	* Constructor.<br>
	*/
	protected JSONStreamReader(int strBufferInitCapacity, final short GREGORIAN_CUTOVER_YEAR) {
		super();
		this.eventType = DOC_READ_NOT_STARTED;
		this.strToken = new CharBuffer(strBufferInitCapacity);
		this.lineNumber = 1;
		this.offset = -1;
		this.lineStartOffset = 0;
//		this.lastStructStartOffset = 0;
		this.documentForm = UNSPECIFIED_JSON_CONTENT_FORM;
		this.nextCharMaybeLf = false;
		this.quoteChar = '\u0000';
		this.innerSpaces = new CharBuffer(10);
		this.literalAcc = new LiteralAccumulator(false/*commaDecimalSeparator*/, GREGORIAN_CUTOVER_YEAR);
		this.currentCmplxEventType = -1;
		this.noQuoteInNonQuotedStr = false;
		this.defaultTzRefNum = NULL_TZ_MINUTES_OFFSET;
		this.stackSzM1 = -1;
		this.propertyNum = -1;
	}
	/**
	* Constructor.<br>
	*/
	protected JSONStreamReader(int strBufferInitCapacity) {
		this(strBufferInitCapacity, LiteralAccumulator.DEFAULT_GREGORIAN_CUTOVER_YEAR);
	}
	
	@SuppressWarnings("unchecked")
	public CC setDefaultTzRefNum(final short defaultTzRefNum) {
		this.defaultTzRefNum = defaultTzRefNum;
		return (CC)this;
	}
	@SuppressWarnings("unchecked")
	public CC setPreferIPv4To4PartVersion(final boolean val) {
		this.preferIPv4To4PartVer = val;
		return (CC)this;
	}
	public CC setPreferIPv4To4PartVersion() {
		return setPreferIPv4To4PartVersion(true);
	}
	@SuppressWarnings("unchecked")
	public CC setNoQuoteInNonQuotedStrings(final boolean val) {
		this.noQuoteInNonQuotedStr = val;
		return (CC)this;
	}
	public CC setNoQuoteInNonQuotedStrings() {
		return setNoQuoteInNonQuotedStrings(true);
	}
	@SuppressWarnings("unchecked")
	public CC setForceDecodeQuotedStrValues(final boolean val) {
		this.forceDecodeQuotedStrValues = val; return (CC)this;
	}
	
	public CC setForceDecodeQuotedStrValues() {
		return setForceDecodeQuotedStrValues(true);
	}
	@SuppressWarnings("unchecked")
	public CC setPostDecodeOtherValueTypes(final boolean val) {
		this.postDecodeOtherValueTypes = val; return (CC)this; 
	}
	
	public CC setPostDecodeOtherValueTypes() {
		return setPostDecodeOtherValueTypes(true);
	}
	
	protected final void pushToStack(final byte startEventType) {
		if (stackSzM1 < 0) {
			if (stack == null || stack.length == 0) {
				stack = new byte[4];
			}
			stack[0] = startEventType;
			stackSzM1 = 0;
			return ;
		}
		stackSzM1++;
		if (stackSzM1 >= stack.length) {
			stack = incrCapacityByHalf(stack, stackSzM1 + 2, stackSzM1);
		}
		stack[stackSzM1] = startEventType;
	}
	
	protected final byte popFromStack() {
		return stack[stackSzM1--];
	}
	
	/**
	* Tells if the character values read using method {@link #readChar() readChar()} may be unicode supplementary code points.<br>
	*/
	protected abstract boolean mayReadSupplCps();
	
	/**
	* Reads the next Unicode character/code point into field {@link #ch ch}.<br>
	* <br><b>IMPORTANT</code> this method must increment field {@link #offset offset} every time a data unit (byte if binary source, char is the source is a string, ...) is read.<br>
	*/
	protected abstract boolean readChar();
	/**
	* Returns the byte-length of an ASCII character, as per the used encoding.<br>
	*/
	protected abstract byte asciiCharByteLen(); //since 2018-02-25
	
	protected boolean skipWs() {
		__loop:
		do 
		{
			if (!readChar()) return false;
			if (nextCharMaybeLf) {
				nextCharMaybeLf = false;
				if (ch == '\n') {
					lineStartOffset += asciiCharByteLen();
					if (!readChar()) return false;
				}
			}
			switch(ch)
			{
			case ' ':
			case '\t':
				continue __loop;
			case '\r':
			case '\n': 
				nextCharMaybeLf = ch == '\r';
				lineNumber++; lineStartOffset = offset;
				continue __loop;
			default:
				return true;
			}
		} while (true); 
	}
	
	protected final void err(final int errNum, final CharSequence msg, final CharSequence callingMthdQName) {
		throw new JSONStreamException(
		callingMthdQName + "-" + errNum + ": " + msg + " (lineNumber=" + lineNumber + ", offset=" + offset +  ", lineStartOffset=" + lineStartOffset + ")"
		);
	}
	protected final void nxt_tok_err(final int errNum, final CharSequence msg) {
		err(errNum, msg, "JSONStreamReader::nextToken");
	}
	
	private final void appendAvailCp(boolean isSupplCp) {
		if (isSupplCp) {
			appendSupplCp(ch, strToken);
		}
		else {
			strToken.appendChar((char)ch);
		}
	}
	
	private final void consumeChar(final int ch) {
		final boolean isSupplCp = /*mayReadSupplCps() && */ch > 0xFFFF;
		if (tokenType == UNSPECIFIED_JSON_TOKEN_TYPE) {
			if (!isSupplCp) {
				if (literalAcc.accumulate((char)ch)) {
					tokenType = VALUE_LITERAL;
					return ;
				}
			}
			tokenType = NMTOKEN;
			strToken.resetLimitOnly();
			innerSpaces.resetLimitOnly();
			appendAvailCp(isSupplCp);
		}
		else if (tokenType == QUOTED_STRING) {
			appendAvailCp(isSupplCp);
		}
		else if (tokenType == VALUE_LITERAL) {
			if (!isSupplCp) {
				if (literalAcc.accumulate((char)ch)) {
					return ;
				}
			}
			strToken.resetLimitOnly();
			innerSpaces.resetLimitOnly();
			literalAcc.getChars(strToken);
			tokenType = NMTOKEN;
			if (ch == ' ' || ch == '\t') {
				innerSpaces.appendChar((char)ch);
			}
			else {
				appendAvailCp(isSupplCp);
			}
		}
		else /*if (quoteChar == '\u0000')*/ {
			if (ch == ' ' || ch == '\t') {
				innerSpaces.appendChar((char)ch);
			}
			else if (!innerSpaces.isEmpty()) {
				strToken.appendChars(innerSpaces);
				innerSpaces.resetLimitOnly();
				if (tokenType == NMTOKEN) {
					tokenType = MULTI_PART_UNQUOTED_STRING;
				}
			}
			appendAvailCp(isSupplCp);
		}
	}
	
	private final void appendSupplCp(int cp, CharBuffer buf) {
		cp -= 0x10000;
		buf.appendChars((char)((cp >>> 10) + MIN_HIGH_SURROGATE), (char)((cp & 0x3FF) + MIN_LOW_SURROGATE));
	}
	
	protected void consumeCurrentChar() {
		consumeChar(ch);
	}
	protected void consumeEscapedCp(final int cp) {
		if (tokenType == UNSPECIFIED_JSON_TOKEN_TYPE) {
			strToken.resetLimitOnly();
			innerSpaces.resetLimitOnly();
			tokenType = UNQUOTED_STRING_WITH_ESCAPED;
		}
		else if (tokenType == VALUE_LITERAL) {
			strToken.resetLimitOnly();
			innerSpaces.resetLimitOnly();
			literalAcc.getChars(strToken);
			tokenType = UNQUOTED_STRING_WITH_ESCAPED;
		}
		else if (tokenType == NMTOKEN) {
			if (!innerSpaces.isEmpty()) {
				strToken.appendChars(innerSpaces);
				innerSpaces.resetLimitOnly();
			}
			tokenType = UNQUOTED_STRING_WITH_ESCAPED;
		}
		else if (tokenType == MULTI_PART_UNQUOTED_STRING) {
			tokenType = UNQUOTED_STRING_WITH_ESCAPED;
		}
		if (cp < 0x10000) {
			strToken.appendChar((char)cp);
			tokenFlags |= HAS_ESCAPED_BMP_CHAR;
		}
		else {
			appendSupplCp(cp, strToken);
			tokenFlags |= HAS_ESCAPED_SUPPL_CP;
		}
	}
	
	protected final void read_escaped_err(final int errNum, final CharSequence msg) {
		err(errNum, msg, "JSONStreamReader::readEscapedCharValue");
	}
	
	final int readEscapedCharValue(final byte cnt) {
		if (!readChar()) {
			read_escaped_err(1, "unexpected EOF");
		}
		int cp = checkHexDigit(ch);
		if (cp < 0) {
			read_escaped_err(2, "hexadecimal digit character expected");
		}
		for (byte i=1;i<cnt;i++)
		{
			if (!readChar()) {
				read_escaped_err(3, "unexpected EOF");
			}
			byte digitVal = checkHexDigit(ch);
			if (digitVal < ZERO) {
				read_escaped_err(4, "hexadecimal digit character expected");
			}
			cp <<= 4;
			cp |= digitVal;
		}
		if (cnt > FOUR && (cp > MAX_CODE_POINT || cp < 0)) {
			read_escaped_err(5, "invalid code point value");
		}
		return cp;
	}
	
	static final boolean isValidIPv4Part(final int part) {
		return part > -1 && part < 256;
	}
	
	private final void finalizeToken() {
		if (tokenType == VALUE_LITERAL) {
			valueType = literalAcc.getActualValueType();
			switch(valueType)
			{
			case IValueTypes.INT:
			case IValueTypes.SHORTINT:
			case IValueTypes.TINYINT:
			case IValueTypes.LONGINT:
			case IValueTypes.DFLOAT: 
			case IValueTypes.SFLOAT: 
			case IValueTypes.UBYTE:
			case IValueTypes.USHORT:
			case IValueTypes.UINT:
			case IValueTypes.BOOLEAN:
			case IValueTypes.NULL_VALUE: 
				tokenType = PRIMITIVE_VALUE_LITERAL;
				return ;
			case IValueTypes.DECIMAL: 
				literalValue = literalAcc.to_big_decimal();
				return ;
			case IValueTypes.INTEGER: 
				literalValue = literalAcc.to_big_int();
				return ;
			case IValueTypes.DATETIME:
			case IValueTypes.DATE:
			case IValueTypes.TIME:
				literalValue = literalAcc.to_instant(defaultTzRefNum);
				return ;
			case IValueTypes.DURATION:
			case IValueTypesExt.DAY_TIME_DURATION:
			case IValueTypesExt.YEAR_MONTH_DURATION:
				literalValue = literalAcc.to_duration();
				return ;
			case IValueTypes.TIMEPERIOD: 
				literalValue = literalAcc.to_time_period();
				return ;
			case IValueTypes.VERSION:
				if (preferIPv4To4PartVer && literalAcc.versionPartsCount() == FOUR && literalAcc.versionMajor() != 0 && literalAcc.versionVariant() != 0) { //case may be an IP4 address
					if (isValidIPv4Part(literalAcc.versionMajor()) && isValidIPv4Part(literalAcc.versionMinor())
							&& isValidIPv4Part(literalAcc.versionSubMinor()) && isValidIPv4Part(literalAcc.versionVariant())) {
						valueType = IValueTypes.COMPLEX;
						literalValue = new IPv4((literalAcc.versionMajor() << 24) | (literalAcc.versionMinor() << 16) |  
											(literalAcc.versionSubMinor() << 8) | literalAcc.versionVariant());
						return ;
					}
				}
				literalValue = literalAcc.to_version();
				return ;
			case IValueTypes.UNITNUMBER: 
				literalValue = literalAcc.to_quantity();
				return ;
			//case ZERO: //handled as part of default
			default:
				strToken.resetLimitOnly();
				innerSpaces.resetLimitOnly();
				literalAcc.getChars(strToken);
				tokenType = literalAcc.valueType() == IValueTypes.DATETIME && (literalAcc.dateTimeSep() == ' ' || literalAcc.dateTimeSep() == '\t') ? MULTI_PART_UNQUOTED_STRING : NMTOKEN;
				valueType = IValueTypes.STRING;
				strValue = toStr(strToken);
				break ;
			}
		} //end if (tokenType == VALUE_LITERAL)
		else {
			valueType = IValueTypes.STRING;
			if (joinerChar == (byte)':') { //case must be property name
				workoutPropertyName();
			}
			else {
				strValue = toStr(strToken);
			}
		}
	}
	
	/**
	* Serves to workout the string for the just fetched name data (fetched into field strToken).<br>
	*/
	protected void workoutPropertyName() { //since 2018-04-24
		if (canConvertFieldNamesToFieldNumbers()) {
			propertyNum = _getToStrHelper().ToFieldNumber(strToken/*nameBuffer*/);
			if (propertyNum < 0) {
				strValue = _getToStrHelper().ToFieldName(strToken/*nameBuffer*/, true/*useStrTableOnlyIfNotFieldNameTables*/);
			}
			else {
				strValue = _getToStrHelper().get_FieldName(propertyNum);
			}
		}
		else {
			strValue = _getToStrHelper().ToFieldName(strToken/*nameBuffer*/, true/*useStrTableOnlyIfNotFieldNameTables*/);
			propertyNum = -1;
		}
	}
	
	/**
	* Reads the next token and the next token joiner.<br>
	* @return <code>false</code> if the end of stream is reached before the first character of the token or the joiner is reached
	* 
	*/
	protected final boolean nextToken() {
		if (!skipWs()) return false;
//		if (currentCmplxEventType == -1/* && ch == '['*/) { //for debug on 2018-04-26
//			System.out.println("JSONStreamReader::nextToken - ch: " + ch + "");
//		}
		quoteChar = ch == '"' || ch == '\'' ? (char)ch : '\u0000';
		if (quoteChar == '\u0000') {
			tokenType =  UNSPECIFIED_JSON_TOKEN_TYPE;
			literalAcc.reset();
		}
		else {
			tokenType =  QUOTED_STRING;
			strToken.resetLimitOnly();
			innerSpaces.resetLimitOnly();
		}
		if (quoteChar != '\u0000') {
			if (!readChar()) {
				nxt_tok_err(1, "unexpected EOF");
			}
		}
		else {
			innerSpaces.resetLimitOnly();
		}
		tok_loop:
		do 
		{
			__switch:
			switch(ch)
			{
			case '\r':
			case '\n':
				if (quoteChar == '\u0000') {
					lineNumber++;
					lineStartOffset = offset;
					nextCharMaybeLf = ch == '\r';
					break tok_loop;
				}
				else if (nextCharMaybeLf) {
					nextCharMaybeLf = false;
					if (ch == '\n') {
						break __switch;
					}
				}
				lineNumber++;
				lineStartOffset = offset;
				nextCharMaybeLf = ch == '\r';
				consumeCurrentChar();
				break __switch;
			case ' ':
			case '\t':
				consumeCurrentChar();
				break __switch;
			case '\\':
				if (!readChar()) {
					nxt_tok_err(2, "unexpected EOF");
				}
				switch(ch)
				{
				case '"':
				case '\'':
				case '\\':
				case '/': 
					break ;
				case 't':	
					ch = '\t'; break ;
				case 'r': 
					ch = '\r'; break ;
				case 'n':
					ch = '\n'; break ;
				case 'f':	
					ch = '\f'; break ;
				case 'b':	
					ch = '\u0008'; break ;
				case 'u':
				case 'x': //not standard, but tolerated
				case 'U': //not standard, but tolerated
					ch = readEscapedCharValue(ch == 'u' ? FOUR : ch == 'x' ? TWO : EIGHT);
					consumeEscapedCp(ch);
					break __switch;
				default:
					nxt_tok_err(3, "bad escaped character syntax"); return false; //dummy return
				}
				consumeCurrentChar();
				break __switch;
			default:
				if (quoteChar != '\u0000') {
					if (ch == quoteChar) break tok_loop;
				}
				else {
					switch(ch)
					{
					case ':':
					case ',': 
					case '{':
					case '[':
					case ']':
					case '}':
						joinerChar = (byte)ch;
						finalizeToken();
						return true;
					case '"':
					case '\'':
						if (noQuoteInNonQuotedStr) {
							nxt_tok_err(3, "unexpected " + (ch == '"' ? "double" : "single")  +  " quote character in non quoted string"); 
						}
						break ;
					}
				}
				if (ch < ' ') {
					nxt_tok_err(4, "unexpected control character"); return false; //dummy return
				}
				consumeCurrentChar();
				break ;
			}
			if (!readChar()) {
				if (quoteChar != '\u0000') {
					nxt_tok_err(5, "unexpected EOF");
				}
				joinerChar = ZERO;
			}
			else {
				continue tok_loop;
			}
		} while (true); //end tok_loop
		__loop:
		do 
		{
			if (!readChar()) {
				joinerChar = ZERO;
				finalizeToken();
				return true;
			}
			if (nextCharMaybeLf) {
				nextCharMaybeLf = false;
				if (ch == '\n') {
					continue ;
				}
			}
			switch(ch)
			{
			case ':':
			case ',':
			case '{':
			case '[':
			case ']':
			case '}':
				joinerChar = (byte)ch;
				finalizeToken();
				return true;
			case ' ':
			case '\t':
				continue __loop;
			case '\r':
			case '\n':
				lineNumber++; lineStartOffset = offset;
				nextCharMaybeLf = ch == '\r';
				continue __loop;
			default:
				nxt_tok_err(6, "token joiner expected");
			}
		} while (true); //end __loop
	}
	
	protected final void fail_next(final int errNum, final CharSequence msg) {
		err(errNum, msg, "JSONStreamReader::next");
	}
	
	protected final byte unxpctd_joiner(final int errNum, final byte joinerChar) {
		fail_next(errNum, "unexpected joiner character - '" + (char)joinerChar + "'"); return -1; //dummy return
	}
	
	protected final void unxptd_nxt_token(final int errNum) {
		fail_next(errNum, "unexpected token or type of next token"); 
	}
	
	protected final byte next(final boolean prevIsComma) {
		if (!prevIsComma) { //a chance that previous token was simple value followed by '}' or ']'
			if ((joinerChar == (byte)'}' && eventType == PROPERTY_VALUE) || (joinerChar == (byte)']' && (eventType == ARRAY_SIMPLE_ITEM || eventType == PROPERTY_ARRAY_SIMPLE_ITEM))) {
				eventType = get_PeerEventType(currentCmplxEventType);
				currentCmplxEventType = stackSzM1 > -1 ? popFromStack() : MINUSONE;
				return eventType;
			}
		}
		if (!nextToken()) {
			if (eventType < SCALAR/*OLD: OBJECT_START*/) {
				return (eventType = EMPTY_JSON_STREAM);
			}
			else if (currentCmplxEventType > MINUSONE) {
				fail_next(1, "unexpected EOF");
			}
			return (eventType = END_OF_STREAM);
		}
		else if (tokenType == UNSPECIFIED_JSON_CONTENT_FORM) {//case there is only a joiner
//			System.out.println("JSONStreamReader - joinerChar: '" + (char)joinerChar + "'");
			boolean isArray = false;
			switch(joinerChar)
			{
			case '{': 
				if (eventType < SCALAR/*OLD: OBJECT_START*/) { //case the event is the very first event
					documentForm = JSON_OBJECT;
				}
//				lastStructStartOffset = offset;
				break; 
			case '[': //case the event is the very first event
				isArray = true;
				if (eventType < SCALAR/*OLD: OBJECT_START*/) {
					documentForm = JSON_ARRAY;
				}
//				lastStructStartOffset = offset;
				break ;
			case ',': 
				if (currentCmplxEventType < ZERO || prevIsComma) {
					fail_next(2, "unexpected comma character");
				}
				return next(true);
			case '\u0000':
				fail_next(6, "dangling scalar"); return -1; //dummy return
			default:
				if (joinerChar == (byte)'}' || joinerChar == (byte)']') {
					if (currentCmplxEventType > MINUSONE) {
						if ((JSONSreamEventTypes.get_ContentForm(currentCmplxEventType) == JSON_ARRAY) != (joinerChar == (byte)']')) {
							fail_next(15, "joiner of unexpected type");
						}
						//ADD CODE TO END CURRENT OBJECT ANd RETURN PROPER OBJECT ENd EVENT
						eventType = get_PeerEventType(currentCmplxEventType);
						currentCmplxEventType = stackSzM1 > -1 ? popFromStack() : MINUSONE;
						return eventType;
					}
				}
				unxpctd_joiner(3, joinerChar);
			}
			___switch: 
			switch(eventType) //switch previous event
			{
			case PROPERTY_NAME:
				eventType = (isArray ? PROPERTY_ARRAY_START : PROPERTY_OBJECT_START); break ;
			case PROPERTY_VALUE:
				return unxpctd_joiner(4, joinerChar); 
			case DOC_READ_NOT_STARTED:
				documentForm = isArray ? JSON_ARRAY : JSON_OBJECT;
				eventType = (documentForm == JSON_OBJECT ? OBJECT_START : ARRAY_START); break ;
			case PROPERTY_ARRAY_START:
				eventType = (isArray ? PROPERTY_ARRAY_ARR_ITM_START : PROPERTY_ARRAY_OBJ_ITM_START); break ;
			case ARRAY_START:
			case PROPERTY_ARRAY_ARR_ITM_START: 
				eventType = (isArray ? ARRAY_START : OBJECT_START); break ;
			case ARRAY_END:
			case OBJECT_END:
			case PROPERTY_ARRAY_ARR_ITM_END:
			case PROPERTY_ARRAY_OBJ_ITM_END: 
				if (prevIsComma && currentCmplxEventType > MINUSONE) { //case another array item to start???
					switch(currentCmplxEventType)
					{
					case ARRAY_START:
					case PROPERTY_ARRAY_START:
					case PROPERTY_ARRAY_ARR_ITM_START: 
						eventType = (isArray ? ARRAY_START : OBJECT_START); break ___switch;
					}
				}
				//System.out.println("JSONStreamReader::next - previousEventType: " + eventType + ", joinerChar: '" + (char)joinerChar + "', currentCmplxEventType: " + currentCmplxEventType);
				return unxpctd_joiner(7, joinerChar); 
			//case PROPERTY_ARRAY_END:
			//case PROPERTY_OBJECT_END:
			default:
				if ((eventType == ARRAY_SIMPLE_ITEM || eventType == PROPERTY_ARRAY_SIMPLE_ITEM) && (joinerChar == (byte)'{' || joinerChar == (byte)'[')) {
					eventType = (isArray ? ARRAY_START : OBJECT_START);
				}
				else {
					unxpctd_joiner(5, joinerChar); 
				}
			} //end ___switch
			if (currentCmplxEventType > MINUSONE) {
				pushToStack(currentCmplxEventType);
			}
			currentCmplxEventType = eventType;
			return eventType;
		} //end if (tokenType == UNSPECIFIED_JSON_CONTENT_FORM) {//case there is only a joiner
		else {
			if (eventType < SCALAR/*OLD: OBJECT_START*/) {
				if (joinerChar == (byte)'\u0000') {
					documentForm = JSON_SCALAR;
					return (eventType = SCALAR);
				}
				fail_next(8, "bad document start");
			}
			switch(get_ContentForm(currentCmplxEventType))
			{
			case JSON_OBJECT:
				switch(eventType)
				{
				case PROPERTY_NAME: //case previous event is a property name event
					if (joinerChar != (byte)',' && joinerChar != (byte)'}') {
						fail_next(9, "comma or '}' character expected");
					}
					return (eventType = PROPERTY_VALUE);
				case PROPERTY_VALUE: //case previous event is a property value
				case PROPERTY_OBJECT_END: //case previous event is the end of an object property value
				case PROPERTY_ARRAY_END: //case previous event is the end of an array property value
				case OBJECT_START:
				case PROPERTY_OBJECT_START: 
				case PROPERTY_ARRAY_OBJ_ITM_START: 
					if ((eventType == PROPERTY_OBJECT_END || eventType == PROPERTY_ARRAY_END) && !prevIsComma) { //case PROPERTY_OBJECT_END or PROPERTY_ARRAY_END
						fail_next(10, "comma character expected before the property name");
					}
					if (joinerChar != (byte)':') {
						fail_next(11, "colon character expected");
					}
					return (eventType = PROPERTY_NAME);
				default:
					//NOTE: should normally not get here... but
					unxptd_nxt_token(12); return -1; //dummy return
				}
			case JSON_ARRAY:
				if (isEndingEvent(eventType) && !prevIsComma) { //case previous event is an ending event but comma character is missing between previous and the just fetched!!!
					fail_next(13, "comma character expected before next array item");
				}
				return (eventType = (currentCmplxEventType == PROPERTY_ARRAY_START ? PROPERTY_ARRAY_SIMPLE_ITEM : ARRAY_SIMPLE_ITEM));
			default:
				//NOTE: should normally not get here... but
				unxptd_nxt_token(14); return -1; //dummy return
			}
		}
	}
	/**
	* {@inheritDoc}
	*/
	public byte next() {
		return next(false);
	}
	/**
	* {@inheritDoc}
	*/
	public final int getInt() {
		return literalAcc.getInt();
	}
	/**
	* {@inheritDoc}
	*/
	public final long getLong() {
		return literalAcc.getLong();
	}
	/**
	* {@inheritDoc}
	* @return <code>false</code>
	*/
	public final boolean isHalfPrecisionFloat() {return false; }
	
	/**
	* {@inheritDoc}
	* @return <code>false</code>
	*/
	public final boolean isUndefined() {
		return false;
	}
	/**
	* {@inheritDoc}
	*/
	public final boolean isNull() {
		return valueType == IValueTypes.NULL_VALUE;
	}
	/**
	* {@inheritDoc}
	* @return <code>isNull()</code>
	*/
	public final boolean isNullOrUndefined() {return isNull(); }
	
	/**
	* {@inheritDoc}
	*/
	public final boolean isPrimitiveValue() {
		return tokenType == PRIMITIVE_VALUE_LITERAL;
	}
	
	/**
	* {@inheritDoc}
	*/
	public final float getFloat() {
		return literalAcc.getFloat();
	}
	/**
	* {@inheritDoc}
	*/
	public final double getDouble() {
		return literalAcc.getDouble();
	}
	/**
	* {@inheritDoc}
	*/
	public final boolean getBoolean() {
		return literalAcc.getBoolean();
	}
	/**
	* {@inheritDoc}
	*/
	public byte[] getBytes() {return null;}
	
	/**
	* {@inheritDoc}
	*/
	public final boolean isKeyValue() {return eventType == PROPERTY_NAME; }
	/**
	* Returns the worked out number of the just/previously fetched property name.<br>
	* @return <code>-1</code> if no number is worked put for the just/previously fetched property name; the number of the just/previously fetched property name, otherwise.
	*/
	public final int getKeyNumber() {return propertyNum; }
	
	/**
	* {@inheritDoc}
	*/
	public boolean isArrayItemValue() {return false; }
	
	/**
	* {@inheritDoc}
	*/
	public int getLineNumber() {return lineNumber; }
	/**
	* {@inheritDoc}
	*/
	public int getOffset() {return offset; }
	/**
	* {@inheritDoc}
	*/
	public int getLineStartOffset() {return lineStartOffset; }
	/**
	* {@inheritDoc}
	*/
	public byte getEventType() {return eventType; }
	
	/**
	* {@inheritDoc}
	*/
	public byte getValueType() {return valueType; }
	/**
	* {@inheritDoc}
	*/
	public core.Value getValue() {return literalValue; }
	
	/**
	* Returns the helper to use in converting string buffer to string.<br>
	*/
	protected abstract StrBufferToStrHelper<? extends Str,? extends Str,? extends Str> _getToStrHelper(); //since 2018-04-13
	
	/**
	* Tells if field names can be converted to field numbers and vice-versa.<br>
	*/
	protected boolean canConvertFieldNamesToFieldNumbers() { //since 2018-04-24
		return _getToStrHelper().canConvertFieldNamesToFieldNumbers();
	}
	
	protected Str toStr(CharBuffer buff) { //NOTE: ceased to be an abstract method on 2018-04-13
		return _getToStrHelper().ToValue(buff); 
	}
	
	public Str getString() {return strValue; }
	/**
	* Gets the character buffer that holds the last fetched string token.<br>
	*/
	public CharBuffer getStringTokenBuffer() {return strToken; } //since 2018-02-27
	
	/**
	* Converts the last fetched non string literal and non primitive to string.<br>
	*/
	public Str getLiteralAsString() {
		CharBuffer buff = new CharBuffer(20);
		literalAcc.getChars(buff);
		return toStr(buff);
	}
	/**
	* {@inheritDoc}
	*/
	public byte getTrinary() {return valueType == core.IValueTypes.BOOLEAN ? getBoolean() ? yes : no : maybe; } //since 2018-03-07
	
	/**
	* {@inheritDoc}
	*/
	public boolean isValueArray() {return false; } //since 2018-03-07
	/**
	* {@inheritDoc}
	*/
	public core.Array.ValueArray<?> getValueArray() {return null; } //since 2018-03-07
	/**
	* {@inheritDoc}
	* @return {@link #CHARS CHARS}
	*/
	@Override
	public final byte getCategory() { //since 2018-03-21
		return CHARS; 
	}
	/**
	* {@inheritDoc}
	* @return {@code this.getValueType()}
	*/
	@Override
	public final byte getRawValueType() { //since 2018-03-21
		return getValueType(); 
	}
	/**
	* {@inheritDoc}
	* @return {@link #JSON_DOC_FORMAT JSON_DOC_FORMAT}
	*/
	@Override
	public final byte getDocumentFormat() {
		return JSON_DOC_FORMAT;
	}
	

}