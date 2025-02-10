package top.kunkun.mydb.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import top.kunkun.mydb.transport.Encoder;
import top.kunkun.mydb.transport.Packager;
import top.kunkun.mydb.transport.Transporter;

public class Launcher {
    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket socket = new Socket("127.0.0.1", 9999);
        Encoder e = new Encoder();
        Transporter t = new Transporter(socket);
        Packager packager = new Packager(t, e);

        Client client = new Client(packager);
        Shell shell = new Shell(client);
        shell.run();
    }
}
