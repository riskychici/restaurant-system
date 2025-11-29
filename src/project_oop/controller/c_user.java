/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_oop.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import project_oop.view.login;
//import sun.security.util.Password;

/**
 *
 * @author Lorem Ipsum25
 */
public class c_user {

    private login v_lg_objct;
    private String username;
    private String password;

    public c_user() {
        v_lg_objct = new login();
        v_lg_objct.setVisible(true);
        v_lg_objct.login_btn().addActionListener(new btn_login_listener());
    }

    private class btn_login_listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            username = v_lg_objct.get_username().getText();
            password = v_lg_objct.get_password().getText();
            if (username.equalsIgnoreCase("riskychici") && password.equalsIgnoreCase("admin")) {
                try {
                    new c_daftarMenu();
                    v_lg_objct.setVisible(false);
                } catch (SQLException ex) {
                    Logger.getLogger(c_user.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                v_lg_objct.label_info().setText("Password kamu salah");
            }
        }

    }

}
