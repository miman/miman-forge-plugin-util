package se.miman.forge.plugin.util.helpers;

import java.io.IOException;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

/**
 * A helper class for working with files containing XML formatted data.
 * 
 * @author Mikael
 */
public interface DomFileHelper {

	/**
	 * Reads the file with the given path + name into a DOM structure (the 
	 * path + filename is from the project resource folder).
	 * @param filename	The filename of the file containing the XML resource
	 * @return	The DOM object read from the file
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public abstract Xpp3Dom readXmlResourceFile(String filename)
			throws IOException, XmlPullParserException;

	/**
	 * Reads the file with the given path + name into a DOM structure (the 
	 * path + filename is related to the actual file system).
	 * @param filename	The filename of the file containing the XML resource
	 * @return	The DOM object read from the file
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public abstract Xpp3Dom readXmlFile(String filename) throws IOException,
			XmlPullParserException;

	/**
	 * Stores the XML generated from the DOM structure in to the path + filename given (relative to the file system, not resource)
	 * @param filename	The path + filename where to store the file
	 * @param dom	The object that is converted into XML and stored into the file
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public abstract void writeXmlFile(String filename, Xpp3Dom dom)
			throws IOException, XmlPullParserException;

}