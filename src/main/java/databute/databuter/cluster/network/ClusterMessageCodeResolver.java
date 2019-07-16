package databute.databuter.cluster.network;

import databute.databuter.network.message.MessageCode;
import databute.databuter.network.message.MessageCodeResolver;

public class ClusterMessageCodeResolver implements MessageCodeResolver {

    @Override
    public MessageCode resolve(int value) {
        for (ClusterMessageCode messageCode : ClusterMessageCode.values()) {
            if (messageCode.value() == value) {
                return messageCode;
            }
        }
        return null;
    }
}
