package databute.databuter.entity;

import databute.databuter.network.message.Message;

public interface EntityMessage extends Message {

    String id();

    String key();

}
