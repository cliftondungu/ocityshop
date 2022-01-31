

package controller;

import java.io.IOException;



import java.util.logging.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;





public class ProductMenuController {
	/*private Scene scene;*/

	public ProductMenuController(/*Scene scene*/) {
		/*this.scene = scene;*/
	}
	//This is the parent stage
	private Stage dialogStage;

	@FXML
	private Button addProduct;
	@FXML
	private Button viewProduct;


	
	public void viewProduct() {
	    try {
	      FXMLLoader loader = new FXMLLoader(
	        getClass().getResource("/view/ProductView.fxml")
	      );
	     /* scene.setRoot((Parent) loader.load());*/
	      AnchorPane root = (AnchorPane) loader.load();
	      dialogStage.setTitle("Products");
	      Scene scene=new Scene(root);
	      ViewProductController controller = 
	        loader.<ViewProductController>getController();
	      dialogStage.setScene(scene);
	      controller.setDialogStage(dialogStage);
	      dialogStage.show();
	    } catch (IOException ex) {
	      Logger.getLogger(ProductMenuController.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    catch (Exception ex) {
		      Logger.getLogger(ProductMenuController.class.getName()).log(Level.SEVERE, null, ex);
		    }
	  }

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	

	public void initialize() {
		
	}

	

	
	
	//This is required as stage.close() in the program will not trigger any events.
	//To have callback listeners on the close event, we trigger the external close event
	private void close() {
		dialogStage.fireEvent(new WindowEvent(dialogStage,WindowEvent.WINDOW_CLOSE_REQUEST));
	}
}




