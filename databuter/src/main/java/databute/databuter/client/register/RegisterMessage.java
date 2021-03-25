package databute.databuter.client.register;

import com.google.common.base.MoreObjects;
import databute.network.message.Message;
import databute.network.message.MessageCode;

public class RegisterMessage implements Message {

    @Override
    public MessageCode messageCode() {
        return MessageCode.REGISTER;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messageCode", messageCode())
                .toString();
    }
}
