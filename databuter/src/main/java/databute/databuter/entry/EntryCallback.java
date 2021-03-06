package databute.databuter.entry;

public interface EntryCallback {

    void onSuccess(Entry entry);

    void onFailure(Exception e);

}
