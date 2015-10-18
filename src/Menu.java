import java.io.OutputStream;
import java.io.InputStream;
import java.lang.String;
import java.util.Vector;

import javax.microedition.lcdui.*;

import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.*;

import javax.microedition.lcdui.List;
import org.xml.sax.*;

class MenuItemType {
    static int Menu = 1;
    static int Item = 2;
}

class MenuItemData {
    String name = new String("");
    Object data = null;
    int id;
    MenuItemData parent;
    int type;

    public String toString() {
        return name;
    }
}

class MenuData extends MenuItemData {
    Vector items = new Vector();

    public String toString() {
        String result = name + "(";

        for (int i=0; i<items.size(); ++i) {
            if (i>0) {
                result += ", ";
            }

            result += items.elementAt(i).toString();
        }

        result += ")";

        return result;
    }
}

public class Menu extends List implements CommandListener {
    MenuData main = null;
    MenuData current = null;
    Command comChoose = new Command("Wybierz", Command.SCREEN, 1);
    Command comBack = new Command("Wstecz", Command.SCREEN, 1);
    MenuCaller caller = null;

    public Menu(String path, MenuCaller newCaller) {
        super(null, IMPLICIT);
        caller = newCaller;
            addCommand(comBack);
            setSelectCommand(comChoose);
            setCommandListener(this);

        try {
            MenuBuilder handler = new MenuBuilder();
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser = SAXParserFactory.newInstance().newSAXParser();

            parser.parse(getClass().getResourceAsStream(path), handler);

            main = handler.getMenu();
        } catch (javax.xml.parsers.ParserConfigurationException e) {
        } catch (org.xml.sax.SAXException e) {
        } catch (java.io.IOException e) {
        }

        setSelectCommand(comChoose);
        addCommand(comBack);
        setCommandListener(this);

        System.out.println(main == null);
        System.out.println(main);

        setCurrent((MenuData)main);
    }

    void setCurrent(MenuData newCurrent) {
        current = newCurrent;

        deleteAll();

        setTitle(current.name);

        for (int i=0; i<current.items.size(); ++i) {
        System.out.println(((MenuItemData)current.items.elementAt(i)).name);
            append(((MenuItemData)current.items.elementAt(i)).name, null);
        }
    }

    public void commandAction(Command command, Displayable screen) {
        if (command == comChoose) {
            int index = getSelectedIndex();
            MenuItemData item = ((MenuItemData)current.items.elementAt(index));

            if (item.type == MenuItemType.Menu) {
                setCurrent((MenuData)item);
            } else {
                caller.itemChosen(this, item.data);
                setCurrent(main);
            }
        } else if (command == comBack) {
            if (current.parent != null) {
                setCurrent((MenuData)current.parent);
            } else {
                caller.menuCanceled(this);
            }
        }
    }
}
