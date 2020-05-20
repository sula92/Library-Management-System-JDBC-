package controller;



import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.BorrowTM;
import util.MemberTM;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class BorrowController implements Initializable {

    public JFXTextField txtBorrowId;
    public FontAwesomeIconView faHome;
    public AnchorPane borrowForm;
    @FXML
    private JFXComboBox<?> cmbBorrowId;

    @FXML
    private JFXComboBox<String> cmbBookId;

    @FXML
    private JFXComboBox<String> cmbCustomerId;

    @FXML
    private JFXDatePicker cmbBorrowDate;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnSave;

    @FXML
    private TableView<BorrowTM> tblBorrow;

    @FXML
    private TableColumn colBorrowId;

    @FXML
    private TableColumn colBorrowedDate;

    @FXML
    private TableColumn colBookId;

    @FXML
    private TableColumn colCustomerId;

    @FXML
    private TableColumn colDelete;

    @FXML
    private Label lab1;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        AddNew();

        if (txtBorrowId.getText() == "" || cmbBookId.getValue() == null || cmbBorrowDate.getValue() == null || cmbCustomerId.getValue() == null) {
            btnSave.setDisable(true);

        } else {
            btnSave.setDisable(false);
        }

        loadMembers();
        loadBooks();
        loadBorrow();
        txtBorrowId.setPromptText("BORROW ID");
        txtBorrowId.setStyle("-fx-text-fill: white; -fx-prompt-text-fill: white");
        cmbBookId.setStyle("-fx-highlight-text-fill: white");
        cmbCustomerId.setStyle("-fx-prompt-text-fill: white");

        colBorrowId.setCellValueFactory(new PropertyValueFactory<>("borrowId"));
        colBorrowedDate.setCellValueFactory(new PropertyValueFactory<>("borrowedDate"));
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("button"));

        cmbBookId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            String bid = newValue;
            boolean available;
            try {
                boolean available1 = checkReturn(newValue);
                System.out.println("a1" + available1);
                boolean available2 = checkBorrow(newValue);
                System.out.println("a2" + available2);

                if (available1 || available2) {
                    available = true;
                    lab1.setText("BOOK IS AVAILABLE");
                    btnSave.setDisable(false);
                } else {
                    available = false;
                    lab1.setText("BOOK IS UNAVAILABLE");
                    btnSave.setDisable(true);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


        });

        tblBorrow.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            BorrowTM selectedItem = tblBorrow.getSelectionModel().getSelectedItem();
            txtBorrowId.setText(selectedItem.getBookId());
            //cmbBorrowDate.setValue(selectedItem.getBorrowedDate());
            cmbBookId.setValue(selectedItem.getBookId());
            cmbCustomerId.setValue(selectedItem.getCusId());

            btnSave.setText("UPDATE");


        });


    }

    @FXML
    void btnAddOnAction(ActionEvent event) {AddNew();}


public void AddNew(){
        btnSave.setText("SAVE");



        tblBorrow.getSelectionModel().clearSelection();
        cmbCustomerId.setDisable(false);
        cmbBorrowDate.setDisable(false);
        cmbBookId.setDisable(false);

        btnSave.setDisable(false);

        // Generate a new id
        int maxCode = 0;
        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet rst = stm.executeQuery("SELECT borrow_id FROM borrow ORDER BY borrow_id DESC LIMIT 1");
            if (rst.next()) {
                maxCode = Integer.parseInt(rst.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        maxCode = maxCode + 1;

        String code = "";
        if (maxCode < 10) {
            code = "00" + maxCode;
        } else if (maxCode < 100) {
            code = "0" + maxCode;
        } else {
            code = "" + maxCode;
        }

        txtBorrowId.setText(String.valueOf(code));

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException {

        if (btnSave.getText().equalsIgnoreCase("save")) {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("insert into borrow values (?,?,?,?)");
            preparedStatement.setString(1, txtBorrowId.getText());
            preparedStatement.setString(2, cmbBookId.getValue());
            preparedStatement.setDate(3, Date.valueOf(cmbBorrowDate.getValue()));
            preparedStatement.setString(4, cmbCustomerId.getValue());

            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                new Alert(Alert.AlertType.INFORMATION, "RECORD ADDED SUCCESSFULLY").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "SOMETHINGS WRONG").show();
            }
            cmbBookId.setValue("");
        } else {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("update borrow set book_id=?, date=?, member_id=? where borrow_id=?");
            preparedStatement.setString(1, cmbBookId.getValue());
            preparedStatement.setDate(2, Date.valueOf(cmbBorrowDate.getValue()));
            preparedStatement.setString(3, cmbCustomerId.getValue());
            preparedStatement.setString(4, txtBorrowId.getText());

            int rows = preparedStatement.executeUpdate();

            if (rows > 0) {
                new Alert(Alert.AlertType.INFORMATION, "RECORD UPDATED SUCCESSFULLY");
            } else {
                new Alert(Alert.AlertType.ERROR, "SOMETHINGS WRONG").show();
            }
            cmbBookId.setValue("");
        }

        tblBorrow.refresh();
        btnAddOnAction(event);
        loadBorrow();

    }

    public void loadMembers() {

        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet rst = stm.executeQuery("SELECT id FROM member");
            ObservableList<String> memberids = cmbCustomerId.getItems();
            memberids.clear();
            while (rst.next()) {

                memberids.add(rst.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void loadBooks() {

        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet rst = stm.executeQuery("SELECT id FROM book");
            ObservableList<String> bookids = cmbBookId.getItems();
            bookids.clear();
            while (rst.next()) {

                bookids.add(rst.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void loadBorrow() {

        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM borrow");
            ObservableList<BorrowTM> borrowTMS = tblBorrow.getItems();
            borrowTMS.clear();
            while (rst.next()) {
                Button del = new Button("DELETE");
                del.setStyle("-fx-background-color: blue");
                BorrowTM btm=new BorrowTM(rst.getString(1), rst.getDate(3), rst.getString(2), rst.getString(4), del);
                borrowTMS.add(btm);
                del.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {


                           // BorrowTM borrowTM=new BorrowTM(btm);
                        try {
                            deleteBorrow(btm.getBorrowId());
                            System.out.println(btm.getBorrowId());
                            tblBorrow.getSelectionModel().clearSelection();
                            borrowTMS.remove(btm);
                            borrowTMS.clear();
                            tblBorrow.refresh();
                            loadBorrow();
                            btnAddOnAction(event);
                            cmbBookId.setValue("");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }





                    }
                });

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //check whether the book is returned or not
    public boolean checkReturn(String bid) throws SQLException {

        System.out.println(bid + "1st");

        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM book_return where borrow_id=?");
        String borid = getBorrowId(bid);
        System.out.println("XXXX" + borid);
        pstm.setString(1, getBorrowId(bid));//get the borrow id of last record of the given bookid & check whether there is any
        ResultSet rs = pstm.executeQuery();//record available in book_return based on the borrowId
        //System.out.println("YYy"+rs.next());


        return rs.next();//return true if there is a record. but if you type rs.next(); agiain, it is false since there is only one record


    }

    //get the borrow id of last record of the given bookid & pass it to checkReturn()
    public String getBorrowId(String bid) throws SQLException {

        System.out.println(bid);
        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement("SELECT borrow_id from borrow  where book_id=? ORDER BY borrow_id DESC LIMIT 1");
        statement.setString(1, bid);
        ResultSet rst = statement.executeQuery();
        if (rst.next()) {
            String newborrowId = rst.getString(1);
            System.out.println(newborrowId);
            return newborrowId;
        } else {
            return null;
        }

    }

    //check whether the book has never borrowed or not
    public boolean checkBorrow(String bid) throws SQLException {


        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("SELECT book_id FROM borrow where book_id=?");
        pstm.setString(1, bid);
        ResultSet rs = pstm.executeQuery();
        System.out.println(rs + "hooooooo");
        if (rs.next()) {
            return false;
        } else {
            return true;
        }

    }


   public void deleteBorrow(String borId) throws SQLException {
       System.out.println(borId);
        PreparedStatement preparedStatement=DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM borrow WHERE borrow_id=?");
        preparedStatement.setString(1,borId);
        int rows=preparedStatement.executeUpdate();
       System.out.println(rows);
        if(rows>0){
           new Alert(Alert.AlertType.INFORMATION,"RECORD DELETED");
        }
        else {
            new Alert(Alert.AlertType.ERROR,"ERROR");
        }

        cmbBookId.setValue("");
   }

    public void homeClicked(MouseEvent mouseEvent) throws IOException {

        URL url=this.getClass().getResource("/view/Main.fxml");
        Parent parent= FXMLLoader.load(url);
        Scene scene=new Scene(parent);
        Stage stage= (Stage) this.borrowForm.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Student");
        stage.setFullScreen(false);
    }
}






