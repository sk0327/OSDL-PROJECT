package com.urbannest.controllers;

import com.urbannest.models.Booking;
import com.urbannest.models.Room;
import com.urbannest.repository.BookingRepository;
import com.urbannest.repository.RoomRepository;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class CheckoutView {
    private VBox view;
    private MainController mainCtrl;
    private BookingRepository bookingRepo;
    private RoomRepository roomRepo;

    private ComboBox<Booking> cmbActiveBookings;

    private Label lblGuestName;
    private Label lblRoomType;
    private Label lblBookingDate;
    private Label lblPriceTotal;
    private Button btnCheckout;

    public CheckoutView(MainController mainController) {
        this.mainCtrl = mainController;
        this.bookingRepo = new BookingRepository();
        this.roomRepo = new RoomRepository();
        buildView();
    }

    private void buildView() {
        view = new VBox(40);
        view.setPadding(new Insets(40, 0, 40, 0));
        view.setAlignment(Pos.TOP_CENTER);

        VBox heroSection = new VBox(10);
        heroSection.setAlignment(Pos.CENTER);

        Label title = new Label("Checkout Management");
        title.getStyleClass().add("hero-title");

        Label subtitle = new Label("Process departing guests and finalized billing.");
        subtitle.getStyleClass().add("hero-subtitle");
        heroSection.getChildren().addAll(title, subtitle);

        // Core Summary Card
        VBox summaryCard = new VBox(25);
        summaryCard.getStyleClass().add("card");
        summaryCard.setPrefWidth(500);
        summaryCard.setMaxWidth(500);

        Label summaryTitle = new Label("Checkout Summary");
        summaryTitle.getStyleClass().add("card-title");

        VBox selectionBox = new VBox(8);
        Label selectionLbl = new Label("Select Active Reservation");
        selectionLbl.getStyleClass().add("form-label");

        cmbActiveBookings = new ComboBox<>();
        cmbActiveBookings.setPrefWidth(Double.MAX_VALUE);
        cmbActiveBookings.getStyleClass().add("combo-box");
        cmbActiveBookings.setOnAction(e -> renderReceipt());

        selectionBox.getChildren().addAll(selectionLbl, cmbActiveBookings);

        VBox receiptBox = new VBox(15);
        receiptBox.setPadding(new Insets(20));
        receiptBox.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-background-radius: 8;");

        lblGuestName = new Label("Guest: --");
        lblGuestName.setStyle("-fx-text-fill: #f8fafc; -fx-font-size: 16px;");

        lblRoomType = new Label("Room: --");
        lblRoomType.setStyle("-fx-text-fill: #94a3b8;");

        lblBookingDate = new Label("Date: --");
        lblBookingDate.setStyle("-fx-text-fill: #94a3b8;");

        HBox totalBox = new HBox(10);
        totalBox.setAlignment(Pos.CENTER_LEFT);
        Label totalPrefix = new Label("Total Amount:");
        totalPrefix.setStyle("-fx-text-fill: #f8fafc; -fx-font-weight: bold; -fx-font-size: 18px;");

        lblPriceTotal = new Label("₹0.00");
        lblPriceTotal.getStyleClass().add("price-tag");
        lblPriceTotal.setStyle("-fx-font-size: 24px; -fx-text-fill: #d4af37;");
        totalBox.getChildren().addAll(totalPrefix, lblPriceTotal);

        receiptBox.getChildren().addAll(lblGuestName, lblRoomType, lblBookingDate, totalBox);

        btnCheckout = new Button("Process Payment & Checkout");
        btnCheckout.getStyleClass().add("danger-btn");
        btnCheckout.setPrefWidth(Double.MAX_VALUE);
        btnCheckout.setPrefHeight(50);
        btnCheckout.setDisable(true);
        btnCheckout.setOnAction(e -> doCheckout());

        summaryCard.getChildren().addAll(summaryTitle, selectionBox, receiptBox, btnCheckout);
        view.getChildren().addAll(heroSection, summaryCard);
    }

    private void renderReceipt() {
        Booking selected = cmbActiveBookings.getValue();
        if (selected == null) {
            lblGuestName.setText("Guest: --");
            lblRoomType.setText("Room: --");
            lblBookingDate.setText("Date: --");
            lblPriceTotal.setText("₹0.00");
            btnCheckout.setDisable(true);
            return;
        }

        lblGuestName.setText("Guest: " + selected.getCustomerName());
        lblRoomType.setText("Room: " + selected.getRoomNumber());
        lblBookingDate.setText("Date: " + selected.getBookingDate());

        // Fetch room price to mock the total
        double price = 0;
        List<Room> rooms = roomRepo.getAllRooms();
        for (Room r : rooms) {
            if (r.getRoomNumber().equals(selected.getRoomNumber())) {
                price = r.getPrice();
                break;
            }
        }

        lblPriceTotal.setText("₹" + price);
        btnCheckout.setDisable(false);
    }

    private void doCheckout() {
        Booking selected = cmbActiveBookings.getValue();
        if (selected == null)
            return;

        // Fetch room price to mock the total
        double price = 0;
        List<Room> rooms = roomRepo.getAllRooms();
        for (Room r : rooms) {
            if (r.getRoomNumber().equals(selected.getRoomNumber())) {
                price = r.getPrice();
                break;
            }
        }

        // Generate and show bill
        showBillDialog(selected, price);

        // Free the room
        roomRepo.updateRoomStatus(selected.getRoomNumber(), "Available");
        // Remove booking
        bookingRepo.removeBookingByRoom(selected.getRoomNumber());

        refresh();
    }

    private void showBillDialog(Booking booking, double price) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Checkout Bill");
        dialog.setHeaderText("Guest Invoice - UrbanNest");

        // Styling the dialog pane
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().addAll(view.getScene().getStylesheets());
        dialogPane.getStyleClass().add("card");
        dialogPane.setStyle("-fx-background-color: #1e293b;");

        TextArea billArea = new TextArea();
        billArea.setEditable(false);
        billArea.setStyle(
                "-fx-font-family: 'Courier New', monospace; -fx-font-size: 14px; -fx-control-inner-background: #0f172a; -fx-text-fill: #f8fafc;");
        billArea.setPrefRowCount(17);
        billArea.setPrefColumnCount(45);

        double tax = price * 0.18; // 18% tax
        double total = price + tax;

        String billText = String.format(
                "=============================================\n" +
                        "               URBANNEST HOTEL               \n" +
                        "               Invoice / Bill                \n" +
                        "=============================================\n" +
                        " Guest Name    : %s\n" +
                        " Room Number   : %s\n" +
                        " Booking Date  : %s\n" +
                        "---------------------------------------------\n" +
                        " Room Charges  : ₹ %10.2f\n" +
                        " Taxes (18%%)   : ₹ %10.2f\n" +
                        "---------------------------------------------\n" +
                        " Grand Total   : ₹ %10.2f\n" +
                        "=============================================\n" +
                        "       Thank you for staying with us!        \n" +
                        "=============================================",
                booking.getCustomerName(), booking.getRoomNumber(), booking.getBookingDate(),
                price, tax, total);

        billArea.setText(billText);

        dialogPane.setContent(billArea);
        dialogPane.getButtonTypes().add(ButtonType.OK);

        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        if (okButton != null) {
            okButton.getStyleClass().add("primary-btn");
        }

        dialog.showAndWait();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }

    public void refresh() {
        cmbActiveBookings.getItems().clear();
        List<Booking> bookings = bookingRepo.getAllBookings();
        cmbActiveBookings.getItems().addAll(bookings);

        // Configure Custom Display for Booking objects
        cmbActiveBookings.setCellFactory(lv -> new ListCell<Booking>() {
            @Override
            protected void updateItem(Booking item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("Room " + item.getRoomNumber() + " - " + item.getCustomerName());
                }
            }
        });
        cmbActiveBookings.setButtonCell(new ListCell<Booking>() {
            @Override
            protected void updateItem(Booking item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("Room " + item.getRoomNumber() + " - " + item.getCustomerName());
                }
            }
        });

        renderReceipt();
    }

    public VBox getView() {
        return view;
    }
}
