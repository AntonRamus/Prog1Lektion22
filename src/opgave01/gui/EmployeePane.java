package opgave01.gui;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import opgave01.application.controller.Controller;
import opgave01.application.model.Employee;

import java.util.Optional;

public class EmployeePane extends GridPane {
    private TextField nameTextField;
    private TextField wageTextField;
    private TextField companyTextField;
    private TextField salaryTextField;
    private TextField employmentYearTextField;
    private ListView<Employee> employeesListView;

    public EmployeePane() {
        this.setPadding(new Insets(20));
        this.setHgap(20);
        this.setVgap(10);
        this.setGridLinesVisible(false);

        this.add(new Label("Employees"), 0, 0);

        employeesListView = new ListView<>();
        this.add(employeesListView, 0, 1, 1, 5);
        employeesListView.setPrefWidth(200);
        employeesListView.setPrefHeight(200);
        employeesListView.getItems().setAll(Controller.getEmployees());
        ChangeListener<Employee> listener = (ov, oldEmployee, newEmployee) -> this.selectedEmployeeChanged(newEmployee);
        employeesListView.getSelectionModel().selectedItemProperty().addListener(listener);

        this.add(new Label("Name:"), 1, 1);

        nameTextField = new TextField();
        this.add(nameTextField, 2, 1);
        nameTextField.setPrefWidth(200);
        nameTextField.setEditable(false);

        this.add(new Label("Hourly Wage:"), 1, 2);

        wageTextField = new TextField();
        this.add(wageTextField, 2, 2);
        wageTextField.setEditable(false);

        this.add(new Label("Company:"), 1, 3);

        companyTextField = new TextField();
        this.add(companyTextField, 2, 3);
        companyTextField.setEditable(false);

        this.add(new Label("Weekly Salary:"), 1, 4);

        salaryTextField = new TextField();
        this.add(salaryTextField, 2, 4);
        salaryTextField.setEditable(false);

        this.add(new Label("Employment Year"),1,5);

        employmentYearTextField = new TextField();
        this.add(employmentYearTextField,2,5);
        employmentYearTextField.setEditable(false);

        HBox hboxButtons = new HBox(40);
        this.add(hboxButtons, 0, 6, 3, 1);
        hboxButtons.setPadding(new Insets(10, 0, 0, 0));
        hboxButtons.setAlignment(Pos.BASELINE_CENTER);

        Button createButton = new Button("Create");
        hboxButtons.getChildren().add(createButton);
        createButton.setOnAction(event -> this.createAction());

        Button updateButton = new Button("Update");
        hboxButtons.getChildren().add(updateButton);
        updateButton.setOnAction(event -> this.updateAction());

        Button deleteButton = new Button("Delete");
        hboxButtons.getChildren().add(deleteButton);
        deleteButton.setOnAction(event -> this.deleteAction());

        if (!employeesListView.getItems().isEmpty()) {
            employeesListView.getSelectionModel().select(0);
        }
    }

    // -------------------------------------------------------------------------

    private void createAction() {
        new EmployeeWindow("Create Employee").showAndWait();
        // Wait for the modal dialog to close
        employeesListView.getItems().setAll(Controller.getEmployees());
        this.updateControls(employeesListView.getSelectionModel().getSelectedItem());
    }

    private void updateAction() {
        Employee employee = employeesListView
                .getSelectionModel()
                .getSelectedItem();
        if (employee != null) {
            new EmployeeWindow("Update Employee", employee).showAndWait();
            // Wait for the modal dialog to close
            employeesListView
                    .getItems()
                    .setAll(Controller.getEmployees());
            employeesListView
                    .getSelectionModel()
                    .select(employeesListView
                            .getSelectionModel()
                            .getSelectedIndex()
                    );
        }
    }

    private void deleteAction() {
        Employee employee = employeesListView.getSelectionModel().getSelectedItem();
        if (employee != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Delete Employee");
            // alert.setContentText("Are you sure?");
            alert.setHeaderText("Are you sure?");
            Optional<ButtonType> result = alert.showAndWait();
            // Wait for the modal dialog to close
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                Controller.deleteEmployee(employee);
                employeesListView.getItems().setAll(Controller.getEmployees());
                this.updateControls(employeesListView.getSelectionModel().getSelectedItem());
            }
        }
    }

    private void selectedEmployeeChanged(Employee newEmployee) {
        this.updateControls(newEmployee);
    }

    public void updateControls(Employee employee) {
        if (employee != null) {
            nameTextField.setText(employee.getName());
            wageTextField.setText("kr " + employee.getWage());
            if (employee.getEmploymentYear() != 0) {
                employmentYearTextField.setText("" + employee.getEmploymentYear());
            } else {
                employmentYearTextField.clear();
            }
            if (employee.getCompany() != null) {
                companyTextField.setText("" + employee.getCompany());
                salaryTextField.setText("kr " + employee.weeklySalary());
            } else {
                companyTextField.clear();
                salaryTextField.clear();
            }
        } else {
            nameTextField.clear();
            wageTextField.clear();
            companyTextField.clear();
            salaryTextField.clear();
            employmentYearTextField.clear();
        }
    }
}