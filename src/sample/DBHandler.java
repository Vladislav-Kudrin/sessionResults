package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import sample.entities.*;

class DBHandler extends DBConfig{
    private Connection connect() throws SQLException {
        String connection = "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + database + ";";

        return DriverManager.getConnection(connection, user, password);
    }

    ObservableList<Student> getStudents() {
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        ObservableList<Student> studentsList = FXCollections.observableArrayList();
        String selectQuery = "SELECT * FROM " + STUDENT;

        try {
            preparedStatement = connect().prepareStatement(selectQuery);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                studentsList.add(new Student(resultSet.getInt(MARKSHEET), resultSet.getString(L_NAME),
                        resultSet.getString(F_NAME), resultSet.getString(PATRONYMIC), resultSet.getString(G_INDEX),
                        resultSet.getByte(GENDER), resultSet.getByte(M_STATUS)));
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }

        return studentsList;
    }

    ObservableList<Grade> getGrades(int marksheet, boolean isExam) {
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        ObservableList<Grade> gradesList = FXCollections.observableArrayList();
        String selectQuery = "SELECT * FROM " + GRADE + ", " + SUBJECT + " WHERE " + MARKSHEET + " = ? AND " +
                IS_EXAM + " = ? AND " + SUBJECT_CODE + " = " + GRADE_CODE;

        try {
            preparedStatement = connect().prepareStatement(selectQuery);

            preparedStatement.setInt(1, marksheet);
            preparedStatement.setBoolean(2, isExam);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                gradesList.add(new Grade(resultSet.getInt(ID), resultSet.getInt(CODE),
                        resultSet.getString(SUBJECT), resultSet.getByte(MARK), resultSet.getBoolean(IS_EXAM)));
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }

        return gradesList;
    }

    ObservableList<String> getSubjectsList() {
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        ObservableList<String> subjectsList = FXCollections.observableArrayList("Предмет*");
        String selectQuery = "SELECT " + SUBJECT + " FROM " + SUBJECT;

        try {
            preparedStatement = connect().prepareStatement(selectQuery);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                subjectsList.add(resultSet.getString(SUBJECT));
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }

        return subjectsList;
    }

    ObservableList<Student> selectStudents(String filter, int choseField, int choseGender, int choseMaritalStatus) {
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        ObservableList<Student> studentsList = FXCollections.observableArrayList();
        String selectQuery = "SELECT * FROM " + STUDENT + " WHERE (";

        switch (choseField) {
            case 1:
                selectQuery = selectQuery + MARKSHEET + " LIKE '%" + filter + "%')";

                break;
            case 2:
                selectQuery = selectQuery + L_NAME + " LIKE '%" + filter + "%')";

                break;
            case 3:
                selectQuery = selectQuery + F_NAME + " LIKE '%" + filter + "%')";

                break;
            case 4:
                selectQuery = selectQuery + PATRONYMIC + " LIKE '%" + filter + "%')";

                break;
            case 5:
                selectQuery = selectQuery + G_INDEX + " LIKE '%" + filter + "%')";

                break;
            default:
                selectQuery = selectQuery + MARKSHEET + " LIKE '%" + filter + "%' OR " +
                        L_NAME + " LIKE '%" + filter + "%' OR " + F_NAME + " LIKE '%" + filter + "%' OR " + PATRONYMIC +
                        " LIKE '%" + filter + "%' OR " + G_INDEX + " LIKE '%" + filter + "%')";
        }

        selectQuery = selectQuery + " AND " + GENDER + " LIKE '%" + ((choseGender == 0) ? "%'" : (choseGender - 1) +
                "%'");
        selectQuery = selectQuery + " AND " + M_STATUS + " LIKE '%" + ((choseMaritalStatus == 0) ? "%'" :
                (choseMaritalStatus - 1) + "%'");

        try {
            preparedStatement = connect().prepareStatement(selectQuery);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                studentsList.add(new Student(resultSet.getInt(MARKSHEET), resultSet.getString(L_NAME),
                        resultSet.getString(F_NAME), resultSet.getString(PATRONYMIC), resultSet.getString(G_INDEX),
                        resultSet.getByte(GENDER), resultSet.getByte(M_STATUS)));
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }

        return studentsList;
    }

    void addStudent(Student student) {
        PreparedStatement preparedStatement;
        String insertStudentQuery = "INSERT INTO " + STUDENT + "(" + MARKSHEET + ", " + L_NAME + ", " + F_NAME + ", " +
                PATRONYMIC + ", " + G_INDEX + ", " + GENDER + "," + M_STATUS + ") " + "VALUES(?, ?, ?, ?, ?, ?, ?)";

        try {
            preparedStatement = connect().prepareStatement(insertStudentQuery);

            preparedStatement.setInt(1, student.getMarksheet());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setString(3, student.getFirstName());
            preparedStatement.setString(4, student.getPatronymic());
            preparedStatement.setString(5, student.getGroup());
            preparedStatement.setByte(6, student.getGender());
            preparedStatement.setByte(7, student.getMaritalStatus());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    void addGrade(int marksheet, Grade grade) {
        PreparedStatement preparedStatement;
        String insertStudentQuery = "INSERT INTO " + GRADE + "(" + CODE + ", " + MARKSHEET + ", " + MARK + ", " +
                IS_EXAM + ") " + "VALUES(?, ?, ?, ?)";

        try {
            preparedStatement = connect().prepareStatement(insertStudentQuery);

            preparedStatement.setInt(1, grade.getCode());
            preparedStatement.setInt(2, marksheet);
            preparedStatement.setByte(3, grade.getMark());
            preparedStatement.setBoolean(4, grade.isExam());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    void deleteStudent(int marksheet) {
        PreparedStatement preparedStatement;
        String deleteQuery = "DELETE FROM " + STUDENT + " WHERE " + MARKSHEET + " = ?";

        try {
            preparedStatement = connect().prepareStatement(deleteQuery);

            preparedStatement.setInt(1, marksheet);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    void deleteGrade(int id) {
        PreparedStatement preparedStatement;
        String deleteQuery = "DELETE FROM " + GRADE + " WHERE " + ID + " = ?";

        try {
            preparedStatement = connect().prepareStatement(deleteQuery);

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    void updateStudent(Student student) {
        PreparedStatement preparedStatement;
        String updateStudentQuery = "UPDATE " + STUDENT + " SET " + L_NAME + " = ?, " + F_NAME + " = ?, " + PATRONYMIC +
                " = ?, " + M_STATUS + " = ? WHERE " + MARKSHEET + " = ?";

        try {
            preparedStatement = connect().prepareStatement(updateStudentQuery);

            preparedStatement.setString(1, student.getLastName());
            preparedStatement.setString(2, student.getFirstName());
            preparedStatement.setString(3, student.getPatronymic());
            preparedStatement.setByte(4, student.getMaritalStatus());
            preparedStatement.setInt(5, student.getMarksheet());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    boolean isGradeExist(int marksheet, int code) {
        PreparedStatement preparedStatement;
        String selectQuery = "SELECT * FROM " + GRADE + " WHERE " + MARKSHEET + " = " + marksheet + " AND " + CODE +
                " = ?";

        try {
            preparedStatement = connect().prepareStatement(selectQuery);

            preparedStatement.setInt(1, code);

            return preparedStatement.executeQuery().next();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }

        return true;
    }

    boolean isMarksheetExist(int marksheet) {
        PreparedStatement preparedStatement;
        String selectQuery = "SELECT * FROM " + STUDENT + " WHERE " + MARKSHEET + " = ?";

        try {
            preparedStatement = connect().prepareStatement(selectQuery);

            preparedStatement.setInt(1, marksheet);

            return preparedStatement.executeQuery().next();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }

        return true;
    }

    boolean testConnection() {
        try {
            connect();

            return true;
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());

            return false;
        }
    }
}
