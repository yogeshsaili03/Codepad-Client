package code.pad.client;

import com.jfoenix.controls.JFXTextField;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Optional;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ClientHomeController implements Initializable {

    @FXML
    private JFXTextField txtfriend;

    public static String Username, Uname;

    @FXML
    private TableView<Friend> Friendable;

    @FXML
    private Label UserHello;

    @FXML
    private TableColumn<Friend, String> Name;

    @FXML
    private TableColumn<Friend, String> Emailid;
    
    @FXML
    private TableColumn<Friend, String> Status;
    ObservableList<Friend> names;

    @FXML
    void Online(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Online.fxml"));

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
                String L = dis.readUTF();
                if (L.equals("done")) {
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

    @FXML
    void FriendReq(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FriendRequest.fxml"));

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void Search(ActionEvent event) {
        if (txtfriend.getText().equals("")) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Empty TextField");
            alert.setHeaderText("Look,This Search Will Show You All Users...");
            alert.setContentText("Are you ok with this?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Search1();
            }
        } else {
            Search1();
        }
    }

    @FXML
    void ChangePass(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ChangePassword.fxml"));

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    private void Search1() {
        Socket cs;
        String Friend = txtfriend.getText();

        System.err.println(Friend);
        try {

            InetAddress iAddress = InetAddress.getLocalHost();
            String Ip = iAddress.getHostAddress();
            System.out.println("Client Ip " + Ip);
            cs = new Socket(Ip, 1234);
            System.out.println("Connected to Server");

            try {
                DataOutputStream dos = new DataOutputStream(cs.getOutputStream());
                DataInputStream dis = new DataInputStream(cs.getInputStream());
                dos.writeUTF("Search");
                dos.writeUTF(Username);
                dos.writeUTF(Friend);
                Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
                Emailid.setCellValueFactory(new PropertyValueFactory<>("Emailid"));
                Status.setCellValueFactory(new PropertyValueFactory<>("button"));
                names = FXCollections.observableArrayList();
                while (true) {
                    String friendname = dis.readUTF();
                    if (friendname.equals("Not Exist")) {
                        System.out.println("User Not Exist");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("User Not Exist.");

                        alert.showAndWait();
                        txtfriend.setText("");
                        txtfriend.requestFocus();

                    } else if (friendname.equals("Done")) {
                        System.out.println("Done");

                        System.out.println("abc");
                        break;
                    }
                    String friendemail = dis.readUTF();
                    String status = dis.readUTF();
                    Friend f = new Friend(friendname, friendemail, status);
                    names.add(f);
                    Button button = f.button;
                    {
                        button.setOnAction((ActionEvent event) -> {
                            if (status.equalsIgnoreCase("Send Friend Request")) {
                                String r = Req(Username, friendname);
                                if (r.equalsIgnoreCase("done")) {
                                    button.setText("Friend Request Sent");
                                }
                                if (r.equalsIgnoreCase("nope")) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Information Dialog");
                                    alert.setHeaderText("Technical Error!");
                                    alert.setContentText("Request Not Sent.");

                                    alert.showAndWait();
                                }
                            }

                            System.out.println("selectedData: " + friendname + "   " + friendemail + "  " + status);
                        });
                    }
                    Friendable.setItems(names);
                    System.out.println(friendname + "   " + friendemail + "  " + status);

                }

            } catch (IOException e) {
                System.out.println(e);
            }
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Technical Error");
            alert.setContentText("Server Down.");
            alert.showAndWait();
            System.out.println(e);
        }

    }

    private String Req(String from, String to) {
        Socket cs;
        try {
            System.out.println(from + " " + to);
            InetAddress iAddress = InetAddress.getLocalHost();
            String Ip = iAddress.getHostAddress();
            System.out.println("Client Ip " + Ip);
            cs = new Socket(Ip, 1234);
            System.out.println("Connected to Server");

            try {
                DataOutputStream dos = new DataOutputStream(cs.getOutputStream());
                DataInputStream dis = new DataInputStream(cs.getInputStream());
                dos.writeUTF("Req");
                dos.writeUTF(from);
                dos.writeUTF(to);
                String r = dis.readUTF();
                if (r.equals("done")) {
                    System.out.println("Request Sent");
                } else if (r.equals("nope")) {
                    System.out.println("Request Not Sent");
                    return "nope";
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return "done";
    }

    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {
        Username = FXMLDocumentController.user;
        Uname = FXMLDocumentController.name;
        System.out.println(Username + "  " + Uname);
        UserHello.setText(" " + Uname);
        FXMLDocumentController.A = "Change";

    }

}
