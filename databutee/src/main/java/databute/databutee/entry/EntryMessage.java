package databute.databutee.entry;

import databute.network.message.Message;

public interface EntryMessage extends Message {

    String id();

    String key();

}
