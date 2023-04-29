package Lab6;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ArxmlSorter {
    public static void main(String[] args) {
        if (!checkNumberOfArguments(args.length))
            return;

        // getting the file name from command line
        String fileName = args[0];

        try {

            checkFileExtension(fileName);

            String outputFileName = getModifiedFileName(fileName);

            File inputFile = new File(fileName);

            checkEmptyFile(inputFile);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputFile);

            doc.getDocumentElement().normalize();

            NodeList List = doc.getElementsByTagName("CONTAINER");
            ArrayList<Element> containers = new ArrayList<Element>();

            for (int i = 0; i < List.getLength(); i++) {
                containers.add((Element) List.item(i));
            }

            Collections.sort(containers, new SortContainer());

            Element rootElement = doc.getDocumentElement();
            for (Element container : containers) {
                rootElement.appendChild(container);
            }

            // Write document to output stream
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            // Activating pretty print format
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(outputFileName));
            transformer.transform(source, result);

            System.out.println("Sorting was done successfully!.");

        } catch (NotVaildAutosarFileException e) {
            System.err.println("Error: wrong file extension, " + e.getMessage() + "\n.arxml file is required");
        } catch (EmptyAutosarFileException e) {
            System.err.println("Error: " + e.getMessage() + " is empty!.");
        } catch (ParserConfigurationException | SAXException | IOException
                | TransformerException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public static boolean checkNumberOfArguments(int count) {
        if (count == 1)
            return true;

        System.out.println("Wrong number of arguments!\nExample:");
        System.out.println("java ArxmlSorter \"input file name\"");
        return false;
    }

    public static String getModifiedFileName(String inputFile) {
        int indexOfLastDot = inputFile.lastIndexOf(".");
        String nameWithoutExtension = inputFile.substring(0, indexOfLastDot);
        String outputName = nameWithoutExtension + "_mod";
        String extension = inputFile.substring(indexOfLastDot + 1);
        outputName += "." + extension;
        return outputName;
    }

    public static void checkEmptyFile(File inputFile)
            throws EmptyAutosarFileException {
        if (inputFile.length() == 0)
            throw new EmptyAutosarFileException(inputFile.getName());

    }

    public static void checkFileExtension(String inputFile)
            throws NotVaildAutosarFileException {
        if (!inputFile.endsWith(".arxml"))
            throw new NotVaildAutosarFileException(inputFile);
    }
}

class SortContainer implements Comparator<Element> {

    @Override
    public int compare(Element o1, Element o2) {
        String name1 = o1.getElementsByTagName("SHORT-NAME").item(0).getTextContent();
        String name2 = o2.getElementsByTagName("SHORT-NAME").item(0).getTextContent();
        return name1.compareTo(name2);
    }

}

// Checked exception so it extends Exception class to be checked at compile time
class NotVaildAutosarFileException extends Exception {
    public NotVaildAutosarFileException(String msg) {
        super(msg);
    }
}

// Unchecked exception so it extends RuntimeException class to be checked at
// run-time
class EmptyAutosarFileException extends RuntimeException {
    public EmptyAutosarFileException(String msg) {
        super(msg);
    }
}