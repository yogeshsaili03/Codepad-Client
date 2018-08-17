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
public class Request {

    private final SimpleStringProperty User;
    public Button A, D;

    public Request(String User) {
        this.User = new SimpleStringProperty(User);
        this.A = new Button("Accept");
        this.D = new Button("Delete");
    }

    public String getUser() {
        return User.get();
    }

    public void setD(Button D) {
        this.D = D;

    }

    public Button getD() {

        return D;
    }

    public void setA(Button A) {
        this.A = A;

    }

    public Button getA() {

        return A;
    }
}
