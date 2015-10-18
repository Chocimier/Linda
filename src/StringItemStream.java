import java.io.OutputStream;
import java.lang.String;
import javax.microedition.lcdui.StringItem;

public class StringItemStream extends OutputStream {
    StringItem item;
    byte[] buffer = new byte[0];

    public StringItemStream(StringItem outputItem) {
        item = outputItem;
    }

    public void flush() {
        item.setText(item.getText() + new String(buffer));

        buffer = new byte[0];
    }

    public void write(byte[] b) {
        byte[] newBuffer = new byte[buffer.length+b.length];

        System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
        System.arraycopy(b, 0, newBuffer, buffer.length, b.length);

        buffer = newBuffer;
    }

    public void write(byte[] b, int off, int len) {
        byte[] c = new byte[len];

        System.arraycopy(b, off, c, 0, len);
        write(c);
    }

    public void write(int b) {
        byte[] arr = {(byte)b};

        write(arr);
    }
}
