package com.dkey;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.dkey.Interfaces.IGetNotesValuesListener;
import com.dkey.Interfaces.ILoadBigImageListener;
import com.dkey.Interfaces.ILoadPhotoListener;
import com.dkey.Interfaces.IObserveTabChangeListener;
import com.dkey.Interfaces.IResetGlossaryListener;
import com.dkey.Interfaces.IResetSpeciesListListener;
import com.dkey.Interfaces.IResetTabListener;
import com.dkey.Interfaces.ITabChangeListener;
import com.dkey.Interfaces.ITabSetChangeListener;
import com.dkey.Interfaces.ITabcontentChangeListener;
import com.dkey.Interfaces.IUpdateAppConfiguration;
import com.dkey.observe.DkeyNotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore.Images;
import android.provider.Settings.Secure;
import android.util.Log;

public class DkeyController {

	private Context _appContext;
	private DkeyModel _dkeyModel;
	private int _currentKey;
	public static String DEVICE_ID;
	SharedPreferences _myPrefs;
	private int _snapcount;
	private boolean _isMoreInfoDetailsPopulated, _isConfigModified;
	private Vector<String> _images;
	private ArrayList<Boolean> _isLeafNodeList;
	private IResetSpeciesListListener _resetSpeciesListlistener;
	private IResetGlossaryListener _resetGlossarylistener;
	IObserveTabChangeListener _observeTabChangeListener;
	ILoadPhotoListener _loadPhotoListener;
	private String _xmlFilename;

	public static enum DkeyTabs {
		key, species, Glossary, Keys, Observe, dkeytab, dkeyObserveTab
	}

	private HashMap<DkeyTabs, ITabcontentChangeListener> _tabContentChangeListenerMap;
	private HashMap<DkeyTabs, ITabSetChangeListener> _tabSetChangeListenerMap;
	private ITabChangeListener _tabChangeListener;
	private IResetTabListener _resetTabListener;
	private ILoadBigImageListener _loadBigImageListener;
	private boolean _shouldRotate;
	private boolean _isConfigIncomplete;
	private boolean _isCameraOn;
	private String _orgID;
	private Hashtable<String, String> _extrafields;
	private ArrayList<MoreInfoRow> _orgInfoRows;
	private Hashtable<String, String> _moreInfoMap;
	private boolean _isNotesModified;
	private IGetNotesValuesListener _getNotesValuesListener;
	private IUpdateAppConfiguration _updateAppConfigurer;
	

	public DkeyController(DkeyApplication context) throws SAXException,
			IOException {
		_appContext = context;
		_tabContentChangeListenerMap = new HashMap<DkeyTabs, ITabcontentChangeListener>();
		_tabSetChangeListenerMap = new HashMap<DkeyController.DkeyTabs, ITabSetChangeListener>();
		_dkeyModel = new DkeyModel();
		intializeCurrentKey();
		_moreInfoMap = new Hashtable<String, String>();
		_extrafields = new Hashtable<String, String>();
		_isLeafNodeList = new ArrayList<Boolean>();
	}

	private void intializeCurrentKey() throws SAXException, IOException {
		_myPrefs = this._appContext.getSharedPreferences("myPrefs",
				Context.MODE_WORLD_READABLE);
		_currentKey = _myPrefs.getInt("CURRENT_DKEY", -1);
		if (_currentKey == -1) {
			_currentKey = _dkeyModel.getPreLoadedKeys().keySet().iterator()
					.next();
			updateSharedPreferences(_currentKey);
		}
		configureParser();
		_moreInfoMap = new Hashtable<String, String>();
		_extrafields = new Hashtable<String, String>();
		_isLeafNodeList = new ArrayList<Boolean>();
	}

	public void configureParser() throws SAXException, IOException {
		InputStream is = _appContext.getResources()
				.openRawResource(_currentKey);
		_dkeyModel.setInputStream(is);
		_dkeyModel.parseXml();
		
	}

	public void updateSharedPreferences(int key) {
		SharedPreferences.Editor prefsEditor = _myPrefs.edit();
		prefsEditor.putInt("CURRENT_DKEY", key);
		prefsEditor.commit();
	}

	public DkeyModel getDkeyModel() {
		return _dkeyModel;
	}

	public void setCurrentKey(int currentKey) throws SAXException, IOException {
		_currentKey = currentKey;
		updateSharedPreferences(_currentKey);
		configureParser();
	}

	public int getCurrentKey() {
		return _currentKey;
	}

	public Document getDocBuilder() {
		return _dkeyModel.getDocBuilder();
	}

	public void resetPreLoadedDkey(int resourceID) {

	}

	public void registerTabContentChangeListener(DkeyTabs tab,
			ITabcontentChangeListener tabContentChangeListener) {
		_tabContentChangeListenerMap.put(tab, tabContentChangeListener);
	}

	public void registerGetNotesValuesListener(IGetNotesValuesListener listener) {
		_getNotesValuesListener = listener;
	}

	public void changeTabContent(DkeyTabs tab) {
		_tabContentChangeListenerMap.get(tab).changeCurrentActivityGroup();
	}

	public void registerTabSetChangeListener(DkeyTabs tabSet,
			ITabSetChangeListener listener) {
		_tabSetChangeListenerMap.put(tabSet, listener);
	}

	public void registerResetSpeciesListListener(
			IResetSpeciesListListener listener) {
		_resetSpeciesListlistener = listener;
	}

	public void resetSpeciesList() {
		if (_resetSpeciesListlistener != null) {
			_resetSpeciesListlistener.resetSpeciesList();
		}
	}

	public void setSelectedTab(DkeyTabs tabSet) {
		_tabSetChangeListenerMap.get(tabSet).setPreviouslySelectedTab();
	}

	public void registerTabChangeListener(ITabChangeListener listener) {
		_tabChangeListener = listener;
	}

	public void resetObserveTabSet() {
		if (_tabChangeListener != null) {
			_tabChangeListener.resetObserveTabSet();
		}
	}

	public void registerResetTabListener(IResetTabListener listener) {
		_resetTabListener = listener;

	}

	public void resetTabs() {
		if (_resetTabListener != null) {
			_resetTabListener.resetTabs();
		}
	}

	public int getSnapCount() {
		return _snapcount;
	}

	public void setSnapCount(int count) {
		_snapcount = count;
	}

	public void addImages(String image) {
		if (_images == null) {
			_images = new Vector<String>();
		}
		_images.add(image);
	}

	public Vector<String> getImages() {
		return _images;
	}

	public void registerObserveTabChangeListener(
			IObserveTabChangeListener listener) {
		_observeTabChangeListener = listener;
	}

	public void setCurrentTabforObserveTabs(int i) {
		if (_observeTabChangeListener != null) {
			_observeTabChangeListener.changeCurrentObserveTab(i);
		}

	}

	public void registerLoadPhotoListener(ILoadPhotoListener listener) {
		_loadPhotoListener = listener;

	}

	public void loadPhotos() {
		if (_loadPhotoListener != null) {
			_loadPhotoListener.loadPhotos();
		}
	}

	public void registerLoadBigImageListener(ILoadBigImageListener listener) {
		_loadBigImageListener = listener;

	}

	public void loadBigImage() {
		if (_loadBigImageListener != null) {
			_loadBigImageListener.loadBigImage(_images.size() - 1);
		}
	}

	public void setShouldRotateImage(boolean b) {
		_shouldRotate = b;

	}

	public boolean ShouldRotate() {
		// TODO Auto-generated method stub
		return _shouldRotate;
	}

	public void setConfigIncomplete(boolean b) {
		_isConfigIncomplete = b;

	}

	public boolean isConfigIcomplete() {
		return _isConfigIncomplete;
	}

	public void setIsCameraOn(boolean value) {
		_isCameraOn = value;
	}

	public boolean isCameraOn() {
		return _isCameraOn;
	}

	public void setOrgID(String i) {
		_orgID = i;
	}

	public String getOrgID() {
		return _orgID;
	}

	public void processOrganismNode() {
		setIsMoreInfoDetailsPopulated(true);
		_orgInfoRows = processOrganismInformation(_orgID);
	

	}
	
	public ArrayList<MoreInfoRow> processOrganismInformation( String id ) {
		if (!id.equals("-1")) {
			NodeList list = DkeyApplication._controller.getDocBuilder()
					.getElementsByTagName("organism");
			ArrayList<MoreInfoRow> rows = null;
			for (int i = 0; i < list.getLength(); i++) {
				NodeList children = list.item(i).getChildNodes();
				boolean rightNode = false;
				for (int j = 0; j < children.getLength(); j++) {
					//
					if (!rightNode
							&& children.item(j).getNodeName().equals("org_id")) {
						if (children.item(j).getChildNodes().item(0)
								.getNodeValue().equals(id)) {
							rightNode = true;
							rows = new ArrayList<MoreInfoRow>();
						}
					}
					if (rightNode) {
						Node organism = children.item(j);
						if (organism != null
								&& !organism.getNodeName().equals("org_id")
								&& !organism.getNodeName().equals("image")
								&& organism.getChildNodes().getLength() > 0) {
							if (organism.getChildNodes().item(0).getNodeValue() != null
									&& !organism.getChildNodes().item(0)
											.getNodeValue().equals("")) {
								MoreInfoRow row = new MoreInfoRow();
								row.key = children.item(j).getNodeName();
								row.separator = ":";
								row.value = organism.getChildNodes().item(0)
										.getNodeValue();
								rows.add(row);
							}
						}
					}
				}
				if (rightNode) {
					break;
				}
			}
			return rows;
		}
		return null;
	}

	public void processMoreInfoRows() {
		if (_isMoreInfoDetailsPopulated && _orgInfoRows != null) {
			for (MoreInfoRow row : _orgInfoRows) {
				_moreInfoMap.put("common_name", row.value);
			}
		}
	}

	public void createXmlDoc(String filename) {
		try {
			if (_getNotesValuesListener == null) {
				processMoreInfoRows();
			}
			_xmlFilename = filename;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("OBSERVATION");
			doc.appendChild(rootElement);

			// Username
			Element username = doc.createElement("USERNAME");
			username.appendChild(doc.createTextNode(_myPrefs.getString(
					"Username", "Username Not set")));
			rootElement.appendChild(username);

			// Device ID
			Element deviceID = doc.createElement("DEVICE_ID");
			deviceID.appendChild(doc.createTextNode(DEVICE_ID));
			rootElement.appendChild(deviceID);

			// Date
			Element date = doc.createElement("DATE");
			Date dt = new Date();
			int hours = dt.getHours();
			int minutes = dt.getMinutes();
			int seconds = dt.getSeconds();
			String curTime = hours + ":" + minutes + ":" + seconds;
			date.appendChild(doc.createTextNode(curTime));
			rootElement.appendChild(date);

			/*-----------------multiple child elements ----------GPS*/
			// gps Co ordinates
			Element GPS = doc.createElement("GPS_COORD");
			rootElement.appendChild(GPS);

			// Latitude
			Element latitude = doc.createElement("LATITUDE");
			latitude.appendChild(doc.createTextNode(getValue("latitude")));
			GPS.appendChild(latitude);

			// longitude
			Element longitude = doc.createElement("LONGITUDE");
			longitude.appendChild(doc.createTextNode(getValue("longitude")));
			GPS.appendChild(longitude);
			/*----------------------------------------------*/

			// Common name
			Element common_name = doc.createElement("COMMON_NAME");
			common_name
					.appendChild(doc.createTextNode(getValue("common_name")));
			rootElement.appendChild(common_name);

			/*-----------------multiple child elements--------SCIENTIFIC NAME*/
			// SCIENTIFIC_NAME
			Element scientificName = doc.createElement("SCIENTIFIC_NAME");
			rootElement.appendChild(scientificName);

			// family
			Element family = doc.createElement("FAMILY");
			family.appendChild(doc.createTextNode(getValue("family")));
			scientificName.appendChild(family);

			// genus
			Element genus = doc.createElement("GENUS");
			genus.appendChild(doc.createTextNode(getValue("genus")));
			scientificName.appendChild(genus);

			// species
			Element species = doc.createElement("SPECIES");
			species.appendChild(doc.createTextNode(getValue("species")));
			scientificName.appendChild(species);
			
			// variety
			Element variety = doc.createElement("VARIETY");
			variety.appendChild(doc.createTextNode(getValue("variety")));
			scientificName.appendChild(variety);

			/*----------------------------------------------*/

			// comments
			Element comments = doc.createElement("COMMENTS");
			comments.appendChild(doc.createTextNode(getValue("comments")));
			rootElement.appendChild(comments);

			/*-----------------multiple child elements ---------MORE FIELDS*/
			// More fields
			Element moreField = doc.createElement("MORE_FIELDS");
			rootElement.appendChild(moreField);

			// extra 1
			Element extra1 = doc.createElement("EXTRA1");
			extra1.appendChild(doc.createTextNode(getMoreFieldValue(0)));
			moreField.appendChild(extra1);

			// extra2
			Element extra2 = doc.createElement("EXTRA2");
			extra2.appendChild(doc.createTextNode(getMoreFieldValue(1)));
			moreField.appendChild(extra2);

			// species
			Element extra3 = doc.createElement("EXTRA3");
			extra3.appendChild(doc.createTextNode(getMoreFieldValue(2)));
			moreField.appendChild(extra3);

			/*----------------------------------------------*/

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filename));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

	/*private String getValue(String key) {
		if (_getNotesValuesListener == null) {
			String value = _moreInfoMap.get(key);
			if (value == null || value.equals("")) {
				return "";
			}
			return value;
		} else {
			return _getNotesValuesListener.NotesValues().get(key);
		}
	}
*/
	public String getValue(String key) {
		if (_getNotesValuesListener == null) {
			String value = _moreInfoMap.get(key);
			if (value == null || value.equals("")) {
				return "";
			}
			return value;
		} else {
			return _getNotesValuesListener.NotesValues().get(key);
		}
	}
	
	public String getMoreFieldValue(int index) {
		if (_getNotesValuesListener != null) {
			return _getNotesValuesListener.moreFieldValues().get(index);
		}
		return "";
	}

	public void destroyAllSessionContent() {

		deleteFile(_xmlFilename);
		if (_images != null) {
			for (String filepath : _images) {
				deleteFile(filepath);
			}
			_images.clear();
		}
		setIsMoreInfoDetailsPopulated(false);
		if (_orgInfoRows != null) {
			_orgInfoRows.clear();
		}
		_orgInfoRows = null;
		_snapcount = 0;
		_observeTabChangeListener = null;
		_loadPhotoListener = null;
		_isConfigIncomplete = false;
		_isConfigModified = false;
		_isNotesModified = false;
		_getNotesValuesListener = null;
		_updateAppConfigurer = null;
	}

	public void setIsConfigModified(boolean value) {
		_isConfigModified = value;
	}

	private void deleteFile(String filename) {
		if (filename != null && filename != "") {
			File file = new File(filename);
			if (file.exists()) {
				file.delete();
			}
		}
	}

	public String getXmlFileName() {
		return _xmlFilename;
	}

	public void setIsMoreInfoDetailsPopulated(boolean value) {
		_isMoreInfoDetailsPopulated = value;
	}

	
	public void setIsNotesModified(boolean b) {
		_isNotesModified = b;

	}

	public void registerAppConfigurer(IUpdateAppConfiguration listener) {

		_updateAppConfigurer = listener;
	}

	public void updateAppConfig() {
		if (_updateAppConfigurer != null) {
			_updateAppConfigurer.updateData();
		}
	}

	public SharedPreferences getSharedPrefs() {
		return _myPrefs;
	}

	public ArrayList<MoreInfoRow> getMoreinfoRows() {
		if (_orgInfoRows == null) {
			Log.d("NOTES",_orgID);
			processOrganismNode();
		}
		Log.d("NOTES-outside",_orgID);
		return _orgInfoRows;
	}

	public ArrayList<Boolean> getIsLeafNodeList() {
		return _isLeafNodeList;
		// TODO Auto-generated method stub
		
	}

	public void resetGlossary() {
		if (_resetGlossarylistener != null) {
			_resetGlossarylistener.resetGlossary();
		}
		
	}

	public void registerResetGlossaryListener(IResetGlossaryListener listener) {
		_resetGlossarylistener = listener;
		
	}

}
