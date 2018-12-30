package com.git.hui.fix.core.endpoint;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Created by @author yihui in 13:46 18/12/30.
 */
public class BasicHttpServer {
    private static ExecutorService bootstrapExecutor = Executors.newSingleThreadExecutor();
    private static ExecutorService taskExecutor;
    private static int PORT = 9999;

    static void startHttpServer() {
        int nThreads = Runtime.getRuntime().availableProcessors();
        taskExecutor =
                new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(100),
                        new ThreadPoolExecutor.DiscardPolicy());

        while (true) {
            try {
                ServerSocket serverSocket = new ServerSocket(PORT);
                bootstrapExecutor.submit(new ServerThread(serverSocket));
                break;
            } catch (Exception e) {
                try {
                    //重试
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        bootstrapExecutor.shutdown();
    }

    private static class ServerThread implements Runnable {

        private ServerSocket serverSocket;

        public ServerThread(ServerSocket s) throws IOException {
            this.serverSocket = s;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Socket socket = this.serverSocket.accept();
                    HttpTask eventTask = new HttpTask(socket);
                    taskExecutor.submit(eventTask);
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
}
