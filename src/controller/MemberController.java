package controller;

import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.BookTM;
import util.MemberTM;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


public class MemberController implements Initializable {

    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtCantact;
    public JFXTextField txtAddress;
    public Button btnSave;
    public Button btnAdd;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colContact;
    public TableColumn colAddress;
    public TableView<MemberTM> tblMembers;

     //ArrayList<MemberTM> memberDB = new ArrayList<>();


    public TableColumn colBtn;
    public FontAwesomeIconView faHome;
    public AnchorPane memberForm;
    public ImageView iv1;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colBtn.setCellValueFactory(new PropertyValueFactory<>("button"));

        btnAdd.setStyle("-fx-background-color:blue");
        btnSave.setStyle("-fx-background-color: blue");

        txtName.setStyle("-fx-prompt-text-fill: white");
        txtId.setStyle("-fx-prompt-text-fill: white");
        txtAddress.setStyle("-fx-prompt-text-fill: white");
        txtCantact.setStyle("-fx-prompt-text-fill: white");

        txtId.setStyle("-fx-text-fill: white");
        txtName.setStyle("-fx-text-fill: white");
        txtAddress.setStyle("-fx-text-fill: white");
        txtCantact.setStyle("-fx-text-fill: white");

        loadMembers();

        tblMembers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MemberTM>() {
            @Override
            public void changed(ObservableValue<? extends MemberTM> observable, MemberTM oldValue, MemberTM newValue) {
                if (newValue == null) {
                    txtName.clear();
                    txtCantact.clear();;
                    txtAddress.clear();
                    txtId.clear();
                    return;
                }
                txtId.setText(newValue.getId());
                txtName.setText(newValue.getName());
                txtCantact.setText(newValue.getContact());
                txtAddress.setText(newValue.getAddress());

                btnSave.setText("UPDATE");

                String x="cusImages/";
                String y=newValue.getId();
                String z=".jpg";
                String xyz=x.concat(y).concat(z);

                Image img=new Image(xyz);
                iv1.setImage(img);
                iv1.setFitHeight(200.0);
                iv1.setFitWidth(300.0);

            }
        });

    }



    public void loadMembers(){

        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM member");
            ObservableList<MemberTM> memberDB = tblMembers.getItems();
            memberDB.clear();
            while (rst.next()) {
                Button del=new Button("DELETE");
                del.setStyle("-fx-background-color: red");

                String id = rst.getString(1);
                String nm = rst.getString(2);
                String con = rst.getString(3);
                String add = rst.getString(4);
                memberDB.add(new MemberTM(id,nm, con, add,del));

                del.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        MemberTM memberTM=new MemberTM(id,nm, con, add,del);
                        btnDelOnAction(memberTM.getId());
                        tblMembers.getSelectionModel().clearSelection();
                        memberDB.remove(memberTM);
                        memberDB.clear();
                        tblMembers.refresh();
                        loadMembers();

                    }
                });

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /*ObservableList<MemberTM> memberTMS= FXCollections.observableList(memberDB);
        tblMembers.setItems(memberTMS);*/

    }

    public void btnSaveOnAction(ActionEvent event) {

        if (txtId.getText().trim().isEmpty() ||
                txtName.getText().trim().isEmpty() ||
                txtCantact.getText().trim().isEmpty()||
                txtAddress.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Description, Qty. on Hand or Unit Price can't be empty").show();
            return;
        }

        String id = txtId.getText().trim();
        String nm = txtName.getText().trim();
        String con = txtCantact.getText().trim();
        String add =txtAddress.getText().trim();



        if (btnSave.getText().equalsIgnoreCase("Save")) {

            try {
                PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO member VALUES (?,?,?,?)");
                pstm.setObject(1, txtId.getText());
                pstm.setObject(2, txtCantact.getText());
                pstm.setObject(3, con);
                pstm.setObject(4, add);
                if (pstm.executeUpdate() == 0) {
                    new Alert(Alert.AlertType.ERROR, "Failed to save the member", ButtonType.OK).show();
                }
                else {
                    addImage(txtId.getText());
                    loadMembers();

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            btnAddOnActio(event);
        } else {
            MemberTM selectedItem = tblMembers.getSelectionModel().getSelectedItem();

            try {
                PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("UPDATE member SET name=?, contact=?, address=? WHERE id=?");
                pstm.setObject(1, nm);
                pstm.setObject(2, con);
                pstm.setObject(3, add);
                pstm.setObject(4, selectedItem.getId());
                if (pstm.executeUpdate() == 0) {
                    new Alert(Alert.AlertType.ERROR, "Failed to update the Member").show();
                }
                else {
                    new Alert(Alert.AlertType.INFORMATION, "Updated Successfully!").show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


            tblMembers.refresh();
            //loadMembers();
            btnAddOnActio(event);
        }
    }

    public void btnAddOnActio(ActionEvent event) {

        txtName.clear();
        txtCantact.clear();
        txtAddress.clear();

        tblMembers.getSelectionModel().clearSelection();
        txtName.setDisable(false);
        txtCantact.setDisable(false);
        txtAddress.setDisable(false);
        txtName.requestFocus();
        btnSave.setDisable(false);
        btnSave.setText("Save");

        // Generate a new id
        int maxCode = 0;
        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet rst = stm.executeQuery("SELECT id FROM member ORDER BY id DESC LIMIT 1");
            if (rst.next()) {
                maxCode = Integer.parseInt(rst.getString(1).replace("M", ""));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        maxCode = maxCode + 1;
        String code = "";
        if (maxCode < 10) {
            code = "M00" + maxCode;
        } else if (maxCode < 100) {
            code = "M0" + maxCode;
        } else {
            code = "M" + maxCode;
        }
        txtId.setText(code);

    }

    public void btnDelOnAction(String mid) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure whether you want to delete this member?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.YES) {
            MemberTM selectedItem = tblMembers.getSelectionModel().getSelectedItem();
            try {
                PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM member WHERE id=?");
                pstm.setObject(1, mid);
                if (pstm.executeUpdate() == 0) {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete the member", ButtonType.OK).show();
                } else {
                    tblMembers.getItems().remove(selectedItem);
                    tblMembers.getSelectionModel().clearSelection();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void addImage(String mid){

        JFileChooser fileChooser=new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter=new FileNameExtensionFilter("*.IMAGE","jpg","gif","png");
        fileChooser.addChoosableFileFilter(filter);
        int result=fileChooser.showSaveDialog(null);
        if(result==JFileChooser.APPROVE_OPTION){
            File selectedFile=fileChooser.getSelectedFile();
            String path=selectedFile.getAbsolutePath();

            int width = 963;    //width of the image
            int height = 640;   //height of the image
            BufferedImage image = null;
            File f = null;

            //read image
            try{
                f = new File(path); //image file path
                image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                image = ImageIO.read(f);
                System.out.println("Reading complete.");
            }catch(IOException e){
                System.out.println("Error: "+e);
            }

            //String corId=getImageId(mid);


            String s="D:\\IT\\DEP5\\library\\src\\cusImages\\";
            String id=mid;
            String ext=".jpg";
            String imgpath=s.concat(id).concat(ext);
            System.out.println(imgpath);

            //write image
            try{
                f = new File(imgpath);  //output file path
                ImageIO.write(image, "jpg", f);
                System.out.println("Writing complete.");
            }catch(IOException e){
                System.out.println("Error: "+e);
            }

            System.out.println(path);

        }
        else if(result== JFileChooser.CANCEL_OPTION){
            System.out.println("No Data");
        }

    }

    public void homeOnClicked(MouseEvent mouseEvent) throws IOException {

        URL url=this.getClass().getResource("/view/Main.fxml");
        Parent parent= FXMLLoader.load(url);
        Scene scene=new Scene(parent);
        Stage stage= (Stage) this.memberForm.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Student");
        stage.setFullScreen(false);
    }

    public void btnImgOnAction(ActionEvent event) {
    }

    public String getImageId(String mid){
        String corId;
        Integer correctid= Integer.valueOf(mid.replace("M",""));
        if(correctid<10) {
            corId = "M00".concat(String.valueOf(correctid));
            return corId;
        }
        else if(correctid>=10 && correctid<100){
            corId="M0".concat(String.valueOf(correctid));
            return corId;
        }
        else {
            corId="M".concat(String.valueOf(correctid));
            return corId;
        }

    }
}
