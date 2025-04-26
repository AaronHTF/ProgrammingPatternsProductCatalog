package org.example.programmingpatternsproductcatalog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;

public class ProductCatalogViewController implements Initializable {
    @FXML
    private Button addProductButton;
    @FXML
    private Button deleteProductButton;
    @FXML
    private Button productDetailsButton;
    @FXML
    private ChoiceBox<String> sortChoiceBox;
    @FXML
    private ChoiceBox<String> filterCategoryChoiceBox;
    @FXML
    private TextField minTextField;
    @FXML
    private TextField maxTextField;
    @FXML
    private Button filterPriceButton;
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> productIdColumn;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, Double> productPriceColumn;
    @FXML
    private TableColumn<Product, String> productCategoryColumn;
    @FXML
    private TableColumn<Product, Integer> productQuantityColumn;

    CatalogManager catalogManager = CatalogManager.getInstance();;
    ObservableList<Product> products;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        productQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        setTableContent();
        loadChoiceBoxes();
    }

    @FXML
    public void handleAddProductButtonAction() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addProductView.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        AddProductViewController addProductViewController = fxmlLoader.getController();
        addProductViewController.setParentController(this);
        stage.setTitle("Add a Product");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleDeleteProductButtonAction() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("deleteProductView.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        DeleteProductViewController deleteProductViewController = fxmlLoader.getController();
        deleteProductViewController.setParentController(this);
        stage.setTitle("Delete a Product");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleUpdateProductButtonAction() throws IOException {
        int id = productTable.getSelectionModel().getSelectedItem().getId();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("updateDetailsView.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        UpdateDetailsViewController updateDetailsViewController = fxmlLoader.getController();
        updateDetailsViewController.loadProduct(id);
        updateDetailsViewController.setParentController(this);
        stage.setTitle("Update a Product");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleRemoveFiltersButton() {
        minTextField.setText("");
        maxTextField.setText("");
        catalogManager.reloadProducts();
        setTableContent();
    }

    @FXML
    public void handleFilterByPriceButtonAction() {
        String min = minTextField.getText();
        String max = maxTextField.getText();

        if (min.trim().isEmpty() || max.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Please enter the minimum and maximum values");
            alert.showAndWait();
        } else {
            List<Product> productsFilteredByPrice = catalogManager.filterByPrice(Double.parseDouble(min), Double.parseDouble(max));
            ObservableList<Product> productObservableList = FXCollections.observableList(productsFilteredByPrice);
            productTable.setItems(productObservableList);
        }
    }

    public void selectSort(ActionEvent event) {
        String sort = sortChoiceBox.getValue();
        switch (sort) {
            case "Alphabetically" -> catalogManager.sortByName();
            case "Price" -> catalogManager.sortByPrice();
        }
        setTableContent();
    }

    public void selectCategory(ActionEvent event) {
        String category = filterCategoryChoiceBox.getValue();
        List<Product> productsFilteredByCategory = catalogManager.filterByCategory(category);
        ObservableList<Product> productObservableList = FXCollections.observableList(productsFilteredByCategory);
        productTable.setItems(productObservableList);
    }

    public void setTableContent() {
        products = FXCollections.observableArrayList(catalogManager.getProductList());
        productTable.setItems(products);
    }

    public void loadChoiceBoxes() {
        String[] sortList = {"Alphabetically", "Price"};
        HashSet<String> categories = catalogManager.getCategories();

        sortChoiceBox.getItems().setAll(sortList);
        filterCategoryChoiceBox.getItems().setAll(categories);

        sortChoiceBox.setOnAction(this::selectSort);
        filterCategoryChoiceBox.setOnAction(this::selectCategory);
    }
}
