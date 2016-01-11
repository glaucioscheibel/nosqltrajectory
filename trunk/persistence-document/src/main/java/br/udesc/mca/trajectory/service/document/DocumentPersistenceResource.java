package br.udesc.mca.trajectory.service.document;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import br.udesc.mca.trajectory.dao.document.DocumentPersistence;
import br.udesc.mca.trajectory.model.Trajectory;

@Path("/trajectorydocument")
@Produces(MediaType.APPLICATION_JSON)
public class DocumentPersistenceResource {
    private DocumentPersistence persistence;

    public DocumentPersistenceResource() {
        this.persistence = DocumentPersistence.getInstance();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Trajectory store(Trajectory t) {
        return this.persistence.store(t);
    }

    @GET
    public List<Trajectory> findAll() {
        return this.persistence.findAll();
    }

    @GET
    @Path("{id}")
    public Trajectory findById(@PathParam("id") Long id) {
        return this.persistence.findById(id);
    }

    @DELETE
    @Path("{id}")
    public void deleteById(@PathParam("id") Long id) {
        this.persistence.deleteById(id);
    }
}
