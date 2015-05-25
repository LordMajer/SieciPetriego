package com.petri.nets.helpers.common;

import java.io.*;

public class ObjectDeepCopier {

    public static <T extends Serializable> T getCopyOf(T toCopy) {
        T graphCopy = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(toCopy);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            graphCopy = (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Próba kopiowania obiektu nie powiodła się");
            e.printStackTrace();
        }
        return graphCopy;
    }
}
