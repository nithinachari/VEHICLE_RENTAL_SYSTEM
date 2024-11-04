import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

// Vehicle class
class Vehicle {
    String vehicleId;
    String model;
    double rentalPricePerDay;
    boolean isAvailable;
    String pickupDateTime;
    String returnDateTime;

    public Vehicle(String vehicleId, String model, double rentalPricePerDay) {
        this.vehicleId = vehicleId;
        this.model = model;
        this.rentalPricePerDay = rentalPricePerDay;
        this.isAvailable = true;
        this.pickupDateTime = "";
        this.returnDateTime = "";
    }

    @Override
    public String toString() {
        return vehicleId + " - " + model + " ($" + rentalPricePerDay + "/day) - " 
                + (isAvailable ? "Available" : "Rented, Pickup: " + pickupDateTime + 
                ", Return: " + returnDateTime);
    }
}

// Rental Management System GUI
public class VehicleRentalSystem extends JFrame {
    private ArrayList<Vehicle> vehicles = new ArrayList<>();
    private JTextArea displayArea;
    private JTextField idField, modelField, priceField;
    private JSpinner pickupDateTimeSpinner, returnDateTimeSpinner;
    private JButton addVehicleButton, rentVehicleButton, returnVehicleButton, viewVehiclesButton, clearFormButton;

    public VehicleRentalSystem() {
        setTitle("Vehicle Rental Management System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(238, 238, 240));

        // Display Area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Arial", Font.PLAIN, 16));
        displayArea.setBackground(new Color(230, 240, 255));
        displayArea.setForeground(new Color(0, 51, 102));
        displayArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // Input Fields Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(new Color(255, 240, 220));

        // Labels and Text Fields
        idField = createLabeledTextField("Vehicle ID:", inputPanel);
        modelField = createLabeledTextField("Model:", inputPanel);
        priceField = createLabeledTextField("Rental Price/Day:", inputPanel);

        // Date and Time Pickers
        pickupDateTimeSpinner = createDateTimeSpinner("Pickup Date/Time:", inputPanel);
        returnDateTimeSpinner = createDateTimeSpinner("Return Date/Time:", inputPanel);

        add(inputPanel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        buttonPanel.setBackground(new Color(240, 255, 240));

        addVehicleButton = createStyledButton("Add Vehicle", new Color(0, 153, 76));
        rentVehicleButton = createStyledButton("Rent Vehicle", new Color(255, 102, 0));
        returnVehicleButton = createStyledButton("Return Vehicle", new Color(255, 0, 0));
        viewVehiclesButton = createStyledButton("View Vehicles", new Color(51, 153, 255));
        clearFormButton = createStyledButton("Clear Form", new Color(192, 192, 192));

        buttonPanel.add(addVehicleButton);
        buttonPanel.add(rentVehicleButton);
        buttonPanel.add(returnVehicleButton);
        buttonPanel.add(viewVehiclesButton);
        buttonPanel.add(clearFormButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        addVehicleButton.addActionListener(e -> addVehicle());
        rentVehicleButton.addActionListener(e -> rentVehicle());
        returnVehicleButton.addActionListener(e -> returnVehicle());
        viewVehiclesButton.addActionListener(e -> viewVehicles());
        clearFormButton.addActionListener(e -> clearForm());
    }

    // Helper method to create labeled text fields
    private JTextField createLabeledTextField(String labelText, JPanel panel) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(51, 0, 102));
        JTextField textField = new JTextField();
        panel.add(label);
        panel.add(textField);
        return textField;
    }

    // Helper method to create DateTime spinner
    private JSpinner createDateTimeSpinner(String labelText, JPanel panel) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(51, 0, 102));
        
        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE);
        JSpinner dateTimeSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateTimeSpinner, "dd-MM-yyyy HH:mm");
        dateTimeSpinner.setEditor(dateEditor);

        panel.add(label);
        panel.add(dateTimeSpinner);
        return dateTimeSpinner;
    }

    // Styled button creator
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        return button;
    }

    // Methods to manage the vehicles
    private void addVehicle() {
        String id = idField.getText();
        String model = modelField.getText();
        double price;
        try {
            price = Double.parseDouble(priceField.getText());
            Vehicle vehicle = new Vehicle(id, model, price);
            vehicles.add(vehicle);
            displayArea.setText("Vehicle added: " + vehicle + "\n");
        } catch (NumberFormatException ex) {
            displayArea.setText("Invalid price. Please enter a numeric value.\n");
        }
    }

    private void rentVehicle() {
        String id = idField.getText();
        Date pickupDate = (Date) pickupDateTimeSpinner.getValue();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String pickupDateTime = sdf.format(pickupDate);

        for (Vehicle vehicle : vehicles) {
            if (vehicle.vehicleId.equals(id) && vehicle.isAvailable) {
                vehicle.isAvailable = false;
                vehicle.pickupDateTime = pickupDateTime;
                displayArea.setText("Vehicle rented: " + vehicle + "\n");
                return;
            }
        }
        displayArea.setText("Vehicle not available or not found.\n");
    }

    private void returnVehicle() {
        String id = idField.getText();
        Date returnDate = (Date) returnDateTimeSpinner.getValue();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String returnDateTime = sdf.format(returnDate);

        for (Vehicle vehicle : vehicles) {
            if (vehicle.vehicleId.equals(id) && !vehicle.isAvailable) {
                try {
                    Date pickupDate = sdf.parse(vehicle.pickupDateTime);
                    long duration = returnDate.getTime() - pickupDate.getTime();
                    long rentalHours = TimeUnit.MILLISECONDS.toHours(duration);

                    // Calculate rental cost based on complete 24-hour cycles
                    long rentalDays = rentalHours / 24;
                    double totalCost = rentalDays * vehicle.rentalPricePerDay;

                    // Add an additional day charge if there are leftover hours
                    if (rentalHours % 24 > 0) {
                        totalCost += vehicle.rentalPricePerDay;
                    }

                    vehicle.isAvailable = true;
                    vehicle.returnDateTime = returnDateTime;
                    displayArea.setText("Vehicle returned: " + vehicle + "\nTotal Rental Cost: $" + totalCost + "\n");
                    return;
                } catch (ParseException e) {
                    displayArea.setText("Error parsing date format.\n");
                }
            }
        }
        displayArea.setText("Vehicle not rented or not found.\n");
    }

    private void viewVehicles() {
        StringBuilder vehicleList = new StringBuilder("Vehicles:\n");
        for (Vehicle vehicle : vehicles) {
            vehicleList.append(vehicle).append(" - ");
            vehicleList.append(vehicle.isAvailable ? "Available\n" : "Rented\n");
        }
        displayArea.setText(vehicleList.toString());
    }

    private void clearForm() {
        idField.setText("");
        modelField.setText("");
        priceField.setText("");
        pickupDateTimeSpinner.setValue(new Date());
        returnDateTimeSpinner.setValue(new Date());
        displayArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VehicleRentalSystem().setVisible(true);
        });
    }
}
