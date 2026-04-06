package com.urbannest.controllers;

import com.urbannest.repository.RoomRepository;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class DashboardView {
    private VBox view;
    private RoomRepository roomRepo;
    
    private Label totalRoomsLbl;
    private Label availableRoomsLbl;
    private Label occupiedRoomsLbl;

    public DashboardView() {
        roomRepo = new RoomRepository();
        buildView();
    }

    private void buildView() {
        view = new VBox(40);
        view.setPadding(new Insets(20, 0, 40, 0));
        view.setAlignment(Pos.TOP_CENTER);

        // Hero Section
        VBox heroSection = new VBox(15);
        heroSection.setAlignment(Pos.CENTER);
        heroSection.setPadding(new Insets(80, 0, 80, 0));

        Label title = new Label("Dashboard Overview");
        title.getStyleClass().add("hero-title");

        Label subtitle = new Label("A high-level view of current hotel operations.");
        subtitle.getStyleClass().add("hero-subtitle");

        heroSection.getChildren().addAll(title, subtitle);

        // Highlights Section (Asymmetrical Layout)
        HBox highlightsLayer = new HBox(40);
        highlightsLayer.setAlignment(Pos.CENTER);
        
        // Large Primary Card
        VBox totalCard = createHighlightCard("Total Rooms", "All active rooms in the hotel database.", totalRoomsLbl = new Label("0"), 500, 250);
        
        // Secondary Stats Stack
        VBox metricStack = new VBox(30);
        metricStack.setAlignment(Pos.CENTER);
        
        VBox availableCard = createHighlightCard("Available Rooms", "Rooms currently open for booking.", availableRoomsLbl = new Label("0"), 350, 110);
        VBox occupiedCard = createHighlightCard("Currently Occupied", "Rooms with active reservations.", occupiedRoomsLbl = new Label("0"), 350, 110);
        
        metricStack.getChildren().addAll(availableCard, occupiedCard);

        highlightsLayer.getChildren().addAll(totalCard, metricStack);

        view.getChildren().addAll(heroSection, highlightsLayer);
    }

    private VBox createHighlightCard(String header, String subText, Label statLbl, double width, double height) {
        VBox card = new VBox(15);
        card.getStyleClass().add("card");
        card.setPrefWidth(width);
        card.setPrefHeight(height);
        card.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label(header);
        title.getStyleClass().add("card-title");
        
        Label subt = new Label(subText);
        subt.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 14px;");

        statLbl.getStyleClass().add("stats-number");
        
        card.getChildren().addAll(title, statLbl, subt);
        return card;
    }

    public void refresh() {
        int total = roomRepo.getTotalRooms();
        int available = roomRepo.getAvailableRooms();
        int occupied = total - available;

        totalRoomsLbl.setText(String.valueOf(total));
        availableRoomsLbl.setText(String.valueOf(available));
        occupiedRoomsLbl.setText(String.valueOf(occupied));
    }

    public VBox getView() {
        return view;
    }
}
