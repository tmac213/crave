package crave.db;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Created by Emilio on 4/26/15.
 */
public class RestaurantWindow extends JFrame implements ActionListener {

    private CraveGUI crave;
    private int restaurantID;
    private String restaurantName, restaurantAddress;
    private JLabel titleLabel;
    private JLabel addressLabel;
    private JLabel priceLabel;
    private JLabel descriptionLabel;
    private final JLabel ratingLabel = new JLabel("Rate this dish:");
    private JPanel titlePanel;
    private JPanel infoPanel;
    private JPanel ratingPanel;

    private HashMap<String, Pair<String, String>> nameToInfo = new HashMap<String, Pair<String, String>>();
    private HashMap<String, Pair<Double, Integer>> nameToRatingAndID = new HashMap<String, Pair<Double, Integer>>();
    private final JComboBox<String> dishes;
    private final JComboBox<Integer> rating;


    public RestaurantWindow(CraveGUI crave, int restaurantID, String defaultDishName) {
        super();
        this.crave = crave;
        this.restaurantID = restaurantID;

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Statement statement = null;
        ResultSet resultSet;

        try {
            statement = crave.conn.createStatement();
            resultSet = statement.executeQuery(String.format("select name, address from restaurants where ID = %d;", restaurantID));
            resultSet.next();
            restaurantName = resultSet.getString("name");
            restaurantAddress = resultSet.getString("address");

            resultSet.close();

            resultSet = statement.executeQuery(String.format("select d.name, s.price, s.description, d.avgRating, d.ID " +
                                                            "from dishes d, serves s " +
                                                            "where (s.dID, s.rID) = (d.ID, %s);", restaurantID));
            while (resultSet.next()) {
                String name = resultSet.getString(1);
                Float price = (float)resultSet.getDouble(2);
                String description = resultSet.getString(3);
                Double rating = resultSet.getDouble(4);
                Integer ID = resultSet.getInt(5);

                if (description == null) {
                    description = "Description not available.";
                }
                nameToInfo.put(name, new Pair<String, String>(formatDecimal(price), description));
                nameToRatingAndID.put(name, new Pair<Double, Integer>(rating, ID));
            }


            resultSet.close();

        } catch (SQLException e) {
            System.err.println("ERROR: could not retrieve restaurant info");
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try { statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }

        // combo boxes
        dishes = new JComboBox(nameToInfo.keySet().toArray(new String[nameToInfo.size()]));
        dishes.setSelectedItem(defaultDishName);
        dishes.addActionListener(this);
        dishes.setActionCommand("dishes");

        Integer[] ratingValues = { 0, 1, 2, 3, 4, 5 };
        rating = new JComboBox<Integer>(ratingValues);
        rating.addActionListener(this);
        rating.setActionCommand("rating");


        // labels
        titleLabel = new JLabel(restaurantName);
        addressLabel = new JLabel(restaurantAddress);


        JLabel priceLabelLeft = new JLabel("Price:");
        priceLabel = new JLabel(nameToInfo.get(defaultDishName).getVal1());
        JLabel descriptionLabelLeft = new JLabel("Description:");
        descriptionLabel = new JLabel(nameToInfo.get(defaultDishName).getVal2());


        // label alignment
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        addressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        priceLabelLeft.setLabelFor(priceLabel);
        descriptionLabelLeft.setLabelFor(descriptionLabel);
        ratingLabel.setLabelFor(rating);

        // label borders
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addressLabel.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        priceLabelLeft.setBorder(BorderFactory.createEmptyBorder(50,0,0,50));
        descriptionLabelLeft.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        priceLabel.setBorder(BorderFactory.createEmptyBorder(50,0,0,0));
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        // Adjust combo boxes
        dishes.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        rating.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        // create panels
        titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(25,50,25,50));

        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        JPanel infoPanelLeft = new JPanel();
        infoPanelLeft.setLayout(new BoxLayout(infoPanelLeft, BoxLayout.Y_AXIS));
        infoPanelLeft.setBorder(BorderFactory.createEmptyBorder(0,0,0,75));

        JPanel infoPanelOuter = new JPanel();
        infoPanelOuter.setLayout(new BoxLayout(infoPanelOuter, BoxLayout.X_AXIS));
        infoPanelOuter.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        ratingPanel = new JPanel();
        ratingPanel.setLayout(new BoxLayout(ratingPanel, BoxLayout.Y_AXIS));
        ratingPanel.setBorder(BorderFactory.createEmptyBorder(50,50,50,0));

        JPanel dishesPanel = new JPanel();
        dishesPanel.setLayout(new BoxLayout(dishesPanel, BoxLayout.Y_AXIS));
        dishesPanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        JPanel ratingPanelInner = new JPanel();
        ratingPanelInner.setLayout(new BoxLayout(ratingPanelInner, BoxLayout.Y_AXIS));
        ratingPanelInner.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));


        titlePanel.add(titleLabel);
        titlePanel.add(addressLabel);

        dishesPanel.add(dishes);

        infoPanel.add(priceLabel);
        infoPanel.add(descriptionLabel);

        infoPanelLeft.add(priceLabelLeft);
        infoPanelLeft.add(descriptionLabelLeft);

        infoPanelOuter.add(infoPanelLeft);
        infoPanelOuter.add(infoPanel);

        ratingPanel.add(ratingLabel);

        ratingPanelInner.add(rating);

        bottomPanel.add(ratingPanel);
        bottomPanel.add(ratingPanelInner);

        Container pane = getContentPane();
        pane.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        pane.add(titlePanel);
        pane.add(dishesPanel);
        pane.add(infoPanelOuter);
        pane.add(bottomPanel);

        setSize(500, 500);
        pack();
        crave.centerFrame(this);
        this.setVisible(true);
    }

    public String formatDecimal(float number) {
        float epsilon = 0.004f; // 4 tenths of a cent
        if (Math.abs(Math.round(number) - number) < epsilon) {
            return String.format("$%10.0f", number); // sdb
        } else {
            return String.format("$%10.2f", number); // dj_segfault
        }
    }

    private void updateLabels(String dishName) {
        priceLabel.setText(nameToInfo.get(dishName).getVal1());
        descriptionLabel.setText(nameToInfo.get(dishName).getVal2());
        revalidate();
        repaint();
        pack();
    }

    private void insertRating(Integer rating, String dishName) {
        System.out.printf("Inserting rating for user %s\n", crave.getCurrentUser());
        Statement statement = null;
        ResultSet resultSet = null;
        int result = -1;
        try {
            statement = crave.conn.createStatement();
            statement.executeUpdate(String.format("insert into reviews(rID, uID, dID, rating) " +
                            "values (%d, %d, %d, %d) " +
                            "on duplicate key update " +
                            "rating = %d;",
                    restaurantID,
                    crave.dbAccess.getCurrentUserID(),
                    nameToRatingAndID.get(dishName).getVal2(),
                    rating,
                    rating));

        } catch (SQLException e) {
            System.err.println("ERROR: could not insert rating");
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {}
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("dishes")) {
            updateLabels((String) dishes.getSelectedItem());
        } else if (e.getActionCommand().equals("rating")) {
            insertRating((Integer)rating.getSelectedItem(), (String)dishes.getSelectedItem());
        } else {
            //TODO go back to previous window
        }
    }


}
