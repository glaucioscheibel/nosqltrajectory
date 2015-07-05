package br.udesc.mca.trajectory.service.document;

import br.udesc.mca.trajectory.dao.document.DocumentPersistence;
import br.udesc.mca.trajectory.dao.document.DocumentPersistence;
import br.udesc.mca.trajectory.model.Trajectory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

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
