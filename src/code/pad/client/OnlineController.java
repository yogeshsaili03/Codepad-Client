/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code.pad.client;

import com.jfoenix.controls.JFXButton;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Yogesh
 */
public class OnlineController implements Initializable {

    @FXML
    private TableView<Online> Friend;
    @FXML
    private TableColumn<Online, String> Invite;
    @FXML
    private TableColumn<Online, String> onlinef;

    ObservableList<Online> O;
    @FXML
    private Label username;
    @FXML
    private Label Notice;
    @FXML
    private JFXButton Home;
    @FXML
    private Label Message;
    @FXML
    private JFXButton Logout;
    Tooltip H, L;

    @FXML
    void Home(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Client Home.fxml"));

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void Logout(ActionEvent event) throws IOException {
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
                dos.writeUTF("Logout");
                System.out.println("! " + ClientHomeController.Username);
                dos.writeUTF(ClientHomeController.Username);
                String Log = dis.readUTF();
                if (Log.equals("done")) {
                    Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    stage.setScene(scene);
                    stage.show();
                } else {
                    Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    stage.setScene(scene);
                    stage.show();
                }
            } catch (IOException e) {
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException e) {
            Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        H = new Tooltip();
        H.setText("Home");
        Home.setTooltip(H);
        L = new Tooltip();
        L.setText("Logout");
        Logout.setTooltip(L);
        username.setText(ClientHomeController.Uname);
        Notice.setVisible(false);
        Online();
    }

    private void Online() {
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
                dos.writeUTF("Online");
                System.out.println(ClientHomeController.Username);
                dos.writeUTF(ClientHomeController.Username);
                onlinef.setCellValueFactory(new PropertyValueFactory<>("Name"));
                Invite.setCellValueFactory(new PropertyValueFactory<>("button"));
                O = FXCollections.observableArrayList();
                while (true) {
                    String friend = dis.readUTF();
                    System.out.println(friend);

                    Message.setText("Click on invite to create Session With Friends...");
                    if (friend.equals("None")) {
                        Friend.setVisible(false);
                        Notice.setVisible(true);
                        Message.setVisible(false);
                        // Notice.setText("No Friend is Online now...");

                    } else if (friend.equals("Done")) {
                        System.out.println("Done");
                        System.out.println("abc");
                        break;
                    }
                    Online o = new Online(friend);
                    O.add(o);
                    Button invite = o.button;
                    {
                        invite.setOnAction((ActionEvent event) -> {

                            invite.setDisable(true);
                            String in = invite(ClientHomeController.Username, friend);
                            if (in.equals("done")) {
                                
                                invite.setText("Invitation Sent");
                                System.out.println("Invitation Sent to " + friend);
                            } 
                            else if (in.equals("rejected")) {
                                invite.setText("Invite");
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Not Accepted");
                                alert.setHeaderText("");
                                alert.setContentText("Invitation Rejected.");

                                alert.showAndWait();
                                invite.setDisable(false);
                            }else if (in.equals("offline")) {
                                invite.setText("Invite");
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Not Available");
                                alert.setHeaderText("Try Again Later");
                                alert.setContentText("User is offline or Server Down...");

                                alert.showAndWait();
                                invite.setDisable(false);
                            }

                        });
                        Friend.setItems(O);
                    }
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
    }

    private String invite(String Me, String You) {
        Socket cs;
        try {
            System.out.println(Me + " " + You);
            InetAddress iAddress = InetAddress.getLocalHost();
            String Ip = iAddress.getHostAddress();
            System.out.println("Client Ip " + Ip);
            cs = new Socket(Ip, 1234);
            System.err.println("Connected to Server");
            try {
                DataOutputStream dos = new DataOutputStream(cs.getOutputStream());
                DataInputStream dis = new DataInputStream(cs.getInputStream());
                dos.writeUTF("Invite");
                dos.writeUTF(Me);
                dos.writeUTF(You);
                String r = dis.readUTF();
                if (r.equals("done")) {
                    System.out.println("Invitation Sent");
                } else if (r.equals("offline")) {
                    System.out.println("Technical Error...");
                    return "offline";
                }
                if (r.equals("rejected")) {
                    System.out.println("Invitation Rejected");
                    return "rejected";
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return "done";
    }

}
