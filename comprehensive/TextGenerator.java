package comprehensive;

import java.io.File;
import java.util.List;

/**
 * The TextGenerator class serves as the main driver to demonstrate text generation
 * using a Markov Chain. It reads input from the command line to get file path, seed word,
 * number of words to generate, and an optional mode parameter.
 * @author Leo Yu
 * @version April 23, 2024
 */
public class TextGenerator {

    /**
     * Main method to process command line arguments and generate text.
     * It expects between 3 and 4 arguments:
     * 1. The path to the text file to be processed.
     * 2. The seed word to start the text generation.
     * 3. The number of words to generate.
     * 4. (Optional) The mode which determines the method of text generation: "one" or "all".
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args){
        if(args.length < 3 || args.length > 4){
            return; // Exit if the number of arguments is incorrect.
        }

        String filePath = args[0];
        String seed = args[1];
        int k = Integer.parseInt(args[2]);

        if(args.length == 3) {
            generateText(filePath, seed, k);
        }
        if(args.length == 4) {
            String generateAll = args[3];
            generateText(filePath, seed, k, generateAll);
        }
    }

    /**
     * Generates text based on a specified mode ("one" or "all").
     * This method initializes a MarkovChain, constructs it using the specified file,
     * and then prints the generated text based on the given seed and mode.
     *
     * @param filePath Path to the input file.
     * @param seed Initial seed word for text generation.
     * @param k Number of words to generate.
     * @param generateAll Mode of generation, "one" for most frequent next word or "all" for probabilistic choice.
     */
    public static void generateText(String filePath, String seed, int k, String generateAll){

        MarkovChain markovChain = new MarkovChain();
        File file = new File(filePath);
        markovChain.constructChain(file);

        List<String> output = markovChain.generateText(seed, k, generateAll);
        for (String word : output) {
            System.out.print(word + " ");
        }

    }

    /**
     * Simplified text generation without specifying a mode.
     * This method initializes a MarkovChain, constructs it using the specified file,
     * and prints the generated text, which repeatedly selects the most frequent subsequent words.
     *
     * @param filePath Path to the input file.
     * @param seed Initial seed word for text generation.
     * @param k Number of words to generate.
     */
    public static void generateText(String filePath, String seed, int k){

        MarkovChain markovChain = new MarkovChain();
        File file = new File(filePath);
        markovChain.constructChain(file);

        List<String> output = markovChain.generateText(seed, k);
        for (String word : output) {
            System.out.print(word + " ");
        }

    }

}
