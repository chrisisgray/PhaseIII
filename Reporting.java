import java.sql.*;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
/* ++++++++++++++++++++++++++++++++++++++++++++++
  Make sure you did the following before execution
     1) Connect to WPI's wifi or vpn
     2) Create an Oracle data source and successfully create a connection
     3) Write your java code (say file name is OracleTest.java) and then compile it
using the  following command
       > javac OracleTest.java
     4) Run it
        > java OracleTest
  ++++++++++++++++++++++++++++++++++++++++++++++  */
public class Main {
    public static void main(String[] argv) throws SQLException {
        System.out.println("-------- Oracle JDBC Connection Testing ------");
        System.out.println("-------- Step 1: Registering Oracle Driver ------");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver? Did you follow the execution steps. ");
            System.out.println("");
            System.out.println("*****Open the file and read the comments in the beginning of the file****");
            System.out.println("");
            e.printStackTrace();
            return;
        }
        System.out.println("Oracle JDBC Driver Registered Successfully !");
        System.out.println("-------- Step 2: Building a Connection ------");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@oracle.wpi.edu:1521:orcl",
                    "tsharich",
                    "NewPassword202302");
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }
        if (connection != null) {
            System.out.println("You made it. Connection is successful. Take control of your database now!");
        } else {
            System.out.println("Failed to make connection!");
        }

        Scanner arguments = new Scanner(System.in);
        if(Integer.parseInt(arguments.nextLine()) == 1){
            System.out.println("Enter Patient SSN");
            Statement stmt = connection.createStatement();
            String str1 = "SELECT * FROM Patient";
            ResultSet AllPatientInfo = stmt.executeQuery(str1);
            while(AllPatientInfo.next()){
                if((AllPatientInfo.getInt("SSN")) == Integer.parseInt(arguments.nextLine())){
                    System.out.println("Patient SSN: " + AllPatientInfo.getInt("SSN"));
                    System.out.println("Patient First Name: " + AllPatientInfo.getString("FName"));
                    System.out.println("Patient Last Name: " + AllPatientInfo.getString("LName"));
                    System.out.println("Patient Address: " + AllPatientInfo.getString("Address"));
                }
            }
        }
        connection.close();
    }
}
