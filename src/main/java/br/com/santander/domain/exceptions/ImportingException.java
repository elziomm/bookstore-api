package br.com.santander.domain.exceptions;

public class ImportingException extends RuntimeException {

    public ImportingException(final String message) {
        super(message);
    }
}
