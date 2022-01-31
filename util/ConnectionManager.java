package helper; 


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager
{

	String databaseUrl;
	String userName;
	String password;
	String driver;

	/**
	 * ConnectionManager constructor to initialize all the connection attributes.
	 */
	public ConnectionManager()
	{
		//database URL veriable
		//databaseUrl = "jdbc:mysql://localhost/javaproject";
		databaseUrl = "jdbc:mysql://localhost:3306/users";
		//database user name credentials
		//userName = "root";
		userName = "root";
		//database password credentials
		//password = "";
		password = "KAlif123*";
		//mySQL database driver
		driver ="com.mysql.cj.jdbc.Driver";
	}




	/**
	 * This method is used to get the connection object
	 * @return connection object to the mySQL database.
	 */
	public Connection getNewConnection()
	{
		Connection connection = null;

		try
		{
			//driver which will be used for which the connection will be drawn
			Class.forName(driver);
			//gets connection from the driver manager
			connection = DriverManager.getConnection(databaseUrl, userName, password);

		}
		catch(SQLException sqle)
		{
			System.err.println("SQLException: "+sqle);
			return null;
		}
		catch(ClassNotFoundException cnfe)
		{
			System.err.println("ClassNotFoundException: "+cnfe);
			return null;
		}
		//return connection
		return connection;
	}//end getNewConnection


}//end class