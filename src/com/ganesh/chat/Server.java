package com.ganesh.chat;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;
import java.util.Enumeration;
import java.util.Vector;

public class Server extends UnicastRemoteObject implements ServerInterface {

    private final String identity = "chat";
    private Vector<ClientInfo> clientVector;

    public Server() throws RemoteException {

        clientVector = new Vector<>();

        try {
            LocateRegistry.createRegistry(1099);
            Naming.rebind(identity, this);
            System.out.println("Server running on port 1099...");
        } catch (RemoteException | MalformedURLException re) {
            System.err.println(re);
        }
    }

    public static void main(String[] args) throws RemoteException {
        new Server();
    }

    @Override
    public void login(String name, ClientInterface ref, PublicKey publicKey) {
        ClientInfo clientInfo = new ClientInfo(name, ref, publicKey);
        clientVector.add(clientInfo);

        broadCastActiveClientList();
    }

    private void broadCastActiveClientList() {
        Vector<String> namesVector = new Vector<>();
        Enumeration<ClientInfo> enumeration = clientVector.elements();
        while (enumeration.hasMoreElements()) {
            ClientInfo ci = enumeration.nextElement();
            namesVector.add(ci.getName());
        }
        Enumeration<ClientInfo> enumeration1 = clientVector.elements();
        while (enumeration1.hasMoreElements()) {
            ClientInfo clientInfo = enumeration1.nextElement();
            try {
                clientInfo.getRef().getAllClientList(namesVector);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void clientWillSendMsgToServer(String message, ClientInterface ref) throws RemoteException {
        ref.serverWillSendMsgToClient(message);
    }

    @Override
    public ClientInterface sendClientRef(String name) {
        ClientInterface clientInterface = null;
        Enumeration<ClientInfo> en = clientVector.elements();
        while (en.hasMoreElements()) {
            ClientInfo ci = en.nextElement();
            if (ci.getName().equals(name)) {
                clientInterface = ci.getRef();
            }
        }
        return clientInterface;
    }

    @Override
    public PublicKey sendPublicKeyToClient(String name) {
        PublicKey publicKey = null;
        Enumeration<ClientInfo> enumeration = clientVector.elements();
        while (enumeration.hasMoreElements()) {
            ClientInfo ci = enumeration.nextElement();
            if (ci.getName().equals(name)) {
                publicKey = ci.getPublicKey();
            }
        }
        return publicKey;
    }

    @Override
    public void logout(String name) {

    }
}
