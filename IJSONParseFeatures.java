package json.parsing;

/**
* Interface with integer number constants (flags) for JSON parse features.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public interface IJSONParseFeatures {

	public static final byte ALLOW_UNQUOTED_FIELD_NAMES = 1;
	
	public static final byte ALLOW_NUMERIC_LEADING_ZEROS = 2;
	/**
	* enabling the feature allows recognition and handling of "C comments" (&#47;* ... *&#47;) and "C++ comments" (// ....)
	*/
	public static final byte ALLOW_COMMENTS = 4;

	public static final byte ALLOW_YAML_COMMENTS = 8;
	/**
	* Allow translation of "missing" values (white-space between two commas, in JSON Array context) into null values, instead of an exception.<br>
	*/
	public static final byte ALLOW_MISSING_VALUES = 16;

}

