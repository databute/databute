package databute.databutee.network;

import com.google.common.base.MoreObjects;
import databute.databutee.Databutee;
import databute.databutee.network.message.Message;
import io.netty.channel.socket.SocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

import static com.google.common.base.Preconditions.checkNotNull;

public class DatabuterSession {

    private final Databutee databutee;
    private final SocketChannel channel;
    private final InetSocketAddress localAddress;
    private final InetSocketAddress remoteAddress;

    public DatabuterSession(Databutee databutee, SocketChannel channel) {
        this.databutee = checkNotNull(databutee, "databutee");
        this.channel = checkNotNull(channel, "channel");
        this.localAddress = checkNotNull(channel.localAddress(), "localAddress");
        this.remoteAddress = checkNotNull(channel.remoteAddress(), "remoteAddress");
    }

    public Databutee databutee() {
        return databutee;
    }

    public CompletableFuture<Void> send(Message message) {
        checkNotNull(message, "message");

        final CompletableFuture<Void> future = new CompletableFuture<>();
        channel.writeAndFlush(message).addListener(writeFuture -> {
            if (writeFuture.isSuccess()) {
                future.complete(null);
            } else {
                future.completeExceptionally(writeFuture.cause());
            }
        });
        return future;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("channel", channel)
                .toString();
    }
}
