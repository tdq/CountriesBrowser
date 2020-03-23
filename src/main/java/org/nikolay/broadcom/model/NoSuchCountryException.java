package org.nikolay.broadcom.model;

public class NoSuchCountryException extends RuntimeException {

    public NoSuchCountryException(String code) {
        super(" Country with provided code is not found: " + code);
    }
}
