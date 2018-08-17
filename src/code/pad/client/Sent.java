/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code.pad.client;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

/**
 *
 * @author Yogesh
 */
public class Sent {

    private final SimpleStringProperty Username;
    public Button C;

    public Sent(String Username) {
        this.Username = new SimpleStringProperty(Username);
        this.C = new Button("Cancel");
    }

    public String getUsername() {
        return Username.get();
    }

    public void setC(Button C) {
        this.C = C;

    }

    public Button getC() {

        return C;
    }
}
