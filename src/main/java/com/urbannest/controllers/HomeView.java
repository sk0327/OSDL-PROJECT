package com.urbannest.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class HomeView {
    private VBox view;
    private MainController mainCtrl;

    public HomeView(MainController mainCtrl) {
        this.mainCtrl = mainCtrl;
        buildView();
    }

    private void buildView() {
        view = new VBox(50);
        view.setPadding(new Insets(60, 0, 60, 0));
        view.setAlignment(Pos.TOP_CENTER);

        // Hero Section
        VBox heroSection = new VBox(15);
        heroSection.setAlignment(Pos.CENTER);

        Label title = new Label("Welcome to UrbanNest");
        title.getStyleClass().add("hero-title");

        Label subtitle = new Label("Manage your boutique hotel experience.");
        subtitle.getStyleClass().add("hero-subtitle");

        heroSection.getChildren().addAll(title, subtitle);

        // Interactive Portal Grid
        FlowPane portals = new FlowPane();
        portals.setHgap(30);
        portals.setVgap(30);
        portals.setAlignment(Pos.CENTER);
        portals.setMaxWidth(1000);

        VBox btnDashboard = createPortalCard("✨", "Dashboard", "View your administrative overview.");
        VBox btnRooms = createPortalCard("🗝️", "Room Management", "Manage all hotel rooms.");
        VBox btnAmenities = createPortalCard("🥂", "Amenities", "View hotel facilities.");
        VBox btnBooking = createPortalCard("📅", "Book a Room", "Create a new guest reservation.");
        VBox btnCheckout = createPortalCard("🛎️", "Manage Checkout", "Process departing guests.");

        btnDashboard.setOnMouseClicked(e -> mainCtrl.navigateTo("Dashboard"));
        btnRooms.setOnMouseClicked(e -> mainCtrl.navigateTo("Room Management"));
        btnAmenities.setOnMouseClicked(e -> mainCtrl.navigateTo("Amenities"));
        btnBooking.setOnMouseClicked(e -> mainCtrl.navigateTo("Book a Room"));
        btnCheckout.setOnMouseClicked(e -> mainCtrl.navigateTo("Checkout"));

        portals.getChildren().addAll(btnDashboard, btnRooms, btnAmenities, btnBooking, btnCheckout);

        view.getChildren().addAll(heroSection, portals);
    }

    private VBox createPortalCard(String icon, String title, String sub) {
        VBox card = new VBox(15);
        card.getStyleClass().add("portal-card");
        card.setPrefSize(280, 220);
        
        Label iconLbl = new Label(icon);
        iconLbl.getStyleClass().add("portal-icon");

        Label titleLbl = new Label(title);
        titleLbl.getStyleClass().add("card-title");

        Label subLbl = new Label(sub);
        subLbl.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 14px; -fx-text-alignment: center;");
        subLbl.setWrapText(true);

        card.getChildren().addAll(iconLbl, titleLbl, subLbl);
        return card;
    }

    public VBox getView() {
        return view;
    }
}
