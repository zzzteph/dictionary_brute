package core;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import common.Utils;

public class ModuleParser {
	private static ModuleParser instance;
	
	public static synchronized ModuleParser getInstance() {
		if (instance == null) {
			instance = new ModuleParser();
		}
		return instance;
	}
	public void parse(String fileName) {

			File inputFile = new File(fileName);
			DocumentBuilder dBuilder = Utils.getDocumentBuilder();
			Document doc = null;
			try {
				doc = dBuilder.parse(inputFile);
			} catch (SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("module");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					System.out.println(eElement.getAttribute("class"));
					NodeList moduleOptions = nNode.getChildNodes();
					
					 for (int j = 0; j < moduleOptions.getLength(); j++) {
	                        if (moduleOptions.item(j).getNodeName().equalsIgnoreCase("#text")) {
	                            continue;
	                        }
	                        System.out.println(j + ">   " + moduleOptions.item(j).getTextContent());
	                        System.out.println(j + ">   " + moduleOptions.item(j).getNodeName());
					 }
				}
			}

	}
}
