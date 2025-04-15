module org.example.programmingpatternsproductcatalog {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.programmingpatternsproductcatalog to javafx.fxml;
    exports org.example.programmingpatternsproductcatalog;
}