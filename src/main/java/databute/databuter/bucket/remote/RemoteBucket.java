package databute.databuter.bucket.remote;

import com.google.common.base.MoreObjects;
import databute.databuter.Databuter;
import databute.databuter.bucket.Bucket;
import databute.databuter.bucket.BucketConfiguration;
import databute.databuter.cluster.remote.RemoteClusterNode;
import databute.databuter.entry.*;
import databute.databuter.entry.delete.DeleteEntryMessage;
import databute.databuter.entry.expire.ExpireEntryMessage;
import databute.databuter.entry.get.GetEntryMessage;
import databute.databuter.entry.set.SetEntryMessage;
import databute.databuter.entry.type.*;

import java.time.Instant;

public class RemoteBucket extends Bucket {

    public RemoteBucket(BucketConfiguration configuration) {
        super(configuration);
    }

    @Override
    public void get(EntryKey entryKey, EntryCallback callback) {
        final GetEntryMessage getEntryMessage = new GetEntryMessage(entryKey.key());
        final RemoteClusterNode remoteClusterNode = (RemoteClusterNode) activeNode();
        final EntryMessageDispatcher dispatcher = Databuter.instance().entryMessageDispatcher();

        dispatcher.enqueue(getEntryMessage.id(), new EntryCallback() {
            @Override
            public void onSuccess(Entry entry) {
                callback.onSuccess(entry);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
        remoteClusterNode.session().send(getEntryMessage);
    }

    @Override
    public void add(Entry entry, EntryCallback callback) {
        SetEntryMessage setEntryMessage = null;
        if (entry instanceof IntegerEntry) {
            setEntryMessage = new SetEntryMessage(entry, EntryValueType.INTEGER);
        } else if (entry instanceof LongEntry) {
            setEntryMessage = new SetEntryMessage(entry, EntryValueType.LONG);
        } else if (entry instanceof StringEntry) {
            setEntryMessage = new SetEntryMessage(entry, EntryValueType.STRING);
        } else if (entry instanceof ListEntry) {
            setEntryMessage = new SetEntryMessage(entry, EntryValueType.LIST);
        } else if (entry instanceof SetEntry) {
            setEntryMessage = new SetEntryMessage(entry, EntryValueType.SET);
        } else if (entry instanceof DictionaryEntry) {
            setEntryMessage = new SetEntryMessage(entry, EntryValueType.DICTIONARY);
        }


        final RemoteClusterNode remoteClusterNode = (RemoteClusterNode) activeNode();
        final EntryMessageDispatcher dispatcher = Databuter.instance().entryMessageDispatcher();

        dispatcher.enqueue(setEntryMessage.id(), new EntryCallback() {
            @Override
            public void onSuccess(Entry entry) {
                callback.onSuccess(entry);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
        remoteClusterNode.session().send(setEntryMessage);
    }

    @Override
    public void remove(EntryKey entryKey, EntryCallback callback) {
        final DeleteEntryMessage deleteEntryMessage = new DeleteEntryMessage(entryKey.key());
        final RemoteClusterNode remoteClusterNode = (RemoteClusterNode) activeNode();
        final EntryMessageDispatcher dispatcher = Databuter.instance().entryMessageDispatcher();

        dispatcher.enqueue(deleteEntryMessage.id(), new EntryCallback() {
            @Override
            public void onSuccess(Entry entry) {
                callback.onSuccess(entry);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
        remoteClusterNode.session().send(deleteEntryMessage);
    }

    @Override
    public void expire(EntryKey entryKey, Instant expirationTimestamp, EntryCallback callback) {
        final ExpireEntryMessage expireEntryMessage = new ExpireEntryMessage(entryKey.key(), expirationTimestamp);
        final RemoteClusterNode remoteClusterNode = (RemoteClusterNode) activeNode();
        final EntryMessageDispatcher dispatcher = Databuter.instance().entryMessageDispatcher();

        dispatcher.enqueue(expireEntryMessage.id(), new EntryCallback() {
            @Override
            public void onSuccess(Entry entry) {
                callback.onSuccess(entry);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
        remoteClusterNode.session().send(expireEntryMessage);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id())
                .add("configuration", configuration())
                .toString();
    }
}
