module it.ecteam.easycharge {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires json.simple;
    requires jxmapviewer2;
    requires java.desktop;
    requires mysql.connector.java;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;

    opens it.ecteam.easycharge to javafx.fxml;
    exports it.ecteam.easycharge;
    exports it.ecteam.easycharge.viewcontroller;
    opens it.ecteam.easycharge.viewcontroller to javafx.fxml;
    exports it.ecteam.easycharge.exceptions;
    opens it.ecteam.easycharge.exceptions to javafx.fxml;
}