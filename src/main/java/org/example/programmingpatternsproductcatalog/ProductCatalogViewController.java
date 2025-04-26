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

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ProductCatalogViewController implements Initializable {
    @FXML
    private Button englishButton;
    @FXML
    private Button frenchButton;
    @FXML
    private Label titleLabel;
    @FXML
    private Label filterByCategoryLabel;
    @FXML
    private Label filterByPriceLabel;
    @FXML
    private Label sortByLabel;
    @FXML
    private Button addProductButton;
    @FXML
    private Button deleteProductButton;
    @FXML
    private Button productDetailsButton;
    @FXML
    private Button removeFiltersButton;
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
    Locale locale;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        productQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        setTableContent();
        loadChoiceBoxes();
        locale = Locale.getDefault();
    }

    @FXML
    public void handleAddProductButtonAction() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addProductView.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        AddProductViewController addProductViewController = fxmlLoader.getController();
        addProductViewController.setParentController(this);
        addProductViewController.loadLanguage(locale);
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
        deleteProductViewController.loadLanguage(locale);
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
        updateDetailsViewController.loadLanguage(locale);
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
            ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages", locale);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(resourceBundle.getString("minAndMax"));
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

    public void changeLanguage(Locale locale) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages", locale);
        titleLabel.setText(resourceBundle.getString("title"));
        filterByCategoryLabel.setText(resourceBundle.getString("filterByCategory"));
        filterByPriceLabel.setText(resourceBundle.getString("filterByPrice"));
        sortByLabel.setText(resourceBundle.getString("sortBy"));
        addProductButton.setText(resourceBundle.getString("addProduct"));
        deleteProductButton.setText(resourceBundle.getString("deleteProduct"));
        productDetailsButton.setText(resourceBundle.getString("updateProduct"));
        filterPriceButton.setText(resourceBundle.getString("filterByPrice"));
        removeFiltersButton.setText(resourceBundle.getString("removeFilters"));
        productIdColumn.setText(resourceBundle.getString("productId"));
        productNameColumn.setText(resourceBundle.getString("productName"));
        productPriceColumn.setText(resourceBundle.getString("productPrice"));
        productCategoryColumn.setText(resourceBundle.getString("productCategory"));
        productQuantityColumn.setText(resourceBundle.getString("productQuantity"));
        englishButton.setText(resourceBundle.getString("english"));
        frenchButton.setText(resourceBundle.getString("french"));
    }

    @FXML
    public void handleEnglishButtonAction() {
        locale = Locale.of("en", "US");
        changeLanguage(locale);
    }

    @FXML
    public void handleFrenchButtonAction() {
        locale = Locale.of("fr", "CA");
        changeLanguage(locale);
    }
}
