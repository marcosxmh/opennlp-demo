package org.fogbeam.example.opennlp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

/**
 * A class to demonstrate tokenization using Apache OpenNLP.
 * The program processes text files in a specified input folder,
 * tokenizes the content, and writes the tokens to a single output file.
 */
public class TokenizerMain {

	/**
	 * The main method that orchestrates the tokenization process.
	 *
	 * @param args Command-line arguments (not used in this implementation).
	 * @throws Exception if an error occurs during file or model processing.
	 */
	public static void main(String[] args) throws Exception {
		// Define paths for the input folder and output file
		String inputFolderPath = "./input"; // Folder containing the input text files
		String outputFilePath = "./output/output-tokens.txt"; // Path to the output file

		// Ensure the input folder exists
		File folder = new File(inputFolderPath);
		if (!folder.isDirectory()) {
			System.out.println("The input folder does not exist or is not a directory: " + inputFolderPath);
			return;
		}

		// Ensure the output directory exists, create it if necessary
		File outputDir = new File(outputFilePath).getParentFile();
		if (!outputDir.exists()) {
			outputDir.mkdirs(); // Create directories if they do not exist
		}

		// Load the tokenizer model from the specified file
		InputStream modelIn = new FileInputStream("models/en-token.model");
		TokenizerModel model = new TokenizerModel(modelIn);
		Tokenizer tokenizer = new TokenizerME(model);

		// List to store all tokens extracted from the files
		List<String> allTokens = new ArrayList<>();

		// Loop through each file in the input folder
		for (File file : folder.listFiles()) {
			// Only process files with ".txt" extension
			if (file.isFile() && file.getName().endsWith(".txt")) {
				System.out.println("Processing file: " + file.getName());
				try (BufferedReader br = new BufferedReader(new FileReader(file))) {
					StringBuilder content = new StringBuilder();
					String line;

					// Read the file line by line and append to the content
					while ((line = br.readLine()) != null) {
						content.append(line).append(" ");
					}

					// Tokenize the content of the file
					String[] tokens = tokenizer.tokenize(content.toString());
					for (String token : tokens) {
						allTokens.add(token); // Add each token to the list
					}
				}
			}
		}

		// Write all tokens to the output file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
			for (String token : allTokens) {
				writer.write(token); // Write each token
				writer.newLine(); // Start a new line after each token
			}
		}

		System.out.println("Tokens processed and saved in: " + outputFilePath);

		// Close the tokenizer model input stream
		modelIn.close();
	}
}


