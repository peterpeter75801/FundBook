package commonUtil;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

import javax.swing.text.JTextComponent;

public class ClipboardUtil {
    
    public static void cut( Component component ) {
        if( component instanceof JTextComponent ) {
            copy( component );
            ((JTextComponent) component).replaceSelection( "" );
        }
    }
    
    public static void copy( Component component ) {
        if( component instanceof JTextComponent ) {
            String copiedString = ((JTextComponent)component).getSelectedText();
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents( new StringSelection( copiedString ), null );
        }
    }
    
    public static void paste( Component component ) throws Exception {
        if( component instanceof JTextComponent ) {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            String copiedString = (String)clipboard.getContents( component ).getTransferData( DataFlavor.stringFlavor );
            ((JTextComponent) component).replaceSelection( copiedString );
        }
    }
}
