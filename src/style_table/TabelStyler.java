/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package style_table;

/**
 *
 * @author Lorem Ipsum25
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class TabelStyler {

    public static void apply(JTable table) {

        // === Font dasar ===
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setSelectionBackground(new Color(220, 240, 255));
        table.setSelectionForeground(Color.BLACK);

        // === Header Styling ===
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        header.setOpaque(false);
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(60, 60, 60));
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // === Renderer Zebra Row + Hover ===
        final int[] hoverRow = {-1};
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    if (row == hoverRow[0]) {
                        c.setBackground(new Color(230, 240, 255)); // hover
                    } else if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(245, 245, 245));
                    }
                }
                return c;
            }
        });

        // === Listener Hover ===
        table.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                hoverRow[0] = table.rowAtPoint(e.getPoint());
                table.repaint();
            }
        });
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                hoverRow[0] = -1;
                table.repaint();
            }
        });
    }
}
