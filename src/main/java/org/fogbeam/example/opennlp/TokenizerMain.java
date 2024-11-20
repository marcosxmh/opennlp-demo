package org.fogbeam.example.opennlp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class TokenizerMain {
	// Logger instance
	private static final Logger logger = Logger.getLogger(TokenizerMain.class.getName());

	public static void main(String[] args) throws Exception {
		String inputFolderPath = "./input"; // Input folder
		String outputFilePath = "./output/output-tokens.txt"; // Output file

		File folder = new File(inputFolderPath);
		if (!folder.isDirectory()) {
			logger.warning("The input folder does not exist or is not a directory: " + inputFolderPath);
			return;
		}

		File outputDir = new File(outputFilePath).getParentFile();
		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}

		InputStream modelIn = new FileInputStream("models/en-token.model");
		TokenizerModel model = new TokenizerModel(modelIn);
		Tokenizer tokenizer = new TokenizerME(model);

		List<String> allTokens = new ArrayList<>();

		for (File file : folder.listFiles()) {
			if (file.isFile() && file.getName().endsWith(".txt")) {
				logger.info("Processing file: " + file.getName());
				try (BufferedReader br = new BufferedReader(new FileReader(file))) {
					StringBuilder content = new StringBuilder();
					String line;

					while ((line = br.readLine()) != null) {
						content.append(line).append(" ");
					}

					String[] tokens = tokenizer.tokenize(content.toString());
					for (String token : tokens) {
						allTokens.add(token);
					}
				} catch (IOException e) {
					logger.log(Level.SEVERE, "Error reading file: " + file.getName(), e);
				}
			}
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
			for (String token : allTokens) {
				writer.write(token);
				writer.newLine();
			}
		}

		logger.info("Tokens processed and saved in: " + outputFilePath);
		modelIn.close();
	}
}
