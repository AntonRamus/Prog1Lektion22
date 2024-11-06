package opgave01.gui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import opgave01.application.model.Company;
import opgave01.application.model.Customer;
import opgave01.storage.Storage;
import java.util.ArrayList;

public class AddCustomerWindow extends Stage {
    private final ListView<Customer> customerListView = new ListView<>();
    private Company company;
    private Label errorLabel;


    public AddCustomerWindow(String title, Company company) {
        initStyle(StageStyle.UTILITY);
        initModality(Modality.APPLICATION_MODAL);
        setResizable(false);
        setTitle(title);
        this.company = company;
        GridPane pane = new GridPane();
        initContent(pane);
        Scene scene = new Scene(pane);
        setScene(scene);
    }

    private void initContent(GridPane pane) {
        pane.setPadding(new Insets(10));
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setGridLinesVisible(false);
        Label customerlbl = new Label("Customers:");
        pane.add(customerlbl,0,0);

        customerListView.getItems().addAll(Storage.getCustomers());
        customerListView.setEditable(false);
        customerListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        pane.add(customerListView,0,1);

        Button btnCancel = new Button("Cancel");
        pane.add(btnCancel, 0, 2);
        GridPane.setHalignment(btnCancel, HPos.LEFT);
        btnCancel.setOnAction(event -> cancelAction());

        Button btnOK = new Button("OK");
        pane.add(btnOK, 0, 2);
        GridPane.setHalignment(btnOK, HPos.RIGHT);
        btnOK.setOnAction(event -> okAction());

        errorLabel = new Label();
        pane.add(errorLabel, 0, 5);
        errorLabel.setStyle("-fx-text-fill: red");

    }

    private void okAction() {
        company.addCustomers(customerListView.getSelectionModel().getSelectedItems());
        hide();
    }

    private void cancelAction() {
        hide();
    }
}
