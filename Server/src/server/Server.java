package server;

import data.Data;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Server extends javax.swing.JFrame {

    public Server() {
        initComponents();
    }

    private ServerSocket server;
    private ObjectInputStream in;
    private DefaultListModel<Data> mod = new DefaultListModel<>();

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        list = new javax.swing.JList<>();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 18)); 
        jLabel1.setText("Server");

        jButton1.setText("Start Server");
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));

        txt.setEditable(false);
        txt.setColumns(20);
        txt.setRows(5);
        jScrollPane1.setViewportView(txt);

        list.setModel(mod);
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(list);

        jButton2.setText("Open");
        jButton2.addActionListener(evt -> open());

        jButton3.setText("Save");
        jButton3.addActionListener(evt -> save());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane2)))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabel1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1)
                        .addComponent(jButton1))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane1)
                        .addComponent(jScrollPane2))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2)
                        .addComponent(jButton3)))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        list.setModel(mod);
        displayAllIPs();
        new Thread(() -> {
            try {
                server = new ServerSocket(9999, 50, InetAddress.getByName("0.0.0.0"));

                txt.append("Server is listening on port 9999...\n");

                while (true) {
                    Socket client = server.accept();
                    txt.append("New client connected.\n");
                    in = new ObjectInputStream(client.getInputStream());

                    new Thread(() -> {
                        try {
                            while (true) {
                                Data data = (Data) in.readObject();
                                if ("new".equalsIgnoreCase(data.getStatus())) {
                                    txt.append("Client connected: " + data.getName() + "\n");
                                } else {
                                    mod.addElement(data);
                                    txt.append("Received file: " + data.getName() + "\n");
                                }
                            }
                        } catch (java.io.EOFException e) {
                            txt.append("Client disconnected.\n");
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }).start();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }

    private void listMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(evt)) {
            open();
        }
    }

    private void open() {
        if (!list.isSelectionEmpty()) {
            Data data = mod.getElementAt(list.getSelectedIndex());
            String status = data.getStatus();
            try {
                File tempFile = File.createTempFile("received_", "." + getFileExtension(data.getName()));
                tempFile.deleteOnExit();

                FileOutputStream fos = new FileOutputStream(tempFile);
                fos.write(data.getFile());
                fos.close();

                if (status.equalsIgnoreCase("Image") || status.equalsIgnoreCase("Video") || status.equalsIgnoreCase("Audio") || status.equalsIgnoreCase("File")) {
                    java.awt.Desktop.getDesktop().open(tempFile);
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot open this file type.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error opening file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No file selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void save() {
        if (!list.isSelectionEmpty()) {
            Data data = mod.getElementAt(list.getSelectedIndex());
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile());
                    fos.write(data.getFile());
                    fos.close();
                    JOptionPane.showMessageDialog(this, "File saved successfully.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex == -1) {
            return ""; 
        }
        return fileName.substring(lastIndex + 1); 
    }

    private void displayAllIPs() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            txt.append("Server started on the following IPs:\n");
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress()) {
                        txt.append("- " + addr.getHostAddress() + "\n");
                    }
                }
            }
        } catch (SocketException e) {
            txt.append("Error fetching network interfaces: " + e.getMessage() + "\n");
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new Server().setVisible(true));
    }

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<Data> list;
    private javax.swing.JTextArea txt;
}
