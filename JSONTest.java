package json.stream;

import core.io.encoding.UTF8CharReader;

class JSONTest {

	public static final void main(java.lang.String[] args) {
		java.io.InputStream fis = null;
		java.lang.String fileDir = "C:\\Users\\hp\\Documents\\sj\\source\\new_json\\";
		try 
		{
			fis = new java.io.BufferedInputStream(new java.io.FileInputStream(fileDir + "Europe_London.json"));
		}
		catch(java.io.IOException ioe)
		{
			ioe.printStackTrace(); return ;
		}
		UTF8CharReader charRdr = new UTF8CharReader(fis, false);
		JSONStreamReaderImpl jsonRdr = new JSONStreamReaderImpl(charRdr, 64);
		while (jsonRdr.next() != IJSONSreamEventTypes.END_OF_STREAM)
		{
			System.out.println("eventType: " + jsonRdr.getEventType() + ", valueType: " + jsonRdr.getValueType() + ", string: '" + jsonRdr.getString() + "', integer: " + jsonRdr.getLong() + ", joinerChar: '" + (char)jsonRdr.joinerChar +  "'");
		}
		System.out.println("\r\n************************");
		try 
		{
			fis = new java.io.BufferedInputStream(new java.io.FileInputStream(fileDir.substring(0, fileDir.length() - 5) + "bdb\\xml_doc_fields_example.json"));
		}
		catch(java.io.IOException ioe)
		{
			ioe.printStackTrace(); return ;
		}
		charRdr = new UTF8CharReader(fis, false);
		jsonRdr = new JSONStreamReaderImpl(charRdr, 64);
		while (jsonRdr.next() != IJSONSreamEventTypes.END_OF_STREAM)
		{
			System.out.println("eventType: " + jsonRdr.getEventType() + ", valueType: " + jsonRdr.getValueType() + ", string: '" + jsonRdr.getString() + "', integer: " + jsonRdr.getLong() + ", joinerChar: '" + (char)jsonRdr.joinerChar +  "'");
		}
	}

}