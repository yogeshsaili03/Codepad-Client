/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code.pad.client;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class RecoverPassController implements Initializable {

    @FXML
    private Label Banner;

    @FXML
    private Label error;

    @FXML
    private JFXTextField OneTP;
    @FXML
    void Submit(ActionEvent event) throws IOException {

        if ((ForgotPasswordController.OTP.equals(OneTP.getText()))) {
            System.out.println("YES");
            Parent root = FXMLLoader.load(getClass().getResource("ChangePassword.fxml"));

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.show();

        } else {
            Banner.setVisible(false);
            error.setVisible(true);
        }

    }

    @FXML
    void Cancel(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Banner.setText(ForgotPasswordController.EmailPass);
        error.setVisible(false);
        FXMLDocumentController.A="Recover";
    }

}
