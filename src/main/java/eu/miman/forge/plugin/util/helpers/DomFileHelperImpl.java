package eu.miman.forge.plugin.util.helpers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.codehaus.plexus.util.xml.XmlStreamReader;
import org.codehaus.plexus.util.xml.XmlStreamWriter;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class DomFileHelperImpl implements DomFileHelper {

	/* (non-Javadoc)
	 * @see se.miman.forge.plugin.util.helpers.DomFileHelper#readXmlResourceFile(java.lang.String)
	 */
	@Override
	public Xpp3Dom readXmlResourceFile(String filename) throws IOException, XmlPullParserException {
		Xpp3Dom dom = null;
		InputStream stream = this.getClass().getResourceAsStream(filename);
		if (stream.available() > 0) {
			XmlStreamReader reader = new XmlStreamReader(stream);
			dom = Xpp3DomBuilder.build(reader);
		}
		stream.close();
		return dom;
	}

	/* (non-Javadoc)
	 * @see se.miman.forge.plugin.util.helpers.DomFileHelper#readXmlFile(java.lang.String)
	 */
	@Override
	public Xpp3Dom readXmlFile(String filename) throws IOException, XmlPullParserException {
		Xpp3Dom dom = null;
		FileInputStream stream = new FileInputStream(filename);
		if (stream.available() > 0) {
			XmlStreamReader reader = new XmlStreamReader(stream);
			dom = Xpp3DomBuilder.build(reader);
		}
		stream.close();
		return dom;
	}

	/* (non-Javadoc)
	 * @see se.miman.forge.plugin.util.helpers.DomFileHelper#writeXmlFile(java.lang.String, org.codehaus.plexus.util.xml.Xpp3Dom)
	 */
	@Override
	public void writeXmlFile(String filename, Xpp3Dom dom) throws IOException, XmlPullParserException {
		FileOutputStream stream = new FileOutputStream(filename);
		XmlStreamWriter writer = new XmlStreamWriter(stream);
		writer.write(dom.toString());
		writer.flush();
		stream.flush();
		writer.close();
		stream.close();
	}
}
