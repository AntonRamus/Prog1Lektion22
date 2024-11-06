package opgave01.gui;

import javafx.beans.value.ChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import opgave01.application.controller.Controller;
import opgave01.application.model.Company;
import opgave01.application.model.Customer;
import opgave01.application.model.Employee;

public class CustomerWindow extends Stage {
    private Customer customer;
    private TextField nameTextField;
    private CheckBox addCompanyCheckBox;
    private ComboBox<Company> selectCompanyComboBox;
    private Label errorLabel;

    public CustomerWindow(String title, Customer customer) {
        initStyle(StageStyle.UTILITY);
        initModality(Modality.APPLICATION_MODAL);
        setResizable(false);
        this.customer = customer;
        setTitle(title);
        GridPane pane = new GridPane();
        initContent(pane);
        Scene scene = new Scene(pane);
        setScene(scene);
    }

    public CustomerWindow(String title) {
        this(title, null);
    }

    private void initContent(GridPane pane) {
        pane.setPadding(new Insets(10));
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setGridLinesVisible(false);
        Label lblName = new Label("Name");
        pane.add(lblName, 0, 0);
        nameTextField = new TextField();
        pane.add(nameTextField, 0, 1);
        nameTextField.setPrefWidth(200);
        addCompanyCheckBox = new CheckBox("Company");
        pane.add(addCompanyCheckBox, 0, 2);
        ChangeListener<Boolean> listener = (ov, oldValue, newValue) -> selectedCompanyChanged(newValue);
        addCompanyCheckBox.selectedProperty().addListener(listener);
        selectCompanyComboBox = new ComboBox<>();
        pane.add(selectCompanyComboBox, 0, 3);
        selectCompanyComboBox.getItems().addAll(Controller.getCompanies());
        selectCompanyComboBox.setDisable(true);
        Button btnCancel = new Button("Cancel");
        pane.add(btnCancel, 0, 4);
        GridPane.setHalignment(btnCancel, HPos.LEFT);
        btnCancel.setOnAction(event -> cancelAction());
        Button btnOK = new Button("OK");
        pane.add(btnOK, 0, 4);
        GridPane.setHalignment(btnOK, HPos.RIGHT);
        btnOK.setOnAction(event -> okAction());
        errorLabel = new Label();
        pane.add(errorLabel, 0, 5);
        errorLabel.setStyle("-fx-text-fill: red");
        initControls();
    }

    private void initControls() {
        if (customer != null) {
            nameTextField.setText(customer.getName());

        } else {
            nameTextField.clear();
            selectCompanyComboBox.getSelectionModel().select(0);
        }
    }

    private void cancelAction() {
        hide();
    }

    private void okAction() {
        String name = nameTextField.getText().trim();
        if (name.isEmpty()) {
            errorLabel.setText("Name is empty");
            return;
        }

        boolean companyIsSelected = addCompanyCheckBox.isSelected();
        Company newCompany = selectCompanyComboBox.getSelectionModel().getSelectedItem();

        // Call application.controller methods
        if (customer != null) {
            if (companyIsSelected) {
                Controller.addCustomerToCompany(customer, newCompany);
            }
        } else {
            if (companyIsSelected) {
                Controller.addCustomerToCompany(Controller.createCustomer(name),newCompany);
            } else {
                Controller.createCustomer(name);
            }

        }
        hide();
    }

    private void selectedCompanyChanged(boolean checked) {
        selectCompanyComboBox.setDisable(!checked);
    }
}