package id.putra.resource;

import java.util.List;

import org.jboss.resteasy.reactive.RestForm;

import id.putra.entity.User;
import id.putra.repository.UserRepository;
import io.quarkiverse.renarde.router.Router;
import io.quarkiverse.renarde.security.ControllerWithUser;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

public class Auth extends ControllerWithUser<User> {

    @Inject
    UserRepository repository;;

    @CheckedTemplate
    static class Templates {
        public static native TemplateInstance login();

        public static native TemplateInstance register();
    }

    @GET
    @Path("/login")
    public TemplateInstance loginPage() {
        return Templates.login();
    }

    @GET
    @Path("/register")
    public TemplateInstance registerPage() {
        return Templates.register();
    }

    @POST
    @Path("/login")
    public Response login(@RestForm String username, @RestForm String password) {
        if (validationFailed()) {
            loginPage();
        }
        User user = repository.find("username", username).firstResult();
        if (user == null || !BcryptUtil.matches(password, user.password)) {
            validation.addError("userName", "Invalid username/pasword");
            prepareForErrorRedirect();
            loginPage();
        }
        NewCookie cookie = security.makeUserCookie(user);
        return Response.seeOther(Router.getURI(id.putra.resource.User::index)).cookie(cookie).build();
    }

    @POST
    @Path("/register")
    public TemplateInstance register(@RestForm String email, @RestForm String username,
            @RestForm String password) {
        if (validationFailed())
            loginPage();
        User newUser = repository.find("username", username).firstResult();
        if (newUser == null) {
            newUser = new User();
            newUser.email = email;
            newUser.username = username;
            newUser.password = BcryptUtil.bcryptHash(password);
            newUser.roles = List.of("User");
            repository.persist(newUser);
        }
        return Templates.login();
    }

    @POST
    @Path("/complete")
    public Response complete(@RestForm String username, @RestForm String password, @RestForm String firstname,
            @RestForm String lastname) {
        if (repository.find("username", username).firstResult() != null)
            validation.addError("userName", "User name already taken");
        User user = repository.find("username", username).firstResult();

        user.username = username;
        user.password = BcryptUtil.bcryptHash(password);
        user.firstname = firstname;
        user.lastname = lastname;

        Response.ResponseBuilder responseBuilder = Response.seeOther(Router.getURI(id.putra.resource.User::index));
        NewCookie cookie = security.makeUserCookie(user);
        responseBuilder.cookie(cookie);
        return responseBuilder.build();
    }
}
