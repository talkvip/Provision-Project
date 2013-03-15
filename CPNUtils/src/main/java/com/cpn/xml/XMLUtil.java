package com.cpn.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class XMLUtil {
	private static final DocumentBuilder builder;
	private static final XPath xPath = XPathFactory.newInstance().newXPath();

	static {
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (final ParserConfigurationException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static final String prettyPrint(final Document aNode) throws IOException {
		OutputFormat format = new OutputFormat(aNode);
		format.setLineWidth(65);
		format.setIndenting(true);
		format.setIndent(2);
		Writer out = new StringWriter();
		XMLSerializer serializer = new XMLSerializer(out, format);
		serializer.serialize(aNode);

		return out.toString();
	}

	public static final String print(final Document aNode) throws IOException {
		OutputFormat format = new OutputFormat(aNode);
		format.setLineWidth(Integer.MAX_VALUE);
		format.setIndenting(false);
		format.setLineSeparator("");
		Writer out = new StringWriter();
		XMLSerializer serializer = new XMLSerializer(out, format);
		serializer.serialize(aNode);
		return out.toString();
	}

	@SuppressWarnings("unchecked")
	public static final <T> List<T> toList(final NodeList aList) {
		final List<T> list = new ArrayList<T>();
		for (int x = 0; x < aList.getLength(); ++x) {
			list.add((T) aList.item(x));
		}
		return list;
	}

	public static final List<String> toStringList(final List<Node> someNodes) {
		final List<String> list = new ArrayList<>();
		for (final Node n : someNodes) {
			list.add(n.getNodeValue());
		}
		return list;
	}

	public static final List<String> toStringList(final NodeList aList) {
		return toStringList(XMLUtil.<Node> toList(aList));
	}

	public static final Document toXML(final String anXMLDoc) {
		try {
			return builder.parse(new ByteArrayInputStream(anXMLDoc.getBytes()));
		} catch (SAXException | IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public static final <T> T xPath(final Node aNode, final String anXPath, final QName aQName) throws XPathExpressionException {
		return (T) xPath.compile(anXPath).evaluate(aNode, aQName);
	}

	public static final int xPathInteger(final Node aNode, final String anXPath) throws XPathExpressionException {
		return Integer.parseInt((String) xPath.compile(anXPath).evaluate(aNode, XPathConstants.STRING));
	}

	public static final List<Node> xPathList(final Node aNode, final String anXPath) throws XPathExpressionException {
		return toList(XMLUtil.<NodeList> xPath(aNode, anXPath, XPathConstants.NODESET));
	}

	public static final String xPathString(final Node aNode, final String anXPath) throws XPathExpressionException {
		return (String) xPath.compile(anXPath).evaluate(aNode, XPathConstants.STRING);
	}

	public static final List<String> xPathStringList(final Node aNode, final String anXPath) throws XPathExpressionException {
		return toStringList(XMLUtil.<NodeList> xPath(aNode, anXPath, XPathConstants.NODESET));
	}

	public static final Node xPathNode(final Node aNode, final String anXPath) throws XPathExpressionException {
		return XMLUtil.<Node> xPath(aNode, anXPath, XPathConstants.NODE);
	}

	private final Node node;

	public XMLUtil(final Node aNode) {
		node = aNode;
	}

	public String get(final String anXPath) throws XPathExpressionException {
		return xPathString(node, anXPath);
	}

	public Calendar getCalendar(final String anXPath) throws XPathExpressionException {
		final Calendar c = new GregorianCalendar();
		Date d;
		try {
			d = DateFormat.getDateInstance().parse(get(anXPath));
		} catch (final ParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		c.setTime(d);
		return c;

	}

	public int getInteger(final String anXPath) throws XPathExpressionException {
		return xPathInteger(node, anXPath);
	}

	public List<Node> getList(final String anXPath) throws XPathExpressionException {
		return xPathList(node, anXPath);
	}

	public String getString(final String anXPath) throws XPathExpressionException {
		return get(anXPath);
	}

	public List<String> getStringList(final String anXPath) throws XPathExpressionException {
		return xPathStringList(node, anXPath);
	}

	public Node getNode(final String anXPath) throws XPathExpressionException {
		return xPathNode(node, anXPath);
	}
}
