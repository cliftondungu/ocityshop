package controller;

import java.io.IOException;

import dao.UserDAO;
import java.net.URL;
import java.util.ResourceBundle;
import model.Customer;
import model.Employee;
import model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LoginRegistrationController {
	
	private Stage dialogStage;

	
	@FXML
	private TextField firstName;
	
	@FXML
	private TextField lastName;
	@FXML
	private PasswordField password;
	@FXML
	private TextField address;
	@FXML
	private TextField userID;
	@FXML
	private PasswordField loginPassword;
	@FXML
	private Label errorLogin,regMessage,userIDError,loginPasswordError,
	errorAddress,errorUserPassword,errorLastName,errorFirstName;


	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	
	public void registerUser() {
	
		boolean register=true;
		if(this.firstName.getText().equals("")||this.firstName.getText()==null){
			this.errorFirstName.setText("Please enter First Name");
			register=false;
		}
		if(this.lastName.getText().equals("")||this.lastName.getText()==null){
			this.errorLastName.setText("Please enter Last Name");
			register=false;
		}
		if(this.password.getText().equals("")||this.password.getText()==null){
			this.errorUserPassword.setText("Please enter Password");
			register=false;
		}
		if(this.address.getText().equals("")||this.address.getText()==null){
			this.errorAddress.setText("Please enter Address");
			register=false;
		}


		if(this.firstName.getText().length()>100){
			this.errorFirstName.setText("First Name cannot comprise more then 100 letters");
			register=false;
		}
		if(this.lastName.getText().length()>100){
			this.errorLastName.setText("Last Name cannot comprise more then 100 letters");
			register=false;
		}
		if(this.password.getText().length()>100){
			this.errorUserPassword.setText("Password cannot comprise more then 100 letters");
			register=false;
		}
		if(this.address.getText().length()>100){
			this.errorAddress.setText("Address cannot comprise more then 100 letters");
			register=false;
		}
		
		if(register){
			this.errorFirstName.setText("");
			this.errorLastName.setText("");
			this.errorUserPassword.setText("");
			this.errorAddress.setText("");
			String firstName = this.firstName.getText();
			String lastName = this.lastName.getText();
			String password = this.password.getText();
			String address = this.address.getText();



			//Create the model object
			Customer customer=new Customer();
			customer.setuFirstName(firstName);
			customer.setuLastName(lastName);
			customer.setuPassword(password);
			customer.setuAddress(address);
			customer.setuRole('c');



			//Create a DAO instance of the model
			UserDAO b = new UserDAO();
			int userid=b.AddUser(customer);
			if(userid!=-1){
				regMessage.setText("Your user ID to login is: "+userid);
				this.firstName.clear();
				this.lastName.clear();
				this.password.clear();
				this.address.clear();
				this.userID.setText(""+userid);

			}
		}
	}


	public void loginToApplication() {
		boolean login=true;
		if(!this.userID.getText().matches("^\\d+$")){
			this.userIDError.setText("Your userID should be a positive Integer");
			login=false;
		}
		if(this.userID.getText()==null||this.userID.getText().equals("")){
			this.userIDError.setText("Please enter UserID");
			login=false;
		}
		if(this.userID.getText().length()>11){
			this.userIDError.setText("User ID Should be less then 11 digits");
			login=false;
		}
		if(this.loginPassword.getText()==null||this.loginPassword.getText().equals("")){
			this.loginPasswordError.setText("Please enter Password");
			login=false;
		}
		if(this.loginPassword.getText().length()>100){
			this.loginPasswordError.setText("The Password you have entered is too long");
			login=false;
		}
		if(login){
			this.loginPasswordError.setText("");
			this.userIDError.setText("");
			this.errorLogin.setText("");
			int uID=-1;

			uID = Integer.parseInt(this.userID.getText());
			String password = this.loginPassword.getText();
			UserDAO userAuthenticate=new UserDAO();
			User user=userAuthenticate.authenticateUser(uID,password);
			if(user!=null){
				this.errorLogin.setText("");
				if(user instanceof Customer)
				{
					try {
						FXMLLoader loader = new FXMLLoader(
								getClass().getResource("/view/ProductViewCustomer.fxml")
								);
						/*scene.setRoot((Parent) loader.load());*/
						AnchorPane root = (AnchorPane) loader.load();
						dialogStage.setTitle("Shop");
						Scene scene=new Scene(root);
						ViewProductController controller = 
								loader.<ViewProductController>getController();
						controller.setUser(user);
						dialogStage.setScene(scene);
						controller.setDialogStage(dialogStage);
						dialogStage.show();
					} catch (IOException ex) {
                                            System.out.println("Error!");
					}
				}
				else if(user instanceof Employee){
					try {
						FXMLLoader loader = new FXMLLoader(
								getClass().getResource("/view/ProductViewEmployee.fxml")
								);
						/*scene.setRoot((Parent) loader.load());*/
						AnchorPane root = (AnchorPane) loader.load();
						dialogStage.setTitle("Shop");
						Scene scene=new Scene(root);
						ViewProductController controller = 
								loader.<ViewProductController>getController();
						controller.setUser(user);
						dialogStage.setScene(scene);
						controller.setDialogStage(dialogStage);
						dialogStage.show();
					} catch (IOException ex) {
                                            System.out.println("Error!");
					}
				}
			}
			else{
				this.errorLogin.setText("Please enter Valid credentials");
			}
		}
	}

	
       
}
