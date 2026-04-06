package com.urbannest.controllers;

import com.urbannest.models.Room;
import com.urbannest.repository.RoomRepository;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class RoomManagementView {
    private VBox view;
    private MainController mainCtrl;
    private RoomRepository roomRepo;
    
    // Form controls
    private TextField txtRoomNo;
    private ComboBox<String> cmbType;
    private TextField txtPrice;

    private FlowPane roomGrid;

    public RoomManagementView(MainController mainController) {
        this.mainCtrl = mainController;
        this.roomRepo = new RoomRepository();
        buildView();
    }

    private void buildView() {
        view = new VBox(40);
        view.setPadding(new Insets(20, 0, 40, 0));
        view.setAlignment(Pos.TOP_CENTER);

        Label header = new Label("Room Management");
        header.getStyleClass().add("hero-title");

        Label subtitle = new Label("Curate and manage UrbanNest room listings.");
        subtitle.getStyleClass().add("hero-subtitle");
        
        VBox heroBlock = new VBox(10);
        heroBlock.setAlignment(Pos.CENTER);
        heroBlock.getChildren().addAll(header, subtitle);

        // Form Card
        VBox formCard = new VBox(20);
        formCard.getStyleClass().add("card");
        Label formTitle = new Label("Add New Room");
        formTitle.getStyleClass().add("card-title");

        HBox formRow = new HBox(15);
        formRow.setAlignment(Pos.CENTER_LEFT);

        txtRoomNo = new TextField();
        txtRoomNo.setPromptText("Room Number");
        txtRoomNo.getStyleClass().add("text-field");

        cmbType = new ComboBox<>();
        cmbType.getItems().addAll("Single", "Double", "Deluxe", "Suite");
        cmbType.getStyleClass().add("combo-box");
        cmbType.getSelectionModel().selectFirst();

        txtPrice = new TextField();
        txtPrice.setPromptText("Price (₹)");
        txtPrice.getStyleClass().add("text-field");

        Button btnSave = new Button("Save Room");
        btnSave.getStyleClass().add("luxury-btn-accent");
        btnSave.setOnAction(e -> saveRoom());

        formRow.getChildren().addAll(txtRoomNo, cmbType, txtPrice, btnSave);
        formCard.getChildren().addAll(formTitle, formRow);

        // Vault of Cards (replaces TableView)
        VBox collectionBlock = new VBox(20);
        Label collectionTitle = new Label("All Rooms");
        collectionTitle.getStyleClass().add("card-title");

        roomGrid = new FlowPane();
        roomGrid.setHgap(25);
        roomGrid.setVgap(25);
        roomGrid.setAlignment(Pos.CENTER);

        collectionBlock.getChildren().addAll(collectionTitle, roomGrid);

        view.getChildren().addAll(heroBlock, formCard, collectionBlock);
    }

    private void saveRoom() {
        String num = txtRoomNo.getText();
        String type = cmbType.getValue();
        String priceStr = txtPrice.getText();

        if (num.isEmpty() || priceStr.isEmpty()) {
            showAlert("Validation", "All fields are required.");
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            Room r = new Room(0, num, type, price, "Available");
            roomRepo.addRoom(r);
            txtRoomNo.clear();
            txtPrice.clear();
            refresh();
            showAlert("Success", "Space added successfully.");
        } catch (NumberFormatException e) {
            showAlert("Error", "Price must be a valid number.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }

    public void refresh() {
        List<Room> rooms = roomRepo.getAllRooms();
        roomGrid.getChildren().clear();

        for (Room r : rooms) {
            VBox card = new VBox(10);
            card.getStyleClass().add("card");
            card.setPrefWidth(250);
            
            Label title = new Label(r.getType() + " - " + r.getRoomNumber());
            title.getStyleClass().add("card-title");
            
            Label priceLbl = new Label("₹" + r.getPrice() + " / night");
            priceLbl.getStyleClass().add("price-tag");

            Label statusLbl = new Label(r.getStatus());
            if (r.getStatus().equals("Available")) {
                statusLbl.setStyle("-fx-text-fill: #a3e635; -fx-font-weight: bold;");
            } else {
                statusLbl.setStyle("-fx-text-fill: #fca5a5; -fx-font-weight: bold;");
            }

            Button btnManage = new Button("Manage");
            btnManage.getStyleClass().add("luxury-btn");
            btnManage.setPrefWidth(Double.MAX_VALUE);

            Button btnDelete = new Button("Delete");
            btnDelete.getStyleClass().add("danger-btn");
            btnDelete.setPrefWidth(Double.MAX_VALUE);
            btnDelete.setOnAction(e -> {
                roomRepo.deleteRoom(r.getRoomNumber());
                refresh();
                showAlert("Success", "Room deleted successfully.");
            });

            card.getChildren().addAll(title, priceLbl, statusLbl, btnManage, btnDelete);
            roomGrid.getChildren().add(card);
        }
    }

    public VBox getView() {
        return view;
    }
}
