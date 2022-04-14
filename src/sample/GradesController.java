package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import sample.entities.Grade;

public class GradesController {
    private static final DBHandler DB_HANDLER = new DBHandler();
    private static final ObservableList<String> SUBJECTS_LIST = DB_HANDLER.getSubjectsList();
    private static int marksheet;
    private Grade currentGrade;

    @FXML
    protected GridPane generalGridPane;

    @FXML
    protected AnchorPane generalAnchorPane;

    @FXML
    private ChoiceBox<String> subjectChoiceBox;

    @FXML
    protected Label markLabel;

    @FXML
    private TextField markTextField;

    @FXML
    private CheckBox examCheckBox;

    @FXML
    private Label issueLabel;

    @FXML
    protected Button addButton;

    @FXML
    protected TabPane tablesTabPane;

    @FXML
    protected Tab marklistsExamTap;

    @FXML
    protected AnchorPane marklistsExamAnchorPane;

    @FXML
    private TableView<Grade> marklistsExamTableView;

    @FXML
    protected Label emptyMarklistsExamLabel;

    @FXML
    private TableColumn<Grade, String> subjectExamTableColumn;

    @FXML
    private TableColumn<Grade, String> markExamTableColumn;

    @FXML
    protected Tab marklistsTab;

    @FXML
    protected AnchorPane marklistsAnchorPane;

    @FXML
    private TableView<Grade> marklistsTableView;

    @FXML
    protected Label emptyMarklistsLabel;

    @FXML
    private TableColumn<Grade, String> subjectTableColumn;

    @FXML
    private TableColumn<Grade, String> markTableColumn;

    @FXML
    private AnchorPane deleteAnchorPane;

    @FXML
    private TextField deleteTextField;

    @FXML
    protected Button deleteButton;

    @FXML
    protected Button refreshButton;

    @FXML
    private void initialize() {
        subjectTableColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        markTableColumn.setCellValueFactory(new PropertyValueFactory<>("mark"));
        subjectExamTableColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        markExamTableColumn.setCellValueFactory(new PropertyValueFactory<>("mark"));
        subjectChoiceBox.setItems(SUBJECTS_LIST);
        subjectChoiceBox.setValue(SUBJECTS_LIST.get(0));

        refreshTable();
    }

    @FXML
    void onClickAddButton() {
        if(isFieldsEmpty() || isMarklistExist()) return;

        currentGrade = new Grade(subjectChoiceBox.getSelectionModel().getSelectedIndex(),
                Byte.parseByte(markTextField.getText()), examCheckBox.isSelected());

        DB_HANDLER.addGrade(marksheet, currentGrade);

        clearFields();
        refreshTable();
    }

    @FXML
    private void onClickDeleteButton() {
        if (deleteTextField.getText().isEmpty()) {
            issueLabel.setText("НУЖНО ВВЕСТИ ID ОЦЕНКИ!");
            issueLabel.setVisible(true);
        } else if (Integer.parseInt(deleteTextField.getText()) == currentGrade.getId()) {
            DB_HANDLER.deleteGrade(currentGrade.getId());

            clearFields();
            refreshTable();
        } else {
            issueLabel.setText("ID НЕ СОВПАДАЕТ!");
            issueLabel.setVisible(true);
        }
    }

    @FXML
    private void refreshTable() {
        issueLabel.setVisible(false);
        deleteAnchorPane.setVisible(false);
        marklistsExamTableView.setItems(DB_HANDLER.getGrades(marksheet, true));
        marklistsTableView.setItems(DB_HANDLER.getGrades(marksheet, false));
    }

    @FXML
    private void onSelectMarklistsTableItem() {
        if ((currentGrade = marklistsTableView.getSelectionModel().getSelectedItem()) != null) {
            deleteAnchorPane.setVisible(true);
            deleteTextField.setPromptText("ID: " + currentGrade.getId());
        } else {
            deleteAnchorPane.setVisible(false);
        }

        issueLabel.setVisible(false);
    }

    @FXML
    private void onSelectMarklistsExamTableItem() {
        if ((currentGrade = marklistsExamTableView.getSelectionModel().getSelectedItem()) != null) {
            deleteAnchorPane.setVisible(true);
            deleteTextField.setPromptText("ID: " + currentGrade.getId());
        } else {
            deleteAnchorPane.setVisible(false);
        }

        issueLabel.setVisible(false);
    }

    @FXML
    private void cutOffLimitMark() {
        String oldMark = markTextField.getText();
        String newMark = oldMark.replaceAll("[^1-5]", "");

        if (!oldMark.equals(newMark)) {
            markTextField.setText(newMark);
            markTextField.positionCaret(markTextField.getLength());
        }

        if (markTextField.getLength() > 1) {
            markTextField.setText(markTextField.getText(0, 1));
            markTextField.positionCaret(1);
        }
    }

    private boolean isFieldsEmpty() {
        if (subjectChoiceBox.getSelectionModel().getSelectedIndex() == 0 || markTextField.getText().isEmpty()) {
            issueLabel.setText("ПОЛЯ СО * ОБЯЗАТЕЛЬНЫ К ЗАПОЛНЕНИЮ!");
            issueLabel.setVisible(true);

            return true;
        } else
            return false;
    }

    private boolean isMarklistExist() {
        if (DB_HANDLER.isGradeExist(marksheet, subjectChoiceBox.getSelectionModel().getSelectedIndex())) {
            issueLabel.setText("ОЦЕНКА УЖЕ ВЫСТАВЛЕНА!");
            issueLabel.setVisible(true);

            return true;
        } else
            return false;
    }

    private void clearFields() {
        markTextField.clear();
        deleteTextField.clear();
        examCheckBox.setSelected(false);
        subjectChoiceBox.setValue(SUBJECTS_LIST.get(0));
    }

    void setMarksheet(int marksheet) {
        GradesController.marksheet = marksheet;

        refreshTable();
    }
}
