package com.ganesh.chat;

import java.security.PrivateKey;
import java.security.PublicKey;

public class ClientInfo {
    private String name;
    private ClientInterface ref;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public ClientInfo() {
    }

    public ClientInfo(String name, ClientInterface ref) {
        this.name = name;
        this.ref = ref;
    }

    public ClientInfo(String name, ClientInterface ref, PublicKey publicKey) {
        this.name = name;
        this.ref = ref;
        this.publicKey = publicKey;
    }

    public ClientInfo(String name, ClientInterface ref, PublicKey publicKey, PrivateKey privateKey) {
        this.name = name;
        this.ref = ref;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getName() {
        return name;
    }

    public ClientInterface getRef() {
        return ref;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public boolean equals(Object o) {
        if (o instanceof ClientInfo)
            return ((ClientInfo) o).getName().equals(name);

        return false;
    }

    public String toString() {
        return "<==" + name + " " + ref + "==>";
    }
}
