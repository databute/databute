package databute.databuter.bucket;

import com.google.gson.Gson;
import databute.databuter.Databuter;
import databute.databuter.ZooKeeperConfiguration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

public class BucketCoordinator {

    private static final Logger logger = LoggerFactory.getLogger(BucketCoordinator.class);

    private PathChildrenCache cache;

    private final BucketGroup bucketGroup;
    private final ZooKeeperConfiguration zooKeeperConfiguration;

    public BucketCoordinator(BucketGroup bucketGroup) {
        this.bucketGroup = checkNotNull(bucketGroup, "bucketGroup");
        this.zooKeeperConfiguration = Databuter.instance().configuration().zooKeeper();
    }

    public void start() throws BucketException {
        registerCacheEvenetListener();
    }

    private void registerCacheEvenetListener() throws BucketException {
        final String path = ZKPaths.makePath(zooKeeperConfiguration.path(), "bucket");

        try {
            cache = new PathChildrenCache(Databuter.instance().curator(), path, true);
            cache.getListenable().addListener(this::onCacheEvent);
            cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        } catch (Exception e) {
            throw new BucketException("Failed to register cache event listener.", e);
        }
    }

    @SuppressWarnings("unused")
    private void onCacheEvent(CuratorFramework curator, PathChildrenCacheEvent event) {
        switch (event.getType()) {
            case INITIALIZED:
                onInitialized();
                break;
            case CHILD_ADDED:
                break;
        }
    }

    private void onInitialized() {
        registerBucketGroup();
    }

    private void registerBucketGroup() {
        for (Bucket bucket : bucketGroup) {
            try {
                final String json = new Gson().toJson(bucket);
                final String path = ZKPaths.makePath(zooKeeperConfiguration.path(), "bucket", bucket.id());
                final String createdPath = Databuter.instance().curator()
                        .create()
                        .creatingParentContainersIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath(path, json.getBytes());
                logger.debug("Registered bucket {} at {}", bucket.id(), createdPath);
            } catch (Exception e) {
                logger.error("Failed to register bucket group.", e);
            }
        }
    }
}
