package com.ganesh.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface ClientInterface extends Remote {
    void serverWillSendMsgToClient(String message) throws RemoteException;

    void getAllClientList(Vector<String> clients) throws RemoteException;
}
