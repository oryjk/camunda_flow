package com.betalpha.fosun.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * created on 2018/5/15
 *
 * @author huzongpeng
 */
@Slf4j
@Service
public class Server {

    private ServerSocket serverSocket;
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;
    @Value("${port}")
    private String port;

    @PostConstruct
    public void initClient() throws Exception {
        serverSocket = new ServerSocket(Integer.parseInt(port));
        Executors.newSingleThreadExecutor().execute(this::getClient);
    }

    private void getClient() {
        log.info("init client");
        try {
            while (true) {
                client = serverSocket.accept();
                log.info("accept client");
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            }
        } catch (IOException e) {
            log.error("get client error", e);
        }
    }

    public String getBloombergRating(String isin) {
        try {
            log.info("send isin:{}", isin);
            out.println(isin);
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                log.info("receive rating:{}", inputLine);
                return inputLine;
            }
        } catch (IOException e) {
            log.error("send isin error", e);
        }
        return null;
    }

    @PreDestroy
    public void disconnect() throws Exception {
        log.info("disconnect");
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        out.println("disconnect");
        out.close();
        client.close();
        serverSocket.close();
    }
}
