package com.ganesh.chat;


import java.security.PrivateKey;
import java.security.PublicKey;

public class Keys {
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public Keys() {
    }

    public Keys(PublicKey publicKey, PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
