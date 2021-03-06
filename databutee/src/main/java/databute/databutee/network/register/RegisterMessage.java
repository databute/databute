package databute.databutee.network.register;

import com.google.common.base.MoreObjects;
import databute.databutee.network.message.Message;
import databute.databutee.network.message.MessageCode;

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
