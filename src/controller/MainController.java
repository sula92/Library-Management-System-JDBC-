package controller;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public ImageView imgCus;
    public ImageView imgBook;
    public AnchorPane MainForm;
    public ImageView imgBorrow;
    public ImageView imgReturn;
    public Label lab1;
    public Label lblDescription;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lab1.setText("ගමේ පුස්තකාලය");

    }

    public void imgCusClick(MouseEvent mouseEvent) {
    }

    public void imgCusMove(MouseEvent mouseEvent) {
    }

    public void imgClick(MouseEvent mouseEvent) {
    }

    public void imgBookMove(MouseEvent mouseEvent) {
    }

    @FXML
    private void navigate(MouseEvent event) throws IOException {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            Parent root = null;

            FXMLLoader fxmlLoader = null;
            switch (icon.getId()) {
                case "imgCus":
                    root = FXMLLoader.load(this.getClass().getResource("/view/Member.fxml"));
                    break;
                case "imgBook":
                    root = FXMLLoader.load(this.getClass().getResource("/view/Book.fxml"));
                    break;
                case "imgBorrow":
                    root = FXMLLoader.load(this.getClass().getResource("/view/Borrow.fxml"));
                    break;
                case "imgReturn":
                    fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/Return.fxml"));
                    root = fxmlLoader.load();
                    break;

            }



            if (root != null) {
                Scene subScene = new Scene(root);
                Stage primaryStage = (Stage) this.MainForm.getScene().getWindow();

                primaryStage.setScene(subScene);
                primaryStage.centerOnScreen();

                TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
                tt.setFromX(-subScene.getWidth());
                tt.setToX(0);
                tt.play();

            }
        }
    }

    public void img_mouseEnterence(MouseEvent mouseEvent) {
        ImageView icon = (ImageView)mouseEvent.getSource();
        ScaleTransition scaleT = new ScaleTransition(Duration.millis(200.0D), icon);
        scaleT.setToX(1.2D);
        scaleT.setToY(1.2D);
        scaleT.play();
        DropShadow glow = new DropShadow();
        glow.setColor(Color.RED);
        glow.setWidth(20.0D);
        glow.setHeight(20.0D);
        glow.setRadius(40.0D);
        icon.setEffect(glow);

        String cat =icon.getId();
       switch (cat){
            case "imgCus":
                this.lab1.setText("Manage Members");
                this.lblDescription.setText("Click to add, update or delete Members");
                break;

            case "imgBook":
                this.lab1.setText("Manage Books");
                this.lblDescription.setText("Click to add, update or delete Books");
                break;

            case "imgBorrow":
                this.lab1.setText("Manage Borrowings");
                this.lblDescription.setText("Click to Add, edit, delete Borrowings");
                break;

            case "imgReturn":
                this.lab1.setText("Manage Returnings");
                this.lblDescription.setText("Click to Manage Lecture Details Or Search Returnings");
                break;

        }

    }



    public void img_mouseExit(MouseEvent mouseEvent) {
        ImageView icon = (ImageView) mouseEvent.getSource();
        ScaleTransition scaleT = new ScaleTransition(Duration.millis(200.0D),icon);
        scaleT.setToX(1D);
        scaleT.setToY(1D);
        scaleT.play();
        icon.setEffect(null);
        DropShadow glow = new DropShadow();
        glow.setColor(Color.BLUE);
        glow.setWidth(20.0D);
        glow.setHeight(20.0D);
        glow.setRadius(40.0D);
        icon.setEffect(glow);
       /* lblMenu.setText("");
        lblDescription.setText("");*/
        lab1.setText("ගමේ පුස්තකාලය");
        lblDescription.setText("");

    }


}
