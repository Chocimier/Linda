import java.io.PrintStream;
import java.util.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.jme.*;

public class Linda extends MIDlet implements CommandListener {
    TextBox code = new TextBox("Kod", "", 300, TextField.ANY);
    Form output = new Form("Wynik");
    Form executing = new Form("Obliczanie");
    List insertMenu = new List("Wstaw…", List.IMPLICIT);
    StringItem item = new StringItem("", "");
    Command comInsertMenu = new Command("Wstaw…", Command.SCREEN, 50);
    Command comInsert = new Command("Wstaw", Command.SCREEN, 50);
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

        pastes.put("Warunek", "if  then\n\nend\n");
        pastes.put("Funkcję", "function ()\n\nend\n");
        pastes.put("Pętlę for", "for i=, do\n\nend\n");
        pastes.put("Pętlę while", "while  do\n\nend\n");
        pastes.put("Werunek else", "\nelse\n");
        pastes.put("Warunek elseif", "elseif  then\n\n");
        pastes.put("return", "return ");
        pastes.put("Pętlę repeat", "repeat\n\nuntil ;\n");
        pastes.put("print()", "print() ");
        pastes.put("local", "local ");
        pastes.put("Prawdę", "true");
        pastes.put("Nieprawdę", "false");
        pastes.put("tonumber", "tonumber()");
        pastes.put("tostring", "tostring()");
        pastes.put("string.()", "string.()");
        pastes.put("math.()", "math.()");

        insertMenu.addCommand(comCode);
        insertMenu.setSelectCommand(comInsert);
        insertMenu.setCommandListener(this);

        Enumeration e = pastes.keys();

        while (e.hasMoreElements()) {
            insertMenu.append(e.nextElement().toString(), null);
        }

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
        } else if (command==comInsert) {
            insert(insertMenu.getSelectedIndex());
            show(code);
        }
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

    void insert(int nr)
    {
        int caret = code.getCaretPosition();
        String current = code.getString();
        String pasted = pastes.get(insertMenu.getString(nr)).toString();

        code.setString(current.substring(0, caret) + pasted + current.substring(caret));
    }
}
