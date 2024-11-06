package opgave01.gui;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import opgave01.application.controller.Controller;
import opgave01.application.model.Company;
import opgave01.application.model.Customer;
import opgave01.application.model.Employee;

import java.util.Optional;

public class CompanyPane extends GridPane {
    private TextField nameTextField;
    private TextField hoursTextField;
    private TextArea employeesTextArea;
    private TextArea txaCustomers;
    private ListView<Company> companyListView;

    public CompanyPane() {
        this.setPadding(new Insets(20));
        this.setHgap(20);
        this.setVgap(10);
        this.setGridLinesVisible(false);

        this.add(new Label("Companies"), 0, 0);

        companyListView = new ListView<>();
        this.add(companyListView, 0, 1, 1, 4);
        companyListView.setPrefWidth(200);
        companyListView.setPrefHeight(200);
        companyListView.getItems().setAll(Controller.getCompanies());

        ChangeListener<Company> listener = (ov, oldCompny, newCompany) -> this.selectedCompanyChanged(newCompany);
        companyListView.getSelectionModel().selectedItemProperty().addListener(listener);

        Label lblName = new Label("Name:");
        this.add(lblName, 1, 1);

        nameTextField = new TextField();
        this.add(nameTextField, 2, 1);
        nameTextField.setEditable(false);

        Label lblHours = new Label("Weekly Hours:");
        this.add(lblHours, 1, 2);

        hoursTextField = new TextField();
        this.add(hoursTextField, 2, 2);
        hoursTextField.setEditable(false);

        Label employeesLabel = new Label("Employees:");
        this.add(employeesLabel, 1, 3);
        GridPane.setValignment(employeesLabel, VPos.BASELINE);
        employeesLabel.setPadding(new Insets(4, 0, 4, 0));

        employeesTextArea = new TextArea();
        this.add(employeesTextArea, 2, 3);
        employeesTextArea.setPrefWidth(200);
        employeesTextArea.setPrefHeight(100);
        employeesTextArea.setEditable(false);

        Label customersLabel = new Label("Customers:");
        this.add(customersLabel, 1, 4);
        GridPane.setValignment(customersLabel, VPos.BASELINE);
        customersLabel.setPadding(new Insets(4, 0, 4, 0));

        txaCustomers = new TextArea();
        this.add(txaCustomers, 2, 4);
        txaCustomers.setPrefWidth(200);
        txaCustomers.setPrefHeight(100);
        txaCustomers.setEditable(false);

        HBox hboxButtons = new HBox(40);
        this.add(hboxButtons, 0, 5, 3, 1);
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

        Button createCustomerButton = new Button("Create Customer");
        hboxButtons.getChildren().add(createCustomerButton);
        createCustomerButton.setOnAction(event -> this.createCustomerAction());

        Button addCustomerToCompanyButton = new Button("Add Customer(s)");
        hboxButtons.getChildren().add(addCustomerToCompanyButton);
        addCustomerToCompanyButton.setOnAction(event -> this.addCustomerToCompanyAction());

        if (!companyListView.getItems().isEmpty()) {
            companyListView.getSelectionModel().select(0);
        }
    }


    // -------------------------------------------------------------------------

    private void addCustomerToCompanyAction() {
        Company company = companyListView.getSelectionModel().getSelectedItem();
        if (company != null) {
            new AddCustomerWindow("Add Customer To " + company.getName(),company).showAndWait();
            //wait for the modal dialog to close
            updateControls(company);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No company selected");
            alert.setHeaderText("Please select the company you wish to add customers to");
            // wait for the modal dialog to close
            alert.show();
        }
    }

    private void createCustomerAction() {
        new CustomerWindow("Create Customer").showAndWait();
        // Wait for the modal dialog to close
        Company company = companyListView.getSelectionModel().getSelectedItem();
        if (company != null) {
            updateControls(company);
        }
    }

    private void createAction() {
        new CompanyWindow("Create Company").showAndWait();
        // Wait for the modal dialog to close
        companyListView.getItems().setAll(Controller.getCompanies());
        int index = companyListView.getItems().size() - 1;
        companyListView.getSelectionModel().select(index);
    }

    private void updateAction() {
        Company company = companyListView.getSelectionModel().getSelectedItem();
        if (company != null) {
            new CompanyWindow("Update Company", company).showAndWait();
            // Wait for the modal dialog to close
            int selectIndex = companyListView.getSelectionModel().getSelectedIndex();
            companyListView.getItems().setAll(Controller.getCompanies());
            companyListView.getSelectionModel().select(selectIndex);
        }
    }

    private void deleteAction() {
        Company company = companyListView.getSelectionModel().getSelectedItem();
        if (company != null) {
            if (company.hasEmployees()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Delete Company");
                alert.setHeaderText("Can't delete a company that has emlpoyees");
                // wait for the modal dialog to close
                alert.show();
            } else if (company.hasCustomers()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Delete Company");
                alert.setHeaderText("Can't delete a company that has customers");
                // wait for the modal dialog to close
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Delete Company");
                // alert.setContentText("Are you sure?");
                alert.setHeaderText("Are you sure?");
                Optional<ButtonType> result = alert.showAndWait();
                if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                    Controller.deleteCompany(company);
                    companyListView.getItems().setAll(Controller.getCompanies());
                    this.updateControls(companyListView.getSelectionModel().getSelectedItem());
                }
            }
        }
    }

    // -------------------------------------------------------------------------

    private void selectedCompanyChanged(Company newCompany) {
        this.updateControls(newCompany);
    }

    public void updateControls(Company company) {
        if (company != null) {
            nameTextField.setText(company.getName());
            hoursTextField.setText("" + company.getHours());
            StringBuilder stringBuilder = new StringBuilder();
            for (Employee emp : company.getEmployees()) {
                stringBuilder.append(emp + "\n");
            }
            employeesTextArea.setText(stringBuilder.toString());

            StringBuilder stringBuilder1 = new StringBuilder();
            for (Customer customer : company.getCustomers()) {
                stringBuilder1.append(customer + "\n");
            }
            txaCustomers.setText(stringBuilder1.toString());
        } else {
            nameTextField.clear();
            hoursTextField.clear();
            employeesTextArea.clear();
            txaCustomers.clear();
        }
    }
}