package de.uni.ki.p3.SVG;

import java.io.*;
import java.net.URI;

import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.svg.SVGDocument;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;


public class SVGParsing { 
	public static SVGDocument toSVGDocument(String fileLocation)
	{

        Document doc;
        SVGDocument svgDoc = null;
        try 
        {
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
            
            File file = new File(fileLocation);
            URI localFileAsUri = file.toURI(); 
            String uri = localFileAsUri.toASCIIString();
            
            doc = f.createDocument(uri);
            
            svgDoc = (SVGDocument)doc;
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
        return svgDoc;
	}
}