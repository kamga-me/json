package json;

/**
* Interface with integer number constants for the JSON token types.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public interface IJSONTokenTypes {
	
	/**
	* Indicates that the token type is unspecified/unknown.<br>
	*/
	public static final byte UNSPECIFIED_JSON_TOKEN_TYPE = 0;

	/**
	* Denotes tokens that are actually textual representations of values of primitive type (int, long, float, double, boolean, null, byte, short).<br>
	*/
	public static final byte PRIMITIVE_VALUE_LITERAL = 1;
	/**
	* Denotes tokens that are actually textual representations of non string values that are not of primitive type (int, long, float, double, boolean, null, byte, short).<br>
	*/
	public static final byte VALUE_LITERAL = 2;
	/**
	* Denotes tokens that are actually quoted strings.<br>
	*/
	public static final byte QUOTED_STRING = 3;
	/**
	* Constant for <code>unquoted</code> strings that do not have any escaped character or whitespace character.<br>
	*/
	public static final byte NMTOKEN = 4;
	/**
	* Constant for <code>unquoted</code> strings that do not have any escaped character but have at least one inner whitespace character.<br>
	*/
	public static final byte MULTI_PART_UNQUOTED_STRING = 5;
	/**
	* Constant for <code>unquoted</code> strings that do have escaped characters and possibly some whitespace characters.<br>
	*/
	public static final byte UNQUOTED_STRING_WITH_ESCAPED = 6;

}