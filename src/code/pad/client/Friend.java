package code.pad.client;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

public class Friend {

    private final SimpleStringProperty Name, Emailid, Status;
    public Button button;

    public Friend(String Name, String Emailid, String Status) {
        this.Name = new SimpleStringProperty(Name);
        this.Emailid = new SimpleStringProperty(Emailid);
        this.Status = new SimpleStringProperty(Status);
        button = new Button(Status);
    }

    public String getName() {
        return Name.get();
    }

    public String getEmailid() {
        return Emailid.get();
    }

    public String getStatus() {
        return Status.get();
    }

    public void setButton(Button button) {
        this.button = button;

    }

    public Button getButton() {

        return button;
    }

}
