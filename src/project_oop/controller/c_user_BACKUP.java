package project_oop.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import project_oop.view.login;
import project_oop.model.m_login;

public class c_user_BACKUP {

    private m_login model;
    private login v_lg_objct;
    private String username;
    private String password;

    public c_user_BACKUP() throws SQLException {
        v_lg_objct = new login();
        v_lg_objct.setVisible(true);
        v_lg_objct.login_btn().addActionListener(new btn_login_listener());
        model = new m_login(); // Initialize model
    }

    private class btn_login_listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            username = v_lg_objct.get_username().getText();
            password = v_lg_objct.get_password().getText();

            try {
                // Menggunakan method model untuk mengecek login
                model.get_login_personal(username, password);

                // Mengecek apakah data yang dikembalikan valid atau tidak
                if (model.rows.isEmpty()) {
                    v_lg_objct.label_info().setText("Email atau Password kamu salah!");
                } else {
                    // Mengecek apakah ID atau username sesuai dengan hasil yang valid
                    Object[] user = model.rows.get(0);  // Mengambil baris pertama
                    String message = (String) user[0]; // "pesan" dari DB
                    int id_personal = (int) user[1];   // "id_personal" dari DB

                    // Jika pesan = "Login Gagal" atau id_personal == 0, berarti login gagal
                    if (message.equals("Login Gagal") || id_personal == 0) {
                        v_lg_objct.label_info().setText("Email atau Password kamu salah!");
                    } else {
                        // Jika login berhasil
                        new c_daftarMenu();
                        v_lg_objct.setVisible(false);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(c_user_BACKUP.class.getName()).log(Level.SEVERE, null, ex);
                v_lg_objct.label_info().setText("Terjadi kesalahan saat koneksi ke database.");
            }
        }
    }
}
