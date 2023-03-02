import java.sql.*;
import java.util.*;
public class Reporting {
    public static void main(String[] args) throws SQLException {
        String username = args[0];
        String password = args[1];
        String url = "jdbc:oracle:thin:@oracle.wpi.edu:1521:orcl";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }
        if (connection != null) {
            Scanner scanner = new Scanner(System.in);
            String option = "";
            while (!option.equals("q")) {
                System.out.println("Choose an option:");
                System.out.println("1- Report Patients Basic Information");
                System.out.println("2- Report Doctors Basic Information");
                System.out.println("3- Report Admissions Information");
                System.out.println("4- Update Admissions Payment");
                System.out.println("q- Quit");

                option = scanner.nextLine();
                switch (option) {
                    case "1":
                    System.out.print("Enter Patient SSN: ");
                    
                    String patientSSN = scanner.nextLine();
                    Statement stmt = connection.createStatement();
                    String str = "SELECT * FROM PATIENT WHERE PATIENT_SSN = " + patientSSN;
                    ResultSet result = stmt.executeQuery(str);

                        try {
                            while (result.next()) {
                                String fName = result.getString("FNAME");
                                String lName = result.getString("LNAME");
                                String address = result.getString("ADDRESS");
                                result.close();
                                stmt.close();
                                System.out.println("Patient SSN: " + patientSSN);
                                System.out.println("Patient First Name: " + fName);
                                System.out.println("Patient Last Name: " + lName);
                                System.out.println("Patient Address: " + address);

                            }
                        } catch(Exception e) {
                            System.out.println("No elements retrieved");
                        }
                        break;

                    case "2":
                    System.out.print("Enter Doctor ID: ");
                    
                    String doctorID = scanner.nextLine();
                    Statement docStmt = connection.createStatement();
                    String docStr = "SELECT d.EMPLOYEEID, d.GENDER, d.SPECIALTY, d.GRADUATEDFROM, e.FNAME, e.LNAME" +
                    "FROM DOCTOR d, EMPLOYEE e WHERE e.ID = " + doctorID;
                    ResultSet docResult = docStmt.executeQuery(docStr);
                        try {
                            while (docResult.next()) {
                                String fName = docResult.getString("FNAME");
                                String lName = docResult.getString("LNAME");
                                String gender = docResult.getString("GENDER");                                
                                String graduatedFrom = docResult.getString("GRADUATEDFROM");
                                String specialty = docResult.getString("SPECIALTY");                                
                                docResult.close();
                                docStmt.close();
                                System.out.println("Doctor ID: " + doctorID);
                                System.out.println("Doctor First Name: " + fName);
                                System.out.println("Doctor Last Name: " + lName);
                                System.out.println("Doctor Gender: " + gender);
                                System.out.println("Doctor Graduated From: " + graduatedFrom);
                                System.out.println("Doctor Specialty: " + specialty);

                            }
                        } catch(Exception e) {
                            System.out.println("No elements retrieved");
                        }
                        break;
                    
                    case "3":
                        System.out.print("Enter Admission Number: ");
                        int admissionNumber = scanner.nextInt();

                        String admissionQuery = "SELECT * FROM admission WHERE admissionnum = ?";
                        String roomQuery = "SELECT * FROM stayin WHERE admissionnum = ?";
                        String doctorQuery = "SELECT doctorid FROM examine WHERE admissionnum = ?";

                        try (PreparedStatement admissionStmt = connection.prepareStatement(admissionQuery);
                             PreparedStatement roomStmt = connection.prepareStatement(roomQuery);
                             PreparedStatement doctorStmt = connection.prepareStatement(doctorQuery)) {

                            admissionStmt.setInt(1, admissionNumber);
                            ResultSet admissionResult = admissionStmt.executeQuery();
                            if (admissionResult.next()) {
                                System.out.println("Admission Number: " + admissionResult.getInt("admissionnum"));
                                System.out.println("Patient SSN: " + admissionResult.getInt("patient_ssn"));
                                System.out.println("Admission date (start date): " + admissionResult.getDate("admissiondate"));
                                System.out.println("Total Payment: " + admissionResult.getDouble("totalpayment"));

                                roomStmt.setInt(1, admissionNumber);
                                ResultSet roomResult = roomStmt.executeQuery();
                                System.out.println("Rooms:");
                                while (roomResult.next()) {
                                    System.out.println("RoomNum: " + roomResult.getInt("roomnum")
                                    + "FromDate: " +roomResult.getDate("startdate")
                                            + "ToDate: " +roomResult.getDate("enddate"));
                                }
                                doctorStmt.setInt(1, admissionNumber);
                                ResultSet doctorResult = doctorStmt.executeQuery();
                                System.out.println("Doctors examined the patient in this admission:");
                                while (doctorResult.next()) {
                                    System.out.println("Doctor ID: " + doctorResult.getInt("doctorid"));
                                }
                            } else {
                                System.out.println("Admission not found.");
                            }
                        }
                        catch (SQLException e) {
                            System.err.println("Error executing SQL query: " + e.getMessage());
                        }
                        break;
                    case "4":
                        System.out.print("Enter Admission Number: ");
                        admissionNumber = scanner.nextInt();

                        System.out.print("Enter the new total payment: ");
                        double newTotalPayment = scanner.nextDouble();

                        String updateQuery = "UPDATE admission SET totalpayment = ? WHERE admissionnum = ?";
                        try (PreparedStatement updateStatment = connection.prepareStatement(updateQuery)) {
                            updateStatment.setDouble(1, newTotalPayment);
                            updateStatment.setInt(2, admissionNumber);

                            int rowsAffected = updateStatment.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("Total payment updated for admission " + admissionNumber + ".");
                            } else {
                                System.out.println("Admission not found.");
                            }

                        } catch (SQLException e) {
                            System.err.println("Error executing SQL query: " + e.getMessage());
                        }

                        break;
                }
            }
        }
    }
}