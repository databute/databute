package databute.databuter.network.message;

import databute.databuter.network.Session;

public interface MessageHandler<S extends Session, M extends Message> {

    S session();

    void handle(M message);

}
