module it.disi.unitn.tirociniofx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.jetbrains.annotations;
    requires org.apache.commons.exec;
    requires org.apache.commons.lang3;
    requires org.apache.commons.io;
    requires org.apache.commons.codec;

    exports it.disi.unitn.lasagna.tirociniofx;
    opens it.disi.unitn.lasagna.tirociniofx to javafx.fxml;
}