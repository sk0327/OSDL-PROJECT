# UrbanNest - Luxury Hotel Management System
**Final Project Report & Evaluation Rubric Mapping**

---

## 1. Project Overview
**UrbanNest** is a comprehensive, sophisticated JavaFX application engineered to manage luxury boutique hotel operations. Abandoning standard administrative templates, UrbanNest leverages a premium "landing-page" architectural paradigm to handle room inventory, guest reservations, amenity curation, and checkout/billing logic. 

The following report demonstrates how the application strictly adheres to and exceeds the evaluation rubric requirements.

---

## 2. Rubric Evaluation Mapping

### 1. Permanent Storage of Data (JDBC)
**Implementation:** 
The application ensures that all critical operational data is securely persisted dynamically rather than relying on session-state memory. 
- **JDBC Integration:** UrbanNest is integrated with an **SQLite** database (`UrbanNest.db`) using the `jdbc:sqlite` driver. 
- **Repository Pattern:** It abstracts database connectivity through `DatabaseManager.java`, which actively manages the schema. Specialized repositories (`RoomRepository.java` and `BookingRepository.java`) execute parameterized SQL queries to `INSERT`, `SELECT`, `UPDATE`, and `DELETE` data, making the storage highly robust against data loss between execution cycles.

### 2. Screen Design with Different Styles or Layouts
**Implementation:**
UrbanNest heavily prioritizes a consumer-branded aesthetic that shatters traditional CRM-style structures.
- **Architectural Layouts:** 
  - `MainController.java` utilizes a `BorderPane` with a custom top navigation bar, wrapping its loaded views in a dynamically centered `StackPane` and a custom headless `ScrollPane`. This achieves an "editorial web layout" bounded to 1200 pixels instead of endlessly stretching elements.
  - The application uses `FlowPane` logic to render dynamic, asymmetrical visual grids (e.g., the Rooms Vault, portal selection cards).
  - The `AmenitiesView.java` builds an intricate `GridPane` photo gallery with overlay nodes.
- **Advanced CSS Styling:** `style.css` relies strictly on custom classes providing deep, glassmorphic layout properties (e.g. `rgba` translucent panels, heavy DropShadow effects, linear gradient backgrounds) transforming JavaFX into a modern luxury interface.

### 3. Usage of Maven or Gradle
**Implementation:**
- The entire project is initialized, built, and executed via **Apache Maven**. 
- The `pom.xml` flawlessly manages core dependencies (such as the SQLite JDBC driver) and manages build steps.
- The project leverages the `javafx-maven-plugin` to orchestrate module path compilation and native execution without requiring manual classpath management, allowing the system to boot instantly via `mvn install javafx:run`.

### 4. Billing Management
**Implementation:**
- Comprehensive checkout workflows are integrated directly into the core `CheckoutView.java` subsystem. 
- **Receipt Generation Engine:** Upon selecting an active reservation from the dropdown, the system queries the customer's information, identifies their occupied suite, and cross-references the `RoomRepository` to fetch the specific architectural suite's financial rate. 
- It then calculates and dynamically populates a styled **"Checkout Summary"** receipt detailing the Total Amount due before releasing the room back into the available pool.

### 5. Various Other Components Usage (Beyond Basic Controls)
**Implementation:**
Instead of solely relying on FXML/Scene Builder, UrbanNest dynamically wires complex JavaFX hierarchies together in pure Java, showcasing deep programmatic manipulation of advanced components:
- **`DatePicker`:** Enforces precise check-in and check-out tracking within `BookingView.java`.
- **`ComboBox`:** Dynamically scales with custom `ListCell` generation to display rich dropdown options (e.g., parsing available room prices directly into the selection text).
- **`ImageView` & `Rectangle` Masks:** Specifically engineered within the Amenities gallery to construct complex layered image cards featuring `setClip()` geometry for precise rounded corners.
- **`ScrollPane` (Headless):** Deployed over primary wrappers with disabled bar policies, establishing immersive touch-friendly panning identical to native web experiences.

---

### Conclusion
UrbanNest fulfills the technical architecture requirements entirely through permanent JDBC connectivity, programmatic view composition, layered component engineering, and robust Maven foundations, wrapped in a meticulously styled designer UI.
