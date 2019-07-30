package databute.databuter.entry.set;

import databute.databuter.Databuter;
import databute.databuter.bucket.Bucket;
import databute.databuter.entry.*;
import databute.databuter.entry.result.fail.EntryOperationFailMessage;
import databute.databuter.entry.result.success.EntryOperationSuccessMessage;
import databute.databuter.entry.type.*;
import databute.databuter.network.Session;
import databute.databuter.network.message.AbstractMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SetEntryMessageHandler extends AbstractMessageHandler<Session, SetEntryMessage> {

    private static final Logger logger = LoggerFactory.getLogger(SetEntryMessageHandler.class);

    public SetEntryMessageHandler(Session session) {
        super(session);
    }

    @Override
    public void handle(SetEntryMessage setEntryMessage) {
        logger.debug("Handling set entry message {}", setEntryMessage);

        final String id = setEntryMessage.id();
        final String key = setEntryMessage.key();

        try {
            final EntryKey entryKey = new EntryKey(key);
            final Bucket bucket = Databuter.instance().bucketGroup().findByKey(entryKey);
            if (bucket == null) {
                // TODO(@ghkim3221): 키에 해당하는 버킷을 찾을 수 없는 경우. 이 경우가 발생할 것인가...?
            } else {
                bucket.get(entryKey, new EntryCallback() {
                    @Override
                    public void onSuccess(Entry entry) {
                        updateEntry(entry, setEntryMessage);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        if (e instanceof NotFoundException) {
                            addEntry(bucket, setEntryMessage);
                        } else {
                            if (e instanceof EmptyEntryKeyException) {
                                session().send(EntryOperationFailMessage.emptyKey(id, key));
                            } else if (e instanceof DuplicateEntryKeyException) {
                                session().send(EntryOperationFailMessage.duplicateKey(id, key));
                            } else if (e instanceof UnsupportedValueTypeException) {
                                session().send(EntryOperationFailMessage.unsupportedValueType(id, key));
                            } else {
                                logger.error("Unknown error to set entry {}", key, e);
                            }
                        }
                    }
                });
            }
        } catch (EmptyEntryKeyException e) {
            session().send(EntryOperationFailMessage.emptyKey(id, key));
        }
    }

    private void addEntry(Bucket bucket, SetEntryMessage setEntryMessage) {
        final String id = setEntryMessage.id();
        final String key = setEntryMessage.key();

        final EntryCallback callback = new EntryCallback() {
            @Override
            public void onSuccess(Entry entry) {
                session().send(EntryOperationSuccessMessage.entry(id, entry));
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
        };

        switch (setEntryMessage.valueType()) {
            case INTEGER:
                addIntegerEntry(bucket, setEntryMessage, callback);
                break;
            case LONG:
                addLongEntry(bucket, setEntryMessage, callback);
                break;
            case STRING:
                addStringEntry(bucket, setEntryMessage, callback);
                break;
            case LIST:
                addListEntry(bucket, setEntryMessage, callback);
                break;
            case SET:
                addSetEntry(bucket, setEntryMessage, callback);
                break;
            case DICTIONARY:
                addDictionaryEntry(bucket, setEntryMessage, callback);
                break;
        }
    }

    private void addIntegerEntry(Bucket bucket, SetEntryMessage setEntryMessage, EntryCallback callback) {
        try {
            final EntryKey entryKey = new EntryKey(setEntryMessage.key());
            final Integer integerValue = (Integer) setEntryMessage.value();
            final IntegerEntry integerEntry = new IntegerEntry(entryKey, integerValue);
            bucket.add(integerEntry, callback);
        } catch (EmptyEntryKeyException e) {
            callback.onFailure(e);
        }
    }

    private void addLongEntry(Bucket bucket, SetEntryMessage setEntryMessage, EntryCallback callback) {
        try {
            final EntryKey entryKey = new EntryKey(setEntryMessage.key());
            final Long longValue = (Long) setEntryMessage.value();
            final LongEntry longEntry = new LongEntry(entryKey, longValue);
            bucket.add(longEntry, callback);
        } catch (EmptyEntryKeyException e) {
            callback.onFailure(e);
        }
    }

    private void addStringEntry(Bucket bucket, SetEntryMessage setEntryMessage, EntryCallback callback) {
        try {
            final EntryKey entryKey = new EntryKey(setEntryMessage.key());
            final String stringValue = (String) setEntryMessage.value();
            final StringEntry stringEntry = new StringEntry(entryKey, stringValue);
            bucket.add(stringEntry, callback);
        } catch (EmptyEntryKeyException e) {
            callback.onFailure(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void addListEntry(Bucket bucket, SetEntryMessage setEntryMessage, EntryCallback callback) {
        try {
            final EntryKey entryKey = new EntryKey(setEntryMessage.key());
            final List<String> listValue = (List<String>) setEntryMessage.value();
            final ListEntry listEntry = new ListEntry(entryKey, listValue);
            bucket.add(listEntry, callback);
        } catch (EmptyEntryKeyException e) {
            callback.onFailure(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void addSetEntry(Bucket bucket, SetEntryMessage setEntryMessage, EntryCallback callback) {
        try {
            final EntryKey entryKey = new EntryKey(setEntryMessage.key());
            final Set<String> setValue = (Set<String>) setEntryMessage.value();
            final SetEntry setEntry = new SetEntry(entryKey, setValue);
            bucket.add(setEntry, callback);
        } catch (EmptyEntryKeyException e) {
            callback.onFailure(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void addDictionaryEntry(Bucket bucket, SetEntryMessage setEntryMessage, EntryCallback callback) {
        try {
            final EntryKey entryKey = new EntryKey(setEntryMessage.key());
            final Map<String, String> dictionaryValue = (Map<String, String>) setEntryMessage.value();
            final DictionaryEntry dictionaryEntry = new DictionaryEntry(entryKey, dictionaryValue);
            bucket.add(dictionaryEntry, callback);
        } catch (EmptyEntryKeyException e) {
            callback.onFailure(e);
        }
    }

    private void updateEntry(Entry entry, SetEntryMessage setEntryMessage) {
        if (entry instanceof IntegerEntry) {
            final IntegerEntry integerEntry = (IntegerEntry) entry;
            updateIntegerEntry(integerEntry, setEntryMessage);
        } else if (entry instanceof LongEntry) {
            final LongEntry longEntry = (LongEntry) entry;
            updateLongEntry(longEntry, setEntryMessage);
        } else if (entry instanceof StringEntry) {
            final StringEntry stringEntry = (StringEntry) entry;
            updateStringEntry(stringEntry, setEntryMessage);
        } else if (entry instanceof ListEntry) {
            final ListEntry listEntry = (ListEntry) entry;
            updateListEntry(listEntry, setEntryMessage);
        } else if (entry instanceof SetEntry) {
            final SetEntry setEntry = (SetEntry) entry;
            updateSetEntry(setEntry, setEntryMessage);
        } else if (entry instanceof DictionaryEntry) {
            final DictionaryEntry dictionaryEntry = (DictionaryEntry) entry;
            updateDictionaryEntry(dictionaryEntry, setEntryMessage);
        }
    }

    private void updateIntegerEntry(IntegerEntry integerEntry, SetEntryMessage setEntryMessage) {
        final Integer integerValue = (Integer) setEntryMessage.value();
        integerEntry.set(integerValue);

        session().send(EntryOperationSuccessMessage.entry(setEntryMessage.id(), integerEntry));
    }

    private void updateLongEntry(LongEntry longEntry, SetEntryMessage setEntryMessage) {
        final Long longValue = (Long) setEntryMessage.value();
        longEntry.set(longValue);

        session().send(EntryOperationSuccessMessage.entry(setEntryMessage.id(), longEntry));
    }

    private void updateStringEntry(StringEntry stringEntry, SetEntryMessage setEntryMessage) {
        final String stringValue = (String) setEntryMessage.value();
        stringEntry.set(stringValue);

        session().send(EntryOperationSuccessMessage.entry(setEntryMessage.id(), stringEntry));
    }

    @SuppressWarnings("unchecked")
    private void updateListEntry(ListEntry listEntry, SetEntryMessage setEntryMessage) {
        final List<String> listValue = (List<String>) setEntryMessage.value();
        listEntry.set(listValue);

        session().send(EntryOperationSuccessMessage.entry(setEntryMessage.id(), listEntry));
    }

    @SuppressWarnings("unchecked")
    private void updateSetEntry(SetEntry setEntry, SetEntryMessage setEntryMessage) {
        final Set<String> setValue = (Set<String>) setEntryMessage.value();
        setEntry.set(setValue);

        session().send(EntryOperationSuccessMessage.entry(setEntryMessage.id(), setEntry));
    }

    @SuppressWarnings("unchecked")
    private void updateDictionaryEntry(DictionaryEntry dictionaryEntry, SetEntryMessage setEntryMessage) {
        final Map<String, String> dictionaryValue = (Map<String, String>) setEntryMessage.value();
        dictionaryEntry.set(dictionaryValue);

        session().send(EntryOperationSuccessMessage.entry(setEntryMessage.id(), dictionaryEntry));
    }
}
