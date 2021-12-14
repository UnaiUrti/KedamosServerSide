/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.restful;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import kedamosServerSide.entities.PersonalResource;
import kedamosServerSide.entities.Type;
import kedamosServerSide.exceptions.ListException;

/**
 *
 * @author Irkus de la Fuente
 */

@Stateless
@Path("kedamosserverside.entities.personalresource")
public class PersonalResourceFacadeREST extends AbstractFacade<PersonalResource> {
private final static Logger logger = Logger.getLogger("KedamosServerSide.restful.PersonalResourceFacadeRest");
    @PersistenceContext(unitName = "KedamosServerSidePU")
    private EntityManager em;

    public PersonalResourceFacadeREST() {
        super(PersonalResource.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML})
    public void create(PersonalResource entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(@PathParam("id") Long id, PersonalResource entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public PersonalResource find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML})
    public List<PersonalResource> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML})
    public List<PersonalResource> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("event/{event_id}/{type}")
    @Produces({MediaType.APPLICATION_XML})
    public PersonalResource findPersonalByType(@PathParam("event_id") Long event_id,@PathParam("type")Type type) {
        PersonalResource per=new PersonalResource();
        try{
            per= (PersonalResource) em.createNamedQuery("findPersonalByType").setParameter("event", event_id).setParameter("type", type).getSingleResult();
        }catch(Exception e){
            logger.severe("Error en listar por  list exception");
            //throw new ListException("Error al listar el personal por tipo");
        }
            return per;
}
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
