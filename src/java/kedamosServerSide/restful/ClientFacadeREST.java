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
import kedamosServerSide.entities.Client;
import kedamosServerSide.security.Crypt;
import kedamosServerSide.security.Email;

/**
 *
 * @author Steven Arce
 */
@Stateless
@Path("kedamosserverside.entities.client")
public class ClientFacadeREST extends AbstractFacade<Client> {

    @PersistenceContext(unitName = "KedamosServerSidePU")
    private EntityManager em;

    public ClientFacadeREST() {
        super(Client.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML})
    public void create(Client entity) {
        String decryptPassword = Crypt.decryptAsimetric(entity.getPassword());
        entity.setPassword(Crypt.hash(decryptPassword));
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(@PathParam("id") Long id, Client entity) {
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
    public Client find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML})
    public List<Client> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML})
    public List<Client> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("getClientByUsername/{username}")
    @Produces({MediaType.APPLICATION_XML})
    public Client getClientByUsername(@PathParam("username") String username) {
        Client client;
        try {
            client = (Client) em.createNamedQuery("getClientByUsername")
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException();
        }
        return client;
    }
    
    @GET
    @Path("validatePassword/{username}/{passwd}")
    @Produces({MediaType.APPLICATION_XML})
    public Client validatePassword(@PathParam("username") String username,
            @PathParam("passwd") String passwd) {

        Client client = null;

        try {

            String decryptPassword = Crypt.decryptAsimetric(passwd);

            client = (Client) em.createNamedQuery("getClientByUsername")
                    .setParameter("username", username)
                    .getSingleResult();

            if (!client.getPassword().equalsIgnoreCase(Crypt.hash(decryptPassword))) {
                throw new NotAuthorizedException("Las contraseñas no coinciden");
            }

        } catch (NoResultException e) {
            throw new NotFoundException();
        }
        return client;
    }

    @PUT
    @Path("changePassword/{id}")
    @Consumes({MediaType.APPLICATION_XML})
    public void changePassword(@PathParam("id") Long id, Client entity) {
        entity.setPassword(Crypt.decryptAsimetric(entity.getPassword()));
        Email.sendEmailChangePassword(entity.getEmail());
        super.edit(entity);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
