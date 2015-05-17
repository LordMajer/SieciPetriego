package com.petri.nets.helpers.common;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectDeepCopier {

    public static  <T extends Serializable> T getCopyOf(T toCopy) {
        ByteOutputStream byteOutputStream = new ByteOutputStream();
        ObjectOutputStream outputStream;
        T graphCopy = null;
        try {
            outputStream = new ObjectOutputStream(byteOutputStream);
            outputStream.writeObject(toCopy);
            ObjectInputStream inputStream = new ObjectInputStream(byteOutputStream.newInputStream());
            graphCopy = (T) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Próba kopiowania obiektu nie powiodła się");
            e.printStackTrace();
        }
        return graphCopy;
    }
}
