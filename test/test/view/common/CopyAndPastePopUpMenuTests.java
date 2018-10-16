package test.view.common;

import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import view.common.CopyAndPastePopUpMenu;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(value=JUnit4.class)
public class CopyAndPastePopUpMenuTests {
    
    private boolean numLockKeyStateBackup;
    
    @Before
    public void setUp() {
        try {
            numLockKeyStateBackup = Toolkit.getDefaultToolkit().getLockingKeyState( KeyEvent.VK_NUM_LOCK );
            Toolkit.getDefaultToolkit().setLockingKeyState( KeyEvent.VK_NUM_LOCK, false );
        } catch( UnsupportedOperationException e ) {
            System.out.println( "Host system doesn't allow accessing the state of this key programmatically." );
        }
    }
    
    @After
    public void tearDown() {
        try {
            Toolkit.getDefaultToolkit().setLockingKeyState( KeyEvent.VK_NUM_LOCK, numLockKeyStateBackup );
        } catch( UnsupportedOperationException e ) {
            System.out.println( "Host system doesn't allow accessing the state of this key programmatically." );
        }
    }
    
    @Test
    public void testCutAndPast() throws Exception {
        CopyAndPastePopUpMenuTestFrame frame = new CopyAndPastePopUpMenuTestFrame();
        frame.setVisible( true );
        Thread.sleep( 3000 );
        
        JOptionPane.showMessageDialog( frame, "請切換為英文輸入法", "Message", JOptionPane.INFORMATION_MESSAGE );
        
        // 確認是否為linux based OS
        int isLinuxBasedOSSelection = 0;
        isLinuxBasedOSSelection = JOptionPane.showConfirmDialog( 
                frame, "Is the OS linux based?", "Check", JOptionPane.YES_NO_OPTION );
        
        Robot bot =  new Robot();
        Thread.sleep( 3000 );
        
        String input = "Test component text.";
        
        // Input textField1 value
        inputString( bot, input );
        bot.keyPress( KeyEvent.VK_END ); bot.keyRelease( KeyEvent.VK_END ); Thread.sleep( 100 );
        bot.keyPress( KeyEvent.VK_SHIFT );
        for( int i = 0; i < 5; i++ ) {
            bot.keyPress( KeyEvent.VK_LEFT ); bot.keyRelease( KeyEvent.VK_LEFT ); Thread.sleep( 100 );
        }
        bot.keyRelease( KeyEvent.VK_SHIFT );
        // Display Copy & Paste menu, and select the Cut item
        if( isLinuxBasedOSSelection == JOptionPane.YES_OPTION ) {
            JOptionPane.showMessageDialog( frame, "Please press context menu key in 3 seconds after closing this dialog.", 
                    "Message", JOptionPane.INFORMATION_MESSAGE );
            Thread.sleep( 3000 );
        } else {
            bot.keyPress( KeyEvent.VK_CONTEXT_MENU ); bot.keyRelease( KeyEvent.VK_CONTEXT_MENU ); Thread.sleep( 100 );
        }
        Thread.sleep( 1000 );
        bot.keyPress( KeyEvent.VK_T ); bot.keyRelease( KeyEvent.VK_T ); Thread.sleep( 100 );
        // Paste the cut string to textField2
        bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
        if( isLinuxBasedOSSelection == JOptionPane.YES_OPTION ) {
            JOptionPane.showMessageDialog( frame, "Please press context menu key in 3 seconds after closing this dialog.", 
                    "Message", JOptionPane.INFORMATION_MESSAGE );
            Thread.sleep( 3000 );
        } else {
            bot.keyPress( KeyEvent.VK_CONTEXT_MENU ); bot.keyRelease( KeyEvent.VK_CONTEXT_MENU ); Thread.sleep( 100 );
        }
        Thread.sleep( 1000 );
        bot.keyPress( KeyEvent.VK_P ); bot.keyRelease( KeyEvent.VK_P ); Thread.sleep( 100 );
        Thread.sleep( 3000 );
        
        // Check testing result
        String expectTextFieldValue1 = "Test component ";
        String expectTextFieldValue2 = "text.";
        String actualTextFieldValue1 = frame.getTextField1().getText();
        String actualTextFieldValue2 = frame.getTextField2().getText();
        assertEquals( expectTextFieldValue1, actualTextFieldValue1 );
        assertEquals( expectTextFieldValue2, actualTextFieldValue2 );
    }
    
    @Test
    public void testCopyAndPaste() throws Exception {
        CopyAndPastePopUpMenuTestFrame frame = new CopyAndPastePopUpMenuTestFrame();
        frame.setVisible( true );
        
        JOptionPane.showMessageDialog( frame, "請切換為英文輸入法", "Message", JOptionPane.INFORMATION_MESSAGE );
        
        // 確認是否為linux based OS
        int isLinuxBasedOSSelection = 0;
        isLinuxBasedOSSelection = JOptionPane.showConfirmDialog( 
                frame, "Is the OS linux based?", "Check", JOptionPane.YES_NO_OPTION );
        
        Robot bot =  new Robot();
        Thread.sleep( 3000 );
        
        String input = "Test component text.";
        
        // Input textField1 value
        inputString( bot, input );
        bot.keyPress( KeyEvent.VK_END ); bot.keyRelease( KeyEvent.VK_END ); Thread.sleep( 100 );
        bot.keyPress( KeyEvent.VK_SHIFT );
        for( int i = 0; i < 5; i++ ) {
            bot.keyPress( KeyEvent.VK_LEFT ); bot.keyRelease( KeyEvent.VK_LEFT ); Thread.sleep( 100 );
        }
        bot.keyRelease( KeyEvent.VK_SHIFT );
        // Display Copy & Paste menu, and select the Copy item
        if( isLinuxBasedOSSelection == JOptionPane.YES_OPTION ) {
            JOptionPane.showMessageDialog( frame, "Please press context menu key in 3 seconds after closing this dialog.", 
                    "Message", JOptionPane.INFORMATION_MESSAGE );
            Thread.sleep( 3000 );
        } else {
            bot.keyPress( KeyEvent.VK_CONTEXT_MENU ); bot.keyRelease( KeyEvent.VK_CONTEXT_MENU ); Thread.sleep( 100 );
        }
        Thread.sleep( 1000 );
        bot.keyPress( KeyEvent.VK_C ); bot.keyRelease( KeyEvent.VK_C ); Thread.sleep( 100 );
        // Paste the copied string to textField2
        bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
        if( isLinuxBasedOSSelection == JOptionPane.YES_OPTION ) {
            JOptionPane.showMessageDialog( frame, "Please press context menu key in 3 seconds after closing this dialog.", 
                    "Message", JOptionPane.INFORMATION_MESSAGE );
            Thread.sleep( 3000 );
        } else {
            bot.keyPress( KeyEvent.VK_CONTEXT_MENU ); bot.keyRelease( KeyEvent.VK_CONTEXT_MENU ); Thread.sleep( 100 );
        }
        Thread.sleep( 1000 );
        bot.keyPress( KeyEvent.VK_P ); bot.keyRelease( KeyEvent.VK_P ); Thread.sleep( 100 );
        Thread.sleep( 3000 );
        
        // Check testing result
        String expectTextFieldValue1 = "Test component text.";
        String expectTextFieldValue2 = "text.";
        String actualTextFieldValue1 = frame.getTextField1().getText();
        String actualTextFieldValue2 = frame.getTextField2().getText();
        assertEquals( expectTextFieldValue1, actualTextFieldValue1 );
        assertEquals( expectTextFieldValue2, actualTextFieldValue2 );
    }
    
    private void inputString( Robot bot, String s ) throws InterruptedException {
        HashMap<Character, Integer> charToKeyCodeMap = new HashMap<Character, Integer>();
        charToKeyCodeMap.put( 'a', KeyEvent.VK_A ); charToKeyCodeMap.put( 'A', KeyEvent.VK_A );
        charToKeyCodeMap.put( 'b', KeyEvent.VK_B ); charToKeyCodeMap.put( 'B', KeyEvent.VK_B );
        charToKeyCodeMap.put( 'c', KeyEvent.VK_C ); charToKeyCodeMap.put( 'C', KeyEvent.VK_C );
        charToKeyCodeMap.put( 'd', KeyEvent.VK_D ); charToKeyCodeMap.put( 'D', KeyEvent.VK_D );
        charToKeyCodeMap.put( 'e', KeyEvent.VK_E ); charToKeyCodeMap.put( 'E', KeyEvent.VK_E );
        charToKeyCodeMap.put( 'f', KeyEvent.VK_F ); charToKeyCodeMap.put( 'F', KeyEvent.VK_F );
        charToKeyCodeMap.put( 'g', KeyEvent.VK_G ); charToKeyCodeMap.put( 'G', KeyEvent.VK_G );
        charToKeyCodeMap.put( 'h', KeyEvent.VK_H ); charToKeyCodeMap.put( 'H', KeyEvent.VK_H );
        charToKeyCodeMap.put( 'i', KeyEvent.VK_I ); charToKeyCodeMap.put( 'I', KeyEvent.VK_I );
        charToKeyCodeMap.put( 'j', KeyEvent.VK_J ); charToKeyCodeMap.put( 'J', KeyEvent.VK_J );
        charToKeyCodeMap.put( 'k', KeyEvent.VK_K ); charToKeyCodeMap.put( 'K', KeyEvent.VK_K );
        charToKeyCodeMap.put( 'l', KeyEvent.VK_L ); charToKeyCodeMap.put( 'L', KeyEvent.VK_L );
        charToKeyCodeMap.put( 'm', KeyEvent.VK_M ); charToKeyCodeMap.put( 'M', KeyEvent.VK_M );
        charToKeyCodeMap.put( 'n', KeyEvent.VK_N ); charToKeyCodeMap.put( 'N', KeyEvent.VK_N );
        charToKeyCodeMap.put( 'o', KeyEvent.VK_O ); charToKeyCodeMap.put( 'O', KeyEvent.VK_O );
        charToKeyCodeMap.put( 'p', KeyEvent.VK_P ); charToKeyCodeMap.put( 'P', KeyEvent.VK_P );
        charToKeyCodeMap.put( 'q', KeyEvent.VK_Q ); charToKeyCodeMap.put( 'Q', KeyEvent.VK_Q );
        charToKeyCodeMap.put( 'r', KeyEvent.VK_R ); charToKeyCodeMap.put( 'R', KeyEvent.VK_R );
        charToKeyCodeMap.put( 's', KeyEvent.VK_S ); charToKeyCodeMap.put( 'S', KeyEvent.VK_S );
        charToKeyCodeMap.put( 't', KeyEvent.VK_T ); charToKeyCodeMap.put( 'T', KeyEvent.VK_T );
        charToKeyCodeMap.put( 'u', KeyEvent.VK_U ); charToKeyCodeMap.put( 'U', KeyEvent.VK_U );
        charToKeyCodeMap.put( 'v', KeyEvent.VK_V ); charToKeyCodeMap.put( 'V', KeyEvent.VK_V );
        charToKeyCodeMap.put( 'w', KeyEvent.VK_W ); charToKeyCodeMap.put( 'W', KeyEvent.VK_W );
        charToKeyCodeMap.put( 'x', KeyEvent.VK_X ); charToKeyCodeMap.put( 'X', KeyEvent.VK_X );
        charToKeyCodeMap.put( 'y', KeyEvent.VK_Y ); charToKeyCodeMap.put( 'Y', KeyEvent.VK_Y );
        charToKeyCodeMap.put( 'z', KeyEvent.VK_Z ); charToKeyCodeMap.put( 'Z', KeyEvent.VK_Z );
        charToKeyCodeMap.put( '1', KeyEvent.VK_1 ); charToKeyCodeMap.put( '!', KeyEvent.VK_1 );
        charToKeyCodeMap.put( '2', KeyEvent.VK_2 ); charToKeyCodeMap.put( '@', KeyEvent.VK_2 );
        charToKeyCodeMap.put( '3', KeyEvent.VK_3 ); charToKeyCodeMap.put( '#', KeyEvent.VK_3 );
        charToKeyCodeMap.put( '4', KeyEvent.VK_4 ); charToKeyCodeMap.put( '$', KeyEvent.VK_4 );
        charToKeyCodeMap.put( '5', KeyEvent.VK_5 ); charToKeyCodeMap.put( '%', KeyEvent.VK_5 );
        charToKeyCodeMap.put( '6', KeyEvent.VK_6 ); charToKeyCodeMap.put( '^', KeyEvent.VK_6 );
        charToKeyCodeMap.put( '7', KeyEvent.VK_7 ); charToKeyCodeMap.put( '&', KeyEvent.VK_7 );
        charToKeyCodeMap.put( '8', KeyEvent.VK_8 ); charToKeyCodeMap.put( '*', KeyEvent.VK_8 );
        charToKeyCodeMap.put( '9', KeyEvent.VK_9 ); charToKeyCodeMap.put( '(', KeyEvent.VK_9 );
        charToKeyCodeMap.put( '0', KeyEvent.VK_0 ); charToKeyCodeMap.put( ')', KeyEvent.VK_0 );
        charToKeyCodeMap.put( '-', KeyEvent.VK_MINUS ); charToKeyCodeMap.put( '_', KeyEvent.VK_MINUS );
        charToKeyCodeMap.put( '=', KeyEvent.VK_EQUALS ); charToKeyCodeMap.put( '+', KeyEvent.VK_EQUALS );
        charToKeyCodeMap.put( '[', KeyEvent.VK_OPEN_BRACKET ); charToKeyCodeMap.put( '{', KeyEvent.VK_OPEN_BRACKET );
        charToKeyCodeMap.put( ']', KeyEvent.VK_CLOSE_BRACKET ); charToKeyCodeMap.put( '}', KeyEvent.VK_CLOSE_BRACKET );
        charToKeyCodeMap.put( '\\', KeyEvent.VK_BACK_SLASH ); charToKeyCodeMap.put( '|', KeyEvent.VK_BACK_SLASH );
        charToKeyCodeMap.put( ';', KeyEvent.VK_SEMICOLON ); charToKeyCodeMap.put( ':', KeyEvent.VK_SEMICOLON );
        charToKeyCodeMap.put( '\'', KeyEvent.VK_QUOTE ); charToKeyCodeMap.put( '\"', KeyEvent.VK_QUOTE );
        charToKeyCodeMap.put( ',', KeyEvent.VK_COMMA ); charToKeyCodeMap.put( '<', KeyEvent.VK_COMMA );
        charToKeyCodeMap.put( '.', KeyEvent.VK_PERIOD ); charToKeyCodeMap.put( '>', KeyEvent.VK_PERIOD );
        charToKeyCodeMap.put( '/', KeyEvent.VK_SLASH ); charToKeyCodeMap.put( '?', KeyEvent.VK_SLASH );
        charToKeyCodeMap.put( '`', KeyEvent.VK_BACK_QUOTE ); charToKeyCodeMap.put( '~', KeyEvent.VK_BACK_QUOTE );
        charToKeyCodeMap.put( ' ', KeyEvent.VK_SPACE );
        ArrayList<Character> shiftPunctuationList = new ArrayList<Character>( 
                Arrays.asList( '~', '!', '@', '#', '$', '%', '^', '&', '*', 
                '(', ')', '_', '+', '{', '}', '|', ':', '\"', '<', '>', '?' ) );
        for( int i = 0; i < s.length(); i++ ) {
            if( Character.isUpperCase( s.charAt( i ) ) || 
                    shiftPunctuationList.indexOf( s.charAt( i ) ) >= 0 ) {
                bot.keyPress( KeyEvent.VK_SHIFT );
            }
            bot.keyPress( charToKeyCodeMap.get( s.charAt( i ) ) );
            bot.keyRelease( charToKeyCodeMap.get( s.charAt( i ) ) );
            Thread.sleep( 100 );
            if( Character.isUpperCase( s.charAt( i ) ) || 
                    shiftPunctuationList.indexOf( s.charAt( i ) ) >= 0 ) {
                bot.keyRelease( KeyEvent.VK_SHIFT );
            }
        }
    }
}

class CopyAndPastePopUpMenuTestFrame extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    private CopyAndPastePopUpMenu copyAndPastePopUpMenu;
    private MenuKeyHandler menuKeyHandler;
    
    private JPanel mainPanel;
    private JTextField textField1;
    private JTextField textField2;
    
    public CopyAndPastePopUpMenuTestFrame() {
        super( "Copy & Paste Pop Up Menu Test" );
        
        copyAndPastePopUpMenu = new CopyAndPastePopUpMenu();
        menuKeyHandler = new MenuKeyHandler( copyAndPastePopUpMenu );
        
        mainPanel = new JPanel();
        
        textField1 = new JTextField( 50 );
        textField1.addKeyListener( menuKeyHandler );
        mainPanel.add( textField1 );
        
        textField2 = new JTextField( 50 );
        textField2.addKeyListener( menuKeyHandler );
        mainPanel.add( textField2 );
        
        mainPanel.setPreferredSize( new Dimension( 566, 100 ) );
        add( mainPanel );
        
        pack();
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    }
    
    @Override
    public void setVisible( boolean b ) {
        super.setVisible( b );
        if( b == true ) {
            textField1.requestFocus();
        }
    }
    
    public JTextField getTextField1() {
        return textField1;
    }
    
    public JTextField getTextField2() {
        return textField2;
    }
    
    private class MenuKeyHandler implements KeyListener {
        
        private CopyAndPastePopUpMenu copyAndPastePopUpMenu;
        
        public MenuKeyHandler( CopyAndPastePopUpMenu copyAndPastePopUpMenu ) {
            this.copyAndPastePopUpMenu = copyAndPastePopUpMenu;
        }
        
        @Override
        public void keyPressed( KeyEvent event ) {
            if( event.getKeyCode() == KeyEvent.VK_CONTEXT_MENU ) {
                JTextField eventComponent = (JTextField)event.getComponent();
                int showPosX = (eventComponent.getCaret().getMagicCaretPosition() != null) ? 
                        (int)eventComponent.getCaret().getMagicCaretPosition().getX() : 0;
                int showPosY = (eventComponent.getCaret().getMagicCaretPosition() != null) ? 
                        (int)eventComponent.getCaret().getMagicCaretPosition().getY() : 0;
                copyAndPastePopUpMenu.show( eventComponent, showPosX, showPosY );
                System.out.println( "Caret posX: " + showPosX + ", " + "Caret posY: " + showPosY );
            }
        }

        @Override
        public void keyReleased( KeyEvent event ) {}

        @Override
        public void keyTyped( KeyEvent event ) {}
    }
}
