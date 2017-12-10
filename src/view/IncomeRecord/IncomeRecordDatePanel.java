package view.IncomeRecord;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import commonUtil.ComparingUtil;
import commonUtil.StringUtil;

public class IncomeRecordDatePanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    private static final int DEFAULT_MONTH_LIST_COUNT = 19;
    
    private IncomeRecordPanel ownerPanel;
    private IncomeRecordTablePanel incomeRecordTablePanel;
    
    private FocusHandler focusHandler;
    private MonthListArrowKeyHandler monthListArrowKeyHandler;
    private MonthListSelectionHandler monthListSelectionHandler;
    private MonthTextFieldCheckingHandler monthTextFieldCheckingHandler;
    private MonthTextFieldArrowKeyHandler monthTextFieldArrowKeyHandler;
    private Font generalFont;
    private JLabel yearLabel;
    private JTextField yearTextField;
    private JLabel monthLabel;
    private JTextField monthTextField;
    private JList<String> monthList;
    
    public IncomeRecordDatePanel( IncomeRecordPanel ownerPanel ) {
        setLayout( null );
        
        this.ownerPanel = ownerPanel;
        
        focusHandler = new FocusHandler();
        monthListArrowKeyHandler = new MonthListArrowKeyHandler();
        monthListSelectionHandler = new MonthListSelectionHandler();
        monthTextFieldCheckingHandler = new MonthTextFieldCheckingHandler();
        monthTextFieldArrowKeyHandler = new MonthTextFieldArrowKeyHandler();
        
        generalFont = new Font( "細明體", Font.PLAIN, 16 );
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        int year = calendar.get( Calendar.YEAR );
        int month = calendar.get( Calendar.MONTH ) + 1;
        
        yearTextField = new JTextField( 4 );
        yearTextField.setBounds( 16, 20, 40, 22 );
        yearTextField.setFont( generalFont );
        yearTextField.addFocusListener( focusHandler );
        yearTextField.addFocusListener( monthTextFieldCheckingHandler );
        yearTextField.addKeyListener( monthTextFieldArrowKeyHandler );
        //yearTextField.addKeyListener( mnemonicKeyHandler );
        //yearTextField.addKeyListener( undoHotKeyHandler );
        //yearTextField.getDocument().addUndoableEditListener( undoEditHandler );
        yearTextField.setText( String.format( "%04d", year ) );
        add( yearTextField );
        
        yearLabel = new JLabel( "年" );
        yearLabel.setBounds( 56, 20, 16, 22 );
        yearLabel.setFont( generalFont );
        add( yearLabel );
        
        monthTextField = new JTextField( 2 );
        monthTextField.setBounds( 72, 20, 24, 22 );
        monthTextField.setFont( generalFont );
        monthTextField.addFocusListener( focusHandler );
        monthTextField.addFocusListener( monthTextFieldCheckingHandler );
        monthTextField.addKeyListener( monthTextFieldArrowKeyHandler );
        //monthTextField.addKeyListener( mnemonicKeyHandler );
        //monthTextField.addKeyListener( undoHotKeyHandler );
        //monthTextField.getDocument().addUndoableEditListener( undoEditHandler );
        monthTextField.setText( String.format( "%02d", month ) );
        add( monthTextField );
        
        monthLabel = new JLabel( "月" );
        monthLabel.setBounds( 96, 20, 16, 22 );
        monthLabel.setFont( generalFont );
        add( monthLabel );
        
        String[] monthListString = new String[ DEFAULT_MONTH_LIST_COUNT ];
        for( int i = 0; i < monthListString.length; i++ ) {
            monthListString[ i ] = String.format( "%04d.%02d", year + (month + i - 1) / 12, (month + i - 1) % 12 + 1 );
        }
        
        monthList = new JList<String>( monthListString );
        monthList.setBounds( 16, 54, 96, 418 );
        monthList.setVisibleRowCount( DEFAULT_MONTH_LIST_COUNT );
        monthList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        monthList.getSelectionModel().addListSelectionListener( monthListSelectionHandler );
        monthList.setSelectedIndex( 0 );
        monthList.setFont( generalFont );
        monthList.setBorder( new JTextField().getBorder() );
        monthList.addKeyListener( monthListArrowKeyHandler );
        //monthList.addKeyListener( mnemonicKeyHandler );
        //monthList.setFocusTraversalKeysEnabled( false );
        //monthList.addKeyListener( specialFocusTraversalPolicyHandler );
        add( monthList );
        
        setPreferredSize( new Dimension( 120, 479 ) );
    }
    
    public JList<String> getMonthList() {
        return monthList;
    }
    
    public void setIncomeRecordTablePanel( IncomeRecordTablePanel incomeRecordTablePanel ) {
        this.incomeRecordTablePanel = incomeRecordTablePanel;
    }
    
    private int findDataIndexInMonthList( int year, int month ) {
        String dataForSearch = String.format( "%04d.%02d", year, month );
        for( int i = 0; i < monthList.getModel().getSize(); i++ ) {
            if( ComparingUtil.compare( dataForSearch, monthList.getModel().getElementAt( i ) ) == 0 ) {
                return i;
            }
        }
        return -1;
    }
    
    private void decreaseMonthTextFieldValue() {
        final int MONTH_MIN_VALUE = 1;
        final int MONTH_MAX_VALUE = 12;
        
        if( !StringUtil.isNumber( monthTextField.getText() ) ) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime( new Date() );
            monthTextField.setText( String.format( "%02d", calendar.get( Calendar.MONTH ) + 1 ) );
            return;
        }
        
        if( Integer.parseInt( monthTextField.getText() ) - 1 >= MONTH_MIN_VALUE ) {
            monthTextField.setText(
                String.format( "%02d", Integer.parseInt( monthTextField.getText() ) - 1 )
            );
        } else {
            monthTextField.setText( String.format( "%02d", MONTH_MAX_VALUE ) );
            decreaseYearTextFieldValue();
        }
        
        if( StringUtil.isNumber( yearTextField.getText() ) && StringUtil.isNumber( monthTextField.getText() ) ) {
            selectMonthListData( 
                Integer.parseInt( yearTextField.getText() ), Integer.parseInt( monthTextField.getText() ) 
            );
        }
    }
    
    private void increaseMonthTextFieldValue() {
        final int MONTH_MIN_VALUE = 1;
        final int MONTH_MAX_VALUE = 12;
        
        if( !StringUtil.isNumber( monthTextField.getText() ) ) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime( new Date() );
            monthTextField.setText( String.format( "%02d", calendar.get( Calendar.MONTH ) + 1 ) );
            return;
        }
        
        if( Integer.parseInt( monthTextField.getText() ) + 1 <= MONTH_MAX_VALUE ) {
            monthTextField.setText(
                String.format( "%02d", Integer.parseInt( monthTextField.getText() ) + 1 )
            );
        } else {
            monthTextField.setText( String.format( "%02d", MONTH_MIN_VALUE ) );
            increaseYearTextFieldValue();
        }
        
        if( StringUtil.isNumber( yearTextField.getText() ) && StringUtil.isNumber( monthTextField.getText() ) ) {
            selectMonthListData( 
                Integer.parseInt( yearTextField.getText() ), Integer.parseInt( monthTextField.getText() ) 
            );
        }
    }
    
    private void decreaseYearTextFieldValue() {
        final int YEAR_MIN_VALUE = 0;
        
        if( !StringUtil.isNumber( yearTextField.getText() ) ) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime( new Date() );
            yearTextField.setText( String.format( "%04d", calendar.get( Calendar.YEAR ) ) );
            return;
        }
        
        if( Integer.parseInt( monthTextField.getText() ) - 1 >= YEAR_MIN_VALUE ) {
            yearTextField.setText(
                String.format( "%04d", Integer.parseInt( yearTextField.getText() ) - 1 )
            );
        }
        
        if( StringUtil.isNumber( yearTextField.getText() ) && StringUtil.isNumber( monthTextField.getText() ) ) {
            selectMonthListData( 
                Integer.parseInt( yearTextField.getText() ), Integer.parseInt( monthTextField.getText() ) 
            );
        }
    }
    
    private void increaseYearTextFieldValue() {
        if( !StringUtil.isNumber( yearTextField.getText() ) ) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime( new Date() );
            yearTextField.setText( String.format( "%04d", calendar.get( Calendar.YEAR ) ) );
            return;
        }
        
        yearTextField.setText( String.format( "%04d", Integer.parseInt( yearTextField.getText() ) + 1 ) );
        
        if( StringUtil.isNumber( yearTextField.getText() ) && StringUtil.isNumber( monthTextField.getText() ) ) {
            selectMonthListData( 
                Integer.parseInt( yearTextField.getText() ), Integer.parseInt( monthTextField.getText() ) 
            );
        }
    }
    
    private void moveDownToSpecifyMonth( int year, int month ) {
        final int MONTH_PER_YEAR = 12;
        
        int headYear = ((year * MONTH_PER_YEAR) + (month - 1) - DEFAULT_MONTH_LIST_COUNT + 1) / MONTH_PER_YEAR;
        int headMonth = ((year * MONTH_PER_YEAR) + (month - 1) - DEFAULT_MONTH_LIST_COUNT + 1) % MONTH_PER_YEAR + 1;
        String[] monthListString = new String[ DEFAULT_MONTH_LIST_COUNT ];
        for( int i = 0; i < monthListString.length; i++ ) {
            monthListString[ i ] = String.format( "%04d.%02d", headYear + (headMonth + i - 1) / 12, (headMonth + i - 1) % 12 + 1 );
        }
        monthList.setListData( monthListString );
        monthList.setSelectedIndex( DEFAULT_MONTH_LIST_COUNT - 1 );
    }
    
    private void moveUpToSpecifyMonth( int year, int month ) {
        String[] monthListString = new String[ DEFAULT_MONTH_LIST_COUNT ];
        for( int i = 0; i < monthListString.length; i++ ) {
            monthListString[ i ] = String.format( "%04d.%02d", year + (month + i - 1) / 12, (month + i - 1) % 12 + 1 );
        }
        monthList.setListData( monthListString );
        monthList.setSelectedIndex( 0 );
    }
    
    private void selectMonthListData( int year, int month ) {
        String dataForSearch = String.format( "%04d.%02d", year, month );
        int indexForSelecting = findDataIndexInMonthList( year, month );
        if( indexForSelecting < 0 && monthList.getModel().getSize() <= 0 ) {
            return;
        } else if( indexForSelecting < 0 && ComparingUtil.compare( dataForSearch, monthList.getModel().getElementAt( 0 ) ) < 0 ) {
            moveUpToSpecifyMonth( year, month );
        } else if( indexForSelecting < 0 && ComparingUtil.compare( dataForSearch, monthList.getModel().getElementAt( 0 ) ) > 0 ) {
            moveDownToSpecifyMonth( year, month );
        } else {
            monthList.setSelectedIndex( indexForSelecting );
        }
    }
    
    private class FocusHandler extends FocusAdapter {
        
        @Override
        public void focusGained( FocusEvent event ) {
            JTextField sourceComponent = (JTextField) event.getSource();
            sourceComponent.selectAll();
        }
    }
    
    private class MonthListArrowKeyHandler implements KeyListener {
        
        @Override
        public void keyPressed( KeyEvent event ) {
            switch( event.getKeyCode() ) {
            case KeyEvent.VK_UP:
                if( monthList.getSelectedIndex() == 0 ) {
                    monthList.clearSelection();
                    decreaseMonthTextFieldValue();
                    selectMonthListData( Integer.parseInt( yearTextField.getText() ), Integer.parseInt( monthTextField.getText() ) );
                }
                break;
            case KeyEvent.VK_DOWN:
                if( monthList.getSelectedIndex() == DEFAULT_MONTH_LIST_COUNT - 1 ) {
                    monthList.clearSelection();
                    increaseMonthTextFieldValue();
                    selectMonthListData( Integer.parseInt( yearTextField.getText() ), Integer.parseInt( monthTextField.getText() ) );
                }
                break;
            }
        }

        @Override
        public void keyReleased( KeyEvent event ) { }

        @Override
        public void keyTyped( KeyEvent event ) { }
    }
    
    private class MonthListSelectionHandler implements ListSelectionListener {
        
        @Override
        public void valueChanged( ListSelectionEvent event ) {
            if( monthList.getSelectedIndex() < 0 || incomeRecordTablePanel == null ) {
                return;
            }
            
            String monthString = monthList.getSelectedValue();
            int year = Integer.parseInt( monthString.substring( 0, 4 ) );
            int month = Integer.parseInt( monthString.substring( 5, 7 ) );
            yearTextField.setText( String.format( "%04d", year ) );
            monthTextField.setText( String.format( "%02d", month ) );
            
            incomeRecordTablePanel.searchIncomeRecordByMonth( year, month );
        }
    }
    
    private class MonthTextFieldArrowKeyHandler implements KeyListener {
        
        @Override
        public void keyPressed( KeyEvent event ) {
            switch( event.getKeyCode() ) {
            case KeyEvent.VK_UP:
                if( event.getSource() == yearTextField ) {
                    decreaseYearTextFieldValue();
                    yearTextField.selectAll();
                } else if( event.getSource() == monthTextField ) {
                    decreaseMonthTextFieldValue();
                    monthTextField.selectAll();
                }
                break;
            case KeyEvent.VK_DOWN:
                if( event.getSource() == yearTextField ) {
                    increaseYearTextFieldValue();
                    yearTextField.selectAll();
                } else if( event.getSource() == monthTextField ) {
                    increaseMonthTextFieldValue();
                    monthTextField.selectAll();
                }
                break;
            }
        }

        @Override
        public void keyReleased( KeyEvent event ) { }

        @Override
        public void keyTyped( KeyEvent event ) { }
    }
    
    private class MonthTextFieldCheckingHandler extends FocusAdapter {
        
        private String yearTextFieldCache;
        private String monthTextFieldCache;
        
        public MonthTextFieldCheckingHandler() {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime( new Date() );
            int year = calendar.get( Calendar.YEAR );
            int month = calendar.get( Calendar.MONTH ) + 1;
            
            yearTextFieldCache = String.format( "%04d", year );
            monthTextFieldCache = String.format( "%02d", month );
        }
        
        @Override
        public void focusGained( FocusEvent event ) {
            if( event.getSource() == yearTextField ) {
                yearTextFieldCache = yearTextField.getText();
            } else if( event.getSource() == monthTextField ) {
                monthTextFieldCache = monthTextField.getText();
            }
        }
        
        @Override
        public void focusLost( FocusEvent event ) {
            if( event.getSource() == yearTextField ) {
                yearTextFieldProcedure();
            } else if( event.getSource() == monthTextField ) {
                monthTextFieldProcedure();
            }
        }
        
        private void yearTextFieldProcedure() {
            int year, month;
            try {
                year = Integer.parseInt( yearTextField.getText() );
            } catch( NumberFormatException e ) {
                yearTextField.setText( yearTextFieldCache );
                year = Integer.parseInt( yearTextFieldCache );
            }
            try {
                month = Integer.parseInt( monthTextField.getText() );
            } catch( NumberFormatException e ) {
                return;
            }
            selectMonthListData( year, month );
        }
        
        private void monthTextFieldProcedure() {
            int year, month;
            try {
                year = Integer.parseInt( yearTextField.getText() );
            } catch( NumberFormatException e ) {
                return;
            }
            try {
                month = Integer.parseInt( monthTextField.getText() );
            } catch( NumberFormatException e ) {
                monthTextField.setText( monthTextFieldCache );
                month = Integer.parseInt( monthTextFieldCache );
            }
            selectMonthListData( year, month );
        }
    }
}
