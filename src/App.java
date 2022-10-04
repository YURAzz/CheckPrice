import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xpath.CachedXPathAPI;
import org.apache.xpath.operations.Or;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class App {

  public static void main(String[] args) throws Exception {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc = db.parse(new File("Title.xml"));
    CheckPrice(doc);
  }

  public static Document CheckPrice(Document doc) {

    Set errorSet = new HashSet();
    HashMap resolutionPrices = new HashMap();
    CachedXPathAPI v = new CachedXPathAPI();
    Integer i = 0;

    String schedulationPath = "/content/group/definition/list[@name='schedulations']/value/schedulation";
    String EstDto = "./group/definition/taxonomylink/value";
    String fruitionPath = "./list[@name='fruitionList']/value/fruition";
    String labelPath = "//taxonomylink[@name='toResolutionTax']/value/ti/@label";
    String idPath = "./taxonomylink[@name='toPriceTax']/value/ti/@id ";

    try {

        NodeList schedulationList = v.selectNodeList(doc, schedulationPath);
        NodeList estDto = v.selectNodeList(schedulationList.item(i), EstDto);

        try {
          Result output = new StreamResult(new File("output.xml"));
          Transformer transformer = TransformerFactory.newInstance().newTransformer();
          Source input = new DOMSource(schedulationList.item(0));
          transformer.transform(input, output);
      } catch ( TransformerFactoryConfigurationError | TransformerException e) {
          e.printStackTrace();
      }

        for (i = 0; i < schedulationList.getLength(); i++) {

          NodeList fruitionNodes = v.selectNodeList(schedulationList.item(i), fruitionPath);

          for (int k = 0; k < fruitionNodes.getLength(); k++){

                  Element currentTypeEl = (Element) estDto.item(k);
                  String currentType = currentTypeEl.getAttribute("descr");
                  NodeList path = v.selectNodeList(fruitionNodes.item(k), labelPath);
                  String currentKey = path.item(k).toString();
                  NodeList ids = v.selectNodeList(schedulationList.item(i), idPath);
                  String id = ids.item(k).toString();



            }
          }
    } catch (TransformerException e1) {
        e1.printStackTrace();
      }

    return doc;
  }
}