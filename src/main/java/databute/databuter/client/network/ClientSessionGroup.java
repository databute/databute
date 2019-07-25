package databute.databuter.client.network;

import com.google.common.collect.Maps;
import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClientSessionGroup {

    private static final Logger logger = LoggerFactory.getLogger(ClientSessionGroup.class);

    private final Map<ChannelId, ClientSession> sessions;
    private final Map<ChannelId, ClientSession> listeningSessions;

    public ClientSessionGroup() {
        this.sessions = Maps.newConcurrentMap();
        this.listeningSessions = Maps.newConcurrentMap();
    }

    public boolean add(ClientSession session) {
        checkNotNull(session, "session");

        boolean added = addSession(session);
        if (session.isListening()) {
            added = (added || addListeningSession(session));
        }

        return added;
    }

    public boolean addSession(ClientSession session) {
        checkNotNull(session, "session");

        final boolean added = (sessions.putIfAbsent(session.channel().id(), session) == null);
        if (added) {
            if (logger.isDebugEnabled()) {
                logger.debug("Added client session {}", session);
            } else {
                logger.info("Added client session {}", session.channel().id());
            }
        }

        return added;
    }

    public boolean addListeningSession(ClientSession session) {
        checkNotNull(session, "session");

        if (session.isListening()) {
            final boolean added = (listeningSessions.putIfAbsent(session.channel().id(), session) == null);
            if (added) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Added listening client session {}", session);
                } else {
                    logger.info("Added listening clinet session {}", session.channel().id());
                }
            }

            return added;
        } else {
            return false;
        }
    }
}
