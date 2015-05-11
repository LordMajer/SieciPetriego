package com.petri.nets.archive;

import com.petri.nets.model.CustomGraph;

import javax.swing.*;
import java.io.*;

public class GraphWriter {

    public static boolean saveGraph(CustomGraph graph) {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showSaveDialog(fileChooser);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
                outputStream.writeObject(graph);
                outputStream.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
