package org.acme.rest;

import org.acme.rest.utils.EchoServer;
import org.acme.rest.utils.UdpUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

@Path("/greeting")
public class GreetingResource {


    @ConfigProperty(name = "udp.port", defaultValue = "4445")
    public int port;

    private EchoServer echoServer;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/start_udp")
    public String start() {
        if (echoServer == null) {
            echoServer = new EchoServer(port);
            echoServer.start();
            return "started";
        } else {
            return "already started";
        }

    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/stop_udp")
    public String stop() {
        if (echoServer != null && echoServer.isAlive()) {
            echoServer.end();
            return "stopped";
        } else {
            return "is not started";
        }

    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/udp")
    public String udp() {
        try {
            UdpUtils.broadcast("Hello", InetAddress.getByName("255.255.255.255"), port);
        } catch (IOException e) {
            e.printStackTrace();
            return "ko";
        }
        return "ok";
    }
}
