package com.terpomo.wavy.marshal;

public interface IPipeMarshaller<T extends IMarshallable> extends IMarshaller<T> {
    Class<T> getPipeClass();
}
