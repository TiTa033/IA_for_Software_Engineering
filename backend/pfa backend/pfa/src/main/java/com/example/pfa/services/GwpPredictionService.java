package com.example.pfa.services;

import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class GwpPredictionService {

    public double predictGwp(String inputJson) throws Exception {
        // Set up the command to run the Python script
        ProcessBuilder processBuilder = new ProcessBuilder("python", "C:/predict_GWP/predict_gwp.py");

        Process process = processBuilder.start();

        // Send JSON input to the Python script via stdin
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
            writer.write(inputJson);
            writer.flush();
        }

        // Capture the output of the Python script
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line);
        }

        // Wait for the process to finish
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            return Double.parseDouble(output.toString());
        } else {
            throw new Exception("Error running Python script");
        }
    }
}
