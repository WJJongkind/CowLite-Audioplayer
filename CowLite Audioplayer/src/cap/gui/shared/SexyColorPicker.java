/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.shared;

import cap.gui.colorscheme.ColorScheme;
import cap.gui.colorscheme.darkmode.DarkMode;
import cap.gui.shared.InputField.InputCondition;
import static cap.util.SugarySyntax.clamp;
import static cap.util.SugarySyntax.unwrappedPerform;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.lang.ref.WeakReference;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static cap.util.SugarySyntax.maxDouble;
import static cap.util.SugarySyntax.minInt;

/**
 *
 * @author Wessel
 */
public class SexyColorPicker extends JPanel implements SexyColorPickerStave.Delegate, SexyColorPickerPanel.Delegate{

    // MARK: - Associated types & constants
    
    public interface Delegate {
        public void didSelectColor(SexyColorPicker colorPicker, Color color);
    }
    
    private static final class Layout {
        public static final int staveWidth = 10;
        public static final int staveMarginLeft = 8;
        public static final int rgbFieldWidth = 30;
        public static final int marginBetweenInputFieldsAndStave = 8;
        public static final int marginBetweenLabelAndInputField = 4;
        public static final int marginBetweenInputFields = 8;
        public static final int marginBetweenRGBAndHex = 16;
    }
    
    private static final class Constants {
        public static final InputCondition rgbInputCondition = text -> { 
            if(text.matches("[a-fA-F0-9]{0,3}")) {
                int value = Integer.parseInt(text);
                return value <= 255 && value >= 0;
            } else {
                return false;
            }
        };
        public static final InputCondition hexInputCondition = text -> { return text.matches("[a-fA-F0-9]{0,6}"); };
    }
    
    // MARK: - Private properties
    
    private final SexyColorPickerStave stave;
    private final SexyColorPickerPanel panel;
    private final JLabel redInputFieldLabel;
    private final JLabel greenInputFieldLabel;
    private final JLabel blueInputFieldLabel;
    private final JLabel hexInputFieldLabel;
    private final InputField redInputField;
    private final InputField greenInputField;
    private final InputField blueInputField;
    private final InputField hexInputField;
    
    private WeakReference<Delegate> delegate;
    
    // MARK: - Initialisers
    
    public SexyColorPicker(ColorScheme colorScheme) {
        super();
        
        stave = new SexyColorPickerStave(SexyColorPickerStave.Orientation.vertical);
        panel = new SexyColorPickerPanel(stave.getColor());
        redInputFieldLabel = new JLabel("R:");
        greenInputFieldLabel = new JLabel("G:");
        blueInputFieldLabel = new JLabel("B:");
        hexInputFieldLabel = new JLabel("Hex color code:");
        redInputField = new InputField(colorScheme.defaultInputFieldColorScheme());
        redInputField.addChangeListener((sender, text) -> rgbInputFieldValueChanged(sender, text));
        redInputField.setInputCondition(Constants.rgbInputCondition);
        greenInputField = new InputField(colorScheme.defaultInputFieldColorScheme());
        greenInputField.addChangeListener((sender, text) -> rgbInputFieldValueChanged(sender, text));
        greenInputField.setInputCondition(Constants.rgbInputCondition);
        blueInputField = new InputField(colorScheme.defaultInputFieldColorScheme());
        blueInputField.addChangeListener((sender, text) -> rgbInputFieldValueChanged(sender, text));
        blueInputField.setInputCondition(Constants.rgbInputCondition);
        hexInputField = new InputField(colorScheme.defaultInputFieldColorScheme());
        hexInputField.addChangeListener((sender, text) -> hexInputFieldValueChanged(sender, text));
        hexInputField.setInputCondition(Constants.hexInputCondition);
        
        layoutComponents(colorScheme);
        
        super.setBackground(new Color(0, 0, 0, 0f));
        stave.setDelegate(this);
        panel.setDelegate(this);
    }
    
    // MARK: - Public methods
    
    public void setDelegate(Delegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }

    // MARK: - SexyColorPickerStaveDelegate
    
    @Override
    public void didSelectColor(SexyColorPickerStave sender, Color color) {
        panel.setBaseColor(color);
    }

    // MARK: - SexyColorPickerPanelDelegate
    
    @Override
    public void didSelectColor(SexyColorPickerPanel sender, Color color) {
        setRGBInputFieldValuesForColor(color);
        setHexInputFieldValueForColor(color);
        unwrappedPerform(delegate, delegate -> delegate.didSelectColor(this, color));
    }
    
    // MARK: - InputFieldChangeListener
    
    private void rgbInputFieldValueChanged(InputField inputField, String newValue) {
        if(newValue.equals("")) { 
            return;
        }
        int parsedValue = Integer.parseInt(newValue);
        Color selectedColor = panel.getSelectedColor();
        
        Color newColor;
        if(inputField == redInputField) {
            newColor = new Color(parsedValue, selectedColor.getGreen(), selectedColor.getBlue());
        } else if(inputField == greenInputField) {
            newColor = new Color(selectedColor.getRed(), parsedValue, selectedColor.getBlue());
        } else {
            newColor = new Color(selectedColor.getRed(), selectedColor.getGreen(), parsedValue);
        }
        
        setHexInputFieldValueForColor(newColor);
        panel.setSelectedColor(newColor);
        stave.setSelectedColor(panel.getBaseColor());
    }
    
    private void hexInputFieldValueChanged(InputField inputField, String newValue) {
        if(newValue.length() < 6) {
            return;
        }
        
        int r = Integer.parseInt(newValue.substring(0, 2), 16);
        int g = Integer.parseInt(newValue.substring(2, 4), 16);
        int b = Integer.parseInt(newValue.substring(4, 6), 16);
        
        Color newColor = new Color(r, g, b);
        
        setRGBInputFieldValuesForColor(newColor);
        panel.setSelectedColor(newColor);
        stave.setSelectedColor(panel.getBaseColor());
    }
    
    private void setRGBInputFieldValuesForColor(Color color) {
        redInputField.setText(color.getRed() + "");
        greenInputField.setText(color.getGreen() + "");
        blueInputField.setText(color.getBlue() + "");
    }
    
    private void setHexInputFieldValueForColor(Color color) {
        String redString = String.format("%02X", color.getRed());
        String greenString = String.format("%02X", color.getGreen());
        String blueString = String.format("%02X", color.getBlue());
        
        hexInputField.setText(redString + greenString + blueString);
    }
    
    // MARK: - Private methods
    
    private void layoutComponents(ColorScheme colorScheme) {
        super.setLayout(new GridBagLayout());
        
        layoutPanel();
        layoutStave();
        layoutRedInputFieldLabel(colorScheme);
        layoutRedInputField(colorScheme);
        layoutGreenInputFieldLabel(colorScheme);
        layoutGreenInputField(colorScheme);
        layoutBlueInputFieldLabel(colorScheme);
        layoutBlueInputField(colorScheme);
        layoutHexInputFieldLabel(colorScheme);
        layoutHexInputField(colorScheme);
    }
    
    private void layoutPanel() {
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 3;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        
        add(panel, c);
    }
    
    private void layoutStave() {
        stave.setPreferredSize(new Dimension(Layout.staveWidth, 0));
        
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 3;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, Layout.staveMarginLeft, 0, 0);
        
        add(stave, c);
    }
    
    private void layoutRedInputFieldLabel(ColorScheme colorScheme) {
        redInputFieldLabel.setFont(colorScheme.font().l().bold().get());
        redInputFieldLabel.setForeground(colorScheme.defaultContentColor());
        
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 3;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(0, Layout.marginBetweenInputFieldsAndStave, 0, 0);
        
        add(redInputFieldLabel, c);
    }
    
    private void layoutRedInputField(ColorScheme colorScheme) {
        redInputField.setPreferredWidth(50);
        redInputField.setFont(colorScheme.font().bold().l().get());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 4;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(0, Layout.marginBetweenLabelAndInputField, 0, 0);
        
        add(redInputField, c);
    }
    
    private void layoutGreenInputFieldLabel(ColorScheme colorScheme) {
        greenInputFieldLabel.setFont(colorScheme.font().bold().l().get());
        greenInputFieldLabel.setForeground(colorScheme.defaultContentColor());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 5;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(0, Layout.marginBetweenInputFields, 0, 0);
        
        add(greenInputFieldLabel, c);
    }
    
    private void layoutGreenInputField(ColorScheme colorScheme) {
        greenInputField.setPreferredWidth(50);
        greenInputField.setFont(colorScheme.font().bold().l().get());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 6;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(0, Layout.marginBetweenLabelAndInputField, 0, 0);
        
        add(greenInputField, c);
    }
    
    private void layoutBlueInputFieldLabel(ColorScheme colorScheme) {
        blueInputFieldLabel.setFont(colorScheme.font().bold().l().get());
        blueInputFieldLabel.setForeground(colorScheme.defaultContentColor());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 7;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(0, Layout.marginBetweenInputFields, 0, 0);
        
        add(blueInputFieldLabel, c);
    }
    
    private void layoutBlueInputField(ColorScheme colorScheme) {
        blueInputField.setPreferredWidth(50);
        blueInputField.setFont(colorScheme.font().bold().l().get());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 8;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(0, Layout.marginBetweenLabelAndInputField, 0, 0);
        
        add(blueInputField, c);
    }
    
    private void layoutHexInputFieldLabel(ColorScheme colorScheme) {
        hexInputFieldLabel.setFont(colorScheme.font().bold().l().get());
        hexInputFieldLabel.setForeground(colorScheme.defaultContentColor());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 3;
        c.gridy = 2;
        c.gridwidth = 6;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(Layout.marginBetweenRGBAndHex, Layout.marginBetweenInputFieldsAndStave, 0, 0);
        
        add(hexInputFieldLabel, c);
    }
    
    private void layoutHexInputField(ColorScheme colorScheme) {
        hexInputField.setFont(colorScheme.font().bold().l().get());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 3;
        c.gridy = 3;
        c.gridwidth = 6;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(0, Layout.marginBetweenInputFieldsAndStave, 0, 0);
        
        add(hexInputField, c);
    }
    
    
    
    
    
    
    public static void main(String[] args) throws IOException {
        
        
        JFrame frame = new JFrame();
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        frame.setVisible(true);
        
        JPanel panel = new JPanel();
        panel.setSize(1000, 1000);
        frame.getContentPane().add(panel);
        panel.setBackground(Color.cyan);
        panel.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = c.BOTH;
        c.weighty = 1;
        c.weightx = 1;
        
        
        SexyColorPicker scp = new SexyColorPicker(new DarkMode());
        panel.add(scp, c);
        
        frame.setVisible(true);
    }
    
    
    
}
