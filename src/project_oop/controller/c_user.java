package project_oop.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import project_oop.model.m_login;
import project_oop.view.login;

/**
 * Controller untuk menangani autentikasi user
 */
public class c_user {

    // ========== ATRIBUT ==========
    private static final Logger LOGGER = Logger.getLogger(c_user.class.getName());

    private final m_login model;
    private final login view;

    // ========== CONSTRUCTOR ==========
    public c_user() throws SQLException {
        this.model = new m_login();
        this.view = new login();

        tampilkanView();
        pasangEventListeners();
    }

    // ========== METHOD INISIALISASI ==========
    private void tampilkanView() {
        view.setVisible(true);
    }

    private void pasangEventListeners() {
        view.login_btn().addActionListener(new LoginButtonListener());
    }

    // ========== METHOD PROSES LOGIN ==========
    private void prosesLogin() {
        String username = view.get_username().getText();
        String password = view.get_password().getText();

        try {
            model.get_login_personal(username, password);

            if (cekLoginBerhasil()) {
                bukaMenuUtama();
            } else {
                showLoginError();
            }

        } catch (SQLException ex) {
            handleErrorLogin(ex);
        }
    }

    private boolean cekLoginBerhasil() {
        if (model.rows.isEmpty()) {
            return false;
        }

        Object[] userData = model.rows.get(0);
        String message = (String) userData[0];
        int idPersonal = (int) userData[1];

        if (!message.equals("Login Gagal") && idPersonal != 0) {
            session_user.user_session.setIdPersonal(idPersonal);

            String nama = (userData.length > 2) ? userData[2].toString() : view.get_username().getText();
            session_user.user_session.setNamaPersonal(nama);

            System.out.println("Session berhasil diisi untuk: " + nama);

            return true;
        }

        return false;
    }

    private void bukaMenuUtama() {
        try {
            new c_daftarMenu();
            view.setVisible(false);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error navigating to main menu", ex);
            view.label_info().setText("Terjadi kesalahan saat membuka menu.");
        }
    }

    private void showLoginError() {
        view.label_info().setText("Email atau Password kamu salah!");
    }

    private void handleErrorLogin(SQLException ex) {
        LOGGER.log(Level.SEVERE, "Database connection error during login", ex);
        view.label_info().setText("Terjadi kesalahan saat koneksi ke database.");
    }

    // ========== INNER CLASS (EVENT LISTENERS) ==========
    private class LoginButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            prosesLogin();
        }
    }
}
