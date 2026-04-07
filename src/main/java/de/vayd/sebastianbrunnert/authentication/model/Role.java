package de.vayd.sebastianbrunnert.authentication.model;

public enum Role {
    GUEST(null),
    USER(User.class);

    private Class<? extends Registerable> clazz;

    Role(Class<? extends Registerable> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends Registerable> getClazz() {
        return clazz;
    }

}

