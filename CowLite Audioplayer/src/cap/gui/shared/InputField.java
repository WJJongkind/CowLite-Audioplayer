/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.shared;

import cap.gui.colorscheme.InputFieldColorScheme;
import static cap.util.SugarySyntax.nilCoalesce;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Wessel
 */
public class InputField extends JTextField implements DocumentListener {
    
    // MARK: - Associated types & constants
    
    private static final class Layout {
        public static final int borderThickness = 3;
    }
    
    public interface ChangeListener {
        public void valueChanged(InputField inputField, String newValue);
    }
    
    public interface InputCondition {
        public boolean conformsToCondition(String text);
    }
    
    // MARK: - Private properties
    
    private final ArrayList<ChangeListener> changeListeners = new ArrayList<>();
    private InputCondition inputCondition = null;
    private boolean muted = false;
    private boolean shouldNotifyChangeListeners = true;
    
    // MARK: - Initialisers
    
    public InputField(InputFieldColorScheme inputFieldColorScheme) {
        super.setBackground(inputFieldColorScheme.getBackgroundColor());
        super.setForeground(inputFieldColorScheme.getTextColor());
        super.setBorder(BorderFactory.createLineBorder(inputFieldColorScheme.getBorderColor(), Layout.borderThickness));
        super.getDocument().addDocumentListener(this);
    }
    
    // MARK: - Public methods
    
    public void addChangeListener(ChangeListener listener) {
        changeListeners.add(listener);
    }
    
    public void removeChangeListener(ChangeListener listener) {
        changeListeners.remove(listener);
    }
    
    public void setPreferredWidth(int width) {
        super.setPreferredSize(new Dimension(width, getFont().getSize() + 2 * Layout.borderThickness));
    }
    
    public void setInputCondition(InputCondition inputCondition) {
        this.inputCondition = inputCondition;
    }
    
    // MARK: - JTextField
    
    @Override
    public void setFont(Font font) {
        super.setFont(font);
        setPreferredWidth(getPreferredSize().width);
    }
    
    @Override
    protected Document createDefaultModel() {
        return new RegexDocument();
    }
    
    public void setText(String text, boolean shouldNotifyListeners) {
        if(text != null && getText() != null && !text.equals(getText()) && !muted) {
            this.shouldNotifyChangeListeners = shouldNotifyListeners;
            super.setText(text);
            this.shouldNotifyChangeListeners = true;
        }
        
    }
    
    @Override
    public void setText(String text) {
        setText(text, true);
    }
    
    // MARK: - DocumentListener
    
    @Override
    public void insertUpdate(DocumentEvent e) {
        notifyChangeListeners();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        notifyChangeListeners();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
    
    // MARK: - Private methods
    
    private void notifyChangeListeners() {
        if(!shouldNotifyChangeListeners) {
            return;
        }
        // We mute the inputfield (i.e. "setText" won't have any effect) because observers may attempt to change the value
        // of this inputfield while we are notfying them of a change. As this is not supported, we will ignore any calls to "setText".
        muted = true;
        for(ChangeListener listener : changeListeners) {
            listener.valueChanged(this, getText());
        }
        muted = false;
    }
    
    // MARK: - Private associated types
    
    private final class RegexDocument extends PlainDocument {
        @Override
        public void insertString(int offset, String newString, AttributeSet attributes) throws BadLocationException {
            if(newString == null) {
                return;
            }
            
            String combined = nilCoalesce(super.getText(0, super.getLength()), "") + newString;
            if(inputCondition == null || (offset >= 0 && inputCondition.conformsToCondition(combined))) {
                super.insertString(offset, newString, attributes);
            }
        }
    }
    
}
