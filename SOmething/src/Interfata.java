import javax.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Interfata extends JFrame {
    private JTextArea textArea1;
    private JPanel panel1;
    private JTextArea textArea2;
    private JButton button1;
    //getter sa putem lua intrebarea
    public JTextArea getTextArea1() {
        return textArea1;
    }

    public void setTextArea2(String textArea2) {
        this.textArea2.setText(textArea2);
    }

    public JButton getButton1() {
        return button1;
    }

    public void setButton1(JButton button1) {
        this.button1 = button1;
    }

    public void setDimension(int w, int h) {
        add(panel1);
        setBounds(400, 300, w, h);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void addButtonListener (ActionListener listener) {
        this.button1.addActionListener(listener);
    }

    public Interfata() {
        setDimension(600, 400);
        //this.setVisible(true);
    }
}
