package com.alostpacket.pocketsax;

import java.lang.reflect.Field;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.os.Handler;
import android.util.Log;

/**
 * Creates data objects from XML nodes. Uses reflection to be more re-usable for
 * different types of data objects and XML. The XML format is somewhat specific
 * in this implementation though.
 * 
 * TODO: define an abstract versionName that can be extended for specific
 * implementations
 * 
 * @see /assets/appsOwnPermissions.xml
 * @see PropertyType
 * @author Patrick Cousins
 * 
 */

public class XMLtoObjetParser extends DefaultHandler
{

	// Debugging
	private static final String		TAG						= "PK_XMLtoVOParser";

	private Boolean					parsingParam			= false;
	private String					paramXMLNodeName		= "";
	private String					currentType				= PropertyType.STRING;
	private Object					tempObj					= null;
	private StringBuffer			stringValue;

	private Handler					handler;

	private static final String		NODE_CLASS_NAME			= "com.alostpacket.pocketsax.ExampleNode";
	private static final String		NODE_XML_NAME			= "item";

	public static final int			SEND_OBJECT_TO_HANDLER	= 666;

	private static final boolean	D						= true;

	private boolean					parsingItem				= false;

	public XMLtoObjetParser( Handler handler )
	{
		this.handler = handler;
		if ( D ) Log.d( TAG, "XMLtoObjetParser created" );
	}

	/**
	 * Called at start of element ex: <name> theoretically this could be called
	 * more than once per tag.... NOTE: some values hard-coded to save space in
	 * the xml
	 */
	@SuppressWarnings( { "unchecked", "rawtypes" } )
	@Override
	public void startElement( String uri, String localName, String qName, Attributes attributes ) throws SAXException
	{

		if ( NODE_XML_NAME.equals( localName ) ) // we found an NODE_XML_NAME node
		{
			try
			{
				Class[] classParm = null;

				Object[] objectParm = null;

				Class cl = Class.forName( NODE_CLASS_NAME );

				java.lang.reflect.Constructor co = cl.getConstructor( classParm );

				tempObj = co.newInstance( objectParm );

				parsingItem = true;
			}
			catch ( Exception e )
			{
				e.printStackTrace();
			}
		}
		else if ( parsingItem ) 
		{
			if ( D ) Log.v( TAG, "startElement found a child of item: "+localName );
			
			paramXMLNodeName = localName;
			parsingParam = true;
			stringValue = new StringBuffer();
		}

	}

	/**
	 * Called at end of element ex: </name> theoretically this could be called
	 * more than once per tag....
	 */
	@Override
	public void endElement( String uri, String localName, String qName ) throws SAXException
	{

		if ( NODE_XML_NAME.equals( localName ) ) // we've completed the Node, time to send it to the handler/adapter...
		{
			try
			{
				handler.obtainMessage( SEND_OBJECT_TO_HANDLER, tempObj ).sendToTarget();
				tempObj = null;
			}
			catch ( Exception e )
			{
				if ( D ) e.printStackTrace();
			}

		}
		else if ( paramXMLNodeName.equalsIgnoreCase( localName ) )
		{
			setField();
			stringValue = null;
			paramXMLNodeName = "";
			parsingParam = false;
			currentType = "";
		}

	}

	/**
	 * Called to get tag characters ex: <name>characters...</name> theoretically
	 * this could be called more than once per tag.... UPDATE: should be able to
	 * handle that now
	 */
	@Override
	public void characters( char[] ch, int start, int length ) throws SAXException
	{
		if ( stringValue == null )
		{
			stringValue = new StringBuffer();
		}

		if ( parsingParam )
		{

			for ( int i = start; i < length; i++ )
			{
				stringValue.append( ch[i] );
			}
		}
	}

	@SuppressWarnings( { "rawtypes" } )
	private void setField()
	{
		Class voClass = tempObj.getClass();
		Field[] fieldsArray = voClass.getFields();
		int len = fieldsArray.length;

		for ( int j = 0; j < len; j++ )
		{

			Field f = fieldsArray[j];

			if ( f.getName().equalsIgnoreCase( paramXMLNodeName ) )
			{
				// if (DebugMode.DEBUGGING) Log.d ( TAG, f.getName ( ) +": "+
				// paramXMLNodeName +":"+ stringValue.toString ( ) );
				try
				{
					if ( currentType.equalsIgnoreCase( PropertyType.STRING ) )
					{
						f.set( tempObj, stringValue.toString() );
					}
				}
				catch ( Exception e )
				{
					e.printStackTrace();
				}
			}
		}
	}

}
