package databute.databuter.network;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

public interface SessionConnector {

    InetSocketAddress localAddress();

    InetSocketAddress remoteAddress();

    CompletableFuture<Void> connect(InetSocketAddress remoteAddress);

}
