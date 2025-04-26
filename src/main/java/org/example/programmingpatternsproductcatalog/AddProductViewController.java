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

public class AddProductViewController {
    @FXML
    private Label titleLabel;
    @FXML
    private Button confirmButton;
    @FXML
    private TextField productIdTextField;
    @FXML
    private TextField productNameTextField;
    @FXML
    private TextField productPriceTextField;
    @FXML
    private TextField productCategoryTextField;
    @FXML
    private TextField productQuantityTextField;
    @FXML
    private Button cancelButton;

    ProductCatalogViewController controller;
    CatalogManager catalogManager = CatalogManager.getInstance();

    @FXML
    public void handleConfirmButtonAction() {
        String id = productIdTextField.getText();
        String name = productNameTextField.getText();
        String price = productPriceTextField.getText();
        String category = productCategoryTextField.getText();
        String quantity = productQuantityTextField.getText();

        if (id.trim().isEmpty() || name.trim().isEmpty() || price.trim().isEmpty() || category.trim().isEmpty() || quantity.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages", controller.locale);
            alert.setHeaderText(resourceBundle.getString("fillAllFields"));
            alert.showAndWait();
        } else {
            try {
                if (catalogManager.isIdTaken(Integer.parseInt(id))) {
                    throw new DuplicateProductIdException();
                }

                Product product = new Product(Integer.parseInt(id), name, Double.parseDouble(price), category, Integer.parseInt(quantity));
                catalogManager.addProduct(product);

                controller.loadChoiceBoxes();
                controller.setTableContent();

                Stage stage = (Stage) confirmButton.getScene().getWindow();
                stage.close();
            }
            catch (DuplicateProductIdException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages", controller.locale);
                alert.setHeaderText(resourceBundle.getString("duplicateId"));
                alert.setContentText(resourceBundle.getString("duplicateProduct"));
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
        titleLabel.setText(resourceBundle.getString("insertProduct"));
        productIdTextField.setPromptText(resourceBundle.getString("productId"));
        productNameTextField.setPromptText(resourceBundle.getString("productName"));
        productPriceTextField.setPromptText(resourceBundle.getString("productPrice"));
        productCategoryTextField.setPromptText(resourceBundle.getString("productCategory"));
        productQuantityTextField.setPromptText(resourceBundle.getString("productQuantity"));
        confirmButton.setText(resourceBundle.getString("confirm"));
        cancelButton.setText(resourceBundle.getString("cancel"));
    }
}
