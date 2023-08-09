package io.uptimego.service;

public interface SshService {
    void establishSshSession(String username, String host, String password) throws Exception;
}
