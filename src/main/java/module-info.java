module org.wgu.software2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.wgu.software2 to javafx.fxml;
    opens org.wgu.software2.misc to javafx.graphics ;
    exports org.wgu.software2.controllers to javafx.fxml;
    opens org.wgu.software2.controllers to javafx.fxml;
    opens org.wgu.software2.models to javafx.base;
}