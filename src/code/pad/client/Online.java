package code.pad.client;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

public class Online {

    private final SimpleStringProperty Name;
    public Button button;
    public Online(String Name) {
        this.Name = new SimpleStringProperty(Name);
        button = new Button("Invite");
    }

    public String getName() {
        return Name.get();
    }
    public void setButton(Button button) {
        this.button = button;

    }

    public Button getButton() {

        return button;
    }
}
