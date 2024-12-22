package client;

import data.Data;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.net.InetAddress;


public class Client extends javax.swing.JFrame {

    public Client() {
        initComponents();
    }

    private Socket socket;
    private ObjectOutputStream out;

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        txtName = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        txtIp = new javax.swing.JTextField();
        txtClientName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 18));
        jLabel1.setText("Client");

        jButton1.setText("Connect");
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));

        txt.setEditable(false);
        txt.setColumns(20);
        txt.setRows(5);
        jScrollPane1.setViewportView(txt);

        jButton2.setText("Send File");
        jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));

        jButton3.setText("Discover Devices");
        jButton3.addActionListener(evt -> discoverDevices());

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Image", "Video", "Audio", "File"}));

        jLabel2.setText("IP");

        jLabel3.setText("Your Name");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabel1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addGap(18, 18, 18)
                    .addComponent(txtIp, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabel3)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtClientName, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jButton1)
                    .addGap(18, 18, 18))
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jButton3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton2)
                            .addGap(18, 18, 18)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1)
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton1)
                                .addComponent(txtIp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2)
                                .addComponent(txtClientName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3))))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(26, 26, 26)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3))
                    .addGap(0, 63, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String serverIP = txtIp.getText().trim();
            if (serverIP.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the server IP address.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String clientName = txtClientName.getText().trim();
            if (clientName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            socket = new Socket(serverIP, 9999);
            txt.append("Connected to server at: " + serverIP + "\n");

            out = new ObjectOutputStream(socket.getOutputStream());
            Data data = new Data();
            data.setStatus("new");
            data.setName(clientName);
            out.writeObject(data);
            out.flush();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String targetIp = JOptionPane.showInputDialog(this, "Enter target IP address:");
            if (targetIp == null || targetIp.isEmpty()) {
                txt.append("No target IP entered. File not sent.\n");
                return;
            }

            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                if (!file.exists() || !file.canRead()) {
                    JOptionPane.showMessageDialog(this, "File does not exist or cannot be accessed.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                socket = new Socket(targetIp, 9999);
                out = new ObjectOutputStream(socket.getOutputStream());

                FileInputStream fis = new FileInputStream(file);
                byte[] data = fis.readAllBytes();
                fis.close();

                Data sendData = new Data();
                sendData.setName(file.getName());
                sendData.setFile(data);
                sendData.setStatus(jComboBox1.getSelectedItem().toString());

                out.writeObject(sendData);
                out.flush();

                txt.append("File sent to " + targetIp + ": " + file.getName() + "\n");
                socket.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
private void discoverDevices() {
    new Thread(() -> {
        try {
            String ipAddress = txtIp.getText().trim();
            if (!ipAddress.contains(".")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid IP address (e.g., 192.168.0.1).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String subnet = ipAddress.substring(0, ipAddress.lastIndexOf('.'));
            txt.append("Scanning for devices on the subnet: " + subnet + ".x\n");

            int maxThreads = 50; // Limit to avoid excessive thread creation
            ExecutorService executor = Executors.newFixedThreadPool(maxThreads);

            for (int i = 1; i <= 254; i++) {
                final int lastOctet = i;
                executor.submit(() -> {
                    try {
                        String host = subnet + "." + lastOctet;
                        InetAddress address = InetAddress.getByName(host);

                        // Use a smaller timeout to speed up discovery
                        if (address.isReachable(500)) {
                            synchronized (txt) {
                                txt.append("Active IP found: " + host + " (" + address.getHostName() + ")\n");
                            }
                        }
                    } catch (Exception e) {
                        // Ignore unreachable hosts
                    }
                });
            }

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);

            txt.append("Device discovery completed.\n");
        } catch (Exception e) {
            txt.append("Error during device discovery: " + e.getMessage() + "\n");
        }
    }).start();
}


    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new Client().setVisible(true));
    }

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txt;
    private javax.swing.JTextField txtClientName;
    private javax.swing.JTextField txtIp;
    private javax.swing.JTextField txtName;
}
