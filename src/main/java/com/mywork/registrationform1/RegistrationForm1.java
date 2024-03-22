/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mywork.registrationform1;

/**
 *
 * @author Eng Kazungu Tracy Chao P
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistrationForm1 extends JFrame implements ActionListener {

    private JLabel nameLabel, mobileLabel, genderLabel, dobLabel, addressLabel;
    private JTextField nameField, mobileField;
    private JRadioButton maleRadioButton, femaleRadioButton;
    private JComboBox<String> dayComboBox, monthComboBox, yearComboBox;
    private JTextArea addressArea, displayArea;
    private JCheckBox termsCheckBox;
    private JButton submitButton, resetButton;

    private final String url = "jdbc:mysql://localhost:3306/registrationform1";
    private final String username = "root";
    private final String password = "";

    public RegistrationForm1() {

        setTitle("Registration Form");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);
        mobileLabel = new JLabel("Mobile:");
        mobileField = new JTextField(20);
        genderLabel = new JLabel("Gender:");
        maleRadioButton = new JRadioButton("Male");
        femaleRadioButton = new JRadioButton("Female");
        dobLabel = new JLabel("Date of Birth:");
        String[] days = new String[31];
        for (int i = 1; i <= 31; i++) {
            days[i - 1] = String.valueOf(i);
        }
        dayComboBox = new JComboBox<>(days);
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        monthComboBox = new JComboBox<>(months);
        String[] years = new String[125];
        for (int i = 1900; i <= 2024; i++) {
            years[i - 1900] = String.valueOf(i);
        }
        yearComboBox = new JComboBox<>(years);
        addressLabel = new JLabel("Address:");
        addressArea = new JTextArea(5, 20);
        termsCheckBox = new JCheckBox("Accept Terms and Conditions");
        submitButton = new JButton("Submit");
        resetButton = new JButton("Reset");
        displayArea = new JTextArea(10, 30);

        submitButton.addActionListener(this);
        resetButton.addActionListener(this);

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadioButton);
        genderGroup.add(femaleRadioButton);

        add(nameLabel);
        add(nameField);
        add(mobileLabel);
        add(mobileField);
        add(genderLabel);
        add(maleRadioButton);
        add(femaleRadioButton);
        add(dobLabel);
        add(dayComboBox);
        add(monthComboBox);
        add(yearComboBox);
        add(addressLabel);
        add(new JScrollPane(addressArea));
        add(termsCheckBox);
        add(submitButton);
        add(resetButton);
        add(displayArea);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {

            String name = nameField.getText();
            String mobile = mobileField.getText();
            String gender = maleRadioButton.isSelected() ? "Male" : "Female";
            String dobString = yearComboBox.getSelectedItem() + " " + monthComboBox.getSelectedItem() + " " + dayComboBox.getSelectedItem();
            String address = addressArea.getText();
            boolean termsAccepted = termsCheckBox.isSelected();

            if (name.isEmpty() || mobile.isEmpty() || address.isEmpty() || !termsAccepted) {
                JOptionPane.showMessageDialog(this, "Please fill in all the fields and accept the terms and conditions.");
                return;
            }

            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy MMMM d", Locale.ENGLISH);
                Date dobDate = inputFormat.parse(dobString);
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDob = outputFormat.format(dobDate);

                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();
                String insertQuery = "INSERT INTO FORM1 (name, mobile, gender, dob, address) VALUES ('" +
                        name + "', '" + mobile + "', '" + gender + "', '" + formattedDob + "', '" + address + "')";
                statement.executeUpdate(insertQuery);
                statement.close();
                connection.close();
            } catch (SQLException | ParseException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred while storing data in the database.");
            }

            displayArea.setText("Name: " + name + "\n" +
                    "Mobile: " + mobile + "\n" +
                    "Gender: " + gender + "\n" +
                    "Date of Birth: " + dobString + "\n" +  // Display original date string
                    "Address: " + address);
        } else if (e.getSource() == resetButton) {
            nameField.setText("");
            mobileField.setText("");
            maleRadioButton.setSelected(false);
            femaleRadioButton.setSelected(false);
            addressArea.setText("");
            termsCheckBox.setSelected(false);
            displayArea.setText("");
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RegistrationForm1();
            }
        });
    }
}