import java.io.File;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.apache.xpath.CachedXPathAPI;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;

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

    String schedulationPath = "/content/group/definition/list[@name='schedulations']/value/schedulation";
    String EstDto = "./group/definition/taxonomylink/value/@descr";
    String fruitionPath = "./list[@name='fruitionList']/value/fruition";
    String labelPath = "./list/value/fruition/taxonomylink[@name='toResolutionTax']/value/ti/@label";
    String idPath = "./list/value/fruition/taxonomylink[@name='toPriceTax']/value/ti/@id ";

    try {
      NodeList schedulationList = v.selectNodeList(doc, schedulationPath);

      for (int i = 0; i < schedulationList.getLength(); i++) {
        NodeList fruitionNodes = v.selectNodeList(schedulationList.item(i), fruitionPath);
        NodeList estDto = v.selectNodeList(schedulationList.item(i), EstDto);
        String currentType = estDto.item(0).getTextContent();

        if (currentType.equals("DTO") || currentType.equals("EST")) {

          for (int k = 0; k < fruitionNodes.getLength(); k++) {
            NodeList path = v.selectNodeList(schedulationList.item(i), labelPath);
            NodeList ids = v.selectNodeList(schedulationList.item(i), idPath);
            String currentKey = path.item(k).getTextContent();
            String currentId = ids.item(k).getTextContent();

            if (resolutionPrices.containsKey(currentKey)) {
              String check = resolutionPrices.get(currentKey).toString();

              if (check.equals(currentId)) {
              } else {
                errorSet.add("Per la quality " + currentKey + " di fruition " + currentType +
                    " sono presenti price category differenti, currentKey mismatch: " + check + " " + currentId);
              }
            } else
              resolutionPrices.put(currentKey, currentId);
          }
        }
      }
    } catch (TransformerException e) {
      e.printStackTrace();
    }
    return doc;
  }
}