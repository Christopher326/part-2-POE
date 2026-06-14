/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapppart3;

import java.util.Random;

/**
 *
 * @author Student
 */
public class Message {

    private String messageId;
    private String hash;
    private String sender;
    private String recipient;
    private String messageText;
    private int messageNumber;

    // Constructor for creating new messages (Part 2)
    public Message(int messageNumber, String sender, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.sender = sender;
        this.recipient = recipient;
        this.messageText = messageText;

        this.messageId = generateID();
        this.hash = createHash();
    }

    // Constructor for loading messages from JSON (Part 3)
    public Message(String messageId, String hash, String sender, String recipient, String messageText) {
        this.messageId = messageId;
        this.hash = hash;
        this.sender = sender;
        this.recipient = recipient;
        this.messageText = messageText;
    }

    private String generateID() {
        Random r = new Random();
        long num = Math.abs(r.nextLong()) % 10000000000L;
        return String.format("%010d", num);
    }

    private String createHash() {
        String[] words = messageText.split(" ");
        String first = words.length > 0 ? words[0] : "";
        String last = words.length > 1 ? words[words.length - 1] : first;
        return (messageId.substring(0, 2) + ":" + messageNumber + ":" + first + last).toUpperCase();
    }

    public String toJsonString() {
        return "{\"messageId\":\"" + messageId + "\",\"hash\":\"" + hash + "\",\"sender\":\"" + sender
                + "\",\"recipient\":\"" + recipient + "\",\"message\":\"" + messageText + "\"}";
    }

    @Override
    public String toString() {
        return "ID: " + messageId + "\nHash: " + hash + "\nFrom: " + sender
                + "\nTo: " + recipient + "\nMessage: " + messageText + "\n";
    }

    public String getMessageId() {
        return messageId;
    }

    public String getHash() {
        return hash;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }
}
