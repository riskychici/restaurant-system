/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package style_table;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Komponen tabel modern yang reusable untuk semua kebutuhan
 *
 * Cara pakai: ModernTable table = new ModernTable(); table.setColumns(new
 * String[]{"No", "Nama", "Email", "Status"}); table.setData(dataList);
 * table.addActionButton("Edit", Color.BLUE, (row) -> { ... }); table.render();
 */
public class ModernTable_backup {

    private JTable table;
    private DefaultTableModel model;
    private List<ColumnConfig> columnConfigs;
    private List<ActionButton> actionButtons;
    private int rowHeight = 70;
    private boolean showNumber = true;
    private int hiddenColumnIndex = -1;

    public ModernTable_backup(JTable existingTable) {
        this.table = existingTable;
        this.columnConfigs = new ArrayList<>();
        this.actionButtons = new ArrayList<>();
    }

    // ===== KONFIGURASI KOLOM =====
    /**
     * Set kolom tabel
     *
     * @param columns Array nama kolom
     */
    public ModernTable_backup setColumns(String[] columns) {
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Hanya kolom aksi yang editable
                return column == columns.length - 1 && !actionButtons.isEmpty();
            }
        };
        return this;
    }

    /**
     * Konfigurasi detail kolom
     */
    public ModernTable_backup configureColumn(int index, ColumnType type, int width) {
        columnConfigs.add(new ColumnConfig(index, type, width));
        return this;
    }

    /**
     * Sembunyikan kolom tertentu (biasanya ID)
     */
    public ModernTable_backup hideColumn(int index) {
        this.hiddenColumnIndex = index;
        return this;
    }

    /**
     * Tampilkan/sembunyikan nomor urut
     */
    public ModernTable_backup showNumbering(boolean show) {
        this.showNumber = show;
        return this;
    }

    /**
     * Set tinggi baris
     */
    public ModernTable_backup setRowHeight(int height) {
        this.rowHeight = height;
        return this;
    }

    // ===== AKSI BUTTONS =====
    /**
     * Tambah tombol aksi
     *
     * @param label Text tombol
     * @param color Warna tombol
     * @param action Callback saat diklik, menerima data row
     */
    public ModernTable_backup addActionButton(String label, Color color, ActionCallback action) {
        actionButtons.add(new ActionButton(label, color, action));
        return this;
    }

    // ===== DATA =====
    /**
     * Set data tabel
     *
     * @param data List of Object[] - setiap array adalah 1 row
     */
    public ModernTable_backup setData(List<Object[]> data) {
        if (model == null) {
            throw new IllegalStateException("Set columns first using setColumns()");
        }

        model.setRowCount(0); // Clear existing data

        int nomor = 1;
        for (Object[] row : data) {
            Object[] newRow;

            if (showNumber) {
                // Tambahkan nomor di kolom pertama
                newRow = new Object[row.length + 1];
                newRow[0] = nomor++;
                System.arraycopy(row, 0, newRow, 1, row.length);
            } else {
                newRow = row;
            }

            model.addRow(newRow);
        }

        return this;
    }

    // ===== RENDER =====
    /**
     * Render tabel dengan semua konfigurasi
     */
    public void render() {
        table.setModel(model);
        table.setFillsViewportHeight(true);

        // Hide kolom jika ada
        if (hiddenColumnIndex >= 0) {
            table.getColumnModel().getColumn(hiddenColumnIndex).setMinWidth(0);
            table.getColumnModel().getColumn(hiddenColumnIndex).setMaxWidth(0);
            table.getColumnModel().getColumn(hiddenColumnIndex).setWidth(0);
        }

        // Styling
        table.setRowHeight(rowHeight);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(6, 0));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(new Color(51, 65, 85));

        // Header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(new Color(71, 85, 105));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        header.setReorderingAllowed(false);

        // Set column widths
        for (ColumnConfig config : columnConfigs) {
            table.getColumnModel().getColumn(config.index).setPreferredWidth(config.width);
        }

        // Apply renderer
        ModernCellRenderer renderer = new ModernCellRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i != hiddenColumnIndex) {
                table.getColumnModel().getColumn(i).setCellRenderer(renderer);
            }
        }

        // Apply editor untuk kolom aksi
        if (!actionButtons.isEmpty()) {
            int actionColumnIndex = table.getColumnCount() - 1;
            table.getColumnModel().getColumn(actionColumnIndex).setCellEditor(
                    new ActionCellEditor()
            );
        }

        // Scroll pane styling
        if (table.getParent().getParent() instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
            scrollPane.getViewport().setBackground(Color.WHITE);
        }
    }

    // ===== HELPER METHODS =====
    public JTable getTable() {
        return table;
    }

    public Object getValueAt(int row, int col) {
        return table.getValueAt(row, col);
    }

    public int getSelectedRow() {
        return table.getSelectedRow();
    }

    // ===== INNER CLASSES =====
    public enum ColumnType {
        NUMBER, // Nomor urut
        TEXT, // Text biasa
        MULTI_LINE, // Text multi-line dengan badge
        PRICE, // Format harga dengan diskon
        STOCK, // Stok dengan status
        ACTIONS           // Tombol aksi
    }

    private static class ColumnConfig {

        int index;
        ColumnType type;
        int width;

        ColumnConfig(int index, ColumnType type, int width) {
            this.index = index;
            this.type = type;
            this.width = width;
        }
    }

    public interface ActionCallback {

        void onClick(int row, Object[] rowData);
    }

    private static class ActionButton {

        String label;
        Color color;
        ActionCallback action;

        ActionButton(String label, Color color, ActionCallback action) {
            this.label = label;
            this.color = color;
            this.action = action;
        }
    }

    // ===== RENDERER =====
    private class ModernCellRenderer extends JPanel implements TableCellRenderer {

        private int hoveredRow = -1;
        private Color hoverColor = new Color(241, 245, 249);

        public ModernCellRenderer() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setOpaque(true);

            table.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row != hoveredRow) {
                        hoveredRow = row;
                        table.repaint();
                    }
                }
            });

            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    hoveredRow = -1;
                    table.repaint();
                }
            });
        }

        @Override
        public Component getTableCellRendererComponent(JTable tbl, Object val,
                boolean isSelected, boolean hasFocus, int row, int col) {

            removeAll();
            setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

            // Background
            if (isSelected) {
                setBackground(new Color(219, 234, 254));
            } else if (row == hoveredRow) {
                setBackground(hoverColor);
            } else {
                setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 250, 251));
            }

            // Cari tipe kolom
            ColumnType type = getColumnType(col);

            switch (type) {
                case NUMBER:
                    renderNumber(val);
                    break;
                case MULTI_LINE:
                    renderMultiLine(val);
                    break;
                case PRICE:
                    renderPrice(val);
                    break;
                case STOCK:
                    renderStock(val);
                    break;
                case ACTIONS:
                    renderActions();
                    break;
                default:
                    renderText(val);
                    break;
            }

            return this;
        }

        private ColumnType getColumnType(int col) {
            for (ColumnConfig config : columnConfigs) {
                if (config.index == col) {
                    return config.type;
                }
            }
            // Default: kolom pertama = number, terakhir = actions
            if (showNumber && col == 0) {
                return ColumnType.NUMBER;
            }
            if (!actionButtons.isEmpty() && col == table.getColumnCount() - 1) {
                return ColumnType.ACTIONS;
            }
            return ColumnType.TEXT;
        }

        private void renderNumber(Object val) {
            setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
            JLabel lbl = new JLabel(val != null ? val.toString() : "");
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
            lbl.setForeground(new Color(100, 116, 139));
            add(lbl);
        }

        private void renderText(Object val) {
            JLabel lbl = new JLabel(val != null ? val.toString() : "");
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lbl.setForeground(new Color(30, 41, 59));
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(Box.createVerticalStrut(15));
            add(lbl);
        }

        private void renderMultiLine(Object val) {
            if (val instanceof Object[]) {
                Object[] info = (Object[]) val;
                String kategori = info.length > 0 ? info[0].toString() : "";
                String nama = info.length > 1 ? info[1].toString() : "";
                String extra = info.length > 2 ? info[2].toString() : "";
                boolean hasBadge = info.length > 3 && (Boolean) info[3];

                JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
                topRow.setOpaque(false);

                if (!kategori.isEmpty()) {
                    topRow.add(createBadge(kategori, new Color(241, 245, 249), new Color(100, 116, 139)));
                }

                if (hasBadge) {
                    topRow.add(createBadge("DISKON AKTIF", new Color(254, 226, 226), new Color(220, 38, 38)));
                }

                topRow.setAlignmentX(Component.LEFT_ALIGNMENT);
                add(topRow);
                add(Box.createVerticalStrut(5));

                JLabel lblNama = new JLabel(nama);
                lblNama.setFont(new Font("Segoe UI", Font.BOLD, 13));
                lblNama.setForeground(new Color(15, 23, 42));
                lblNama.setAlignmentX(Component.LEFT_ALIGNMENT);
                add(lblNama);

                if (!extra.isEmpty()) {
                    add(Box.createVerticalStrut(3));
                    JLabel lblExtra = new JLabel(extra);
                    lblExtra.setFont(new Font("Segoe UI", Font.ITALIC, 10));
                    lblExtra.setForeground(new Color(148, 163, 184));
                    lblExtra.setAlignmentX(Component.LEFT_ALIGNMENT);
                    add(lblExtra);
                }
            }
        }

        private void renderPrice(Object val) {
            if (val instanceof String[]) {
                String[] prices = (String[]) val;
                String hargaAsli = prices[0];
                String hargaDiskon = prices.length > 1 ? prices[1] : "";
                String diskon = prices.length > 2 ? prices[2] : "";

                boolean adaDiskon = !hargaDiskon.isEmpty() && !hargaDiskon.equals("0") && !hargaDiskon.equals(hargaAsli);

                if (adaDiskon) {
                    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
                    topPanel.setOpaque(false);

                    JLabel lblAsli = new JLabel("<html><strike>" + hargaAsli + "</strike></html>");
                    lblAsli.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                    lblAsli.setForeground(new Color(148, 163, 184));
                    topPanel.add(lblAsli);

                    if (!diskon.isEmpty() && !diskon.equals("0")) {
                        String persenText = diskon.contains("%") ? diskon : diskon + "%";
                        topPanel.add(createBadge("-" + persenText, new Color(254, 226, 226), new Color(220, 38, 38)));
                    }

                    topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    add(topPanel);
                    add(Box.createVerticalStrut(4));

                    JLabel lblDiskon = new JLabel(hargaDiskon);
                    lblDiskon.setFont(new Font("Segoe UI", Font.BOLD, 16));
                    lblDiskon.setForeground(new Color(220, 38, 38));
                    lblDiskon.setAlignmentX(Component.LEFT_ALIGNMENT);
                    add(lblDiskon);
                } else {
                    add(Box.createVerticalStrut(10));
                    JLabel lblNormal = new JLabel(!hargaAsli.isEmpty() ? hargaAsli : "Rp 0");
                    lblNormal.setFont(new Font("Segoe UI", Font.BOLD, 16));
                    lblNormal.setForeground(new Color(15, 23, 42));
                    lblNormal.setAlignmentX(Component.LEFT_ALIGNMENT);
                    add(lblNormal);
                }
            }
        }

        private void renderStock(Object val) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            String stokText = val != null ? val.toString() : "0";

            try {
                int stok = Integer.parseInt(stokText);

                JLabel lblStok = new JLabel(stokText);
                lblStok.setFont(new Font("Segoe UI", Font.BOLD, 20));
                lblStok.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel lblStatus = new JLabel();
                lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 9));
                lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

                if (stok > 10) {
                    lblStok.setForeground(new Color(22, 163, 74));
                    lblStatus.setText("TERSEDIA");
                    lblStatus.setForeground(new Color(22, 163, 74));
                } else if (stok > 0) {
                    lblStok.setForeground(new Color(234, 179, 8));
                    lblStatus.setText("TERBATAS");
                    lblStatus.setForeground(new Color(234, 179, 8));
                } else {
                    lblStok.setForeground(new Color(220, 38, 38));
                    lblStatus.setText("HABIS");
                    lblStatus.setForeground(new Color(220, 38, 38));
                }

                add(Box.createVerticalStrut(8));
                add(lblStok);
                add(Box.createVerticalStrut(2));
                add(lblStatus);
            } catch (NumberFormatException e) {
                JLabel lbl = new JLabel(stokText);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
                lbl.setForeground(new Color(100, 116, 139));
                lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
                add(Box.createVerticalStrut(15));
                add(lbl);
            }
        }

        private void renderActions() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 4, 15));
            for (ActionButton btn : actionButtons) {
                add(createButton(btn.label, btn.color));
            }
        }

        private JLabel createBadge(String text, Color bg, Color fg) {
            JLabel badge = new JLabel(text);
            badge.setFont(new Font("Segoe UI", Font.BOLD, 9));
            badge.setForeground(fg);
            badge.setBackground(bg);
            badge.setOpaque(true);
            badge.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(fg.brighter(), 1),
                    BorderFactory.createEmptyBorder(2, 6, 2, 6)
            ));
            return badge;
        }

        private JButton createButton(String text, Color color) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 10));
            btn.setForeground(Color.WHITE);
            btn.setBackground(color);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setPreferredSize(new Dimension(60, 32));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btn.addMouseListener(new MouseAdapter() {
                Color originalColor = color;

                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(originalColor.darker());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(originalColor);
                }
            });

            return btn;
        }
    }

    // ===== EDITOR =====
    private class ActionCellEditor extends DefaultCellEditor {

        private JPanel panel;

        public ActionCellEditor() {
            super(new JCheckBox());
        }

        @Override
        public Component getTableCellEditorComponent(JTable tbl, Object val,
                boolean isSelected, int row, int col) {

            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 15));
            panel.setBackground(isSelected ? new Color(219, 234, 254)
                    : (row % 2 == 0 ? Color.WHITE : new Color(249, 250, 251)));

            for (ActionButton actionBtn : actionButtons) {
                JButton btn = createButton(actionBtn.label, actionBtn.color);
                btn.addActionListener(e -> {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        Object[] rowData = new Object[table.getColumnCount()];
                        for (int i = 0; i < table.getColumnCount(); i++) {
                            rowData[i] = table.getValueAt(selectedRow, i);
                        }
                        actionBtn.action.onClick(selectedRow, rowData);
                    }
                    stopCellEditing();
                });
                panel.add(btn);
            }

            return panel;
        }

        private JButton createButton(String text, Color color) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 10));
            btn.setForeground(Color.WHITE);
            btn.setBackground(color);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setPreferredSize(new Dimension(60, 32));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btn.addMouseListener(new MouseAdapter() {
                Color originalColor = color;

                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(originalColor.darker());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(originalColor);
                }
            });

            return btn;
        }
    }
}
