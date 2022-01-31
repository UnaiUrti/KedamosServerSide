/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.restful;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import kedamosServerSide.entities.EventManager;
import kedamosServerSide.security.Crypt;

/**
 *
 * @author Freak
 */
@Stateless
@Path("kedamosserverside.entities.eventmanager")
public class EventManagerFacadeREST extends AbstractFacade<EventManager> {

    @PersistenceContext(unitName = "KedamosServerSidePU")
    private EntityManager em;

    public EventManagerFacadeREST() {
        super(EventManager.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML})
    public void create(EventManager entity) {
        String decryptPassword = Crypt.decryptAsimetric(entity.getPassword());
        entity.setPassword(Crypt.hash(decryptPassword));
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(@PathParam("id") Long id, EventManager entity) {
        if (entity.getPassword().length() >=200) {
            entity.setPassword(Crypt.hash(Crypt.decryptAsimetric(entity.getPassword())));
        }
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
    public EventManager find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML})
    public List<EventManager> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML})
    public List<EventManager> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("eventManagerLoginValidation/{username}/{passwd}")
    @Produces({MediaType.APPLICATION_XML})
    public EventManager eventManagerLoginValidation(@PathParam("username") String username,
            @PathParam("passwd") String passwd) {
        EventManager eventManager = null;
        String decryptPassword = Crypt.decryptAsimetric(passwd);
        try {
            eventManager = (EventManager) em.createNamedQuery("getEventManagerByUsername")
                    .setParameter("username", username)
                    .getSingleResult();
            if (!eventManager.getPassword().equalsIgnoreCase(Crypt.hash(decryptPassword))) {
                throw new NotAuthorizedException("Las contraseñas no coinciden");
            }
        } catch (NoResultException e) {
            throw new NotFoundException();
        }
        return eventManager;
    }

    @GET
    @Path("isEmailExisting/{email}")
    @Produces({MediaType.APPLICATION_XML})
    public EventManager isEmailExisting(@PathParam("email") String email) {
        EventManager eventManager;
        try {
            eventManager = (EventManager) em.createNamedQuery("getEventManagerByEmail")
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException();
        }
        return eventManager;
    }

    @GET
    @Path("isUsernameExisting/{username}")
    @Produces({MediaType.APPLICATION_XML})
    public EventManager isUsernameExisting(@PathParam("username") String username) {
        EventManager eventManager;
        try {
            eventManager = (EventManager) em.createNamedQuery("getEventManagerByUsername")
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException();
        }
        return eventManager;
    }

    @GET
    @Path("getEventManagerByUsername/{username}")
    @Produces({MediaType.APPLICATION_XML})
    public EventManager getEventManagerByUsername(@PathParam("username") String username) {
        EventManager eventManager;
        try {
            eventManager = (EventManager) em.createNamedQuery("getEventManagerByUsername")
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException();
        }
        return eventManager;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
