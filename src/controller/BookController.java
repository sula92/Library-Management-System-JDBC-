package controller;

import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entity.Book;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.BookTM;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BookController {
    public JFXTextField isbn;
    public JFXTextField title;
    public JFXTextField author;
    public JFXTextField edition;
    public Button btnSave;
    public Button btnAdd;
    public TableView<BookTM> bkTbl;
    public TableColumn colIsbn;
    public TableColumn colTit;
    public TableColumn colEdi;
    public TextField txtSearch;
    public TableColumn colAuth;
    public Button btnDelete;
    public FontAwesomeIconView faHome;
    public AnchorPane bookForm;
    public Button btnReport;

    public void initialize() {

        isbn.setEditable(false);

        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTit.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuth.setCellValueFactory(new PropertyValueFactory<>("author"));
        colEdi.setCellValueFactory(new PropertyValueFactory<>("edition"));


        loadAllBooks();

        bkTbl.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BookTM>() {
            @Override
            public void changed(ObservableValue<? extends BookTM> observable, BookTM oldValue, BookTM newValue) {
                BookTM selectedItem = bkTbl.getSelectionModel().getSelectedItem();

                if (selectedItem == null) {
                    btnSave.setText("Save");
                    //btnDelete.setDisable(true);
                    isbn.clear();
                    title.clear();
                    author.clear();
                    edition.clear();
                    return;
                }

                btnSave.setText("Update");
                btnSave.setDisable(false);
                btnDelete.setDisable(false);
                title.setDisable(false);
                author.setDisable(false);
                edition.setDisable(false);
                isbn.setText(selectedItem.getIsbn());
                title.setText(selectedItem.getTitle());
                author.setText(selectedItem.getAuthor());
                edition.setText(selectedItem.getEdition());

            }
        });
    }

    public void loadAllBooks() {
        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM book");
            ObservableList<BookTM> books = bkTbl.getItems();
            books.clear();
            while (rst.next()) {
                String isbn = rst.getString(1);
                String title = rst.getString(2);
                String author = rst.getString(3);
                String edition = rst.getString(4);
                books.add(new BookTM(isbn, title, author, edition));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void btnSaveOnAction(ActionEvent event) {

        if (isbn.getText().trim().isEmpty() ||
                title.getText().trim().isEmpty() ||
                author.getText().trim().isEmpty()||
                edition.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Description, Qty. on Hand or Unit Price can't be empty").show();
            return;
        }

        String isb = isbn.getText().trim();
        String tit = title.getText().trim();
        String auth = author.getText().trim();
        String edi =edition.getText().trim();



        if (btnSave.getText().equalsIgnoreCase("Save")) {

            try {
                PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO book VALUES (?,?,?,?)");
                pstm.setObject(1, isbn.getText());
                pstm.setObject(2, title.getText());
                pstm.setObject(3, auth);
                pstm.setObject(4, edi);
                if (pstm.executeUpdate() == 0) {
                    new Alert(Alert.AlertType.ERROR, "Failed to save the book", ButtonType.OK).show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            btnAddOnAction(event);
        } else {
            BookTM selectedItem = bkTbl.getSelectionModel().getSelectedItem();

            try {
                PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("UPDATE book SET title=?, author=?, edition=? WHERE id=?");
                pstm.setObject(1, tit);
                pstm.setObject(2, auth);
                pstm.setObject(3, edi);
                pstm.setObject(4, selectedItem.getIsbn());
                if (pstm.executeUpdate() == 0) {
                    new Alert(Alert.AlertType.ERROR, "Failed to update the Book").show();
                }
                else {
                    new Alert(Alert.AlertType.INFORMATION, "Updates Successfully!").show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


            bkTbl.refresh();
            btnAddOnAction(event);
        }
        loadAllBooks();

    }

    public void btnAddOnAction(ActionEvent event) {

        title.clear();
        author.clear();
        edition.clear();

        bkTbl.getSelectionModel().clearSelection();
        title.setDisable(false);
        author.setDisable(false);
        edition.setDisable(false);
        title.requestFocus();
        btnSave.setDisable(false);

        // Generate a new id
        int maxCode = 0;
        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet rst = stm.executeQuery("SELECT id FROM book ORDER BY id DESC LIMIT 1");
            if (rst.next()) {
                maxCode = Integer.parseInt(rst.getString(1).replace("B", ""));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        maxCode = maxCode + 1;
        String code = "";
        if (maxCode < 10) {
            code = "B00" + maxCode;
        } else if (maxCode < 100) {
            code = "B0" + maxCode;
        } else {
            code = "B" + maxCode;
        }
        isbn.setText(code);

    }

    public void txtSerAction(KeyEvent keyEvent) {
        bkTbl.getItems().clear();

        ObservableList<BookTM> bks=bkTbl.getItems();
        bkTbl.getItems().clear();
        bks.clear();

        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM book");

            bks.clear();
            while (rst.next()) {
                String isbn = rst.getString(1);
                String title = rst.getString(2);
                String author = rst.getString(3);
                String edition = rst.getString(4);
                bks.add(new BookTM(isbn, title, author, edition));
            }

            bkTbl.setItems(bks);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        String value=txtSearch.getText();


        for (BookTM temp: bks
        ) {

            if (temp.getIsbn().contains(value) || temp.getTitle().contains(value) || temp.getAuthor().contains(value)) {
                bks.add(temp);
            }
        }
        bkTbl.setItems(bks);




    }



    public void btnDeleteOnAction(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure whether you want to delete this item?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.YES) {
            BookTM selectedItem = bkTbl.getSelectionModel().getSelectedItem();
            try {
                PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM book WHERE id=?");
                pstm.setObject(1, selectedItem.getIsbn());
                if (pstm.executeUpdate() == 0) {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete the item", ButtonType.OK).show();
                } else {
                    bkTbl.getItems().remove(selectedItem);
                    bkTbl.getSelectionModel().clearSelection();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void homeClicked(MouseEvent mouseEvent) throws IOException {

        URL url=this.getClass().getResource("/view/Main.fxml");
        Parent parent= FXMLLoader.load(url);
        Scene scene=new Scene(parent);
        Stage stage= (Stage) this.bookForm.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Student");
        stage.setFullScreen(false);
    }

    public void btnReportOnAction(ActionEvent event) throws JRException {




        JasperDesign jasperDesign = JRXmlLoader.load(BookController.class.
                getResourceAsStream("/reports/books.jrxml"));
        JasperReport jasperReport= JasperCompileManager.compileReport(jasperDesign);
        Map<String, Object> params = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(bkTbl.getItems()));
        JasperViewer.viewReport(jasperPrint);
    }


}
