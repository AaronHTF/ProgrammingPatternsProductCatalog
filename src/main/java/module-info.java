module org.example.programmingpatternsproductcatalog {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.programmingpatternsproductcatalog to javafx.fxml;
    exports org.example.programmingpatternsproductcatalog;
}