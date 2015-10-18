import java.io.PrintStream;
import java.util.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.jme.*;

public class Linda extends MIDlet implements CommandListener, MenuCaller {
    TextBox code = new TextBox("Kod", "", 300, TextField.ANY);
    Form output = new Form("Wynik");
    Form executing = new Form("Obliczanie");
    Menu insertMenu = new Menu("/wstawki.xml", this);
    StringItem item = new StringItem("", "");
    Command comInsertMenu = new Command("Wstaw…", Command.SCREEN, 50);
    Command comCode = new Command("Kod", Command.SCREEN, 100);
    Command comExecute = new Command("Wykonaj", Command.SCREEN, 200);
    Command comOutput = new Command("Wynik", Command.SCREEN, 300);
    Command comExit = new Command("Wyjdź", Command.EXIT, 400);
    Command comStop = new Command("Zatrzymaj", Command.SCREEN, 1);
    PrintStream stream = new PrintStream(new StringItemStream(item));
    Hashtable pastes = new Hashtable();

    public Linda() {
        output.addCommand(comExecute);
        output.addCommand(comCode);
        output.addCommand(comExit);
        output.setCommandListener(this);
        output.append(item);

        executing.addCommand(comCode);
        executing.setCommandListener(this);
        executing.append("Obliczanie trwa");

        code.addCommand(comInsertMenu);
        code.addCommand(comExecute);
        code.addCommand(comOutput);
        code.addCommand(comExit);
        code.setCommandListener(this);


        show(code);
    }

    public void startApp() {}

    public void pauseApp() {}

    public void destroyApp(boolean force) {
        this.notifyDestroyed();
    }

    public void commandAction(Command command, Displayable screen) {
        if (command==comExecute) {
            show(executing);
            item.setText("");
            execute(code.getString());
            show(output);
        } else if (command==comCode) {
            show(code);
        } else if (command==comOutput) {
            show(output);
        } else if (command==comExit) {
            destroyApp(true);
        } else if (command==comStop) {
            show(executing);
        } else if (command==comInsertMenu) {
            show(insertMenu);
        }
    }

    public void itemChosen(Menu menu, Object data) {
        insert((String) data);
        show(code);
    }
    public void menuCanceled(Menu menu) {
        show(code);
    }

    void show(Displayable ekran) {
        Display.getDisplay(this).setCurrent(ekran);
    }

    void execute(String kod) {
        Globals globals = JmePlatform.standardGlobals();
        globals.STDOUT = stream;

        globals.load(kod).call();
        stream.flush();
    }

    void insert(String paste) {
        code.insert(paste, code.getCaretPosition());
    }
}
