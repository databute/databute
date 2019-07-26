package databute.databuter.bucket.local;

import com.google.common.base.MoreObjects;
import com.google.gson.Gson;
import databute.databuter.Databuter;
import databute.databuter.bucket.Bucket;
import databute.databuter.bucket.BucketConfiguration;
import databute.databuter.bucket.BucketException;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;

public class LocalBucket extends Bucket {

    public LocalBucket(BucketConfiguration configuration) {
        super(configuration);
    }

    public String save() throws BucketException {
        // TODO(@ghkim3221): 비동기로 ZooKeeper에 저장
        try {
            final String json = new Gson().toJson(configuration());
            final String zooKeeperPath = Databuter.instance().configuration().zooKeeper().path();
            final String path = ZKPaths.makePath(zooKeeperPath, "bucket", id());
            return Databuter.instance().curator()
                    .create()
                    .creatingParentContainersIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(path, json.getBytes());
        } catch (Exception e) {
            throw new BucketException("Failed to save bucket " + id() + " to the ZooKeeper.");
        }
    }

    public void update() throws BucketException {
        // TODO(@ghkim3221): 비동기로 ZooKeeper에 업데이트
        try {
            final String json = new Gson().toJson(configuration());
            final String zooKeeperPath = Databuter.instance().configuration().zooKeeper().path();
            final String path = ZKPaths.makePath(zooKeeperPath, "bucket", id());
            Databuter.instance().curator()
                    .setData()
                    .forPath(path, json.getBytes());
        } catch (Exception e) {
            throw new BucketException("Failed to update bucket " + id() + " to the ZooKeeper.");
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id())
                .add("configuration", configuration())
                .toString();
    }
}
