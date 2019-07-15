package databute.databuter.network;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

public interface SessionAcceptor {

    InetSocketAddress localAddress();

    CompletableFuture<Void> bind(InetSocketAddress localAddress);

}
