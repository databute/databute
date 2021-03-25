package databute.databuter.entry;

import databute.network.message.Message;

public interface EntryMessage extends Message {

    String id();

    String key();

}
