package comprehensive;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class MarkovChainTimer {

    private static final String BASE_PATH = "CS2420/src/comprehensive/abc"; // Adjust this path as necessary.

    // Helper method to create a file with a given number of distinct words and file size
    private static File createInputFile(int numDistinctWords, int fileSizeKB, String fileName) throws IOException {
        File file = new File("abc");
        Random random = new Random();
        List<String> words = new ArrayList<>();
        for (int i = 0; i < numDistinctWords; i++) {
            words.add("Word" + i);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            int size = 0;
            while (size < fileSizeKB * 1024) {
                String word = words.get(random.nextInt(words.size()));
                writer.write(word + " ");
                size += word.length() + 1;
            }
        }

        return file;
    }

    // Test for increasing number of distinct words
    public static void testIncreasingDistinctWords() throws IOException {
        int[] numWords = {100, 200, 500, 1000, 2000, 10000, 1000000};
        for (int num : numWords) {
            File testFile = createInputFile(num, 100, "test_distinct_" + num + ".txt"); // 100KB files
            MarkovChain markovChain = new MarkovChain();
            long startTime = System.nanoTime();
            markovChain.constructChain(testFile);
            long endTime = System.nanoTime();
            System.out.println("Distinct Words: " + num + ", Time Taken: " + (endTime - startTime) / 1_000_000 + " ms");
        }
    }

    // Test for increasing file sizes with few distinct words
    public static void testIncreasingFileSizes() throws IOException {
        int[] fileSizes = {1, 10, 100, 1000, 10000, 1000000}; // In KB
        for (int size : fileSizes) {
            File testFile = createInputFile(100, size, "test_size_" + size + ".txt"); // 100 distinct words
            MarkovChain markovChain = new MarkovChain();
            long startTime = System.nanoTime();
            markovChain.constructChain(testFile);
            long endTime = System.nanoTime();
            System.out.println("File Size: " + size + " KB, Time Taken: " + (endTime - startTime) / 1_000_000 + " ms");
        }
    }

    // Test for increasing 'k' values in generateText
    public static void testIncreasingKValues(String filePath, String seed, String mode) throws IOException {
        int[] kValues = {1, 5, 10, 20, 50};
        MarkovChain markovChain = new MarkovChain();
        markovChain.constructChain(new File(filePath));

        for (int k : kValues) {
            long startTime = System.nanoTime();
            List<String> output = markovChain.generateText(seed, k, mode);
            long endTime = System.nanoTime();
            System.out.println("K Value: " + k + ", Mode: " + mode + ", Time Taken: " + (endTime - startTime) / 1_000_000 + " ms");
            System.out.println("Output: " + String.join(" ", output));
        }
    }

    public static void main(String[] args) {
        try {
            testIncreasingDistinctWords();
            testIncreasingFileSizes();
            testIncreasingKValues("abc", "b", "all");
            // Example call: testIncreasingKValues("path_to_large_file.txt", "exampleSeed", "all");
        } catch (IOException e) {
            System.err.println("Error during testing: " + e.getMessage());
        }
    }
}
