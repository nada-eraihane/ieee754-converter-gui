package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class GuiOne {
    // fields for the text fields and other variables
    private static JTextField decimalField; // text field for decimal input
    private static JTextField binaryField;  // text field for binary input
    private static int decimalPlaces = 6;   // default decimal places for float conversion
    private static JTextField activeField;  // tracks active text field

    public static void main(String[] args) {
        // create the main frame for the application
        JFrame frame = new JFrame("IEEE 754 Converter");
        frame.setSize(510, 500); // size of the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close the application when the frame is closed
        frame.setLayout(null); // absolute layout

        //background colour for the frame
        frame.getContentPane().setBackground(Color.LIGHT_GRAY);

        // create the decimal text field
        decimalField = new JTextField();
        decimalField.setBounds(50, 20, 390, 50); // position and size of the text field
        decimalField.setFont(new Font("Arial", Font.PLAIN, 12)); //  font & size
        frame.add(decimalField); // add the text field to the frame

        // Create the binary text field
        binaryField = new JTextField();
        binaryField.setBounds(50, 80, 390, 50); // position and size of the text field
        binaryField.setFont(new Font("Arial", Font.PLAIN, 12)); // font & size
        frame.add(binaryField); // add the text field to the frame

        // focus listener to decimalField to keep track of the active field
        decimalField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                activeField = decimalField; // set activeField to decimalField
                decimalField.setCaretPosition(decimalField.getText().length()); // move caret to end
            }

            @Override
            public void focusLost(FocusEvent e) {
                // do nothing when focus is lost
            }
        });

        //focus listener to binaryField to keep track of the active field
        binaryField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                activeField = binaryField; // set activeField to binaryField
                binaryField.setCaretPosition(binaryField.getText().length()); // move caret to end
            }

            @Override
            public void focusLost(FocusEvent e) {
                // do nothing when focus is lost
            }
        });

        // create buttons 
        createButtons(frame);

        // make the frame visible
        frame.setVisible(true);
    }

    // create buttons and add them to the frame
    private static void createButtons(JFrame frame) {
        // configurations
        String[] buttonLabels = {
                "to decimal", "to binary", "clear",
                "7", "8", "9", "decimal places",
                "4", "5", "6", "left", "right",
                "1", "2", "3", "enter",
                "0", ".", "-", "s", "delete"
        };

        Color[] buttonColors = {
                Color.ORANGE, Color.ORANGE, Color.MAGENTA,
                Color.BLUE, Color.BLUE, Color.BLUE, Color.GRAY,
                Color.BLUE, Color.BLUE, Color.BLUE, Color.GRAY, Color.GRAY,
                Color.BLUE, Color.BLUE, Color.BLUE, Color.GRAY,
                Color.BLUE, Color.BLUE, Color.GRAY, Color.GRAY, Color.GRAY
        };

        int buttonWidth = 70, buttonHeight = 50; // size of a regular button
        int doubleButtonWidth = buttonWidth * 2 + 10; // size of a double-sized button
        int x = 50, y = 150; // position for the first row and column of buttons

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.setFont(new Font("Arial", Font.BOLD, 12)); // font style
            button.setBackground(buttonColors[i]); // set background colour
            button.setForeground(Color.WHITE); // text colour 

            // set button size and position based on its label
            if (buttonLabels[i].equals("to decimal") || buttonLabels[i].equals("to binary") || buttonLabels[i].equals("enter") 
            		|| buttonLabels[i].equals("decimal places")) {
                button.setBounds(x, y, doubleButtonWidth, buttonHeight); // position and size of double-sized button
                x += doubleButtonWidth + 10; // move to the next position
            } else {
                button.setBounds(x, y, buttonWidth, buttonHeight); // position and size of normal button
                x += buttonWidth + 10; // move to the next position
            }

            // move to the next row if needed
            if ((buttonLabels[i].equals("clear") || buttonLabels[i].equals("decimal places") || buttonLabels[i].equals("right") 
            		|| buttonLabels[i].equals("enter"))) {
                x = 50;
                y += buttonHeight + 10;
            }

            button.setUI(new RoundedButtonUI()); // rounded edges
            button.addActionListener(new ButtonClickListener()); // add action listener
            frame.add(button); // add button to the frame
        }
    }

    // custom ButtonUI class for rounded edges
    static class RoundedButtonUI extends javax.swing.plaf.basic.BasicButtonUI {
        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            AbstractButton button = (AbstractButton) c;
            button.setOpaque(false);
            button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton button = (AbstractButton) c;
            paintBackground(g, button, button.getModel().isPressed() ? 2 : 0);
            super.paint(g, c);
        }

        private void paintBackground(Graphics g, JComponent c, int yOffset) {
            Dimension size = c.getSize();
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(c.getBackground().darker());
            g.fillRoundRect(0, yOffset, size.width, size.height - yOffset, 10, 10);
            g.setColor(c.getBackground());
            g.fillRoundRect(0, yOffset, size.width, size.height + yOffset - 5, 10, 10);
        }
    }

    // action listener class for handling button clicks
    private static class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand(); // get the label of the clicked button

            switch (command) {
                case "clear":
                    decimalField.setText(""); // clear the decimal field
                    binaryField.setText(""); // clear the binary field
                    break;
                case "to binary":
                    convertToBinary(); // convert decimal to binary
                    break;
                case "to decimal":
                    convertToDecimal(); // convert binary to decimal
                    break;
                case "enter":
                    break;
                case "decimal places":
                    setDecimalPlaces(); // set the number of decimal places
                    break;
                case "left":
                    moveCursorLeft(); // move cursor to the left
                    break;
                case "right":
                    moveCursorRight(); // move cursor to the right
                    break;
                case "s":
                    displaySignBit(); // display the sign bit of the binary number
                    break;
                case "delete":
                    deleteCharacter(); // delete the character at the cursor position
                    break;
                default:
                    if (activeField != null) {
                        int position = activeField.getCaretPosition(); // get current cursor position
                        // insert the button's text at the current cursor position
                        activeField.setText(activeField.getText().substring(0, position) + command + activeField.getText().substring(position));
                        activeField.setCaretPosition(position + 1); // move cursor to the next position
                    }
                    break;
            }
        }

        // left button
        private void moveCursorLeft() {
            if (activeField != null) {
                int position = activeField.getCaretPosition();
                if (position > 0) {
                    activeField.setCaretPosition(position - 1); // move cursor one position left
                }
            }
        }

        // right button
        private void moveCursorRight() {
            if (activeField != null) {
                int position = activeField.getCaretPosition();
                if (position < activeField.getText().length()) {
                    activeField.setCaretPosition(position + 1); // move cursor one position right
                }
            }
        }

        // "s" button 
        private void displaySignBit() {
            String binaryInput = binaryField.getText();
            if (binaryInput.length() == 64) {
                char signBit = binaryInput.charAt(0); // get the first bit
                String sign = (signBit == '0') ? "0 (+ve)" : "1 (-ve)"; // determine the sign
                JOptionPane.showMessageDialog(null, "Sign Bit: " + sign); // display the sign bit
            } else {
                JOptionPane.showMessageDialog(null, "Binary field must be a 64-bit binary number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // to binary button
        private void convertToBinary() {
            try {
                String decimalInput = decimalField.getText();
                if (decimalInput.contains(".")) {
                    double decimal = Double.parseDouble(decimalInput);
                    binaryField.setText(doubleToBinaryString(decimal)); // convert to binary and display (floats)
                } else {
                    long decimal = Long.parseLong(decimalInput);
                    binaryField.setText(Long.toBinaryString(decimal)); // convert to binary and display
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid decimal number.", "Error", JOptionPane.ERROR_MESSAGE);// error handling
            }
        }

        // to decimal button
        private void convertToDecimal() {
            try {
                String binaryInput = binaryField.getText();
                if (binaryInput.length() == 64) {
                    double decimal = binaryStringToDouble(binaryInput);
                    decimalField.setText(String.format("%." + decimalPlaces + "f", decimal)); // convert to decimal and display (float)
                } else {
                    long decimal = Long.parseLong(binaryInput, 2);
                    decimalField.setText(String.valueOf(decimal)); // convert to decimal and display
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid binary number.", "Error", JOptionPane.ERROR_MESSAGE);// error handling
            }
        }

        // decimal places button
        private void setDecimalPlaces() {
            String input = JOptionPane.showInputDialog(null, "Enter the number of decimal places:", decimalPlaces);
            try {
                decimalPlaces = Integer.parseInt(input); // set the number of decimal places
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);// error handling
            }
        }

        // delete  button
        private void deleteCharacter() {
            if (activeField != null) {
                int position = activeField.getCaretPosition();
                if (position > 0) {
                    String text = activeField.getText();
                    activeField.setText(text.substring(0, position - 1) + text.substring(position)); // remove the character
                    activeField.setCaretPosition(position - 1); // move cursor back one position
                }
            }
        }

        // to convert a double to an IEEE 754 binary string
        private String doubleToBinaryString(double value) {
            long longBits = Double.doubleToLongBits(value);
            return String.format("%64s", Long.toBinaryString(longBits)).replace(' ', '0'); // convert and pad with leading zeros
        }

        // to convert an IEEE 754 binary string to a double
        private double binaryStringToDouble(String binary) {
            long longBits = Long.parseUnsignedLong(binary, 2);
            return Double.longBitsToDouble(longBits); // convert from binary string to double
        }
    }
}
