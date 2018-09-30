package commonUtil;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.swing.text.JTextComponent;

public class ClipboardUtil {
    
    public static void cut( Component component ) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        if( component instanceof JTextComponent ) {
            String copiedString = ((JTextComponent)component).getText();
            clipboard.setContents( new StringSelection( copiedString ), null );
        }
        
    }
    
    public static void copy( Component component ) {
        
    }
    
    public static void paste( Component component ) {
        
    }
}
