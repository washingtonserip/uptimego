package io.uptimego.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public interface NetworkService {
    public InetAddress getByName(String hostname) throws UnknownHostException;
    boolean isReachable(InetAddress address, int timeout) throws IOException;
}
