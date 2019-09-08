/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui.shared;

import cap.gui.DefaultWindow;
import cap.gui.ViewController;
import cap.gui.Window;
import cap.gui.colorscheme.ColorScheme;
import cap.gui.colorscheme.darkmode.DarkMode;
import cap.gui.shared.InputField.InputCondition;
import static cap.util.SugarySyntax.unwrappedPerform;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.lang.ref.WeakReference;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Wessel
 */
public class SexyColorPickerViewController implements ViewController, SexyColorPickerStave.Delegate, SexyColorPickerPanel.Delegate {

    // MARK: - Associated types & constants
    
    public interface Delegate {
        public void didSelectColor(SexyColorPickerViewController colorPicker, Color color);
        public void didPressConfirm(SexyColorPickerViewController colorPicker, Color color);
        public void didPressCancel(SexyColorPickerViewController colorPicker);
    }
    
    private static final class Layout {
        public static final int contentMargin = 8;
        public static final int staveWidth = 10;
        public static final int staveMarginLeft = 8;
        public static final int rgbFieldWidth = 30;
        public static final int marginBetweenInputFieldsAndStave = 8;
        public static final int marginBetweenLabelAndInputField = 4;
        public static final int marginBetweenInputFields = 8;
        public static final int marginBetweenRGBAndHex = 8;
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
    
    private final JPanel view;
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
    private final Button confirmButton;
    private final Button cancelButton;
    
    private WeakReference<Delegate> delegate;
    
    // MARK: - Initialisers
    
    public SexyColorPickerViewController(ColorScheme colorScheme) {
        super();
        
        view = new JPanel();
        
        stave = new SexyColorPickerStave(SexyColorPickerStave.Orientation.vertical);
        panel = new SexyColorPickerPanel(stave.getColor());
        
        redInputFieldLabel = new JLabel("R:");
        redInputField = new InputField(colorScheme.defaultInputField());
        redInputField.addChangeListener((sender, text) -> rgbInputFieldValueChanged(sender, text));
        redInputField.setInputCondition(Constants.rgbInputCondition);
        
        greenInputFieldLabel = new JLabel("G:");
        greenInputField = new InputField(colorScheme.defaultInputField());
        greenInputField.addChangeListener((sender, text) -> rgbInputFieldValueChanged(sender, text));
        greenInputField.setInputCondition(Constants.rgbInputCondition);
        
        blueInputFieldLabel = new JLabel("B:");
        blueInputField = new InputField(colorScheme.defaultInputField());
        blueInputField.addChangeListener((sender, text) -> rgbInputFieldValueChanged(sender, text));
        blueInputField.setInputCondition(Constants.rgbInputCondition);
        
        hexInputFieldLabel = new JLabel("Hex color code:");
        hexInputField = new InputField(colorScheme.defaultInputField());
        hexInputField.addChangeListener((sender, text) -> hexInputFieldValueChanged(sender, text));
        hexInputField.setInputCondition(Constants.hexInputCondition);
        
        confirmButton = new Button("Confirm", colorScheme.defaultButton(), colorScheme.font().m().bold());
        confirmButton.addActionListener(e -> {
            unwrappedPerform(delegate, delegate -> delegate.didPressConfirm(this, panel.getSelectedColor()));
        });
        
        cancelButton = new Button("Cancel", colorScheme.defaultButton(), colorScheme.font().m().bold());
        cancelButton.addActionListener(e -> {
            unwrappedPerform(delegate, delegate -> delegate.didPressCancel(this));
        });
        
        layoutComponents(colorScheme);
        
        view.setOpaque(false);
        view.setBackground(new Color(0, 0, 0, 0f));
        stave.setDelegate(this);
        panel.setDelegate(this);
    }
    
    // MARK: - Public methods
    
    public void setDelegate(Delegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }
    
    // MARK: - ViewController
    
    @Override
    public JComponent getView() {
        return view;
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
    
    // MARK: - Private methods, InputFieldChangeListeners
    
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
        redInputField.setText(color.getRed() + "", false);
        greenInputField.setText(color.getGreen() + "", false);
        blueInputField.setText(color.getBlue() + "", false);
    }
    
    private void setHexInputFieldValueForColor(Color color) {
        String redString = String.format("%02X", color.getRed());
        String greenString = String.format("%02X", color.getGreen());
        String blueString = String.format("%02X", color.getBlue());
        
        hexInputField.setText(redString + greenString + blueString, false);
    }
    
    // MARK: - Private methods
    
    private void layoutComponents(ColorScheme colorScheme) {
        view.setLayout(new GridBagLayout());
        
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
        layoutButtons(colorScheme);
    }
    
    private void layoutPanel() {
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 4;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(Layout.contentMargin, Layout.contentMargin, Layout.contentMargin, 0);
        
        view.add(panel, c);
    }
    
    private void layoutStave() {
        stave.setPreferredSize(new Dimension(Layout.staveWidth, 0));
        
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 4;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(Layout.contentMargin, Layout.staveMarginLeft, Layout.contentMargin, 0);
        
        view.add(stave, c);
    }
    
    private void layoutRedInputFieldLabel(ColorScheme colorScheme) {
        redInputFieldLabel.setFont(colorScheme.font().m().bold().get());
        redInputFieldLabel.setForeground(colorScheme.general().getContentColor());
        
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 3;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(Layout.contentMargin, Layout.marginBetweenInputFieldsAndStave, 0, 0);
        
        view.add(redInputFieldLabel, c);
    }
    
    private void layoutRedInputField(ColorScheme colorScheme) {
        redInputField.setPreferredWidth(50);
        redInputField.setFont(colorScheme.font().bold().m().get());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 4;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(Layout.contentMargin, Layout.marginBetweenLabelAndInputField, 0, 0);
        
        view.add(redInputField, c);
    }
    
    private void layoutGreenInputFieldLabel(ColorScheme colorScheme) {
        greenInputFieldLabel.setFont(colorScheme.font().bold().m().get());
        greenInputFieldLabel.setForeground(colorScheme.general().getContentColor());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 5;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(Layout.contentMargin, Layout.marginBetweenInputFields, 0, 0);
        
        view.add(greenInputFieldLabel, c);
    }
    
    private void layoutGreenInputField(ColorScheme colorScheme) {
        greenInputField.setPreferredWidth(50);
        greenInputField.setFont(colorScheme.font().bold().m().get());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 6;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(Layout.contentMargin, Layout.marginBetweenLabelAndInputField, 0, 0);
        
        view.add(greenInputField, c);
    }
    
    private void layoutBlueInputFieldLabel(ColorScheme colorScheme) {
        blueInputFieldLabel.setFont(colorScheme.font().bold().m().get());
        blueInputFieldLabel.setForeground(colorScheme.general().getContentColor());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 7;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(Layout.contentMargin, Layout.marginBetweenInputFields, 0, 0);
        
        view.add(blueInputFieldLabel, c);
    }
    
    private void layoutBlueInputField(ColorScheme colorScheme) {
        blueInputField.setPreferredWidth(50);
        blueInputField.setFont(colorScheme.font().bold().m().get());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 8;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(Layout.contentMargin, Layout.marginBetweenLabelAndInputField, 0, Layout.contentMargin);
        
        view.add(blueInputField, c);
    }
    
    private void layoutHexInputFieldLabel(ColorScheme colorScheme) {
        hexInputFieldLabel.setFont(colorScheme.font().bold().m().get());
        hexInputFieldLabel.setForeground(colorScheme.general().getContentColor());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 3;
        c.gridy = 2;
        c.gridwidth = 6;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(Layout.marginBetweenRGBAndHex, Layout.marginBetweenInputFieldsAndStave, 0, Layout.contentMargin);
        
        view.add(hexInputFieldLabel, c);
    }
    
    private void layoutHexInputField(ColorScheme colorScheme) {
        hexInputField.setFont(colorScheme.font().bold().m().get());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 3;
        c.gridy = 3;
        c.gridwidth = 6;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(0, Layout.marginBetweenInputFieldsAndStave, 0, Layout.contentMargin);
        
        view.add(hexInputField, c);
    }
    
    private void layoutButtons(ColorScheme colorScheme) {
        JPanel container = new JPanel();
        container.setBackground(new Color(0, 0, 0, 0));
        container.setLayout(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        container.add(cancelButton);
        container.add(confirmButton);
        
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 3;
        c.gridy = 4;
        c.gridwidth = 6;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0, 0, Layout.contentMargin, 0);
       
        view.add(container, c);
    }
    
    
    
    
    
    
    public static void main(String[] args) throws IOException {
        
        
        JFrame frame = new JFrame();
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 0, 0, 0));
        panel.setSize(1000, 1000);
        frame.getContentPane().add(panel);
        panel.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = c.BOTH;
        c.weighty = 1;
        c.weightx = 1;
        
        
        SexyColorPickerViewController scp = new SexyColorPickerViewController(new DarkMode());
        Window defaultWindow = new DefaultWindow(new DarkMode());
        defaultWindow.presentViewController(scp);
        
        defaultWindow.setSize(new Dimension(500, 250));
        defaultWindow.setVisible(true);
        
        
//        panel.add(scp.getView(), c);
//        
//        frame.setUndecorated(true);
//        frame.setBackground(new Color(0, 0, 0, 0));
//        frame.setVisible(true);
    }
    
    
    
}
