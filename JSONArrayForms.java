package json;

/**
* Utility class with integer number constants for the forms of JSON array nodes.<br>
* 
* @author Marc E. KAMGA
* @version 1.0 
* @copyright Marc E. KAMGA
*/
public final class JSONArrayForms { //since 2018-04-23

	/**
	* Constant for <code>array</code> form - the content of the array node is an instance of {@link core.Array Array}.<br>
	*/
	public static final byte ARRAY = 0;
	/**
	* Constant for <code>array-list</code> form - the node items are held in an array and their base/ancestor class is {@link core.Thing Thing}.<br>
	*/
	public static final byte ARRAY_LIST = 1;
	/**
	* Constant for <code>linked-list</code> form - the node items are chained/linked together.<br>
	*/
	public static final byte LINKED_LIST = 2;

}