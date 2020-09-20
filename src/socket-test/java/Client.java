import java.net.*;
import java.io.*;


class Client {
    private static Socket clSock;
    private static PrintWriter out;
    private static BufferedReader in;

    public static void main(String[] args) {
        // init connection
        try {
            clSock = new Socket("127.0.0.1", 8080);
            out = new PrintWriter(clSock.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clSock.getInputStream()));

        } catch (Exception e) {}

        try {
            while (true) {
                out.println("ping");
                String response;
                while (true) {
                    try {
                        response = in.readLine();
                        break;
                    } catch (Exception e) {}
                }
                System.out.println(response);
            }
        } catch (Exception e) {}
        
        try {
            in.close();
            out.close();
            clSock.close();
        } catch (Exception e) {}
    }
}
