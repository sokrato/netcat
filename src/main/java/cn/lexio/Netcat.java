package cn.lexio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Netcat {
    private final int bufSize;

    public Netcat(int bufSize) {
        this.bufSize = bufSize;
    }

    private void copyIO(InputStream src, OutputStream dst) throws IOException {
        byte[] buf = new byte[bufSize];
        while (true) {
            int cnt = src.read(buf);
            if (cnt < 1) {
                break;
            }
            dst.write(buf, 0, cnt);
        }
    }

    private void closeSocket(Socket sock) {
        try {
            sock.close();
        } catch (IOException ex) {
            //
        }
    }

    private void pipe(Socket sock) {
        Thread main = Thread.currentThread();
        Thread sib = new Thread(() -> {
            try {
                copyIO(System.in, sock.getOutputStream());
            } catch (IOException ex) {
                //
            }
            closeSocket(sock);
            main.interrupt();
        });
        sib.setDaemon(true);
        sib.start();

        try {
            copyIO(sock.getInputStream(), System.out);
        } catch (IOException ex) {
            //
        }

        sib.interrupt();
        closeSocket(sock);
    }

    public void asServer(int port) throws IOException {
        try (ServerSocket ss = new ServerSocket(port, 1)) {
            Socket sock = ss.accept();
            pipe(sock);
        }
    }

    public void asClient(String host, int port) throws IOException {
        Socket sock = new Socket(host, port);
        pipe(sock);
    }

    public static void main(String[] args) {
        if (args.length != 2 || "-h".equals(args[0]) || "--help".equals(args[0])) {
            help();
            return;
        }

        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            System.err.println("invalid port number: " + args[1]);
            System.exit(1);
            return;
        }

        Netcat app = new Netcat(1024);
        try {
            if ("-l".equals(args[0])) {
                app.asServer(port);
            } else {
                app.asClient(args[0], port);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    private static void help() {
        System.out.println("This is a mimic of GNU netcat/nc");
        System.out.println("As server: nc -l port");
        System.out.println("As client: nc host port");
    }
}
