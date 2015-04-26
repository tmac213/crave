package crave.db;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Emilio on 4/26/15.
 */
public class DishWindow extends JFrame implements ActionListener {
    private CraveGUI crave;
    private SearchWindow window;
    private JTextArea textArea;
    private JScrollPane pane;

    public DishWindow(CraveGUI c, SearchWindow w) {
        super();
        this.crave = c;
        this.window = w;

        this.textArea = new JTextArea();

        this.pane = new JScrollPane(this.textArea);
        this.pane.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Search Result(s)"),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        this.pane.getVerticalScrollBar().setUnitIncrement(16);
        this.pane.getHorizontalScrollBar().setUnitIncrement(16);
        this.pane.setPreferredSize(this.window.getSize());


        this.add(pane, "Center");

        this.setSize(this.window.getSize());

        //position the Frame NEXT to the CraveGUI window
        this.setLocation(this.window.getX() + (int)this.window.getSize().getWidth(), this.window.getY());

        this.pack();

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
