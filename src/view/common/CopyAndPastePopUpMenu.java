package view.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import commonUtil.ClipboardUtil;

public class CopyAndPastePopUpMenu extends JPopupMenu {
    
    private static final long serialVersionUID = 1L;
    
    private MenuItemHandler menuItemHandler;
    
    private JMenuItem cutItem;
    private JMenuItem copyItem;
    private JMenuItem pasteItem;
    
    public CopyAndPastePopUpMenu() {
        menuItemHandler = new MenuItemHandler( this );
        
        cutItem = new JMenuItem( "剪下(T)" );
        cutItem.setMnemonic( 'T' );
        cutItem.addActionListener( menuItemHandler );
        add( cutItem );
        
        copyItem = new JMenuItem( "複製(C)" );
        copyItem.setMnemonic( 'C' );
        copyItem.addActionListener( menuItemHandler );
        add( copyItem );
        
        pasteItem = new JMenuItem( "貼上(P)" );
        pasteItem.setMnemonic( 'P' );
        pasteItem.addActionListener( menuItemHandler );
        add( pasteItem );
    }
    
    private class MenuItemHandler implements ActionListener {
        
        private JPopupMenu ownerPopupMenu;
        
        public MenuItemHandler( JPopupMenu ownerPopupMenu ) {
            this.ownerPopupMenu = ownerPopupMenu; 
        }
        
        @Override
        public void actionPerformed( ActionEvent event ) {
            if( event.getSource() == cutItem ) {
                ClipboardUtil.cut( ownerPopupMenu.getInvoker() );
            } else if( event.getSource() == copyItem ) {
                ClipboardUtil.copy( ownerPopupMenu.getInvoker() );
            } else if( event.getSource() == pasteItem ) {
                try {
                    ClipboardUtil.paste( ownerPopupMenu.getInvoker() );
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
        }
    }
}
