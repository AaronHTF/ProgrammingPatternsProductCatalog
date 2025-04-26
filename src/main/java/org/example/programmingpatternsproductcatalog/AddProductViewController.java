package org.example.programmingpatternsproductcatalog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddProductViewController {
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
            alert.setHeaderText("Please fill all the fields!");
            alert.showAndWait();
        } else {
            try {
                if (catalogManager.isIdTaken(Integer.parseInt(id))) {
                    throw new DuplicateProductIdException();
                }

                Product product = new Product(Integer.parseInt(id), name, Double.parseDouble(price), category, Integer.parseInt(quantity));
                catalogManager.addProduct(product);

                controller.setTableContent();

                Stage stage = (Stage) confirmButton.getScene().getWindow();
                stage.close();
            }
            catch (DuplicateProductIdException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Duplicate Product ID");
                alert.setContentText(e.getMessage());
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
}
