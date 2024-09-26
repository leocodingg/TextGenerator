package comprehensive;

import java.io.*;
import java.util.*;

/**
 * This class implements a Markov chain for text generation. It reads an input text,
 * builds a model of word transitions, and then uses this model to generate new text.
 * The text transitions and their frequencies are stored in a nested HashMap.
 * @author Leo Yu
 * @version April 23, 2024
 */
public class MarkovChain {

    /**
     * This is the main data structure where the Markov chain stores the transitions
     * between words. The outer HashMap keys are words, and each key maps to another
     * HashMap. This inner HashMap's keys are the words that follow the outer key,
     * with values representing the frequency of this transition.
     */
    private HashMap<String, HashMap<String, Integer>> chain;

    /**
     * Constructor initializes the Markov chain's main data structure.
     */
    public MarkovChain() {
        chain = new HashMap<>();
    }

    /**
     * Reads an input file line by line, processes it to extract words, and builds
     * the transition model. Words are normalized to lower case, split by spaces,
     * and non-alphanumeric characters are ignored. This method updates the `chain`
     * with counts of word transitions.
     *
     * @param input File to read and construct the chain from.
     */
    public void constructChain(File input) {
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(input));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.toLowerCase();

                String[] tokens = line.split("\\s+");
                List<String> words = new ArrayList<>();
                for (String token : tokens) {
                    if (!token.isEmpty()) {
                        String word = token.split("[^a-zA-Z0-9_]")[0];
                        if (!word.isEmpty()) {
                            words.add(word);
                        }
                    }
                }

                String previousWord = null;
                for (String word : words) {
                    if (previousWord != null) {
                        HashMap<String, Integer> currentMap = chain.getOrDefault(previousWord, new HashMap<>());
                        currentMap.put(word, currentMap.getOrDefault(word, 0) + 1);
                        chain.put(previousWord, currentMap);
                    }
                    previousWord = word;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates text based on the constructed Markov chain starting from a given seed word.
     * The text length is determined by `k`. This method can behave in two modes ("one" and "all"):
     * - "one" mode returns the most frequent subsequent word.
     * - "all" mode selects the next word based on a probability proportional to the transition frequency.
     *
     * @param seed Starting word for the generated text.
     * @param k Length of the generated text.
     * @param mode Mode of generation ("one" or "all").
     * @return A list of words representing the generated text.
     */
    public List<String> generateText(String seed, int k, String mode) {
        List<String> result = new ArrayList<>();
        result.add(seed);
        String current = seed;

        for (int i = 1; i < k; i++) {
            HashMap<String, Integer> nextWords = chain.get(current);
            if (nextWords == null || nextWords.isEmpty()) {
                current = seed; // wrap around if no next words
                result.add(seed);
            } else {
                current = getNextWord(nextWords, mode, current);
                result.add(current);
            }
        }
        return result;
    }

    /**
     * Simplified text generation method that returns the most frequent subsequent words
     * from the seed word without any probabilistic variation. It always selects the top
     * `k` frequent words following the seed.
     *
     * @param seed Starting word for the generated text.
     * @param k Number of words to generate.
     * @return A list of the top k words following the seed word.
     */
    public List<String> generateText(String seed, int k) {
        List<String> results = new ArrayList<>();
        HashMap<String, Integer> nextWords = chain.get(seed);

        if (nextWords == null || nextWords.isEmpty()) {
            for (int i = 0; i < k; i++) {
                results.add(seed);
            }
            return results;
        }

        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(nextWords.entrySet());
        sortedEntries.sort(Map.Entry.<String, Integer>comparingByValue().reversed().thenComparing(Map.Entry::getKey));

        for (int i = 0; i < Math.min(k, sortedEntries.size()); i++) {
            results.add(sortedEntries.get(i).getKey());
        }

        return results;
    }

    /**
     * Selects the next word based on the specified mode ("one" or "all"). The "one" mode
     * returns the word with the highest transition frequency, while "all" mode uses
     * a weighted probability distribution based on all possible transitions.
     *
     * @param nextWords HashMap of words and their corresponding transition frequencies.
     * @param mode Selection mode.
     * @param seed Current word.
     * @return The next word based on the chosen mode.
     */
    public String getNextWord(HashMap<String, Integer> nextWords, String mode, String seed) {
        Random random = new Random();
        if (mode.equals("one")) {
            return Collections.max(nextWords.entrySet(), Comparator.comparingInt((Map.Entry<String, Integer> e) -> -e.getValue()).thenComparing(Map.Entry::getKey)).getKey();
        } else if (mode.equals("all")) {
            int total = 0;
            for (Map.Entry<String, Integer> entry : nextWords.entrySet()) {
                total += entry.getValue();
            }

            double rand = random.nextDouble();
            double cumulativeProbability = 0.0;
            for (Map.Entry<String, Integer> entry : nextWords.entrySet()) {
                double probability = (double) entry.getValue() / total;
                cumulativeProbability += probability;
                if (rand <= cumulativeProbability) {
                    return entry.getKey();
                }
            }
        }
        return seed; // Fallback to seed if no valid transition found.
    }
}
