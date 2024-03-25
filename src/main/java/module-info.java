module com.example.tirociniofx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;

    exports it.disi.unitn.lasagna.tirociniofx;
    opens it.disi.unitn.lasagna.tirociniofx to javafx.fxml;
}