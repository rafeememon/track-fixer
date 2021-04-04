package com.rafeememon.util;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;

public class Documents {

    public static Document readDocument(String path) throws Exception {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return documentBuilder.parse(new File(path));
    }

    public static void writeDocument(String path, Document document) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(path));
        transformer.transform(source, result);
    }

    @SuppressWarnings("deprecation")
    public static void removeXmlWhitespace(String path) throws IOException {
        File file = new File(path);
        String contents = FileUtils.readFileToString(file);
        String updated = contents.replaceAll(">\\s+<", "><");
        FileUtils.writeStringToFile(file, updated);
    }

    private Documents() {
        // utility class
    }

}
