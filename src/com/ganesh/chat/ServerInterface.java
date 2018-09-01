package com.ganesh.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;

public interface ServerInterface extends Remote {
    void login(String name, ClientInterface ref, PublicKey publicKey) throws RemoteException;
//    void login(String name, ClientInterface ref) throws RemoteException;

    void clientWillSendMsgToServer(String message, ClientInterface ref) throws RemoteException;

    ClientInterface sendClientRef(String name) throws RemoteException;

    PublicKey sendPublicKeyToClient(String name) throws RemoteException;

    void logout(String name) throws RemoteException;
}
