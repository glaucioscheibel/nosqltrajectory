package br.udesc.mca.trajectory.service.column;

import br.udesc.mca.trajectory.dao.column.ColumnPersistence;
import br.udesc.mca.trajectory.dao.column.ColumnPersistence;
import br.udesc.mca.trajectory.model.Trajectory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/trajectorycolumn")
@Produces(MediaType.APPLICATION_JSON)
public class ColumnPersistenceResource {
    private ColumnPersistence persistence;

    public ColumnPersistenceResource() {
        this.persistence = ColumnPersistence.getInstance();
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
