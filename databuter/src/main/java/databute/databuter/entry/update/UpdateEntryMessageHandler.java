package databute.databuter.entry.update;

import databute.databuter.Databuter;
import databute.databuter.bucket.Bucket;
import databute.databuter.cluster.ClusterCoordinator;
import databute.databuter.cluster.ClusterNode;
import databute.databuter.cluster.remote.RemoteClusterNode;
import databute.databuter.entry.*;
import databute.databuter.entry.result.fail.EntryOperationFailMessage;
import databute.databuter.entry.result.success.EntryOperationSuccessMessage;
import databute.databuter.entry.type.*;
import databute.network.Session;
import databute.network.message.AbstractMessageHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class UpdateEntryMessageHandler extends AbstractMessageHandler<Session, UpdateEntryMessage> {

    private static final Logger logger = LoggerFactory.getLogger(UpdateEntryMessageHandler.class);

    public UpdateEntryMessageHandler(Session session) {
        super(session);
    }

    @Override
    public void handle(UpdateEntryMessage updateEntryMessage) {
        logger.debug("handling update entry message {}", updateEntryMessage);

        final String id = updateEntryMessage.id();
        final String key = updateEntryMessage.key();

        try {
            final EntryKey entryKey = new EntryKey(key);
            final Bucket bucket = Databuter.instance().bucketGroup().findByKey(entryKey);
            if (bucket == null) {
                // TODO(@ghkim3221): 키에 해당하는 버킷을 찾을 수 없는 경우. 이 경우가 발생할 것인가...?
            } else {
                bucket.get(entryKey, new EntryCallback() {
                    @Override
                    public void onSuccess(Entry entry) {
                        if (StringUtils.equals(bucket.activeNodeId(), Databuter.instance().id())) {
                            if (StringUtils.isNotEmpty(bucket.standbyNodeId())) {
                                final ClusterCoordinator clusterCoordinator = Databuter.instance().clusterCoordinator();
                                final ClusterNode node = clusterCoordinator.find(bucket.standbyNodeId());
                                if (node instanceof RemoteClusterNode) {
                                    final RemoteClusterNode remoteNode = (RemoteClusterNode) node;

                                    final EntryMessageDispatcher dispatcher = Databuter.instance().entryMessageDispatcher();
                                    dispatcher.enqueue(updateEntryMessage.id(), new EntryCallback() {
                                        @Override
                                        public void onSuccess(Entry e) {
                                            updateEntry(entry, updateEntryMessage);
                                        }

                                        @Override
                                        public void onFailure(Exception e) {
                                            if (e instanceof EmptyEntryKeyException) {
                                                session().send(EntryOperationFailMessage.emptyKey(id, key));
                                            } else if (e instanceof DuplicateEntryKeyException) {
                                                session().send(EntryOperationFailMessage.duplicateKey(id, key));
                                            } else if (e instanceof UnsupportedValueTypeException) {
                                                session().send(EntryOperationFailMessage.unsupportedValueType(id, key));
                                            } else {
                                                logger.error("Unknown error to update entry {}", key, e);
                                            }
                                        }
                                    });
                                    remoteNode.session().send(updateEntryMessage);
                                }
                            } else {
                                updateEntry(entry, updateEntryMessage);
                            }
                        } else {
                            updateEntry(entry, updateEntryMessage);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        if (e instanceof NotFoundException) {
                            session().send(EntryOperationFailMessage.notFound(id, key));
                        } else if (e instanceof EmptyEntryKeyException) {
                            session().send(EntryOperationFailMessage.emptyKey(id, key));
                        } else if (e instanceof DuplicateEntryKeyException) {
                            session().send(EntryOperationFailMessage.duplicateKey(id, key));
                        } else if (e instanceof UnsupportedValueTypeException) {
                            session().send(EntryOperationFailMessage.unsupportedValueType(id, key));
                        } else {
                            logger.error("Unknown error to get entry {}", key, e);
                        }
                    }
                });
            }
        } catch (EmptyEntryKeyException e) {
            session().send(EntryOperationFailMessage.emptyKey(id, key));
        }
    }

    private void updateEntry(Entry entry, UpdateEntryMessage updateEntryMessage) {
        if (entry instanceof IntegerEntry) {
            final IntegerEntry integerEntry = (IntegerEntry) entry;
            updateIntegerEntry(integerEntry, updateEntryMessage);
        } else if (entry instanceof LongEntry) {
            final LongEntry longEntry = (LongEntry) entry;
            updateLongEntry(longEntry, updateEntryMessage);
        } else if (entry instanceof StringEntry) {
            final StringEntry stringEntry = (StringEntry) entry;
            updateStringEntry(stringEntry, updateEntryMessage);
        } else if (entry instanceof ListEntry) {
            final ListEntry listEntry = (ListEntry) entry;
            updateListEntry(listEntry, updateEntryMessage);
        } else if (entry instanceof SetEntry) {
            final SetEntry setEntry = (SetEntry) entry;
            updateSetEntry(setEntry, updateEntryMessage);
        } else if (entry instanceof DictionaryEntry) {
            final DictionaryEntry dictionaryEntry = (DictionaryEntry) entry;
            updateDictionaryEntry(dictionaryEntry, updateEntryMessage);
        }
    }

    private void updateIntegerEntry(IntegerEntry integerEntry, UpdateEntryMessage updateEntryMessage) {
        final Integer integerValue = (Integer) updateEntryMessage.value();
        integerEntry.set(integerValue);

        session().send(EntryOperationSuccessMessage.entry(updateEntryMessage.id(), integerEntry));
    }

    private void updateLongEntry(LongEntry longEntry, UpdateEntryMessage updateEntryMessage) {
        final Long longValue = (Long) updateEntryMessage.value();
        longEntry.set(longValue);

        session().send(EntryOperationSuccessMessage.entry(updateEntryMessage.id(), longEntry));
    }

    private void updateStringEntry(StringEntry stringEntry, UpdateEntryMessage updateEntryMessage) {
        final String stringValue = (String) updateEntryMessage.value();
        stringEntry.set(stringValue);

        session().send(EntryOperationSuccessMessage.entry(updateEntryMessage.id(), stringEntry));
    }

    @SuppressWarnings("unchecked")
    private void updateListEntry(ListEntry listEntry, UpdateEntryMessage updateEntryMessage) {
        final List<String> listValue = (List<String>) updateEntryMessage.value();
        listEntry.set(listValue);

        session().send(EntryOperationSuccessMessage.entry(updateEntryMessage.id(), listEntry));
    }

    @SuppressWarnings("unchecked")
    private void updateSetEntry(SetEntry setEntry, UpdateEntryMessage updateEntryMessage) {
        final Set<String> setValue = (Set<String>) updateEntryMessage.value();
        setEntry.set(setValue);

        session().send(EntryOperationSuccessMessage.entry(updateEntryMessage.id(), setEntry));
    }

    @SuppressWarnings("unchecked")
    private void updateDictionaryEntry(DictionaryEntry dictionaryEntry, UpdateEntryMessage updateEntryMessage) {
        final Map<String, String> dictionaryValue = (Map<String, String>) updateEntryMessage.value();
        dictionaryEntry.set(dictionaryValue);

        session().send(EntryOperationSuccessMessage.entry(updateEntryMessage.id(), dictionaryEntry));
    }
}
