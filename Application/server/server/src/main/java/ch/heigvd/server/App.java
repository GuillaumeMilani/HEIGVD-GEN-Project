package ch.heigvd.server;

import ch.heig.bdd.BDD;
import ch.heig.bdd.ILog;
import java.net.ServerSocket;
import java.rmi.server.UID;

/**
 *
 * @author Tony Clavien
 * @author Maxime Guillod
 *
 */
public class App implements ILog {

    private ServerSocket server;
    private final BDD bdd;
    private final Thread threadListenClient;
    private final UID uid;

    public App() {
        this.bdd = BDD.getInstance();
        this.uid = new UID();
        bdd.logInfo(this, "START MAIN APP");
        
        /*
        We start the thread which listen for new connection
        */
        threadListenClient = new Thread(new ListenClient());
        threadListenClient.start();
    }

    public UID getUid() {
        return uid;
    }

    public static void main(String[] args) {
        App app = new App();
    }

}