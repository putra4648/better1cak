package id.putra.resource;

import id.putra.entity.User;
import id.putra.repository.UserRepository;
import io.quarkiverse.renarde.router.Router;
import io.quarkiverse.renarde.security.ControllerWithUser;
import io.quarkiverse.renarde.security.RenardeSecurity;
import io.quarkiverse.renarde.util.StringUtils;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Auth extends ControllerWithUser<User> {


    @Inject
    UserRepository repository;
    ;

    @CheckedTemplate
    static class Templates {
        public static native TemplateInstance login();

        public static native TemplateInstance register();

        public static native TemplateInstance welcome();
    }


    /**
     * Login page
     */
    public TemplateInstance login() {
        return Templates.login();
    }

    public TemplateInstance register() {
        return Templates.register();
    }

    /**
     * Welcome page at the end of registration
     */
    @Authenticated
    public TemplateInstance welcome() {
        return Templates.welcome();
    }

    /**
     * Manual login form
     */
    @POST
    public Response manualLogin(@RestForm String username, @RestForm String password) {
        if (validationFailed()) {
            login();
        }
        User user = repository.find("username", username).firstResult();
        if (user == null || !BcryptUtil.matches(password, user.password)) {
            validation.addError("userName", "Invalid username/pasword");
            prepareForErrorRedirect();
            login();
        }
        NewCookie cookie = security.makeUserCookie(user);
        return Response.seeOther(Router.getURI(Application::index)).cookie(cookie).build();
    }


    /**
     * Manual registration form, sends confirmation email
     */
    @POST
    public TemplateInstance manualRegister(@RestForm String email, @RestForm String username, @RestForm String password) {
        if (validationFailed()) login();
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
    public Response complete(@RestForm String username, @RestForm String password, @RestForm String firstname, @RestForm String lastname) {


        if (repository.find("username", username).firstResult() != null)
            validation.addError("userName", "User name already taken");
        User user = repository.find("username", username).firstResult();

        user.username = username;
        user.password = BcryptUtil.bcryptHash(password);
        user.firstname = firstname;
        user.lastname = lastname;

        Response.ResponseBuilder responseBuilder = Response.seeOther(Router.getURI(Auth::welcome));
        NewCookie cookie = security.makeUserCookie(user);
        responseBuilder.cookie(cookie);
        return responseBuilder.build();
    }
}
