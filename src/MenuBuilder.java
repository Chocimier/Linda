import java.io.OutputStream;
import java.io.InputStream;
import java.lang.String;
import java.util.Vector;

import javax.microedition.lcdui.StringItem;

import org.xml.sax.helpers.DefaultHandler;

import javax.microedition.lcdui.List;
import org.xml.sax.*;

public class MenuBuilder extends DefaultHandler {
    protected MenuData menu = null;
    protected MenuItemData currentItem = null;
    protected int idCounter = 0;

    public void startElement(String ns, String localName, String qName, Attributes attrs) throws SAXException {
        MenuItemData newItem;

        ++idCounter;

        if (qName.equals("menu")) {
            newItem = new MenuData();
            newItem.type = MenuItemType.Menu;

            if (menu == null) {
                menu = (MenuData) newItem;
            }
        } else if (qName.equals("item")) {
            newItem = new MenuItemData();
            newItem.type = MenuItemType.Item;
        } else {
            return;
        }

        if (attrs.getValue("name") != null) {
            newItem.name = attrs.getValue("name");
        }

        newItem.id = idCounter;
        newItem.parent = currentItem;

        if (newItem.parent != null) {
            ((MenuData)(newItem.parent)).items.addElement(newItem);
        }

        currentItem = newItem;
    }

    public void endElement(String ns, String localName, String qName) throws SAXException {
        currentItem = currentItem.parent;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        currentItem.data = new String(ch, start, length);

        if (currentItem.type == MenuItemType.Item && currentItem.name.length() == 0) {
            currentItem.name = (String) currentItem.data;
        }
    }

    public void clear() {
        menu = null;
        currentItem = null;
        idCounter = 0;
    }

    public MenuData getMenu() {
        return menu;
    }
}
