/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.restful;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import kedamosServerSide.entities.Event;
import kedamosServerSide.entities.PersonalResource;
import kedamosServerSide.entities.Type;
import kedamosServerSide.exceptions.ListException;
import kedamosServerSide.exceptions.PersonalDontExistException;

/**
 *Esta clase maneja los personales asignados a un evento
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
    @Consumes({MediaType.APPLICATION_XML})
    public void create(PersonalResource entity) {
        //compruebo que el evento esta en el contexto de persistencia si no lo meto con el merge
       if(!em.contains(entity.getEvent()))
            em.merge(entity);
       
       
        em.flush();
       //super.create(entity);
       
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
    /**
     * Buscar todos los personales de un tipo en especifico
     * @param type
     * @return
     * @throws ListException 
     */
    @GET
    @Path("EventBypersonalType/{type}")
    @Produces({MediaType.APPLICATION_XML})
    public Set<PersonalResource> findPersonalByType(@PathParam("type")Type type) throws ListException {
       Set<PersonalResource> per=null;
        
             per=new HashSet( em.createNamedQuery("findPersonalByType").setParameter("type", type).getResultList());
        if(per.isEmpty()){
            logger.severe("Error en listar por  list exception");
            throw new ListException("Error al listar el personal por tipo");
        }
            return per;
}
    /**
     * Buscar los personales de un precio en especifico
     * @param price
     * @return
     * @throws ListException 
     */
     @GET
    @Path("FindByPRice/{price}")
    @Produces({MediaType.APPLICATION_XML})
    public Set<PersonalResource> findPersonalByPrice(@PathParam("price")Float price) throws ListException{
       Set<PersonalResource> per=null;
        
             per=new HashSet( em.createNamedQuery("findPersonalByType").setParameter("price", price).getResultList());
        if(per.isEmpty()){
            logger.severe("Error en listar por  list exception");
            throw new ListException("Tipo no existe");
        }
            return per;
}
    /**
     * Buscar un personal de un evento en concreto mediante el tipo
     * @param type
     * @param event_id
     * @return
     * @throws PersonalDontExistException 
     */
       @GET
    @Path("BYIDANDTYPE/{event_id}/{type}")
    @Produces({MediaType.APPLICATION_XML})
    public PersonalResource findPersonalByEventAndType(@PathParam("type")Type type,@PathParam("event")Long event_id) throws PersonalDontExistException {
       PersonalResource per=null;
      
             per=(PersonalResource) em.createNamedQuery("findPersonalByEventAndType").setParameter("type", type).setParameter("event", event_id).getSingleResult();
        if(per==null){
          throw new PersonalDontExistException("personal inecistente");  
        }
            return per;
}
    /**
     * Borrar todos los personales de un tipo
     * @param type
     * @throws PersonalDontExistException 
     */
     @DELETE
    @Path("DeleteByType/{type}")
    public void deletePersonalByType(@PathParam("type") Type type) throws PersonalDontExistException {
        em.createNamedQuery("deletePersonalByType").setParameter("type", type).executeUpdate();
    
}
    /**
     * Modificar la cantidad de un tipo de personal
     * @param quantity
     * @param personalresource_id 
     */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("UpdateQuantity/{personalresource_id}/{quantity}")
    public void updateQuantityOfAPErsonal(@PathParam("quantity") Long quantity,@PathParam("personalresource_id") Long personalresource_id) {
        PersonalResource per=null;
        //Selecciono el PersonalResource que quiere el cliente y le modifico quantity con el setter
        per=(PersonalResource)em.createNamedQuery("updateQuantityOfAPErsonal").setParameter("personalresource_id", personalresource_id).getSingleResult();
        per.setQuantity(quantity);
        if(!em.contains(per))
            em.merge(per);
        em.flush();
            
    }
    
    @GET
    @Path("getPersonalByEvent/{event_id}")
    @Produces({MediaType.APPLICATION_XML})
    public List<PersonalResource> getPersonalByEvent(@PathParam("event_id")Long event_id)  {
       List<PersonalResource> per;
        
             per=  em.createNamedQuery("getPersonalByEvent").setParameter("event_id", event_id).getResultList();
        
            return per;
}
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
