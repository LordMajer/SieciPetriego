package com.petri.nets.archive;

import com.petri.nets.model.CustomGraph;

import javax.swing.*;
import java.io.*;

public class GraphReader {

    public static CustomGraph loadGraph(CustomGraph currentGraph) {       
        JFileChooser fileChooser = new JFileChooser();        
        try {
            
            StringBuilder initialDirectory = new StringBuilder();
            initialDirectory.append(new File(".").getCanonicalPath());
            initialDirectory.append(File.separator);
            initialDirectory.append("src");
            initialDirectory.append(File.separator);
            initialDirectory.append("main");
            initialDirectory.append(File.separator);
            initialDirectory.append("resources");
            initialDirectory.append(File.separator);
            initialDirectory.append("models");

            fileChooser.setCurrentDirectory(new File(initialDirectory.toString()));
            
        } catch (Exception e) {
            
        }
             
        int returnVal = fileChooser.showOpenDialog(fileChooser);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                CustomGraph loadedGraph = (CustomGraph) inputStream.readObject();
                JOptionPane.showMessageDialog(null, "Graf został wczytany", "INFORMACJA", JOptionPane.INFORMATION_MESSAGE);
                return loadedGraph;
            } catch (ClassNotFoundException | IOException e) {
                JOptionPane.showMessageDialog(null, "Wczytywanie grafu nie powiodło się!!!", "BŁĄD", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
        return currentGraph;
    }
}
