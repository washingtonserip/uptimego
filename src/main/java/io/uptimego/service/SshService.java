package io.uptimego.service;

public interface SshService {
    public void establishSshSession(String username, String host, String password) throws Exception;
}
