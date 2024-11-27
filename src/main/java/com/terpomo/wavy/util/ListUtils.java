package com.terpomo.wavy.util;

import com.terpomo.wavy.IWavyDisposable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    public static <T extends IWavyDisposable> List<T> buildNewList(int numOfItems, Class<T> objType, List<T> currentObjs, Constructor<T> constructor, Object[] initArgs) {
        T p = null;
        int currentSize = currentObjs.size();
        List<T> newList = new ArrayList<>();
        for (int i = 0; i < Math.min(currentSize, numOfItems); i++) {
            newList.add(currentObjs.get(i));
        }
        for (int i = 0; i < currentSize-numOfItems; i++) {
            p = currentObjs.get(currentObjs.size()-1);
            p.wavyDispose();
            currentObjs.remove(p);
        }
        for (int i = 0; i < numOfItems-currentSize; i++) {
            try {
                p = constructor.newInstance(initArgs);
                newList.add(p);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return newList;
    }

}
