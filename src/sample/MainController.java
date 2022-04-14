package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.entities.Grade;
import sample.entities.Student;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MainController {
    private Student currentStudent;
    private final DBHandler DB_HANDLER = new DBHandler();
    private ToggleGroup genders = new ToggleGroup();
    private ObservableList<Grade> currentExamGradesList;
    private ObservableList<Grade> currentGradesList;
    private ObservableList<String> searchFilters = FXCollections.observableArrayList("Все",
            "№ з.к.",
            "Фамилия",
            "Имя",
            "Отчество",
            "Группа");
    private ObservableList<String> genderFilters = FXCollections.observableArrayList("Пол",
            "Мужской",
            "Женский");
    private ObservableList<String> maritalStatusFilters = FXCollections.observableArrayList("Отношения",
            "Нет",
            "Есть");

    @FXML
    protected GridPane generalGridPane;

    @FXML
    protected AnchorPane generalAnchorPane;

    @FXML
    protected Label marksheetLabel;

    @FXML
    private TextField marksheetTextField;

    @FXML
    protected Label lastNameLabel;

    @FXML
    private TextField lastNameTextField;

    @FXML
    protected Label firstNameLabel;

    @FXML
    private TextField firstNameTextField;

    @FXML
    protected Label patronymicLabel;

    @FXML
    private TextField patronymicTextField;

    @FXML
    protected Label groupLabel;

    @FXML
    private TextField groupTextField;

    @FXML
    private AnchorPane genderAnchorPane;

    @FXML
    protected Label genderLabel;

    @FXML
    private RadioButton maleRadioButton;

    @FXML
    private RadioButton femaleRadioButton;

    @FXML
    private CheckBox maritalStatusCheckBox;

    @FXML
    private Button addButton;

    @FXML
    private Label issueLabel;

    @FXML
    private TableView<Student> studentsTableView;

    @FXML
    protected Label emptyStudentsLabel;

    @FXML
    private TableColumn<Student, String> marksheetTableColumn;

    @FXML
    private TableColumn<Student, String> lastNameTableColumn;

    @FXML
    private TableColumn<Student, String> firstNameTableColumn;

    @FXML
    private TableColumn<Student, String> patronymicTableColumn;

    @FXML
    private TableColumn<Student, String> groupTableColumn;

    @FXML
    private TableColumn<Student, String> genderTableColumn;

    @FXML
    private TableColumn<Student, String> maritalStatusTableColumn;

    @FXML
    private AnchorPane actionsAnchorPane;

    @FXML
    private TextField deleteTextField;

    @FXML
    protected Button deleteButton;

    @FXML
    protected Button editButton;

    @FXML
    private AnchorPane editAnchorPane;

    @FXML
    protected Button confirmButton;

    @FXML
    protected Button discardButton;

    @FXML
    protected Button exportStudentsButton;

    @FXML
    private Button refreshButton;

    @FXML
    private AnchorPane gradeAnchorPane;

    @FXML
    protected Label gradeLabel;

    @FXML
    private ListView<String> gradeListView;

    @FXML
    private Button moreButton;

    @FXML
    protected Button exportGradesButton;

    @FXML
    private AnchorPane searchAnchorPane;

    @FXML
    private ChoiceBox<String> filterChoiceBox;

    @FXML
    private TextField filterTextField;

    @FXML
    protected Button searchButton;

    @FXML
    private ChoiceBox<String> genderChoiceBox;

    @FXML
    private ChoiceBox<String> maritalStatusChoiceBox;

    @FXML
    private void initialize() {
        marksheetTableColumn.setCellValueFactory(new PropertyValueFactory<>("marksheet"));
        lastNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        patronymicTableColumn.setCellValueFactory(new PropertyValueFactory<>("patronymic"));
        groupTableColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        genderTableColumn.setCellValueFactory(new PropertyValueFactory<>("stringGender"));
        maritalStatusTableColumn.setCellValueFactory(new PropertyValueFactory<>("stringMaritalStatus"));
        maleRadioButton.setToggleGroup(genders);
        femaleRadioButton.setToggleGroup(genders);
        maleRadioButton.setSelected(true);
        filterChoiceBox.setItems(searchFilters);
        filterChoiceBox.setValue(searchFilters.get(0));
        genderChoiceBox.setItems(genderFilters);
        genderChoiceBox.setValue(genderFilters.get(0));
        maritalStatusChoiceBox.setItems(maritalStatusFilters);
        maritalStatusChoiceBox.setValue(maritalStatusFilters.get(0));

        refreshTable();
    }

    @FXML
    private void refreshTable() {
        issueLabel.setVisible(false);
        studentsTableView.setItems(DB_HANDLER.getStudents());

        resetElements();
    }

    @FXML
    void onClickAddButton() {
        if(isFieldsEmpty() || isMarksheetExist()) return;

        currentStudent = new Student(Integer.parseInt(marksheetTextField.getText()), lastNameTextField.getText(),
                firstNameTextField.getText(), patronymicTextField.getText(), groupTextField.getText(),
                (byte) ((maleRadioButton.isSelected()) ? 0 : 1), (byte) ((maritalStatusCheckBox.isSelected()) ? 1 : 0));

        DB_HANDLER.addStudent(currentStudent);

        clearFields();
        refreshTable();
    }

    @FXML
    private void onSelectTableItem() {
        if ((currentStudent = studentsTableView.getSelectionModel().getSelectedItem()) != null) {
            currentExamGradesList = DB_HANDLER.getGrades(currentStudent.getMarksheet(), true);
            currentGradesList = DB_HANDLER.getGrades(currentStudent.getMarksheet(), false);

            actionsAnchorPane.setVisible(true);

            showMarklist();
        } else
            resetElements();
    }

    @FXML
    private void onClickDeleteButton() {
        if (deleteTextField.getText().isEmpty()) {
            issueLabel.setText("НУЖНО ПОДТВЕРДИТЬ № з.к.!");
            issueLabel.setVisible(true);
        } else if (Integer.parseInt(deleteTextField.getText()) == currentStudent.getMarksheet()) {
            DB_HANDLER.deleteStudent(currentStudent.getMarksheet());

            clearFields();
            refreshTable();
        } else {
            issueLabel.setText("№ з.к. НЕ СОВПАДАЕТ!");
            issueLabel.setVisible(true);
        }
    }

    @FXML
    private void onClickEditButton() {
        marksheetTextField.setDisable(true);
        groupTextField.setDisable(true);
        genderAnchorPane.setDisable(true);
        addButton.setDisable(true);
        refreshButton.setDisable(true);
        actionsAnchorPane.setDisable(true);
        gradeAnchorPane.setDisable(true);
        searchAnchorPane.setDisable(true);
        studentsTableView.setDisable(true);
        issueLabel.setVisible(false);
        editAnchorPane.setVisible(true);
        marksheetTextField.setText(String.valueOf(currentStudent.getMarksheet()));
        lastNameTextField.setText(currentStudent.getLastName());
        firstNameTextField.setText(currentStudent.getFirstName());
        patronymicTextField.setText(currentStudent.getPatronymic());
        groupTextField.setText(currentStudent.getGroup());
        maritalStatusCheckBox.setSelected(currentStudent.getMaritalStatus() == 1);

        if (currentStudent.getGender() == 0) maleRadioButton.setSelected(true);
        else femaleRadioButton.setSelected(true);
    }

    @FXML
    private void onClickConfirmButton() {
        if (!isFieldsEmpty()) {
            currentStudent.setLastName(lastNameTextField.getText());
            currentStudent.setFirstName(firstNameTextField.getText());
            currentStudent.setPatronymic(patronymicTextField.getText());
            currentStudent.setMaritalStatus((byte) ((maritalStatusCheckBox.isSelected()) ? 1 : 0));

            DB_HANDLER.updateStudent(currentStudent);

            clearFields();
            restoreElements();
            refreshTable();
        }
    }

    @FXML
    private void onClickDiscardButton() {
        clearFields();
        restoreElements();
    }

    @FXML
    private void onClickSearchButton() {
        if (filterTextField.getText().isEmpty()) {
            refreshTable();
            return;
        }

        studentsTableView.setItems(DB_HANDLER.selectStudents(filterTextField.getText(),
                filterChoiceBox.getSelectionModel().getSelectedIndex(),
                genderChoiceBox.getSelectionModel().getSelectedIndex(),
                maritalStatusChoiceBox.getSelectionModel().getSelectedIndex()));

        onSelectTableItem();
    }

    @FXML
    private void onClickMoreButton() throws Exception {
        final FXMLLoader FXML_LOADER = new FXMLLoader();
        final Stage STAGE = new Stage();
        GradesController gradesController;
        Parent root;

        FXML_LOADER.setLocation(MainController.class.getResource("grades.fxml"));

        root = FXML_LOADER.load();
        gradesController = FXML_LOADER.getController();

        gradesController.setMarksheet(currentStudent.getMarksheet());
        STAGE.setScene(new Scene(root, 390, 359));
        STAGE.setTitle("Оценки");
        STAGE.setResizable(false);
        STAGE.initOwner(moreButton.getScene().getWindow());
        STAGE.initModality(Modality.WINDOW_MODAL);
        STAGE.showAndWait();

        currentExamGradesList = DB_HANDLER.getGrades(currentStudent.getMarksheet(), true);

        showMarklist();
    }

    @FXML
    private void onClickExportMarklistButton() {
        int marksheet = currentStudent.getMarksheet();

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(marksheet + "marklist.doc"))) {
            write(bufferedWriter, marksheet);

            bufferedWriter.close();
            issueLabel.setText("ДОКУМЕНТ " + marksheet + "marklist.doc УСПЕШНО СОЗДАН!");
        }
        catch(IOException e) {
            issueLabel.setText("ДОКУМЕНТ НЕ СОЗДАН!");
        }

        issueLabel.setVisible(true);
    }

    @FXML
    private void onClickExportStudentsButton() {
        int marksheet;
        ObservableList<Student> studentsList = studentsTableView.getItems();

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("students.doc"))) {
            for (Student student : studentsList) {
                currentStudent = student;
                marksheet = currentStudent.getMarksheet();
                currentGradesList = DB_HANDLER.getGrades(marksheet, false);
                currentExamGradesList = DB_HANDLER.getGrades(marksheet, true);

                write(bufferedWriter, marksheet);
            }

            bufferedWriter.close();
            issueLabel.setText("ДОКУМЕНТ students.doc УСПЕШНО СОЗДАН!");
        }
        catch(IOException e) {
            issueLabel.setText("ДОКУМЕНТ НЕ СОЗДАН!");
        }

        issueLabel.setVisible(true);
    }

    @FXML
    private void cutOffLimitString(KeyEvent event) {
        TextField textField = (TextField) event.getTarget();
        String oldString = textField.getText();
        String newString = oldString.replaceAll("\\s", "");

        if (!oldString.equals(newString)) {
            textField.setText(newString);
            textField.positionCaret(textField.getLength());
        }

        if (textField.getLength() > 32) {
            textField.setText(textField.getText(0, 32));
            textField.positionCaret(32);
        }
    }

    @FXML
    private void cutOffLimitMarksheet(KeyEvent event) {
        TextField textField = (TextField) event.getTarget();
        String oldNumber = textField.getText();
        String newNumber = oldNumber.replaceAll("\\D", "");

        if (!oldNumber.equals(newNumber)) {
            textField.setText(newNumber);
            textField.positionCaret(textField.getLength());
        }

        if (textField.getLength() > 6) {
            textField.setText(textField.getText(0, 6));
            textField.positionCaret(6);
        }
    }

    @FXML
    private void cutOffLimitGroup() {
        String oldGroup = groupTextField.getText();
        String newGroup = oldGroup.replaceAll("[^A-Za-z0-9А-Яа-яЁё]", "");

        if (!oldGroup.equals(newGroup)) {
            groupTextField.setText(newGroup);
            groupTextField.positionCaret(groupTextField.getLength());
        }

        if (groupTextField.getLength() > 6) {
            groupTextField.setText(groupTextField.getText(0, 6));
            groupTextField.positionCaret(6);
        }
    }

    private void showMarklist() {
        int size = Math.min(currentExamGradesList.size(), 5);

        gradeListView.getItems().clear();

        for (int index = 0; index < size; index++) {
            Grade examGrade = currentExamGradesList.get(index);

            gradeListView.getItems().add(examGrade.getSubject() + ": " + examGrade.getMark());
        }

        gradeAnchorPane.setVisible(true);
    }

    private void write(BufferedWriter bufferedWriter, int marksheet) throws IOException {
        bufferedWriter.write("СТУДЕНТ\n" + marksheet + "; " + currentStudent.getLastName() + "; " +
                currentStudent.getFirstName() + "; " + currentStudent.getPatronymic() + "; " +
                currentStudent.getGroup() + "; " + currentStudent.getStringGender() + "; " +
                currentStudent.getStringMaritalStatus() + "\nЭКЗАМЕНЫ\n");

        for (Grade examGrade : currentExamGradesList) {
            bufferedWriter.write(examGrade.getSubject() + ": " + examGrade.getMark() + "; ");
        }

        bufferedWriter.write("\nЗАЧЁТЫ\n");

        for (Grade grade : currentGradesList) {
            bufferedWriter.write(grade.getSubject() + ": " + grade.getMark() + "; ");
        }

        bufferedWriter.write("\n\n");
    }

    private boolean isFieldsEmpty() {
        if (marksheetTextField.getText().isEmpty() || lastNameTextField.getText().isEmpty() ||
                firstNameTextField.getText().isEmpty() || groupTextField.getText().isEmpty()) {
            issueLabel.setText("ПОЛЯ СО * ОБЯЗАТЕЛЬНЫ К ЗАПОЛНЕНИЮ!");
            issueLabel.setVisible(true);

            return true;
        } else
            return false;
    }

    private boolean isMarksheetExist() {
        if (DB_HANDLER.isMarksheetExist(Integer.parseInt(marksheetTextField.getText()))) {
            issueLabel.setText("НОМЕР ЗАЧЕТНОЙ КНИЖКИ УЖЕ СУЩЕСТВУЕТ!");
            issueLabel.setVisible(true);

            return true;
        } else return false;
    }

    private void restoreElements() {
        marksheetTextField.setDisable(false);
        groupTextField.setDisable(false);
        genderAnchorPane.setDisable(false);
        addButton.setDisable(false);
        refreshButton.setDisable(false);
        actionsAnchorPane.setDisable(false);
        gradeAnchorPane.setDisable(false);
        searchAnchorPane.setDisable(false);
        studentsTableView.setDisable(false);
        issueLabel.setVisible(false);
        editAnchorPane.setVisible(false);
        maritalStatusCheckBox.setSelected(false);
        maleRadioButton.setSelected(true);

        onSelectTableItem();
    }

    private void resetElements() {
        actionsAnchorPane.setVisible(false);
        gradeAnchorPane.setVisible(false);
        exportStudentsButton.setVisible(studentsTableView.getItems().size() > 0);
    }

    private void clearFields() {
        maritalStatusCheckBox.setSelected(false);
        maleRadioButton.setSelected(true);
        marksheetTextField.clear();
        lastNameTextField.clear();
        firstNameTextField.clear();
        patronymicTextField.clear();
        groupTextField.clear();
        filterTextField.clear();
        deleteTextField.clear();
        filterChoiceBox.setValue(searchFilters.get(0));
        genderChoiceBox.setValue(searchFilters.get(0));
        maritalStatusChoiceBox.setValue(searchFilters.get(0));
    }
}
