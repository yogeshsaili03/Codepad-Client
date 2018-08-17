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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class FriendRequestController implements Initializable {

    @FXML
    private Label userf;
    @FXML
    private Label username;
    @FXML
    private TableView<Request> RequestTable;

    @FXML
    private TableColumn<Request, String> User;

    @FXML
    private TableColumn<Request, String> Accept;
    @FXML
    private Label SR;
    @FXML
    private TableColumn<Request, String> Delete;

    @FXML
    private TableView<Sent> SentReq;

    @FXML
    private TableColumn<Sent, String> Username;

    @FXML
    private TableColumn<Sent, String> Cancel;

    @FXML
    private JFXButton ViewReceived;
    @FXML
    private JFXButton ViewSent;
    ObservableList<Request> U;
    ObservableList<Sent> Sent;

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
    void ViewReceived(ActionEvent event) {
        RequestTable.setVisible(true);
        userf.setVisible(false);
        SentReq.setVisible(false);
        ViewSent.setVisible(true);
        ViewReceived.setVisible(false);
        Request();
    }

    @FXML
    void ClientHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Client Home.fxml"));

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Request();
        SentReq.setVisible(false);
        ViewReceived.setVisible(false);

    }

    @FXML
    void ViewSent(ActionEvent event) {
        RequestTable.setVisible(false);
        userf.setVisible(false);
        SentReq.setVisible(true);
        ViewReceived.setVisible(true);
        Sent();

    }

    private void Sent() {
        username.setText(ClientHomeController.Uname);
        ViewSent.setVisible(false);
        SR.setText("Sent");
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
                dos.writeUTF("SentReq");
                System.out.println(ClientHomeController.Username);
                dos.writeUTF(ClientHomeController.Username);
                Username.setCellValueFactory(new PropertyValueFactory<>("Username"));
                Cancel.setCellValueFactory(new PropertyValueFactory<>("C"));
                Sent = FXCollections.observableArrayList();
                while (true) {
                    String friendname = dis.readUTF();
                    if (friendname.equals("No Request")) {
                        RequestTable.setVisible(false);
                        SentReq.setVisible(false);
                        userf.setVisible(true);
                        userf.setText("No Sent Requests");

                    } else if (friendname.equals("Done")) {
                        System.out.println("Done");
                        System.out.println("abc");
                        break;
                    }
                    Sent S = new Sent(friendname.toUpperCase());
                    Sent.add(S);
                    Button Can = S.C;
                    {
                        Can.setOnAction((ActionEvent event) -> {
                            System.out.println(ClientHomeController.Username + " " + friendname);

                            String D = Cancel(ClientHomeController.Username, friendname);
                            if (D.equalsIgnoreCase("done")) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Information Dialog");
                                alert.setHeaderText(null);
                                alert.setContentText("Request Cancelled Successfully...");

                                alert.showAndWait();
                            } else if (D.equalsIgnoreCase("nope")) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Information Dialog");
                                alert.setHeaderText("Try Again");
                                alert.setContentText("Technical Error Occur.");

                                alert.showAndWait();

                            }
                            Sent();
                        });
                    }
                    SentReq.setItems(Sent);
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

    private void Request() {
        username.setText(ClientHomeController.Uname);
        SR.setText("Received");
        Socket cs;
        try {
            InetAddress iAddress = InetAddress.getLocalHost();
            String Ip = iAddress.getHostAddress();
            System.out.println("Client Ip " + Ip);
            cs = new Socket(Ip, 1234);
            System.out.println("Connected to Server Received Req.");
            try {
                DataOutputStream dos = new DataOutputStream(cs.getOutputStream());
                DataInputStream dis = new DataInputStream(cs.getInputStream());
                dos.writeUTF("FriReq");
                System.out.println(ClientHomeController.Username);
                dos.writeUTF(ClientHomeController.Username);
                User.setCellValueFactory(new PropertyValueFactory<>("User"));
                Accept.setCellValueFactory(new PropertyValueFactory<>("A"));
                Delete.setCellValueFactory(new PropertyValueFactory<>("D"));
                U = FXCollections.observableArrayList();
                while (true) {
                    String friendname = dis.readUTF();
                    if (friendname.equals("No Request")) {
                        RequestTable.setVisible(false);
                        SentReq.setVisible(false);
                        userf.setVisible(true);
                        userf.setText("No Pending Requests");

                    } else if (friendname.equals("Done")) {
                        System.out.println("Done");
                        System.out.println("abc");
                        break;
                    }
                    Request R = new Request(friendname.toUpperCase());
                    U.add(R);
                    Button Acc = R.A;
                    {
                        Acc.setOnAction((ActionEvent event) -> {
                            System.out.println(friendname + " " + ClientHomeController.Username);

                            String D = Accept(friendname, ClientHomeController.Username);
                            if (D.equalsIgnoreCase("done")) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Information Dialog");
                                alert.setHeaderText(null);
                                alert.setContentText("Request Accepted Successfully...");

                                alert.showAndWait();
                            } else if (D.equalsIgnoreCase("nope")) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Information Dialog");
                                alert.setHeaderText("Try Again");
                                alert.setContentText("Technical Error Occur.");

                                alert.showAndWait();

                            }
                            Request();
                        });
                    }
                    Button Del = R.D;
                    {
                        Del.setOnAction((ActionEvent event) -> {
                            System.out.println(friendname);
                            String D = Delete(friendname, ClientHomeController.Username);
                            if (D.equalsIgnoreCase("done")) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Information Dialog");
                                alert.setHeaderText(null);
                                alert.setContentText("Request Deleted Successfully...");

                                alert.showAndWait();
                            } else if (D.equalsIgnoreCase("nope")) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Information Dialog");
                                alert.setHeaderText("Try Again");
                                alert.setContentText("Technical Error Occur.");

                                alert.showAndWait();

                            }
                            Request();
                        });
                    }
                    RequestTable.setItems(U);
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

    private String Accept(String from, String to) {
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
                dos.writeUTF("Accept");
                dos.writeUTF(from);
                dos.writeUTF(to);
                String r = dis.readUTF();
                if (r.equals("Done")) {
                    System.out.println("Request Accepted");
                } else if (r.equals("nope")) {
                    System.out.println("Technical Error...");
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

    private String Cancel(String from, String to) {
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
                dos.writeUTF("Delete");
                dos.writeUTF(from);
                dos.writeUTF(to);
                String r = dis.readUTF();
                if (r.equals("Done")) {
                    System.out.println("Request Cancelled");
                } else if (r.equals("nope")) {
                    System.out.println("Technical Error...");
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

    private String Delete(String from, String to) {
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
                dos.writeUTF("Delete");
                dos.writeUTF(from);
                dos.writeUTF(to);
                String r = dis.readUTF();
                if (r.equals("Done")) {
                    System.out.println("Request Deleted");
                } else if (r.equals("nope")) {
                    System.out.println("Technical Error...");
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
}
