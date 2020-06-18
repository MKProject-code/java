package mkproject.maskat.LoginManager;

import org.bukkit.entity.Entity;

public class ClonableEntityWrapper implements Cloneable {
    private final Entity entity;
    public ClonableEntityWrapper(Entity entity) {
        this.entity = entity;
    }
    public Entity getEntity() {
        return this.entity;
    }
}
