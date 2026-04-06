package com.urbannest.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

public class AmenitiesView {
    private VBox view;

    public AmenitiesView() {
        buildView();
    }

    private void buildView() {
        view = new VBox(40);
        view.setPadding(new Insets(20, 0, 40, 0));
        view.setAlignment(Pos.TOP_CENTER);

        // Header
        VBox heroSection = new VBox(15);
        heroSection.setAlignment(Pos.CENTER);
        heroSection.setPadding(new Insets(40, 0, 40, 0));

        Label title = new Label("OUR FACILITIES");
        title.getStyleClass().add("hero-title");

        Label subtitle = new Label("Indulge in our curated collection of world-class amenities.");
        subtitle.getStyleClass().add("hero-subtitle");

        heroSection.getChildren().addAll(title, subtitle);

        // Gallery Grid
        GridPane gallery = new GridPane();
        gallery.setHgap(30);
        gallery.setVgap(30);
        gallery.setAlignment(Pos.CENTER);

        StackPane spa = createGalleryImage("/com/urbannest/images/amenity_spa.png", "The Atelier Spa");
        StackPane restaurant = createGalleryImage("/com/urbannest/images/amenity_restaurant.png", "Fine Dining");
        StackPane pool = createGalleryImage("/com/urbannest/images/luxury_room_2.png", "Sunset Resort Pool");
        StackPane gym = createGalleryImage("/com/urbannest/images/amenity_gym.png", "Executive Fitness");

        gallery.add(spa, 0, 0);
        gallery.add(restaurant, 1, 0);
        gallery.add(pool, 2, 0);
        gallery.add(gym, 1, 1); // Centered bottom row

        view.getChildren().addAll(heroSection, gallery);
    }

    private StackPane createGalleryImage(String imagePath, String overlayText) {
        StackPane card = new StackPane();
        card.getStyleClass().add("gallery-card");
        card.setPrefSize(350, 400);

        ImageView imageView = new ImageView();
        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            imageView.setImage(image);
            imageView.setFitWidth(350);
            imageView.setFitHeight(400);
            imageView.setPreserveRatio(false);

            Rectangle clip = new Rectangle(350, 400);
            clip.setArcWidth(12);
            clip.setArcHeight(12);
            imageView.setClip(clip);
        } catch (Exception ex) {
            // fallback
        }

        // Overlay Label
        Label label = new Label(overlayText);
        label.setStyle("-fx-font-family: 'Playfair Display'; -fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");
        
        VBox textOverlay = new VBox(label);
        textOverlay.setAlignment(Pos.BOTTOM_CENTER);
        textOverlay.setPadding(new Insets(30));
        textOverlay.setStyle("-fx-background-color: linear-gradient(to top, rgba(0,0,0,0.8), transparent); -fx-background-radius: 0 0 12 12;");
        textOverlay.setMaxHeight(150);

        StackPane.setAlignment(textOverlay, Pos.BOTTOM_CENTER);

        if (imageView.getImage() != null) {
            card.getChildren().addAll(imageView, textOverlay);
        } else {
            Rectangle rect = new Rectangle(350, 400, javafx.scene.paint.Color.web("#1e293b"));
            rect.setArcWidth(12);
            rect.setArcHeight(12);
            card.getChildren().addAll(rect, textOverlay);
        }

        return card;
    }

    public VBox getView() {
        return view;
    }
}
