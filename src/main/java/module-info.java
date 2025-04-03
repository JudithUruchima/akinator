module ec.edu.uees.akinator {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.json;
    requires javafx.media;

    opens ec.edu.uees.akinator to javafx.fxml;
    exports ec.edu.uees.akinator;
    
    
}
