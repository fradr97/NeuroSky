import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharsetEncoder;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;

public class ThinkGearSocketClient {
    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final int DEFAULT_PORT = 13854;

    private String host;
    private int port;
    private boolean connected;
    SocketChannel channel;
    Scanner in;

    public ThinkGearSocketClient() {
        this.host = DEFAULT_HOST;
        this.port = DEFAULT_PORT;
        this.connected = false;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public void connect() throws IOException {
        if (!this.connected) {
            System.out.println("connect() - Starting new connection...");
            this.channel = SocketChannel.open(new InetSocketAddress(this.host, this.port));

            CharsetEncoder enc = StandardCharsets.US_ASCII.newEncoder();
            String jsonCommand = "{\"enableRawOutput\": false, \"format\": \"Json\"}\n";
            this.channel.write(enc.encode(CharBuffer.wrap(jsonCommand)));

            this.in = new Scanner(channel);
            this.connected = true;
        } else {
            System.out.println("connect() - Already connected...");
        }
    }

    public boolean isDataAvailable() {
        if (this.connected) {
            return this.in.hasNextLine();
        } else {
            return false;
        }
    }

    public String getData() {
        return this.in.nextLine();
    }

    public void close() throws IOException {
        if (this.connected) {
            System.out.println("close() - Closing connection...");
            this.in.close();
            this.channel.close();
            this.connected = false;
        }
    }
}
