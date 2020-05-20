package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entity.Return;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.ReturnTM;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.ResourceBundle;

public class ReturnController implements Initializable {

    public FontAwesomeIconView faHome;
    public AnchorPane returnForm;
    @FXML
    private JFXComboBox<String> cmbBorrowId;

    @FXML
    private JFXDatePicker dateReturned;

    @FXML
    private Button btnReturn;

    @FXML
    private TableView<ReturnTM> tblReturn;

    @FXML
    private TableColumn colBorrowId;

    @FXML
    private TableColumn colBorrowDate;

    @FXML
    private TableColumn colReturneDate;

    @FXML
    private TableColumn colFine;

    @FXML
    private TableColumn colAmount;

    @FXML
    private JFXTextField txtSearch;


    @Override
    public void initialize(URL location, ResourceBundle resources) {



        colBorrowId.setCellValueFactory(new PropertyValueFactory<>("borrowId"));
        colBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        colReturneDate.setCellValueFactory(new PropertyValueFactory<>("returnedDate"));
        colFine.setCellValueFactory(new PropertyValueFactory<>("fine"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        loadTableData();
        try {
            loadBorrowIds();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tblReturn.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            String bid=newValue.getBorrowId();
            Date date=newValue.getReturnedDate();
            btnReturn.setText("UPDATE");
            cmbBorrowId.setValue(bid);


        });

    }

    private void loadBorrowIds() throws SQLException {

        ObservableList<String> bids=cmbBorrowId.getItems();

        PreparedStatement preparedStatement=DBConnection.getInstance().getConnection().prepareStatement("SELECT borrow_id FROM borrow\n" +
                "WHERE borrow_id NOT IN (SELECT borrow_id FROM book_return);");
        ResultSet rst=preparedStatement.executeQuery();
        while (rst.next()){
            bids.add(rst.getString(1));

        }



    }


    public void loadTableData() {
        System.out.println("xxxxxxxxxx");

        try {
            PreparedStatement preparedStatement= DBConnection.getInstance().getConnection().prepareStatement("SELECT B.borrow_id, B.date, R.date, F.amount FROM ((borrow B INNER JOIN book_return R ON B.borrow_id = R.borrow_id) INNER JOIN fee F ON R.borrow_id = F.borrow_id)");
            ResultSet rst=preparedStatement.executeQuery();
            ObservableList<ReturnTM> returnTMS=tblReturn.getItems();
            returnTMS.clear();
            while (rst.next()){
                String bID=rst.getString(1);
                Date bdate=rst.getDate(2);
                Date rdate=rst.getDate(3);
                double amount=rst.getDouble(4);

                System.out.println(bID);
                System.out.println(bdate);

                String fine;
                if(amount>0.0){
                    fine="YES";
                }
                else {
                    fine="NO";
                }
                ReturnTM returnTM=new ReturnTM(bID,bdate,rdate,fine,amount);

                returnTMS.add(returnTM);
                //tblReturn.setItems(returnTMS);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void btnReturnOnAction(ActionEvent event) throws SQLException {

        String bid=cmbBorrowId.getValue();
        java.sql.Date date= java.sql.Date.valueOf(dateReturned.getValue());

        Return aReturn=new Return(bid,date);

        if (btnReturn.getText().equalsIgnoreCase("returned")) {


            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("insert into book_return values(?,?)");
            preparedStatement.setString(1, aReturn.getBorrowId());
            preparedStatement.setDate(2, aReturn.getReturnedDate());
            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                new Alert(Alert.AlertType.INFORMATION, "RECORD ADDED SUCCESSFULLY").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "AN ERROR").show();
            }

            if (rows > 0) {

                PreparedStatement preparedStatement2 = DBConnection.getInstance().getConnection().prepareStatement("insert into fee values(?,?)");
                preparedStatement2.setString(1, aReturn.getBorrowId());
                preparedStatement2.setInt(2, getFine(bid));
                int rows2 = preparedStatement2.executeUpdate();
                if (rows2 > 0) {
                    new Alert(Alert.AlertType.INFORMATION, "RECORD ADDED SUCCESSFULLY").show();

                } else {
                    new Alert(Alert.AlertType.ERROR, "AN ERROR").show();
                }
            } else {
                PreparedStatement preparedStatement3 = DBConnection.getInstance().getConnection().prepareStatement("update book_return set date=? where borrow_id=?");
                preparedStatement3.setDate(1, date);
                preparedStatement3.setString(2, bid);
                int rows3 = preparedStatement3.executeUpdate();
                if (rows3 > 0) {
                    new Alert(Alert.AlertType.INFORMATION, "RECORD UPDATED SUCCESSFULLY").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "AN ERROR").show();
                }

            }
        }

        ObservableList<ReturnTM> returnTMS=tblReturn.getItems();
        returnTMS.clear();
        tblReturn.refresh();
        loadTableData();

        ObservableList<String> bids=cmbBorrowId.getItems();
        bids.clear();
        loadBorrowIds();


    }

    private int getFine(String bid) throws SQLException {

        System.out.println("aaaaaa"+bid);


            LocalDate date2;

            PreparedStatement preparedStatement=DBConnection.getInstance().getConnection().prepareStatement("select date FROM borrow where borrow_id=?");
            preparedStatement.setString(1,bid);

            ResultSet rst=preparedStatement.executeQuery();
            rst.next();
            Date d=rst.getDate(1);
            System.out.println(d);
            //LocalDate date1=LocalDate.parse((CharSequence) rst.getDate(1));
              LocalDate date1= ((java.sql.Date) d).toLocalDate();
            System.out.println(date1);
            date2=dateReturned.getValue();

            int duration = (int) ChronoUnit.DAYS.between(date1, date2);
            int fee=duration*10;
            System.out.println("ccccccc"+duration);
            System.out.println("dddddd"+fee);

            return fee;
    }

    public void homeOnClicked(MouseEvent mouseEvent) throws IOException {

        URL url=this.getClass().getResource("/view/Main.fxml");
        Parent parent= FXMLLoader.load(url);
        Scene scene=new Scene(parent);
        Stage stage= (Stage) this.returnForm.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Student");
        stage.setFullScreen(false);
    }
}
