/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code.pad.client;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Yogesh
 */
public class ChangePasswordController implements Initializable {

    @FXML
    private JFXPasswordField HidePass;

    @FXML
    private JFXTextField ShowPass;

    @FXML
    private JFXPasswordField HideRe;

    @FXML
    private JFXTextField ShowRe;

    @FXML
    private Label User;
    @FXML
    private JFXButton Cancel;

    @FXML
    private JFXButton Back;

    @FXML
    void Back(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Client Home.fxml"));

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void Cancel(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void HideP(MouseEvent event) {
        HidePass.setVisible(true);
        HidePass.setText(ShowPass.getText());
        ShowPass.setVisible(false);
    }

    @FXML
    void HideP2(MouseEvent event) {
        HideRe.setVisible(true);
        HideRe.setText(ShowRe.getText());
        ShowRe.setVisible(false);
    }

    @FXML
    void ShowP(MouseEvent event) {
        ShowPass.setVisible(true);
        ShowPass.setText(HidePass.getText());
        HidePass.setVisible(false);
    }

    @FXML
    void ShowP2(MouseEvent event) {
        ShowRe.setVisible(true);
        ShowRe.setText(HideRe.getText());
        HideRe.setVisible(false);
    }

    @FXML
    void Submit(ActionEvent event) {
        if (HidePass.getText().isEmpty() || HideRe.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Password Fields Can't be Empty.");

            alert.showAndWait();

            HidePass.requestFocus();
        } else if (HidePass.getText().equals(HideRe.getText())) {
            System.out.println("True");
            Socket cs = null;

            try {

                cs = new Socket("127.0.0.1", 1234);
                System.out.println("Connected to Server");

                try {

                    DataOutputStream dos = new DataOutputStream(cs.getOutputStream());
                    DataInputStream dis = new DataInputStream(cs.getInputStream());
                    dos.writeUTF("ChangePass");

                    dos.writeUTF(User.getText());
                    dos.writeUTF(HidePass.getText());
                    String set = dis.readUTF();

                    if (set.equals("Set")) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText("");
                        alert.setContentText("Password Changed Successfully.");

                        alert.showAndWait();
                        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

                        Scene scene = new Scene(root);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                        stage.setScene(scene);
                        stage.show();

                    } else if (set.equals("notset")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText("Try Later");
                        alert.setContentText("Server Down...");

                        alert.showAndWait();

                    }
                } catch (IOException e) {
                    System.out.println(e);
                }

            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Try Later");
                alert.setContentText("Server Down...");

                alert.showAndWait();
                System.out.println(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Check Password");
            alert.setContentText("Passwords Not Matched.");

            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {
        if (FXMLDocumentController.A.equals("Recover")) {
            User.setText(ForgotPasswordController.user.toUpperCase());
            Back.setVisible(false);
        } else if (FXMLDocumentController.A.equals("Change")) {

            User.setText(ClientHomeController.Username.toUpperCase());
            Cancel.setVisible(false);
        }
        ShowPass.setVisible(false);
        ShowRe.setVisible(false);

    }

}
