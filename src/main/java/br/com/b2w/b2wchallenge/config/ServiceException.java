package br.com.b2w.b2wchallenge.config;

public class ServiceException extends Exception {

    public ServiceException(String entity, String id) {
        super(String.format("%s with id %s does not exists and cannot be updated.", entity, id));
    }

}
