/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.chatapppart3;

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 *
 * @author Student
 */
public class CHATAPPpart3 {

    private static final String FILE_NAME = "storedMessages.json";
    private static final Scanner scanner = new Scanner(System.in);
    private static int totalSent = 0;
    private static Message[] messages;

    static String registeredUsername = "";
    static String registeredPassword = "";
    static String registeredCell = "";
    static String loggedInUser = "";

    public static void main(String[] args) {
        // Part 1: Registration
        registerUser();

        // Part 1: Login
        loginUser();

        // Part 2 & 3: Main menu
        mainMenu();
    }

    // ================= Part 1: Registration =================
    private static void registerUser() {
        boolean valid = false;

        while (!valid) {
            System.out.print("Enter username : ");
            String username = scanner.nextLine();

            if (username.contains("_") && username.length() <= 5) {
                System.out.println("Username successfully captured!");
                registeredUsername = username;
                valid = true;
            } else {
                System.out.println("Username is not correctly formatted.");
            }
        }

        valid = false;
        while (!valid) {
            System.out.print("Enter password : ");
            String password = scanner.nextLine();

            if (isValidPassword(password)) {
                System.out.println("Password successfully captured!");
                registeredPassword = password;
                valid = true;
            } else {
                System.out.println("Password is not correctly formatted.");
            }
        }

        valid = false;
        while (!valid) {
            System.out.print("Enter cellphone number : ");
            String cell = scanner.nextLine();

            if (cell.matches("^\\+27\\d{1,10}$")) {
                System.out.println("Cellphone number successfully captured!");
                registeredCell = cell;
                valid = true;
            } else {
                System.out.println("Cellphone number is not correctly formatted.");
            }
        }
    }

    private static boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        if (!password.matches(".*[!@#$%^&*()-+=].*")) {
            return false;
        }
        return true;
    }

//   =============== my part 1 =====login========== 
    private static void loginUser() {
        boolean loggedIn = false;

        while (!loggedIn) {
            System.out.print("Login - Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Login - Enter password: ");
            String password = scanner.nextLine();

            if (username.equals(registeredUsername) && password.equals(registeredPassword)) {
                loggedIn = true;
                loggedInUser = username;
                System.out.println("Login successful! Welcome " + loggedInUser);
            } else {
                System.out.println("Username or password incorrect. Try again.");
            }
        }
    }

    // ================= Main Menu =================
    private static void mainMenu() {

        System.out.print("How many messages do you want to send today? ");
        int limit = Integer.parseInt(scanner.nextLine());
        messages = new Message[limit];

        boolean running = true;

        while (running) {
            System.out.println("\n===== QUICKCHAT MENU =====");
            System.out.println("1. Send Message");
            System.out.println("2. Display Sent Messages");
            System.out.println("3. Search Message by ID");
            System.out.println("4. Search by Recipient");
            System.out.println("5. Display Longest Message");
            System.out.println("6. Delete Message by Hash");
            System.out.println("7. Full Report");
            System.out.println("8. Exit");

            System.out.print("Choose option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    sendMessage(limit);
                    break;
                case "2":
                    displaySentMessages();
                    break;
                case "3":
                    searchByMessageID();
                    break;
                case "4":
                    searchByRecipient();
                    break;
                case "5":
                    displayLongestMessage();
                    break;
                case "6":
                    deleteByHash();
                    break;
                case "7":
                    displayFullReport();
                    break;
                case "8":
                    System.out.println("Exiting QuickChat. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

//   ==============my part 2==== send message============= 
    private static void sendMessage(int limit) {
        if (totalSent >= limit) {
            System.out.println("Message limit reached!");
            return;
        }

        System.out.print("Enter recipient number: ");
        String recipient = scanner.nextLine();

        System.out.print("Enter your message: ");
        String text = scanner.nextLine();

        if (text.length() > 250) {
            System.out.println("Message too long! Max 250 characters.");
            return;
        }

        Message msg = new Message(totalSent, loggedInUser, recipient, text);

        System.out.println("\n1. Send Message\n2. Delete Message\n3. Store Message");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                messages[totalSent] = msg;
                saveMessageToFile(msg);
                totalSent++;
                System.out.println("Message sent successfully!\n" + msg);
                break;
            case "2":
                System.out.println("Message discarded.");
                break;
            case "3":
                saveMessageToFile(msg);
                System.out.println("Message stored successfully.");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void saveMessageToFile(Message msg) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(msg.toJsonString());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving message to file.");
        }
    }

    private static void displaySentMessages() {
        if (totalSent == 0) {
            System.out.println("No messages sent yet.");
            return;
        }
        for (int i = 0; i < totalSent; i++) {
            System.out.println(messages[i]);
        }
    }

//  ================ my part 3===== Utilities================ 
    private static Message parseJsonLine(String line) {
        try {
            String id = line.split("\"messageId\":\"")[1].split("\"")[0];
            String hash = line.split("\"hash\":\"")[1].split("\"")[0];
            String sender = line.split("\"sender\":\"")[1].split("\"")[0];
            String recipient = line.split("\"recipient\":\"")[1].split("\"")[0];
            String text = line.split("\"message\":\"")[1].split("\"")[0];

            return new Message(id, hash, sender, recipient, text);
        } catch (Exception e) {
            return null;
        }
    }

    private static void searchByMessageID() {
        System.out.print("Enter Message ID: ");
        String id = scanner.nextLine();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                Message m = parseJsonLine(line);
                if (m != null && m.getMessageId().equals(id)) {
                    System.out.println("Message found:\n" + m);
                    return;
                }
            }
            System.out.println("Message ID not found.");
        } catch (IOException e) {
            System.out.println("Error reading JSON file.");
        }
    }

    private static void searchByRecipient() {
        System.out.print("Enter Recipient Number: ");
        String recipient = scanner.nextLine();

        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                Message m = parseJsonLine(line);
                if (m != null && m.getRecipient().equals(recipient)) {
                    System.out.println(m);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No messages found for this recipient.");
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON file.");
        }
    }

    private static void displayLongestMessage() {
        Message longest = null;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                Message m = parseJsonLine(line);
                if (m != null) {
                    if (longest == null || m.getMessageText().length() > longest.getMessageText().length()) {
                        longest = m;
                    }
                }
            }
            if (longest != null) {
                System.out.println("Longest Message:\n" + longest);
            } else {
                System.out.println("No messages found.");
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON file.");
        }
    }

    private static void deleteByHash() {
        System.out.print("Enter Message Hash to delete: ");
        String hash = scanner.nextLine();

        File originalFile = new File(FILE_NAME);
        File tempFile = new File("temp.json");
        boolean deleted = false;

        try (BufferedReader br = new BufferedReader(new FileReader(originalFile)); BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                Message m = parseJsonLine(line);
                if (m != null && m.getHash().equals(hash)) {
                    deleted = true;
                } else {
                    bw.write(line);
                    bw.newLine();
                }
            }

        } catch (IOException e) {
            System.out.println("Error processing deletion.");
        }

        if (originalFile.delete() && tempFile.renameTo(originalFile)) {
            System.out.println(deleted ? "Message successfully deleted." : "Hash not found.");
        }
    }

    private static void displayFullReport() {
        System.out.println("\n=== FULL MESSAGES REPORT ===");
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                Message m = parseJsonLine(line);
                if (m != null) {
                    System.out.println(m);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON file.");
        }
    }
}
