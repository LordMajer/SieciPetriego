package com.petri.nets.archive;

import com.petri.nets.model.CustomGraph;

import javax.swing.*;
import java.io.*;

public class GraphReader {

    public static CustomGraph loadGraph(CustomGraph currentGraph) {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(fileChooser);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                return (CustomGraph) inputStream.readObject();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
        return currentGraph;
    }
}
