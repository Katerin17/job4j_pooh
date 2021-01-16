import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.http.HttpRequest;

public class Connection implements Closeable {
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public void send(HttpRequest message) throws IOException {
        out.writeObject(message);
    }

    public HttpRequest receive() throws ClassNotFoundException, IOException {
        return (HttpRequest) in.readObject();
    }

    @Override
    public void close() throws IOException {
        out.flush();
        in.close();
        out.close();
        socket.close();
    }

}
