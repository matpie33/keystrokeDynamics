package pl.master.thesis.others;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
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

import pl.master.thesis.features.Feature;
import pl.master.thesis.myOwnClassification.DataStatistics;

public class DataSaver {
	
	private String rootNode = "UserData";
	
	private String featureValueTag = "Value";
	private String featureWeightTag = "Weight";
	
	private String directoryName = "usersData";
	private String extension = ".xml";
	
	public void saveDataToFile(DataStatistics data){
		    Document dom;
		    
		    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		    try {
		    	
		        DocumentBuilder db = dbf.newDocumentBuilder();	       
		        dom = db.newDocument();

		        Element rootElement = dom.createElement(rootNode);
        		
        		for (Feature f: data.getFeatures()){
        			createFeature(rootElement, dom, f.getName(), f.getValue(), f.getWeight() );
        		}
    	        
		        dom.appendChild(rootElement);

		        try {
		            Transformer tr = TransformerFactory.newInstance().newTransformer();
		            tr.setOutputProperty(OutputKeys.INDENT, "yes");
		            tr.setOutputProperty(OutputKeys.METHOD, "xml");
		            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		            String filename = directoryName+File.separator+data.getUserId()+extension;
		            File file = new File(directoryName);
		            file.mkdir();
		            tr.transform(new DOMSource(dom), new StreamResult(
		            		new FileOutputStream(filename)));
		        } 
		        catch (TransformerException te) {
		        	te.printStackTrace();
		        } 
		        catch (IOException ioe) {
		        	ioe.printStackTrace();
		        }
		    }
		    catch (ParserConfigurationException pce) {
		    	pce.printStackTrace();
		    }
		    
		}
	
	private void createFeature (Element e, Document dom, String featureName, double value, double weight){
		
		Element e1 = dom.createElement(featureName);
		
        Element e2 = dom.createElement(featureValueTag);
        e2.appendChild(dom.createTextNode(""+value));
        Element e3 = dom.createElement(featureWeightTag);
        e3.appendChild(dom.createTextNode(""+weight));
        
        
        e1.appendChild(e2);
        e1.appendChild(e3);
        e.appendChild(e1);
	}
	
	public List <Feature> readFromXml(String filename) throws ParserConfigurationException, SAXException, IOException{
		File file = new File(filename);
		if (!file.exists()){
			return new ArrayList <>();
		}
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
		        .newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(file);
		
		Node e = document.getElementsByTagName(rootNode).item(0);
		NodeList list = e.getChildNodes();
		List<Feature> features = new ArrayList <>();
		for (int i=0; i<list.getLength(); i++){
			Node node = list.item(i);
			if (node instanceof Element == false){
				continue;
			}
			Element element = getXmlElement(node);	
			String featureName = node.getNodeName();
			String value = element.getElementsByTagName(featureValueTag).item(0)
					.getTextContent();
			double valueD = Double.parseDouble(value);
			String weight = element.getElementsByTagName(featureWeightTag).item(0)
					.getTextContent();
			double weightD = Double.parseDouble(weight);
			Feature f = new Feature(featureName, valueD);
			f.setWeight(weightD);
			features.add(f);
		}
		return features;
	}
	
	private Element getXmlElement (Node node){
		Element element = null;
		if (node.getNodeType() == Node.ELEMENT_NODE){
			element = (Element) node;
		}
		return element;
	}
	
}
