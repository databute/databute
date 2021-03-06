package databute.databuter.network.message;

public class MessageCodeResolver {

    public MessageCode resolve(int value) {
        for (MessageCode messageCode : MessageCode.values()) {
            if (messageCode.value() == value) {
                return messageCode;
            }
        }
        return null;
    }
}
