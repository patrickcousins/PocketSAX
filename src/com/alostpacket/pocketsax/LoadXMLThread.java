package com.alostpacket.pocketsax;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.os.Handler;
import android.util.Log;


public class LoadXMLThread extends Thread
{
	// Debugging
    private static final String 		TAG 								= "LoadXMLThread";
	private static final boolean 		D 									= true;
	
	
	private final Context	context;
	private final Handler	handler;


	public LoadXMLThread( Context c, Handler h)
	{
		this.context = c;
		this.handler = h;
	}
	

	@Override
	public void run()
	{
		if (D) Log.v ( TAG, "loading... " );

		XMLtoObjetParser 		xmlToVOHandler 		= new XMLtoObjetParser( handler );   
	    SAXParserFactory 	spf 				= SAXParserFactory.newInstance();
	    
	    try
	    {
	    	SAXParser 	sp 	= spf.newSAXParser();
	    	XMLReader 	xr 	= sp.getXMLReader();
	    	InputSource is 	= new InputSource();
	    	
	    	xr.setContentHandler(xmlToVOHandler);
	    	
	    	is.setEncoding( "UTF-8" );
	    	is.setByteStream( context.getAssets().open( "example.xml" ) );
	    	
	    	xr.parse(is);
	    	
	    	
	    }
	    catch (Exception e) 
	    {
	    	if (D) Log.e ( TAG, "loadXML Exception... " );
	    	if (D) e.printStackTrace ( );
		}
	}
	

}
