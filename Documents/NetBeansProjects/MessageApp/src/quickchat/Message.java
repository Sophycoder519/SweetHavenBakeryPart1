package quickchat;

import java.io.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Message {
    public List<String> sentMessages = new ArrayList<>();
    public List<String> disregardedMessages = new ArrayList<>();
    public List<String> storedMessages = new ArrayList<>();
    public List<String> messageHashes = new ArrayList<>();
    public List<String> messageIDs = new ArrayList<>();

    public boolean checkMessageID(String id) {
        return id.length() <= 10;
    }

    public int checkRecipientCell(String cell) {
        if (cell.length() <= 10 && cell.startsWith("0")) return 1;
        return 0;
    }

    public String createMessageHash(String id, int count, String message) {
        String[] words = message.split(" ");
        return (id.substring(0, 2) + ":" + count + ":" + words[0] + words[words.length - 1]).toUpperCase();
    }

    public String generateMessageID() {
        return String.valueOf((long)(Math.random() * 1_000_000_0000L));
    }

    public String SentMessage(String message, String decision) {
        switch (decision.toLowerCase()) {
            case "send" -> {
                sentMessages.add(message);
                return "Message successfully sent.";
            }
            case "store" -> {
                return "Message successfully stored.";
            }
            case "disregard" -> {
                disregardedMessages.add(message);
                return "Press 0 to delete message.";
            }
            default -> {
                return "Invalid choice.";
            }
        }
    }

    public String printMessages() {
        if (sentMessages.isEmpty()) return "No messages sent.";
        StringBuilder sb = new StringBuilder();
        for (String msg : sentMessages) {
            sb.append(msg).append("\n");
        }
        return sb.toString();
    }

    public int returnTotalMessagess() {
        return sentMessages.size();
    }

    public void storeMessage(String id, String hash, String recipient, String message) {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("hash", hash);
        obj.put("recipient", recipient);
        obj.put("message", message);

        JSONArray array = new JSONArray();
        array.add(obj);

        try (FileWriter writer = new FileWriter("stored_messages.json", true)) {
            writer.write(array.toJSONString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadStoredMessagesFromJson() {
        storedMessages.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("stored_messages.json"))) {
            String line;
            JSONParser parser = new JSONParser();
            while ((line = br.readLine()) != null) {
                JSONArray arr = (JSONArray) parser.parse(line);
                JSONObject obj = (JSONObject) arr.get(0);
                storedMessages.add((String) obj.get("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String displaySendersAndRecipients() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sentMessages.size(); i++) {
            sb.append("Recipient: ").append(messageIDs.get(i))
              .append(" | Message: ").append(sentMessages.get(i)).append("\n");
        }
        return sb.toString();
    }

    public String getLongestSentMessage() {
        String longest = "";
        for (String msg : sentMessages) {
            if (msg.length() > longest.length()) longest = msg;
        }
        return longest;
    }

    public String searchByMessageID(String id) {
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(id)) {
                if (i < sentMessages.size()) return sentMessages.get(i);
                if (i < storedMessages.size()) return storedMessages.get(i);
            }
        }
        return "Message not found.";
    }

    public List<String> searchByRecipient(String recipient) {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(recipient)) {
                if (i < sentMessages.size()) results.add(sentMessages.get(i));
                else if (i < storedMessages.size()) results.add(storedMessages.get(i));
            }
        }
        return results;
    }

    public boolean deleteMessageByHash(String hash) {
        int index = messageHashes.indexOf(hash);
        if (index != -1 && index < storedMessages.size()) {
            String removed = storedMessages.remove(index);
            messageHashes.remove(index);
            messageIDs.remove(index);
            return true;
        }
        return false;
    }

    public String displayFullReport() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sentMessages.size(); i++) {
            sb.append("Hash: ").append(messageHashes.get(i)).append("\n");
            sb.append("Recipient: ").append(messageIDs.get(i)).append("\n");
            sb.append("Message: ").append(sentMessages.get(i)).append("\n\n");
        }
        return sb.toString();
    }

    public void populateTestData() {
        addTestMessage("Did you get the cake?", "+27834557896", "Sent");
        addTestMessage("Where are you? You are late! I have asked you to be on time.", "+27838884567", "Stored");
        addTestMessage("Yohoooo, I am at your gate.", "+27834484567", "Disregard");
        addTestMessage("It is dinner time !", "0838884567", "Sent");
        addTestMessage("Ok, I am leaving without you.", "+27838884567", "Stored");
    }

    private void addTestMessage(String msg, String recipient, String flag) {
        String id = generateMessageID();
        String hash = createMessageHash(id, messageHashes.size(), msg);
        messageIDs.add(recipient);
        messageHashes.add(hash);

        switch (flag.toLowerCase()) {
            case "sent" -> sentMessages.add(msg);
            case "stored" -> {
                storedMessages.add(msg);
                storeMessage(id, hash, recipient, msg);
            }
            case "disregard" -> disregardedMessages.add(msg);
        }
    }
}
