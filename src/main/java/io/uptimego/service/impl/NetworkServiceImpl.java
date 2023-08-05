package io.uptimego.service.impl;

import io.uptimego.service.NetworkService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Service

public class NetworkServiceImpl implements NetworkService {
    @Override
    public InetAddress getByName(String host) throws UnknownHostException {
        return InetAddress.getByName(host);
    }

    @Override
    public boolean isReachable(InetAddress address, int timeout) throws IOException {
        return address.isReachable(timeout);
    }
}