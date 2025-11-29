package project_oop.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import project_oop.model.m_daftarMenu;
import project_oop.view.pesanan;
import project_oop.view.daftarMenu;

public class c_pesanan {

    private m_daftarMenu model;
    private pesanan view1;
    private daftarMenu view2;

    public c_pesanan() throws SQLException {
        this.model = new m_daftarMenu();
        this.view1 = new pesanan();
        this.view2 = new daftarMenu();
        this.view1.setVisible(true);
    }

}
