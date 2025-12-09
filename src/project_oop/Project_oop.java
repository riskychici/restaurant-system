/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_oop;

import java.sql.ResultSet;
import java.sql.SQLException;
import koneksi.koneksi;
import project_oop.controller.c_user;
import com.formdev.flatlaf.FlatLightLaf;
import project_oop.controller.c_kategori_produk;
import project_oop.controller.c_daftarMenu;
import project_oop.controller.c_produk;

/**
 *
 * @author Lorem Ipsum25
 */
public class Project_oop {

    public static void main(String[] args) {
//        c_user us = new c_user();

        try {
            FlatLightLaf.setup();  // atau FlatDarculaLaf.setup()
        } catch (Exception ex) {
            System.err.println("Gagal menerapkan FlatLaf: " + ex.getMessage());
        }

        java.awt.EventQueue.invokeLater(() -> {
            try {
//                new c_kategori_produk(); // panggil controller utama kamu
                new c_daftarMenu();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
//        try (koneksi db = new koneksi()) {
//            System.out.println("Koneksi sukses (try-with-resources)!");
//
//            // Tes query
//            ResultSet rs = db.executeSelect("SELECT version()");
//            if (rs.next()) {
//                System.out.println("Postgres version: " + rs.getString(1));
//            }
//            rs.close();
//
//        } catch (SQLException ex) {
//            System.err.println("Gagal: " + ex.getMessage());
//            ex.printStackTrace();
//        }
        // koneksi otomatis ditutup di sini
    }

}
