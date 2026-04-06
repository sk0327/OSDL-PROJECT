package com.urbannest.controllers;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class MainController {
    
    private BorderPane root;
    private HBox navBar;
    private VBox contentArea;
    private StackPane centeredWrapper;
    
    // Views
    private HomeView homeView;
    private DashboardView dashboardView;
    private RoomManagementView roomManagementView;
    private BookingView bookingView;
    private CheckoutView checkoutView;
    private AmenitiesView amenitiesView;

    private HBox navLinks;

    public MainController() {
        homeView = new HomeView(this);
        dashboardView = new DashboardView();
        roomManagementView = new RoomManagementView(this);
        bookingView = new BookingView(this);
        checkoutView = new CheckoutView(this);
        amenitiesView = new AmenitiesView();

        initLayout();
    }

    private void initLayout() {
        root = new BorderPane();
        root.getStyleClass().add("root");

        // Top Navigation Bar
        navBar = new HBox(20);
        navBar.getStyleClass().add("nav-bar");
        navBar.setAlignment(Pos.CENTER_LEFT);

        Label brand = new Label("UrbanNest");
        brand.getStyleClass().add("brand-logo");

        // Spacer
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        navLinks = new HBox(15);
        navLinks.setAlignment(Pos.CENTER_RIGHT);

        Button btnHome = createNavButton("Home");
        Button btnDashboard = createNavButton("Dashboard");
        Button btnRooms = createNavButton("Rooms");
        Button btnAmenities = createNavButton("Amenities");

        btnHome.setOnAction(e -> navigateTo("Home"));
        btnDashboard.setOnAction(e -> navigateTo("Dashboard"));
        btnRooms.setOnAction(e -> navigateTo("Room Management"));
        btnAmenities.setOnAction(e -> navigateTo("Amenities"));

        navLinks.getChildren().addAll(btnHome, btnDashboard, btnRooms, btnAmenities);
        navBar.getChildren().addAll(brand, spacer, navLinks);

        // Content Area Container
        contentArea = new VBox();
        contentArea.getStyleClass().add("content-pane");
        contentArea.setAlignment(Pos.TOP_CENTER);
        contentArea.setMaxWidth(1200);

        // Wrapper
        centeredWrapper = new StackPane(contentArea);
        centeredWrapper.setAlignment(Pos.TOP_CENTER);
        
        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(centeredWrapper);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        
        root.setTop(navBar);
        root.setCenter(scrollPane);
        
        // Set default view
        navigateTo("Home");
    }

    private Button createNavButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("nav-btn");
        return btn;
    }

    public void navigateTo(String destination) {
        // Clear active styles
        for (javafx.scene.Node node : navLinks.getChildren()) {
            if (node instanceof Button) {
                node.getStyleClass().remove("active");
            }
        }

        javafx.scene.Node viewToLoad = null;

        switch (destination) {
            case "Home":
                viewToLoad = homeView.getView();
                if (navLinks.getChildren().size() > 0) ((Button) navLinks.getChildren().get(0)).getStyleClass().add("active");
                break;
            case "Dashboard":
                dashboardView.refresh();
                viewToLoad = dashboardView.getView();
                if (navLinks.getChildren().size() > 1) ((Button) navLinks.getChildren().get(1)).getStyleClass().add("active");
                break;
            case "Room Management":
                roomManagementView.refresh();
                viewToLoad = roomManagementView.getView();
                if (navLinks.getChildren().size() > 2) ((Button) navLinks.getChildren().get(2)).getStyleClass().add("active");
                break;
            case "Amenities":
                viewToLoad = amenitiesView.getView();
                if (navLinks.getChildren().size() > 3) ((Button) navLinks.getChildren().get(3)).getStyleClass().add("active");
                break;
            case "Book a Room":
                bookingView.refresh();
                viewToLoad = bookingView.getView();
                break;
            case "Checkout":
                checkoutView.refresh();
                viewToLoad = checkoutView.getView();
                break;
        }

        if (viewToLoad != null) {
            contentArea.getChildren().clear();
            VBox.setVgrow(viewToLoad, Priority.ALWAYS);
            contentArea.getChildren().add(viewToLoad);
        }
    }

    public BorderPane getRoot() {
        return root;
    }
}
