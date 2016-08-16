package core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import common.Logger;
import common.Utils;

import core.beans.Options;

public class Parser {
	private static Parser instance;

	public static synchronized Parser getInstance() {
		if (instance == null) {
			instance = new Parser();
		}
		return instance;
	}

	public List<Options> parse(String fileName) {

		List<Options> ret = new ArrayList<Options>();
		Options tmp;
		File inputFile = new File(fileName);
		DocumentBuilder dBuilder = Utils.getDocumentBuilder();
		Document doc = null;
		try {
			doc = dBuilder.parse(inputFile);
		} catch (SAXException e ) {
			Logger.error(e.getMessage());

		}
		catch(IOException e)
		{
			Logger.error(e.getMessage());
		}
		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("module");

		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;
				tmp = new Options(eElement.getAttribute("class"));
				NodeList moduleOptions = nNode.getChildNodes();

				for (int j = 0; j < moduleOptions.getLength(); j++) {
					if (moduleOptions.item(j).getNodeName()
							.equalsIgnoreCase("#text")) {
						continue;
					}
					tmp.add(moduleOptions.item(j).getNodeName(), moduleOptions
							.item(j).getTextContent());

				}
				ret.add(tmp);
				
			}
		}
		return ret;
	}
}
