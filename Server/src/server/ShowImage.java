package server;

import javax.swing.ImageIcon;

public class ShowImage extends javax.swing.JDialog {

    public ShowImage(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    private void initComponents() {
        lb = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        lb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lb));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lb));
        pack();
        setLocationRelativeTo(null);
    }

    public void set(ImageIcon icon) {
        lb.setIcon(icon);
    }

    private javax.swing.JLabel lb;
}
