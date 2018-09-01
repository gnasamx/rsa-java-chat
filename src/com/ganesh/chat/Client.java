package com.ganesh.chat;

import javax.crypto.Cipher;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Vector;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Client extends JFrame implements ClientInterface, ActionListener {
    private JList list;
    private JTextField jTextField;
    private JTextArea jTextArea;
    private DefaultListModel model;
    private String clientName;
    private ServerInterface serverRef;
    private ClientInterface clientRef;
    private PublicKey publicKey, publicKeyOfReceiver;
    private PrivateKey privateKey;

    public Client(String name) {
        super("Chat Client: " + name);
        clientName = name;

        generateKeys();

        setSize(720, 475);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = getContentPane();
        container.setLayout(null);

        JLabel jLabel = new JLabel();
        jLabel.setBounds(5, 5, 500, 15);
        container.add(jLabel);


        jTextArea = new JTextArea();
        JScrollPane jsp1 = new JScrollPane(jTextArea);
        jsp1.setBounds(5, 25, 500, 380);
        container.add(jsp1);


        model = new DefaultListModel();

        list = new JList(model);
        JScrollPane jsp2 = new JScrollPane(list);
        jsp2.setBounds(510, 25, 200, 380);
        container.add(jsp2);

        jTextField = new JTextField();
        jTextField.setBounds(5, 410, 500, 25);
        container.add(jTextField);


        JButton jButton = new JButton("SEND");
        jButton.setBounds(510, 410, 200, 25);
        container.add(jButton);

        jTextArea.setEditable(false);
        setVisible(true);

        try {
            System.setProperty("java.rmi.server.hostname", "192.168.0.5");
            serverRef = (ServerInterface) Naming.lookup("rmi://localhost:1099/chat");
            UnicastRemoteObject.exportObject(this);

            serverRef.login(clientName, this, publicKey);

        } catch (Exception e) {
            System.err.println(e);
        }

        jButton.addActionListener(this);
        jTextField.addActionListener(this);

        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String selectedName = (String) list.getSelectedValue();

                Color green = Color.decode("#329832");
                jLabel.setForeground(green);
                jLabel.setText(selectedName + " is online");
                try {
                    clientRef = serverRef.sendClientRef(selectedName);
                    publicKeyOfReceiver = serverRef.sendPublicKeyToClient(selectedName);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }
        };
        list.addMouseListener(mouseListener);


    }

    public static void main(String[] args) {
        new Client(args[0]);
    }

    private static String encrypt(PublicKey publicKey, String message) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherText = encryptCipher.doFinal(message.getBytes(UTF_8));

        return Base64.getEncoder().encodeToString(cipherText);
    }

    private static String decrypt(PrivateKey privateKey, String encrypted) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(encrypted);

        Cipher decriptCipher = Cipher.getInstance("RSA");
        decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(decriptCipher.doFinal(bytes), UTF_8);
    }

    private void generateKeys() {
        GeneratePublicPrivateKeys generatePublicPrivateKeys = new GeneratePublicPrivateKeys();
        Keys keys = generatePublicPrivateKeys.generateKeys("RSA", 1024);
        publicKey = keys.getPublicKey();
        privateKey = keys.getPrivateKey();
        System.out.println("Keys generated for you: " + clientName);
    }

    @Override
    public void serverWillSendMsgToClient(String message) {

        try {

            String decryptedMsg = decrypt(privateKey, message);
            jTextArea.append(decryptedMsg + "\n");
        } catch (Exception e) {
            System.out.println("Problem with decryption");
            e.printStackTrace();
        }

    }

    @Override
    public void getAllClientList(Vector<String> clients) {
        clients.remove(clientName);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                model.removeAllElements();
                Enumeration<String> en = clients.elements();
                while (en.hasMoreElements()) {
                    String s = en.nextElement();
                    model.addElement(s);
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String message = jTextField.getText();

        if (!jTextField.getText().isEmpty()) {
            try {
                String encryptedMsg = encrypt(publicKeyOfReceiver, message);
                serverRef.clientWillSendMsgToServer(encryptedMsg, clientRef);
            } catch (Exception e1) {
                System.out.print("Problem with encryption");
                e1.printStackTrace();
            }

            jTextField.setText("");
        }
    }
}
