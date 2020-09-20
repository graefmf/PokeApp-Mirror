import java.net.*;
import java.io.*;


class Server {
    private static ServerSocket servSock;
    private static Socket clSock;
    private static PrintWriter out;
    private static BufferedReader in;

    public static void main(String[] args) {

        try {
            servSock = new ServerSocket(8080);
            clSock = servSock.accept();
            out = new PrintWriter(clSock.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clSock.getInputStream()));
        } catch (Exception e) {}

        try {
            while (true) {
                String response;
                while (true) {
                    try {
                        response = in.readLine();
                        break;
                    } catch (Exception e) {}
                }
                System.out.println(response);
                out.println("pong");
            }
        } catch (Exception e) {}

        try {
            in.close();
            out.close();
            servSock.close();
            clSock.close();
        } catch (Exception e) {}
    }
}
