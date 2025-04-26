// -------------------------------------------------------
// Assignment 3
// Written by: Aaron Ho Tim Fat 2314854
// For “Programming patterns” Section 2 – Winter 2025
// --------------------------------------------------------

package org.example.programmingpatternsproductcatalog;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class DeleteProductViewController {
    @FXML
    private Label titleLabel;
    @FXML
    private Button confirmButton;
    @FXML
    private TextField productIdTextField;
    @FXML
    private Button cancelButton;

    ProductCatalogViewController controller;
    CatalogManager catalogManager = CatalogManager.getInstance();

    @FXML
    public void handleConfirmButtonAction() {
        String id = productIdTextField.getText();

        if (id.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages", controller.locale);
            alert.setHeaderText(resourceBundle.getString("missingId"));
            alert.showAndWait();
        } else {
            try {
                catalogManager.deleteProductById(Integer.parseInt(id));

                controller.loadChoiceBoxes();
                controller.setTableContent();

                Stage stage = (Stage) confirmButton.getScene().getWindow();
                stage.close();
            }
            catch (ProductNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages", controller.locale);
                alert.setHeaderText(resourceBundle.getString("invalidId"));
                alert.setContentText(resourceBundle.getString("productNotFound"));
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void handleCancelButtonAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void setParentController(ProductCatalogViewController controller) {
        this.controller = controller;
    }

    public void loadLanguage(Locale locale) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages", locale);
        titleLabel.setText(resourceBundle.getString("productId"));
        productIdTextField.setPromptText(resourceBundle.getString("productId"));
        confirmButton.setText(resourceBundle.getString("confirm"));
        cancelButton.setText(resourceBundle.getString("cancel"));
    }
}
