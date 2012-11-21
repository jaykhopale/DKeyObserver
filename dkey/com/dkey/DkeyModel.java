package com.dkey;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DkeyModel {

	private InputStream _is;
	Document doc;
	private static HashMap<Integer,String> preLoadedKeys = new HashMap<Integer, String>();
	public DkeyModel( ) throws SAXException, IOException {
		
		//preLoadedKeys.put(R.raw.newdkey, "Oak's Key");
		//preLoadedKeys.put( R.raw.oakskey, "Patterson's Key");
		//preLoadedKeys.put( R.raw.testing, "Testing Key");
		preLoadedKeys.put( R.raw.phloxkey, "Phlox key");
	}
	
	public void setInputStream(InputStream is) {
		_is = is;
	}
	public void parseXml() throws SAXException, IOException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();	 
			doc = dBuilder.parse( _is );
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public HashMap<Integer, String> getPreLoadedKeys() {
		return preLoadedKeys;
	}
	
	public Document getDocBuilder() {
		return doc;  
	}
}

