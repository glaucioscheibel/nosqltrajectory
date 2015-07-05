package br.udesc.mca.trajectory.service.graph;

import br.udesc.mca.trajectory.dao.graph.GraphPersistence;
import br.udesc.mca.trajectory.dao.graph.GraphPersistence;
import br.udesc.mca.trajectory.model.Trajectory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/trajectorygraph")
@Produces(MediaType.APPLICATION_JSON)
public class GraphPersistenceResource {
    private GraphPersistence persistence;

    public GraphPersistenceResource() {
        this.persistence = GraphPersistence.getInstance();
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
