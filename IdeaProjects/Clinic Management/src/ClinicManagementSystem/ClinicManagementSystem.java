package ClinicManagementSystem;

import com.acc.utils.ConnectDB;

import java.sql.*;
import java.util.Scanner;

class ClinicManagementOperations{
    public static void showMenu(){
        System.out.println("Clinic MANAGEMENT SYSTEM ");
        System.out.println("1. Add Patient");
        System.out.println("2. View Patients");
        System.out.println("3. View Doctors");
        System.out.println("4. Book Appointment");
        System.out.println("5. Exit");
        System.out.println("Enter your choice: ");
    }

    public static void takeChoice(Patient patient,Doctor doctor,Scanner scanner,Connection connection){
        while(true){
            ClinicManagementOperations.showMenu();
            int choice = scanner.nextInt();

            switch(choice){
                case 1:
                    // Add Patient
                    patient.addPatient();
                    System.out.println();
                    break;
                case 2:
                    // View Patient
                    patient.viewData();
                    System.out.println();
                    break;
                case 3:
                    // View Doctors
                    doctor.viewData();
                    System.out.println();
                    break;
                case 4:
                    // Book Appointment
                    bookAppointment(patient, doctor, connection, scanner);
                    System.out.println();
                    break;
                case 5:
                    System.out.println("THANK YOU! FOR USING CLINIC MANAGEMENT SYSTEM!!");
                    return;
                default:
                    System.out.println("Enter valid choice!!!");
                    break;
            }

        }
    }
    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner){
        System.out.print("Enter Patient Id: ");
        int patientId = scanner.nextInt();
        System.out.print("Enter Doctor Id: ");
        int doctorId = scanner.nextInt();
        System.out.print("Enter appointment date (YYYY-MM-DD): ");
        String appointmentDate = scanner.next();
        if(patient.checkById(patientId) && doctor.checkById(doctorId)){
            if(checkDoctorAvailability(doctorId, appointmentDate, connection)){
                String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES(?, ?, ?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if(rowsAffected>0){
                        System.out.println("Appointment Booked!");
                    }else{
                        System.out.println("Failed to Book Appointment!");
                    }
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }
            }else{
                System.out.println("Doctor not available on this date!!");
            }
        }else{
            System.out.println("Either doctor or patient doesn't exist!!!");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection){
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                return count==0;
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}

public class ClinicManagementSystem {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
           Connection connection= ConnectDB.getDBConnection();
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);
        ClinicManagementOperations.takeChoice(patient,doctor,scanner,connection);
    }



}