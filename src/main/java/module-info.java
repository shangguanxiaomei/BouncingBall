module felix.bouncingball {
    requires javafx.controls;
    requires javafx.fxml;


    opens felix.bouncingball to javafx.fxml;
    exports felix.bouncingball;
}