package databute.databuter.entry.delete;

import databute.databuter.Databuter;
import databute.databuter.bucket.Bucket;
import databute.databuter.cluster.ClusterCoordinator;
import databute.databuter.cluster.ClusterNode;
import databute.databuter.cluster.remote.RemoteClusterNode;
import databute.databuter.entry.*;
import databute.databuter.entry.result.fail.EntryOperationFailMessage;
import databute.databuter.entry.result.success.EntryOperationSuccessMessage;
import databute.databuter.network.Session;
import databute.databuter.network.message.AbstractMessageHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteEntryMessageHandler extends AbstractMessageHandler<Session, DeleteEntryMessage> {

    private static final Logger logger = LoggerFactory.getLogger(DeleteEntryMessageHandler.class);

    public DeleteEntryMessageHandler(Session session) {
        super(session);
    }

    @Override
    public void handle(DeleteEntryMessage deleteEntryMessage) {
        logger.debug("Handling delete entry message {}", deleteEntryMessage);

        final String id = deleteEntryMessage.id();
        final String key = deleteEntryMessage.key();

        try {
            final EntryKey entryKey = new EntryKey(key);
            final Bucket bucket = Databuter.instance().bucketGroup().findByKey(entryKey);
            if (bucket == null) {
                // TODO(@ghkim3221): 키에 해당하는 버킷을 찾을 수 없는 경우. 이 경우가 발생할 것인가...?
            } else {
                if (StringUtils.equals(bucket.activeNodeId(), Databuter.instance().id())) {
                    if (StringUtils.isNotEmpty(bucket.standbyNodeId())) {
                        final ClusterCoordinator clusterCoordinator = Databuter.instance().clusterCoordinator();
                        final ClusterNode node = clusterCoordinator.find(bucket.standbyNodeId());
                        if (node instanceof RemoteClusterNode) {
                            final RemoteClusterNode remoteNode = (RemoteClusterNode) node;
                            final EntryMessageDispatcher dispatcher = Databuter.instance().entryMessageDispatcher();

                            dispatcher.enqueue(deleteEntryMessage.id(), new EntryCallback() {
                                @Override
                                public void onSuccess(Entry e) {
                                    deleteEntry(entryKey, bucket, deleteEntryMessage);
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
                                        logger.error("Unknown error to remove entry {}", key, e);
                                    }
                                }
                            });
                            remoteNode.session().send(deleteEntryMessage);
                        }
                    } else {
                        // 스탠바이 노드가 없는 경우
                        deleteEntry(entryKey, bucket, deleteEntryMessage);
                    }
                } else {
                    // 스탠바이 노드
                    deleteEntry(entryKey, bucket, deleteEntryMessage);
                }
            }
        } catch (EmptyEntryKeyException e) {
            session().send(EntryOperationFailMessage.emptyKey(id, key));
        } catch (UnsupportedValueTypeException e) {
            session().send(EntryOperationFailMessage.unsupportedValueType(id, key));
        }
    }

    private void deleteEntry(EntryKey entryKey, Bucket bucket, DeleteEntryMessage deleteEntryMessage) {
        final String id = deleteEntryMessage.id();
        final String key = deleteEntryMessage.key();

        bucket.remove(entryKey, new EntryCallback() {
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
                    logger.error("Unknown error to remove entry {}", key, e);
                }
            }
        });
    }
}
