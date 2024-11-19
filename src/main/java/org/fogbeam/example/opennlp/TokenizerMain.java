package org.fogbeam.example.opennlp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class TokenizerMain {
	public static void main(String[] args) throws Exception {
		// Hardcoded paths
		String inputFolderPath = "./input"; // Folder containing example.txt
		String outputFilePath = "./output/output-tokens.txt"; // Output file path

		// Ensure the input folder exists
		File folder = new File(inputFolderPath);
		if (!folder.isDirectory()) {
			System.out.println("The input folder does not exist or is not a directory: " + inputFolderPath);
			return;
		}

		// Ensure the output directory exists (create it if necessary)
		File outputDir = new File(outputFilePath).getParentFile();
		if (!outputDir.exists()) {
			outputDir.mkdirs(); // Create directories if they do not exist
		}

		// Load the tokenizer model
		InputStream modelIn = new FileInputStream("models/en-token.model");
		TokenizerModel model = new TokenizerModel(modelIn);
		Tokenizer tokenizer = new TokenizerME(model);

		// List to store all tokens from all files
		List<String> allTokens = new ArrayList<>();

		// Loop through each file in the input folder
		for (File file : folder.listFiles()) {
			// Only process files with ".txt" extension
			if (file.isFile() && file.getName().endsWith(".txt")) {
				System.out.println("Processing file: " + file.getName());
				try (BufferedReader br = new BufferedReader(new FileReader(file))) {
					StringBuilder content = new StringBuilder();
					String line;

					// Read the file line by line and append to content
					while ((line = br.readLine()) != null) {
						content.append(line).append(" ");
					}

					// Tokenize the file content
					String[] tokens = tokenizer.tokenize(content.toString());
					for (String token : tokens) {
						allTokens.add(token); // Add tokens to the list
					}
				}
			}
		}

		// Write all tokens to the output file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
			for (String token : allTokens) {
				writer.write(token);
				writer.newLine(); // Write each token on a new line
			}
		}

		System.out.println("Tokens processed and saved in: " + outputFilePath);

		// Close the model input stream
		modelIn.close();
	}
}

