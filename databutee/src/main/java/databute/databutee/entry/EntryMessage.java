package databute.databutee.entry;

import databute.databutee.network.message.Message;

public interface EntryMessage extends Message {

    String id();

    String key();

}
