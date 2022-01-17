/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.restful;


import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import kedamosServerSide.entities.Category;
import kedamosServerSide.entities.Comment;
import kedamosServerSide.entities.Event;

/**
 *
 * @author Adrian Franco, Irkus de la Fuente
 */
@Stateless
@Path("kedamosserverside.entities.event")
public class EventFacadeREST extends AbstractFacade<Event> {

    @PersistenceContext(unitName = "KedamosServerSidePU")
    private EntityManager em;

    public EventFacadeREST() {
        super(Event.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML})
    public void create(Event entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(@PathParam("id") Long id, Event entity) {
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
    public Event find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML})
    public List<Event> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML})
    public List<Event> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }       
    
    @GET
    @Path("date/{date}")
    @Produces({MediaType.APPLICATION_XML})
    public List<Event> searchEventByDate (@PathParam("date") Date date){
        List <Event> event = null;
        try{
            event = em.createNamedQuery("searchEventByDate").setParameter("date", date).getResultList();
        }catch(Exception e){
            
        }
        return event;
    }
    
    @GET
    @Path("price/{price}")
    @Produces({MediaType.APPLICATION_XML})
    public List<Event> searchEventByPrice (@PathParam("price") Float price){
        List <Event> event = null;
        try{
            event = em.createNamedQuery("searchEventByPrice").setParameter("price", price).getResultList();
        }catch(Exception e){
            
        }
        return event;
    }
    
    @GET
    @Path("place_id/{place_id}")
    @Produces({MediaType.APPLICATION_XML})
    public List<Event> searchEventByPlace (@PathParam("place_id") Long place){
        List <Event> event = null;
        try{
            event = em.createNamedQuery("searchEventByPlace").setParameter("place_id", place).getResultList();
        }catch(Exception e){
            
        }
        return event;
    }
   
    @GET
    @Path("category/{category}")
    @Produces({MediaType.APPLICATION_XML})
    public Set<Event> searchEventByCategory (@PathParam("category") Category category){
        Set <Event> event = null;
    
            event = new HashSet(em.createNamedQuery("searchEventByCategory").setParameter("category", category).getResultList());
   
        return event;
    }
    
    @GET
    @Path("findComment/{event_id}/id/{user_id}")
    @Produces({MediaType.APPLICATION_XML})
    public Comment findById(@PathParam("event_id") Long event_id, @PathParam("user_id") Long user_id) {
        Comment has = null;

        has = (Comment) em.createNamedQuery("findById").setParameter("event", event_id).setParameter("client", user_id).getSingleResult();

        return has;

    }
    @GET
    @Path("eventId/{event_id}")
    @Produces({MediaType.APPLICATION_XML})
    public Set<Comment> findByEvent(@PathParam("event_id") Long event_id) {
        Set<Comment> has = null;

        has = new HashSet<>(em.createNamedQuery("findByEvent").setParameter("event", event_id).getResultList());

        return has;
     
    }
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}