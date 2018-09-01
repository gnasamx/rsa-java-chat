package com.ganesh.chat;

import java.security.*;


public class GeneratePublicPrivateKeys {

    private volatile GeneratePublicPrivateKeys instance;

    public GeneratePublicPrivateKeys getInstance() {
        if (instance == null) {
            instance = new GeneratePublicPrivateKeys();
        }
        return instance;
    }

    public Keys generateKeys(String keyAlgorithm, int numBits) {
        Keys keys = null;
        try {
            // Get the public/private key pair
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(keyAlgorithm);
            keyGen.initialize(numBits);
            KeyPair keyPair = keyGen.genKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            keys = new Keys(publicKey, privateKey);

//            Base64.Encoder encoder = Base64.getEncoder();
//            System.out.println("Private key===== " + encoder.encodeToString(privateKey.getEncoded()));
//            System.out.println("Public key===== " + encoder.encodeToString(publicKey.getEncoded()));

        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception");
            System.out.println("No such algorithm: " + keyAlgorithm);
        }
        return keys;
    }

}