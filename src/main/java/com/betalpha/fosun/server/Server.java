package com.betalpha.fosun.server;

import com.google.common.collect.Lists;
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
import java.util.List;
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

    public List<String> getBloombergRating(String isin) {
        List<String> inputLines = Lists.newArrayList();
        try {
            log.info("send isin:{}", isin);
            out.println(isin);
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if ("end".equals(inputLine)) {
                    break;
                }
                inputLines.add(inputLine);
            }
            log.info("receive rating:{}", inputLines);
        } catch (IOException e) {
            log.error("send isin error", e);
        }
        return inputLines;
    }

    @PreDestroy
    public void disconnect() throws Exception {
        log.info("disconnect");
        PrintWriter printWriter = new PrintWriter(client.getOutputStream(), true);
        printWriter.println("disconnect");
        printWriter.close();
        client.close();
        serverSocket.close();
    }
}
