package id.putra.resource;

import io.quarkiverse.renarde.Controller;
import io.quarkiverse.renarde.security.RenardeSecurity;
import io.quarkiverse.renarde.security.RenardeUser;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

public class Application extends Controller {

    @Inject
    RenardeSecurity security;


    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance index() {

        if(security.hasUserCookie()) {
            return Templates.index(security.getUser().userId());
        }

        return Auth.Templates.login();
    }

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance index(String user);
    }
}
