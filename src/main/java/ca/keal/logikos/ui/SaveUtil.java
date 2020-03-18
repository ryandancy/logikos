package ca.keal.logikos.ui;

import ca.keal.logikos.field.Field;
import ca.keal.logikos.util.DeserializationException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.swing.text.html.parser.Parser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * Utilities for saving {@link Field}s to files.
 */
final class SaveUtil {

  /**
   * Attempt to save without user input; if we don't have a filename, see {@link #saveAs(Stage, Field)}.
   */
  static void save(Stage stage, Field field) {
    if (field.getFilename() == null || field.getName() == null) {
      saveAs(stage, field);
    } else {
      writeAndCheck(field);
    }
  }

  /**
   * Prompt the user for a filename and (if not present) a name for the field, then save.
   */
  static void saveAs(Stage stage, Field field) {
    // TODO a method for changing the name
    if (field.getName() == null) {
      String name = promptForName();
      if (name == null) return;
      field.setName(name);
    }
    
    String filename = promptForFilenameToSave(stage);
    if (filename == null) return;
    field.setFilename(filename);
    
    writeAndCheck(field);
  }
  
  private static String promptForFilenameToSave(Stage stage) {
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Save as...");
    chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Logikos Files", "*.lgk"));
    chooser.setInitialFileName("new-gate.lgk");
    File selected = chooser.showSaveDialog(stage);
    return selected == null ? null : selected.getAbsolutePath();
  }
  
  private static String promptForName() {
    TextInputDialog dialog = new TextInputDialog("New Gate");
    dialog.setTitle("Enter component name...");
    dialog.setContentText("Enter a name for the component which will be shown in its symbol.");
    dialog.getDialogPane().getButtonTypes().add(
        new ButtonType("Done", ButtonBar.ButtonData.OK_DONE));
    Optional<String> result = dialog.showAndWait();
    return result.orElse(null);
  }
  
  private static void writeAndCheck(Field field) {
    try {
      write(field);
    } catch (IOException | ParserConfigurationException | TransformerException e) {
      e.printStackTrace();
      Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred while saving.\n" + e.getMessage());
      alert.show();
    }
  }
  
  private static void write(Field field) throws IOException, ParserConfigurationException, TransformerException {
    if (field.getFilename() == null || field.getName() == null) {
      throw new IllegalArgumentException("No filename or no name specified for file.");
    }
    
    // serialize to XML
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = factory.newDocumentBuilder();
    Document doc = docBuilder.newDocument();
    Element root = field.toXml(doc);
    doc.appendChild(root);
    
    // write to the file
    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
    transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // for now
    transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
    transformer.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(field.getFilename())));
  }

  /**
   * Open a field from a file and return the opened field.
   */
  static Field open(Stage stage) {
    String filename = promptForFilenameToOpen(stage);
    if (filename == null) return null;
    
    try {
      Field field = parseField(filename);
      field.setFilename(filename);
      return field;
    } catch (DeserializationException | ParserConfigurationException | SAXException | IOException e) {
      e.printStackTrace();
      Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred while opening.\n" + e.getMessage());
      alert.show();
      return null;
    }
  }
  
  private static String promptForFilenameToOpen(Stage stage) {
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Open...");
    chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Logikos Files", "*.lgk"));
    File selected = chooser.showOpenDialog(stage);
    return selected == null ? null : selected.getAbsolutePath();
  }
  
  private static Field parseField(String filename) throws DeserializationException, ParserConfigurationException,
      SAXException, IOException {
    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document doc = builder.parse(new File(filename));
    Element root = doc.getDocumentElement();
    return Field.fromXml(root);
  }
  
}
