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
    private JLabel nameLabel;
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

            resultSet = statement.executeQuery(String.format("select d.name, s.price, s.description, d.avgRating, d.ID" +
                                                            "from dishes d, serves s" +
                                                            "where (s.dID, s.rID) = (d.ID, %s);"));
            while (resultSet.next()) {
                String name = resultSet.getString(1);
                String price = Double.toString(resultSet.getDouble(2));
                String description = resultSet.getString(3);
                Double rating = resultSet.getDouble(4);
                Integer ID = resultSet.getInt(5);

                if (description == null) {
                    description = "Description not available.";
                }
                nameToInfo.put(name, new Pair<String, String>(price, description));
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

        nameLabel = new JLabel(defaultDishName);
        priceLabel = new JLabel(nameToInfo.get(defaultDishName).getVal1());
        descriptionLabel = new JLabel(nameToInfo.get(defaultDishName).getVal2());


        // label alignment
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        addressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ratingLabel.setLabelFor(rating);

        // label borders
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addressLabel.setBorder(BorderFactory.createEmptyBorder());

        // Adjust combo boxes
        dishes.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        rating.setBorder(BorderFactory.createEmptyBorder());

        // create panels
        titlePanel = new JPanel();
        titleLabel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder());

        ratingPanel = new JPanel();
        ratingPanel.setLayout(new BoxLayout(ratingPanel, BoxLayout.X_AXIS));
        ratingPanel.setBorder(BorderFactory.createEmptyBorder());



        titlePanel.add(titleLabel);
        titlePanel.add(addressLabel);
        titleLabel.add(dishes);

        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(descriptionLabel);

        ratingPanel.add(ratingLabel);
        ratingPanel.add(rating);

        Container pane = getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(titlePanel);
        pane.add(infoPanel);
        pane.add(ratingPanel);

        setSize(500, 300);
        pack();
        crave.centerFrame(this);

    }

    private void updateLabels(String dishName) {
        nameLabel.setText(dishName);
        priceLabel.setText(nameToInfo.get(dishName).getVal1());
        descriptionLabel.setText(nameToInfo.get(dishName).getVal2());
        revalidate();
        repaint();
        pack();
    }

    private void insertRating(Integer rating, String dishName) {
        System.out.printf("Checking for rating for user %s\n", crave.getCurrentUser());
        Statement statement = null;
        ResultSet resultSet = null;
        int result = -1;
        try {
            statement = crave.conn.createStatement();
            statement.executeUpdate(String.format("insert into reviews(rID, uID, dID, rating)" +
                            "values (%d, %d, %d, %d)" +
                            "on duplicate key update" +
                            "rating = values (%d);",
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
