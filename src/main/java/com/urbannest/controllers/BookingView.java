package com.urbannest.controllers;

import com.urbannest.models.Booking;
import com.urbannest.models.Room;
import com.urbannest.repository.BookingRepository;
import com.urbannest.repository.RoomRepository;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.util.List;

public class BookingView {
    private VBox view;
    private MainController mainCtrl;
    private BookingRepository bookingRepo;
    private RoomRepository roomRepo;

    private ComboBox<String> cmbRoom;
    private TextField txtName;
    private TextField txtContact;
    private DatePicker dpCheckIn;
    private DatePicker dpCheckOut;
    private TextField txtGuests;

    public BookingView(MainController mainController) {
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
        
        Label title = new Label("Book a Room");
        title.getStyleClass().add("hero-title");

        Label subtitle = new Label("Select your dates and preferred room type to reserve your stay.");
        subtitle.getStyleClass().add("hero-subtitle");
        heroSection.getChildren().addAll(title, subtitle);

        // Booking Engine Card
        VBox engineCard = new VBox(25);
        engineCard.getStyleClass().add("card");
        engineCard.setPrefWidth(600);
        engineCard.setMaxWidth(600);

        Label engineTitle = new Label("Booking Details");
        engineTitle.getStyleClass().add("card-title");

        // Form Layout
        GridPane form = new GridPane();
        form.setHgap(20);
        form.setVgap(20);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        form.getColumnConstraints().addAll(col1, col2);

        // Name
        VBox boxName = createInputBox("Guest Name", txtName = new TextField());
        // Contact
        VBox boxContact = createInputBox("Contact Number", txtContact = new TextField());
        // Room
        cmbRoom = new ComboBox<>();
        cmbRoom.setPrefWidth(Double.MAX_VALUE);
        cmbRoom.getStyleClass().add("combo-box");
        VBox boxRoom = createInputBox("Room Selection", cmbRoom);
        
        // Dates
        dpCheckIn = new DatePicker(LocalDate.now());
        dpCheckIn.getStyleClass().add("date-picker");
        dpCheckIn.setPrefWidth(Double.MAX_VALUE);
        VBox boxCheckIn = createInputBox("Check-in", dpCheckIn);

        dpCheckOut = new DatePicker(LocalDate.now().plusDays(1));
        dpCheckOut.getStyleClass().add("date-picker");
        dpCheckOut.setPrefWidth(Double.MAX_VALUE);
        VBox boxCheckOut = createInputBox("Check-out", dpCheckOut);

        // Guests
        VBox boxGuests = createInputBox("Number of Guests", txtGuests = new TextField());
        txtGuests.setText("1");

        form.add(boxName, 0, 0);
        form.add(boxContact, 1, 0);
        form.add(boxRoom, 0, 1, 2, 1);
        form.add(boxCheckIn, 0, 2);
        form.add(boxCheckOut, 1, 2);
        form.add(boxGuests, 0, 3, 2, 1);

        Button btnBook = new Button("Confirm Reservation");
        btnBook.getStyleClass().add("luxury-btn-accent");
        btnBook.setPrefWidth(Double.MAX_VALUE);
        btnBook.setPrefHeight(50);
        btnBook.setOnAction(e -> handleBooking());

        engineCard.getChildren().addAll(engineTitle, form, btnBook);

        view.getChildren().addAll(heroSection, engineCard);
    }

    private VBox createInputBox(String labelText, Control inputControl) {
        VBox box = new VBox(8);
        Label label = new Label(labelText);
        label.getStyleClass().add("form-label");
        box.getChildren().addAll(label, inputControl);
        return box;
    }

    private void handleBooking() {
        String name = txtName.getText();
        String contact = txtContact.getText();
        String roomSel = cmbRoom.getValue();
        
        if(name.isEmpty() || contact.isEmpty() || roomSel == null) {
            showAlert("Incomplete Forms", "Please complete all fields to secure your reservation.");
            return;
        }

        String roomNo = roomSel.split(" ")[1];

        Booking b = new Booking(0, name, contact, roomNo, dpCheckIn.getValue().toString());
        bookingRepo.addBooking(b);
        roomRepo.updateRoomStatus(roomNo, "Occupied");

        showAlert("Reservation Confirmed", "Welcome to UrbanNest. Your stay is secured.");
        txtName.clear();
        txtContact.clear();
        refresh();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(title.equals("Reservation Confirmed") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }

    public void refresh() {
        // Fetch rooms in a background thread to prevent UI freezing
        Thread fetchThread = new Thread(() -> {
            List<Room> rooms = roomRepo.getAllRooms();
            
            // Update the UI safely on the JavaFX Application Thread
            javafx.application.Platform.runLater(() -> {
                cmbRoom.getItems().clear();
                for (Room r : rooms) {
                    if (r.getStatus().equals("Available")) {
                        cmbRoom.getItems().add(r.getType() + " " + r.getRoomNumber() + " - ₹" + r.getPrice());
                    }
                }
                if (!cmbRoom.getItems().isEmpty()) {
                    cmbRoom.getSelectionModel().selectFirst();
                }
            });
        });
        fetchThread.start();
    }

    public VBox getView() {
        return view;
    }
}
