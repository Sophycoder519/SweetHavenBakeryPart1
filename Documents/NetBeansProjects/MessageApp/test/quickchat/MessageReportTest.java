package quickchat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MessageReportTest {
    Message msg;

    @BeforeEach
    public void setup() {
        msg = new Message();
        msg.populateTestData();
    }

    @Test
    public void testSentMessagesArray() {
        assertTrue(msg.sentMessages.contains("Did you get the cake?"));
        assertTrue(msg.sentMessages.contains("It is dinner time !"));
    }

    @Test
    public void testLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.", msg.getLongestSentMessage());
    }

    @Test
    public void testSearchByMessageID() {
        String result = msg.searchByMessageID("0838884567");
        assertEquals("It is dinner time !", result);
    }

    @Test
    public void testSearchByRecipient() {
        List<String> results = msg.searchByRecipient("+27838884567");
        assertTrue(results.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(results.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteMessageByHash() {
        String hashToDelete = msg.messageHashes.get(1);
        boolean deleted = msg.deleteMessageByHash(hashToDelete);
        assertTrue(deleted);
    }

    @Test
    public void testDisplayFullReport() {
        String report = msg.displayFullReport();
        assertTrue(report.contains("Hash:"));
        assertTrue(report.contains("Recipient:"));
        assertTrue(report.contains("Message:"));
    }
}
