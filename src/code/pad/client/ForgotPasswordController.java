/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code.pad.client;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
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
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class ForgotPasswordController implements Initializable {

    @FXML
    private JFXTextField username;

    @FXML
    private Label lbl;

    @FXML
    private JFXRadioButton Email;

    @FXML
    private ToggleGroup mygroup;

    @FXML
    private ProgressIndicator Progress;

    @FXML
    private JFXRadioButton SMS;
    public static String EmailPass, OTP, user;

    @FXML
    void Forgot(ActionEvent event) throws InterruptedException {
        Progress.setVisible(true);
        if (username.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Your Username");
            alert.showAndWait();
            Progress.setVisible(false);
            username.requestFocus();
        } else if (!(Email.isSelected()) && !(SMS.isSelected())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please Select Recovery Option");
            alert.showAndWait();
            Progress.setVisible(false);
            Email.requestFocus();
        } else if ((Email.isSelected()) && (SMS.isSelected())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Both Recovery Option Can't Be Selected");
            Progress.setVisible(false);
            alert.showAndWait();
            Email.requestFocus();
        } else if (Email.isSelected()) {
            Socket cs;

            try {

                InetAddress iAddress = InetAddress.getLocalHost();
                String Ip = iAddress.getHostAddress();
                System.out.println("Client Ip " + Ip);
                cs = new Socket(Ip, 1234);
                System.out.println("Connected to Server");

                try {

                    DataOutputStream dos = new DataOutputStream(cs.getOutputStream());
                    DataInputStream dis = new DataInputStream(cs.getInputStream());
                    dos.writeUTF("Forgot1");
                    dos.writeUTF(username.getText());

                    String send = dis.readUTF();

                    if (send.equals("send")) {
                        OTP = dis.readUTF();
                        System.out.println(OTP);
                        EmailPass = "One Time Password Sent On Registered Email Id";
                        user = username.getText();
                        Parent root = FXMLLoader.load(getClass().getResource("RecoverPass.fxml"));
                        Scene scene = new Scene(root);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                    } else if (send.equals("notsent")) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText("Try Later");
                        alert.setContentText("Server Down...");
                        alert.showAndWait();
                        Progress.setVisible(false);
                        lbl.setText("Email not Sent");
                    } else if (send.equals("notexist")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText("Username Not Existed");
                        alert.setContentText("Check Your Username Again");
                        alert.showAndWait();
                        Progress.setVisible(false);
                        lbl.setText("Check Your Username Again...");
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }

            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Technical Error");
                alert.setContentText("Server Down.");
                alert.showAndWait();
                System.out.println(e);
            }
        } else if (SMS.isSelected()) {
            Socket cs = null;

            try {

                InetAddress iAddress = InetAddress.getLocalHost();
                String Ip = iAddress.getHostAddress();
                System.out.println("Client Ip " + Ip);
                cs = new Socket(Ip, 1234);
                System.out.println("Connected to Server");

                try {
                    DataOutputStream dos = new DataOutputStream(cs.getOutputStream());
                    DataInputStream dis = new DataInputStream(cs.getInputStream());
                    dos.writeUTF("Forgot2");
                    dos.writeUTF(username.getText());
                    String sendsms = dis.readUTF();

                    if (sendsms.equals("sendsms")) {
                        OTP = dis.readUTF();

                        System.out.println(OTP);
                        EmailPass = "One Time Password Sent On Registered Number";
                        user = username.getText();
                        Parent root = FXMLLoader.load(getClass().getResource("RecoverPass.fxml"));

                        Scene scene = new Scene(root);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                        stage.setScene(scene);
                        stage.show();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText("Check Your Inbox");
                        alert.setContentText("Password Sent On Registered Mobile Number");

                        alert.showAndWait();

                    } else if (sendsms.equals("notsent")) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText("Try Later");
                        alert.setContentText("Server Down...");

                        alert.showAndWait();
                        Progress.setVisible(false);
                        lbl.setText("SMS not Sent");
                    } else if (sendsms.equals("notexist")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText("Username Not Existed");
                        alert.setContentText("Check Your Username Again");

                        alert.showAndWait();
                        Progress.setVisible(false);
                        lbl.setText("Check Your Username Again...");
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }

            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Technical Error");
                alert.setContentText("Server Down.");

                alert.showAndWait();
                System.out.println(e);
            }

        }
    }

    @FXML
    void cancel(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lbl.setText("Enter Your Username");
        Progress.setVisible(false);
    }

}
