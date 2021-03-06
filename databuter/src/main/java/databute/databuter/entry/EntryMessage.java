package databute.databuter.entry;

import databute.databuter.network.message.Message;

public interface EntryMessage extends Message {

    String id();

    String key();

}
