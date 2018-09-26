package json;

import string.String;
import core.document.IDocumentLoader;
import core.document.ISimpleDocBuildHelper;
import json.stream.IJSONStreamReader;
import core.io.ICharReader;
import core.io.StreamableInput;

/**
* Interface that JSON document loaders implement.<br>
* JSON document loaders are part of <code>SDLAj</code> framework.<br>
* <code>SDLAj</code> stands for Simple Document Load API for JSON.<br>
*
* @param <N> the base class for nodes
* @param <D> the class for nodes of kind document
* @param <S> the base class for sequences/arrays
* @param <T> the target class of the complete documents
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*
* @see mime.IDocumentLoaderProvider IDocumentLoaderProvider
*/
public interface IJSONDocumentLoader<N extends core.Thing, D extends N, S extends N, T, DocBuildHelper extends ISimpleDocBuildHelper<N,D,S,T>> extends IDocumentLoader<N,D,T,DocBuildHelper> { //NOTE: extends IDocumentLoader since 2018-04-23

	/**
	* Loads the JSON document file whose system identifier is supplied.<br>
	* @param systemId the file-path, url, BDB file path, ... of the JSON document file to load.
	*/
	@Override
	public T load(String systemId, DocBuildHelper docBuildHelper);
	/**
	* Loads the JSON document file whose system identifier is supplied.<br>
	* @param systemId the file-path, url, BDB file path, ... of the JSON document file to load.
	*/
	@Override
	public T load(java.lang.String systemId, DocBuildHelper docBuildHelper);
	/**
	* Loads the JSON document which can be stream-read using the supplied JSON stream reader.<br>
	* <b>IMPORTANT</b>: this method does not attempt to fetch a next JSON stream event as soon as the end of current document is reached.<br>
	* @param jsonStreamReader the JSON stream reader to use in reading the JSON document file/data.
	* @param namesFromBufConvAssumedRefs {@link #yes yes} if names from the string buffer conversion must be considered reference strings; {@link #no no} if names from the string buffer conversion must not be considered reference strings; {@link #maybe maybe} to indicate that there is no value for the parameter.
	* @param valuesFromBufConvAssumedRefs {@link #yes yes} if string values from the string buffer conversion must be considered reference strings; {@link #no no} if string values from the string buffer conversion must not be considered reference strings; {@link #maybe maybe} to indicate that there is no value for the parameter.
	*/
	public T load(IJSONStreamReader<? extends String> jsonStreamReader, DocBuildHelper docBuildHelper, byte namesFromBufConvAssumedRefs, byte valuesFromBufConvAssumedRefs);
	/**
	* @return <code>this.load(jsonStreamReader,docBuildHelper,{@link #maybe maybe},{@link #maybe maybe})</code>
	*/
	public T load(IJSONStreamReader<? extends String> jsonStreamReader, DocBuildHelper docBuildHelper);
	
	/**
	* Loads the JSON document using the supplied Unicode character reader.<br>
	* <b>IMPORTANT</b>: this method does not attempt to fetch a next character as soon as the end of document is reached.<br>
	*/
	@Override
	public T load(ICharReader sourceReader, DocBuildHelper docBuildHelper);
	/**
	* Loads the JSON document using the streamable input.<br>
	* <b>IMPORTANT</b>: this method ceases to attempt to fetch a next character upon the firing of end-of-document stream read event.<br>
	* @param jsonInput the streamable JSON character input to use in reading the document from the backing file/source/resource - its stringDecoder field is to be ignored in case the format of the document data is a biary format and not a text format
	*/
	@Override
	public T load(StreamableInput jsonInput, DocBuildHelper docBuildHelper); //since 2018-04-24
	
	

}