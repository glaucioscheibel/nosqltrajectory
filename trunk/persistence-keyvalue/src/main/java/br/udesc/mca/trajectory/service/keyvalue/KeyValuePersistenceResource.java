package br.udesc.mca.trajectory.service.keyvalue;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import br.udesc.mca.trajectory.dao.keyvalue.KeyValuePersistence;
import br.udesc.mca.trajectory.dao.keyvalue.RedisJsonPersistence;
import br.udesc.mca.trajectory.model.Trajectory;

@Path("/trajectorykeyvalue")
@Produces(MediaType.APPLICATION_JSON)
public class KeyValuePersistenceResource {
    private KeyValuePersistence persistence;

    public KeyValuePersistenceResource() {
        this.persistence = RedisJsonPersistence.getInstance();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Trajectory store(Trajectory t) {
        return this.persistence.store(t);
    }

    public List<Trajectory> findAll() {
        return this.persistence.findAll();
    }

    @GET
    @Path("{id}")
    public Trajectory findById(@PathParam("id") Long id) {
        return this.persistence.findById(id);
    }

    public void deleteById(long id) {
        this.persistence.deleteById(id);
    }
}
