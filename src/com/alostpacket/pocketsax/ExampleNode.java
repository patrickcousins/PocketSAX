package com.alostpacket.pocketsax;




/**
 * Example RSS node base on Wikipedia entry for RSS.  All fields are public for simplicity.   
 * But with annotations these could be made private and/or use setTitle()  etc.
 * 
 * <pre>{@code
<item>
  <title>Example entry</title>
  <description>Here is some text containing an interesting description.</description>
  <link>http://www.wikipedia.org/</link>
  <guid>unique string per item</guid>
  <pubDate>Mon, 06 Sep 2009 16:20:00 +0000 </pubDate>
 </item>

 } 
 </pre>
 * @author patrick cousins
 *
 */
public class ExampleNode
{
	public String title;
	
	public String description;
	
	public String link;
	
	public String guid;
	
	public String pubDate;
}
