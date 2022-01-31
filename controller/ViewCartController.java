package controller;

import dao.cartDAO;
import dao.cartDAO.CartTable;
import dao.OrderDAO;
import helper.ConfirmAlert;
import model.CartItem;
import model.Order;
import model.Product;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ViewCartController implements Initializable{

	ObservableList<CartTable> data;
	private User user;
	private Stage dialogStage;
	@FXML private TableView<CartTable> tableView;
	@FXML private TableColumn<CartTable, String> productID;
	@FXML private TableColumn<CartTable, String> prodName;
	@FXML private TableColumn<CartTable, String> noItems;
	@FXML private TableColumn<CartTable, String> priceTax;
	@FXML private Label  sessionLabel,errorMessage,successMessage;
	@FXML
	private TextField quantity;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.errorMessage.setTextFill(Color.web("#ff1a1a"));
	}

	//Method to set the parent stage of the current view
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	public void setCartTable(User user) {
		// TODO Auto-generated method stub
		this.sessionLabel.setText("Welcome "+user.getuFirstName());
		productID.setCellValueFactory(new PropertyValueFactory<CartTable, String>("id"));
		productID.setMinWidth(tableView.getMaxWidth()/6);
		prodName.setCellValueFactory(new PropertyValueFactory<CartTable, String>("name"));
		prodName.setMinWidth(tableView.getMaxWidth()/6);

		priceTax.setCellValueFactory(new PropertyValueFactory<CartTable, String>("price"));
		priceTax.setMinWidth(tableView.getMaxWidth()/6);

		noItems.setCellValueFactory(new PropertyValueFactory<CartTable, String>("noOfItems"));
		noItems.setMinWidth(tableView.getMaxWidth()/6);
		data=new cartDAO().getAllCartItems(user);
		tableView.setItems(data);
		this.user=user;
	}
	public void backToProductView(){
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/view/ProductViewCustomer.fxml")
					);
			/*scene.setRoot((Parent) loader.load());*/
			AnchorPane root = (AnchorPane) loader.load();
			dialogStage.setTitle("Products");
			Scene scene=new Scene(root);
			ViewProductController controller = 
					loader.<ViewProductController>getController();
			controller.setUser(user);
			dialogStage.setScene(scene);
			controller.setDialogStage(dialogStage);
			dialogStage.show();
		} catch (IOException ex) {
			Logger.getLogger(ViewCartController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void chechoutItems(){
		
		if(data.isEmpty()){
			this.errorMessage.setText("Sorry, It Seems you have Nothing to checkout!!");
		}
		else{
			if(tableView.getSelectionModel().getSelectedIndex()!=-1){
			boolean result=new ConfirmAlert().displayAlert("Order Confirmation", "Are you sure you wanna order this item?");
			if(result){
				Order order=new Order();
				order.setProductID(Integer.parseInt(data.get(tableView.getSelectionModel().getSelectedIndex()).getId()));
				order.setCustomerID(user.getUserID());
				order.setPrice(Float.parseFloat(data.get(tableView.getSelectionModel().getSelectedIndex()).getPrice()));
				order.setQuantity(Integer.parseInt(data.get(tableView.getSelectionModel().getSelectedIndex()).getNoOfItems()));
				order.setStatus('p');
				order.setProductName(data.get(tableView.getSelectionModel().getSelectedIndex()).getName());
				int orderID=new OrderDAO().AddOrder(order);
				if(orderID>0){
					this.successMessage.setText("Order Confirmed, Please Note Order ID"+orderID);
					Product product=new Product();
					CartItem cartItem =new CartItem();
					product.setProductID(Integer.parseInt(data.get(tableView.getSelectionModel().getSelectedIndex()).getId()));
					cartItem.setCartID(user.getUserID());
					cartItem.setProduct(product);
					cartDAO cartDAO=new cartDAO();
					if(cartDAO.removeFromCart(cartItem)){

						data.remove(data.get(tableView.getSelectionModel().getSelectedIndex()));
					}
					else{
						this.errorMessage.setText("Sorry the product couldn't be removed");
					}
				}
				else
				{
					this.errorMessage.setText("Sorry This product is out of stock");
				}
			}
		}else
		{
			this.errorMessage.setText("Please select a product to checkout");
		}
		}
		
	}
	public void removeItem(){
		if(data.isEmpty()){
			this.errorMessage.setText("Sorry, It Seems you have No item in your cart!!");
		}
		else{
			if(tableView.getSelectionModel().getSelectedIndex()!=-1){
			Product product=new Product();
			CartItem cartItem =new CartItem();
			product.setProductID(Integer.parseInt(data.get(tableView.getSelectionModel().getSelectedIndex()).getId()));
			cartItem.setCartID(user.getUserID());
			cartItem.setProduct(product);
			cartDAO cartDAO=new cartDAO();
			if(cartDAO.removeFromCart(cartItem)){
				this.errorMessage.setText(data.get(tableView.getSelectionModel().getSelectedIndex()).getName()+" removed");
				data.remove(data.get(tableView.getSelectionModel().getSelectedIndex()));
			}
			else{
				this.errorMessage.setText("Sorry the product couldn't be removed");
			}
		}
			else{
				this.errorMessage.setText("Please select an item to be removed");
			}
		}
		
	}

	public void viewOrders(){
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/view/OrderViewCustomer.fxml")
					);

			AnchorPane root = (AnchorPane) loader.load();
			dialogStage.setTitle("Orders");
			Scene scene=new Scene(root);
			OrderController controller = 
					loader.<OrderController>getController();

			controller.setOrdersTable(user,this,false);
			dialogStage.setScene(scene);
			controller.setDialogStage(dialogStage);
			dialogStage.show();
		} catch (IOException ex) {
			Logger.getLogger(ViewCartController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void logOut(){
		this.user=null;
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/view/UserLoginReg.fxml")
					);
			/*scene.setRoot((Parent) loader.load());*/
			AnchorPane root = (AnchorPane) loader.load();
			dialogStage.setTitle("Orders");
			Scene scene=new Scene(root);
			LoginRegistrationController controller = 
					loader.<LoginRegistrationController>getController();

			dialogStage.setScene(scene);
			controller.setDialogStage(dialogStage);
			dialogStage.show();
		} catch (IOException ex) {
			Logger.getLogger(ViewProductController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
