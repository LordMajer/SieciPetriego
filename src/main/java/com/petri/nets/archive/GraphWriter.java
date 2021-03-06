package com.petri.nets.archive;

import com.petri.nets.model.CustomGraph;

import javax.swing.*;
import java.io.*;

public class GraphWriter {

    public static void saveGraph(CustomGraph graph) {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showSaveDialog(fileChooser);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
                outputStream.writeObject(graph);
                outputStream.flush();
                JOptionPane.showMessageDialog(null, "Graf został zapisany", "INFORMACJA", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Zapisywanie grafu nie powiodło się!!!", "BŁĄD", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
