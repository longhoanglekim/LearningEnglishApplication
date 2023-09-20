module App {
	requires javafx.controls;
	
	opens main to javafx.graphics, javafx.fxml;
}
