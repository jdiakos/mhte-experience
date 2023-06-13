package com.iknowhow.mhte.projectsexperience.configuration;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.security.auth.Subject;

@Component
public class FilenetConfig {

    @Value("${app.filenet.url}")
    private String uri;
    @Value("${app.filenet.username}")
    private String username;
    @Value("${app.filenet.password}")
    private String password;
    @Value("${app.filenet.objectStrone.name}")
    private String objectStoreName;

    public Connection getConnection() {
        Connection connection = Factory.Connection.getConnection(uri);
        Subject subject = UserContext.createSubject(connection, username, password, null);
        UserContext.get().pushSubject(subject);

        return connection;
    }

    public Domain getDomain() {

        return Factory.Domain.fetchInstance(getConnection(), null, null);
    }

    public ObjectStore getObjectStore() {
        ObjectStore objectStore = Factory.ObjectStore.getInstance(getDomain(), objectStoreName);
        objectStore.refresh();

        return objectStore;
    }
}
