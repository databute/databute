package databute.databutee;

import io.netty.channel.EventLoopGroup;

import java.net.InetSocketAddress;
import java.util.List;

public interface DatabuteeConfiguration {

    EventLoopGroup loopGroup();

    List<InetSocketAddress> addresses();

}
