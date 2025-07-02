package id.putra.resource;

import java.io.IOException;
import java.nio.file.Files;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import io.quarkiverse.renarde.Controller;
import io.quarkiverse.renarde.security.RenardeSecurity;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Authenticated
public class User extends Controller {

    @Inject
    RenardeSecurity security;

    @CheckedTemplate
    static class Templates {
        public static native TemplateInstance index();

        public static native TemplateInstance createPost();
    }

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance index() {
        return Templates.index();
    }

    @GET
    @Path("/create-post")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance createPostPage() {
        return Templates.createPost();
    }

    @POST
    @Path("/create-post")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public TemplateInstance createPost(@RestForm("post") FileUpload fileUpload) {
        if (fileUpload != null) {
            String path = "/run/media/putra/Ngoding/Project/memes";
            try {
                Files.write(java.nio.file.Path.of(path, fileUpload.fileName()),
                        Files.readAllBytes(fileUpload.filePath()));
                return Templates.index();
            } catch (IOException e) {
                e.printStackTrace();
                throw new InternalError("Failed to save image");
            }
        }
        return Auth.Templates.login();
    }

}
