/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package messageapp;

import javax.swing.JOptionPane;
import quickchat.Login;
import quickchat.Message;

/**
 *
 * @author RC_Student_lab
 */
public class MessageApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
   Login login = new Login();
        Message message = new Message();

        // Register user
        String username = JOptionPane.showInputDialog("Register - Enter username:");
        String password = JOptionPane.showInputDialog("Register - Enter password:");
        String cell = JOptionPane.showInputDialog("Register - Enter phone number:");
        String firstName = JOptionPane.showInputDialog("Enter your first name:");
        String lastName = JOptionPane.showInputDialog("Enter your last name:");

        String registration = login.registerUser(username, password, cell, firstName, lastName);
        JOptionPane.showMessageDialog(null, registration);

        if (!registration.contains("successfully")) return;

        // Login
        String enteredUsername = JOptionPane.showInputDialog("Login - Enter username:");
        String enteredPassword = JOptionPane.showInputDialog("Login - Enter password:");
        boolean isLoggedIn = login.loginUser(enteredUsername, enteredPassword);

        JOptionPane.showMessageDialog(null, login.returnLoginStatus(isLoggedIn));
        if (!isLoggedIn) return;

        // Message Loop
        int count = Integer.parseInt(JOptionPane.showInputDialog("How many messages do you want to enter?"));
        for (int i = 0; i < count; i++) {
            String recipient = JOptionPane.showInputDialog("Enter recipient number (e.g. +27831234567):");
            if (!recipient.matches("^\\+\\d{1,3}\\d{7,9}$")) {
                JOptionPane.showMessageDialog(null, "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.");
                continue;
            }

            String messageContent = JOptionPane.showInputDialog("Enter your message (max 250 characters):");
            if (messageContent.length() > 250) {
                JOptionPane.showMessageDialog(null, "Message exceeds 250 characters by " + (messageContent.length() - 250));
                continue;
            }

            String id = message.generateMessageID();
            String hash = message.createMessageHash(id, i, messageContent);
            message.messageIDs.add(recipient);
            message.messageHashes.add(hash);

            String decision = JOptionPane.showInputDialog("Choose action: Send / Store / Disregard");
            String result = message.SentMessage(messageContent, decision);

            if (decision.equalsIgnoreCase("send")) {
                // Already added to sentMessages in SentMessage()
            } else if (decision.equalsIgnoreCase("store")) {
                message.storedMessages.add(messageContent);
                message.storeMessage(id, hash, recipient, messageContent);
            } else if (decision.equalsIgnoreCase("disregard")) {
                // Already added to disregardedMessages in SentMessage()
            }

            JOptionPane.showMessageDialog(null, result + "\nHash: " + hash + "\nMessage ID: " + id);
        }

        // Load from JSON
        message.loadStoredMessagesFromJson();

        // Post-message menu
        String menu = """
                Select an option:
                1 - View Sent Messages and Recipients
                2 - View Longest Sent Message
                3 - Search by Recipient Number
                4 - Search all Messages sent to a Recipient
                5 - Delete Message by Hash
                6 - Display Full Message Report
                7 - Exit
                """;

        while (true) {
            int option = Integer.parseInt(JOptionPane.showInputDialog(menu));
            switch (option) {
                case 1 -> JOptionPane.showMessageDialog(null, message.displaySendersAndRecipients());
                case 2 -> JOptionPane.showMessageDialog(null, "Longest Sent Message:\n" + message.getLongestSentMessage());
                case 3 -> {
                    String searchID = JOptionPane.showInputDialog("Enter recipient number:");
                    JOptionPane.showMessageDialog(null, message.searchByMessageID(searchID));
                }
                case 4 -> {
                    String searchRecip = JOptionPane.showInputDialog("Enter recipient number:");
                    var results = message.searchByRecipient(searchRecip);
                    JOptionPane.showMessageDialog(null, String.join("\n", results));
                }
                case 5 -> {
                    String hash = JOptionPane.showInputDialog("Enter message hash to delete:");
                    boolean deleted = message.deleteMessageByHash(hash);
                    String msg = deleted ? "Message successfully deleted." : "Hash not found.";
                    JOptionPane.showMessageDialog(null, msg);
                }
                case 6 -> JOptionPane.showMessageDialog(null, message.displayFullReport());
                case 7 -> {
                    JOptionPane.showMessageDialog(null, "Thank you for using QuickChat. Goodbye!");
                    return;
                }
                default -> JOptionPane.showMessageDialog(null, "Invalid option.");
            }
        }
    }
    
}
