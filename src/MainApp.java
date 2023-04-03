import Model.Students;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainApp {
    private static JdbcImpl jdbc;
    private static Scanner scanner;
    public static void main(String[] args) {
        //         JdbcImpl jdbc = new JdbcImpl();
//        String url="jdbc:postgresql://localhost:5432/postgres";
//        String username ="postgres";
//        String password ="1234";
        jdbc = new JdbcImpl();
        scanner = new Scanner(System.in);
        int option = 0;
        do {

            System.out.println("1.Insert Students in operation");
            System.out.println("2.Display students in operation");
            System.out.println("3.update students in operation");
            System.out.println("4.Delete students in operation");
            System.out.println("5.select student in database");
            System.out.println("6.Exit");
            System.out.println("Choose:");
            Students students = new Students();
            option = scanner.nextInt();
            switch (option) {
                case 1 -> {
                    System.out.println("======================insert student in operation===============");

                    System.out.println("Enter id:");
                    students.setId(scanner.nextInt());
                    System.out.println("Enter name:");
                    students.setName(scanner.next());
                    System.out.println("Enter gender:");
                    students.setGender(scanner.next());
                    insertStudents(students);
                    System.out.println("=================================================================");
                }
                case 2 -> {
                    System.out.println("=================Display student in operation=================");
                    selectStudents();
                    System.out.println("=============================================================");
                }
                case 3 -> {
                    int id;
                    String newName;
                    String newGender;
                    System.out.println("===========Update Student in operation=============");
                    System.out.println("Enter id:");
                    id=scanner.nextInt();
                    System.out.println("Enter new name:");
                    newName=scanner.next();
                    System.out.println("Enter new gender:");
                    newGender =scanner.next();
                    updateStudent(id,newName,newGender);
                }
                case 4 -> {
                    int delete;
                    System.out.println("=============delete Student by id operation===============");
                    System.out.println("Enter id:");
                    delete = scanner.nextInt();
                    deleteStudentById(delete);
                }
                case 5 -> {
                    System.out.println("==============select Student in operation=================");
                    int Option=0;
                    do{
                        System.out.println("1.select by id operation");
                        System.out.println("2.select by name operation");
                        System.out.println("3.exit");
                        System.out.println("Choose:");
                        Option =scanner.nextInt();
                        switch (Option){
                            case 1->{
                                int id;
                                System.out.println("=================select by id===============");
                                System.out.println("Enter id:");
                                id=scanner.nextInt();
                                selectStudentById(id);
                            }
                            case 2->{
                                System.out.println("===================select by name======================");
                                System.out.println("Enter name:");
                                String name =scanner.next();
                                selectStudentByName(name);
                            }
                            default -> {
                                System.out.println("Exit the program....!");
                                break;
                            }
                        }
                    }while (Option!=3);
                }
                default -> {
                    System.out.println("Exit the program....!");
                    break;
                }
            }


        } while (option != 6);
    }
    //Insert recorde
    private static void insertStudents(Students students){
        try(Connection conn = jdbc.dataSource().getConnection()){
            String insertSql ="INSERT INTO student(id,name,gender) " +
                    "VALUES(?,?,?)";
            PreparedStatement statement =conn.prepareStatement(insertSql);
            statement.setInt(1,students.getId());
            statement.setString(2,students.getName());
            statement.setString(3, students.getGender());

            int count =statement.executeUpdate();
            System.out.println(count);

        }catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("You successfully to insert!");
    }
    private static void selectStudents(){
        try(Connection conn = jdbc.dataSource().getConnection() ){
//            System.out.println(conn.getSchema());
            //1.Create SQL Statement
            String selectSql ="SELECT * FROM student";
            PreparedStatement statement =conn.prepareStatement(selectSql);
            //2.Execute SQL Statement
            ResultSet resultSet = statement.executeQuery();
            //3.Process Result with ResultSql
            List<Students> students = new ArrayList<>();
            while (resultSet.next()){
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String gender =resultSet.getString("gender");
                students.add(new Students(id,name,gender));
            }
            students.forEach(System.out::println);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void updateStudent(int id, String newName, String newGender) {
        try (Connection conn = jdbc.dataSource().getConnection()) {
            String updateSql = "UPDATE student SET name=?, gender=? WHERE id=?";
            PreparedStatement statement = conn.prepareStatement(updateSql);
            statement.setString(1, newName);
            statement.setString(2, newGender);
            statement.setInt(3, id);
            int count = statement.executeUpdate();
            System.out.println(count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void deleteStudentById(int id) {
        try (Connection conn = jdbc.dataSource().getConnection()) {
            String deleteSql = "DELETE FROM student WHERE id=?";
            PreparedStatement statement = conn.prepareStatement(deleteSql);
            statement.setInt(1, id);
            int count = statement.executeUpdate();
            System.out.println(count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void selectStudentById(int id) {
        try (Connection conn = jdbc.dataSource().getConnection()) {
            String selectSql = "SELECT * FROM student WHERE id=?";
            PreparedStatement statement = conn.prepareStatement(selectSql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Integer studentId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String gender = resultSet.getString("gender");
                System.out.println(new Students(studentId, name, gender));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void selectStudentByName(String name) {
        try (Connection conn = jdbc.dataSource().getConnection()) {
            String selectSql = "SELECT * FROM student WHERE name=?";
            PreparedStatement statement = conn.prepareStatement(selectSql);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String studentName = resultSet.getString("name");
                String gender = resultSet.getString("gender");
                System.out.println(new Students(id, studentName, gender));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
