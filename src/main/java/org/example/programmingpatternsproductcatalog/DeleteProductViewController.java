package org.example.programmingpatternsproductcatalog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Text;

public class DeleteProductViewController {
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
            alert.setHeaderText("Please enter the Product ID");
            alert.showAndWait();
        } else {
            try {
                catalogManager.deleteProductById(Integer.parseInt(id));

                controller.setTableContent();

                Stage stage = (Stage) confirmButton.getScene().getWindow();
                stage.close();
            }
            catch (ProductNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Invalid Product ID");
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
