package databute.databuter.entity;

public interface EntityCallback {

    void onSuccess(Entity entity);

    void onFailure(Exception e);

}
