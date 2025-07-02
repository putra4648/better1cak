package id.putra.repository;

import id.putra.entity.User;
import io.quarkiverse.renarde.security.RenardeSecurity;
import io.quarkiverse.renarde.security.RenardeUser;
import io.quarkiverse.renarde.security.RenardeUserProvider;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User>, RenardeUserProvider {
    @Inject
    RenardeSecurity security;

    @Override
    public RenardeUser findUser(String tenantId, String authId) {
        return find("id", authId).firstResult();
    }
}
