package json;


/**
* Provider class for providing instances of {@link JSONObjectImpl1 JSONObjectImpl1} that smartly adapts search algorithm as the number of properties augments.<br>
* It is worth noting that using the constructor of class {@link JSONObjectImpl1 JSONObjectImpl1} to create instances of the same class result in instances that are well-suited for objects that have less than a hundred of properties.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public final class JSONObjectImpl1Factory extends JSONObject.JSONObjectFactory { //since 2018-04-25

	/**
	* The single instance of the class.<br>
	*/
	public static final JSONObjectImpl1Factory INSTANCE = new JSONObjectImpl1Factory();
	
	/**
	* Constructor.<br>
	*/
	private JSONObjectImpl1Factory() {}
	/**
	* {@inheritDoc}
	*/
	public final  JSONObjectImpl1 newJSONObject() {return new JSONObjectImpl1_2(); }
	
	
}