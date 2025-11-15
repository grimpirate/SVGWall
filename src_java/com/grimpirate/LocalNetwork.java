package com.grimpirate;

import java.net.InetAddress;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;

public class LocalNetwork
{
    private String networkInterface = null;
    private String inetAddress = null;

    public LocalNetwork()
    {
        try
        {
            for(NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces()))
            {
                if(networkInterface.isLoopback())
                    continue;
                if(!networkInterface.isUp())
                    continue;
                
                for(InetAddress inetAddress : Collections.list(networkInterface.getInetAddresses()))
                {
                    if(!(inetAddress instanceof Inet4Address))
                        continue;
                    if(inetAddress.isLinkLocalAddress())
                        continue;

                    this.networkInterface = networkInterface.getDisplayName();
                    this.inetAddress = inetAddress.getHostAddress();
                }
            }
        }
        catch(SocketException ex)
        {
            this.networkInterface = "ERROR";
            this.inetAddress = "ERROR";
        }
    }

    public String getInetAddress()
    {
        return inetAddress;
    }

    public String getNetworkInterface()
    {
        return networkInterface;
    }
}