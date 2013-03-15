package com.cpn.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLNode {

	public static class XMLNodeFactory{
		Document document;
		public XMLNodeFactory(Document aDocument) {
			document = aDocument;
		}
		public XMLNode l(String aName, XMLNode... children){
			return new XMLNode(document, aName, children);
		}
		public XMLNode l(String aName, String attribute, String value, XMLNode... children){
			XMLNode n =  new XMLNode(document, aName, children);
			n.node.setAttribute(attribute, value);
			return n;
		}
		
		public XMLNode l(String aName, String aText){
			XMLNode x = new XMLNode(document, aName, new XMLNode[0]);
			x.node.appendChild(document.createTextNode(aText));
			return x;
		}
	}

	public Element node;
	public XMLNode(Document aDocument, String aName, XMLNode... nodes) {
		Element newNode = aDocument.createElement(aName);
		for(XMLNode n : nodes){
			newNode.appendChild(n.node);
		}
		node = newNode;
	}
	public XMLNode() {
		
	}
}
