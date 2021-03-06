package databute.databutee;

import databute.databutee.entry.Entry;

public interface Callback {

    void onSuccess(Entry entry);

    void onFailure(Exception e);

}
