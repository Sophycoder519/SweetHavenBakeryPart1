package quickchat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    Message msg = new Message();

    @Test
    public void testMessageLengthSuccess() {
        String shortMessage = "Hi Mike, can you join us for dinner tonight?";
        assertTrue(shortMessage.length() <= 250);
        String result = shortMessage.length() <= 250 ? "Message ready to send." : "Message too long.";
        assertEquals("Message ready to send.", result);
    }

    @Test
    public void testMessageLengthFailure() {
        String longMessage = "x".repeat(260);
        int extra = longMessage.length() - 250;
        String result = "Message exceeds 250 characters by " + extra + ", please reduce size.";
        assertEquals("Message exceeds 250 characters by 10, please reduce size.", result);
    }

    @Test
    public void testRecipientSuccess() {
        String recipient = "+27831234567";
        boolean valid = recipient.matches("^\\+\\d{1,3}\\d{7,9}$");
        String result = valid ? "Cell phone number successfully captured." : "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        assertEquals("Cell phone number successfully captured.", result);
    }

    @Test
    public void testRecipientFailure() {
        String recipient = "0831234567";
        boolean valid = recipient.matches("^\\+\\d{1,3}\\d{7,9}$");
        String result = valid ? "Cell phone number successfully captured." : "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", result);
    }

    @Test
    public void testMessageHashExact() {
        String hash = msg.createMessageHash("0012345678", 0, "Hi Mike, can you join us for dinner tonight");
        assertEquals("00:0:HITONIGHT", hash);
    }

    @Test
    public void testMessageHashInLoop() {
        String[] messages = {
            "Hi Mike, can you join us for dinner tonight",
            "Hello world from the gate",
            "Keegan, I made dinner",
            "Let’s test more messages"
        };

        for (int i = 0; i < messages.length; i++) {
            String hash = msg.createMessageHash("0099887766", i, messages[i]);
            assertTrue(hash.matches("^\\d{2}:" + i + ":[A-Z0-9]+$"));
        }
    }

    @Test
    public void testMessageIDGenerated() {
        String id = msg.generateMessageID();
        assertNotNull(id);
        assertTrue(id.length() <= 10);
        System.out.println("Message ID generated: " + id);
    }

    @Test
    public void testMessageSendOption() {
        String result = msg.SentMessage("Test message", "Send");
        assertEquals("Message successfully sent.", result);
    }

    @Test
    public void testMessageDisregardOption() {
        String result = msg.SentMessage("Test message", "Disregard");
        assertEquals("Message disregarded.", result);
    }

    @Test
    public void testMessageStoreOption() {
        String result = msg.SentMessage("Test message", "Store");
        assertEquals("Message stored.", result);
    }
}
