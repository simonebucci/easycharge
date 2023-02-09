module it.ecteam.easycharge {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    requires java.sql;
    requires json.simple;
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