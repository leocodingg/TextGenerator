package comprehensive;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

public class MarkovChainTest {

    private MarkovChain createAndFillMarkovChain() throws Exception {
        String content = "the cat sat on the mat. the cat is a bat";
        File tempFile = Files.createTempFile("test", ".txt").toFile();
        Files.writeString(tempFile.toPath(), content);
        MarkovChain mc = new MarkovChain();
        mc.constructChain(tempFile);
        return mc;
    }

//    @Test
//    void testConstructChainWithValidFile() throws Exception {
//        MarkovChain mc = createAndFillMarkovChain();
//        assertEquals(2, mc.chain.get("the").get("cat").intValue());
//        assertEquals(1, mc.chain.get("the").get("mat").intValue());
//        assertEquals(1, mc.chain.get("cat").get("is").intValue());
//    }

    @Test
    void testGenerateTextWithValidInput() throws Exception {
        MarkovChain mc = createAndFillMarkovChain();
        List<String> output = mc.generateText("the", 3, "all");
        assertTrue(output.size() == 3);
        assertEquals("the", output.get(0));
    }

    @Test
    void testGenerateTextWithNonexistentSeed() throws Exception {
        MarkovChain mc = createAndFillMarkovChain();
        List<String> output = mc.generateText("dog", 3, "all");
        assertTrue(output.isEmpty() || output.equals(List.of("dog")));
    }
    @Test
    void test1() throws Exception{
       String string =  "the cat_sat on the! mat. the ca't is a bat";
        File tempFile = Files.createTempFile("test", ".txt").toFile();
        Files.writeString(tempFile.toPath(), string);
        MarkovChain mc = new MarkovChain();
        mc.constructChain(tempFile);

    }

    @Test
    void testGenerateTextWithZeroK() throws Exception {
        MarkovChain mc = createAndFillMarkovChain();
        List<String> output = mc.generateText("", 0, "all");
        assertTrue(output.isEmpty());
    }

    @Test
    void testGenerateTextModeOne() throws Exception {
        MarkovChain mc = createAndFillMarkovChain();
        List<String> output = mc.generateText("d", 3, "one");
        assertEquals(List.of("the", "cat", "sat"), output);
    }

    @Test
    void testGenerateTextWithoutModeArgument() throws Exception {
        MarkovChain mc = createAndFillMarkovChain();
        List<String> output = mc.generateText("the", 3);
        assertEquals(List.of("cat", "mat", "is"), output); // Assuming sorted by descending probability and lexicographically
    }

    @Test
    void testGenerateTextModeAll() throws Exception {
        MarkovChain mc = createAndFillMarkovChain();
        List<String> output = mc.generateText("the", 100, "all");
        assertEquals(100, output.size());
        assertEquals("the", output.get(0)); // Always starts with seed
    }

    @Test
    void testGenerateTextWithEmptyChain() throws Exception {
        MarkovChain mc = new MarkovChain(); // Empty chain
        List<String> output = mc.generateText("anything", 1, "all");
        assertTrue(output.isEmpty() || output.equals(List.of("anything")));
    }

//    @Test
//    void testGenerateTextWithBoundaryValues() throws Exception {
//        MarkovChain mc = createAndFillMarkovChain();
//        List<String> output = mc.generateText("the", Integer.MAX_VALUE, "all");
//        assertNotNull(output);
////        assertTrue(output.size() <= Integer.MAX_VALUE);
//        assertEquals("the", output.get(0)); // Always starts with seed
//    }

    @Test
    void testChainReactsToNoNextWordsCorrectly() throws Exception {
        MarkovChain mc;
    }
}