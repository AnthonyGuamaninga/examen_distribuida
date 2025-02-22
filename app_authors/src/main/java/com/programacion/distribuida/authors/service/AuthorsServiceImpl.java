package com.programacion.distribuida.authors.service;

import com.programacion.distribuida.authors.db.Author;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;

@ApplicationScoped
public class AuthorsServiceImpl implements AuthorsService {
    @Inject
    EntityManager em;

    @Override
    public Author getById(int id) {
        return em.find(Author.class, id);
    }

    @Override
    public List<Author> getAll() {
        return em.createQuery("select p from Author p order by p.id", Author.class).getResultList();
    }

    @Override
    public String insertAuthor(Author author) {
        try {
            em.getTransaction().begin();
            em.persist(author);
            em.getTransaction().commit();
            return "¡Author "+author.getId()+" insertada con exito!";
        }catch(Exception e) {
            return "Error al insertar: "+e.getMessage();
        }
    }

    @Override
    public Author putAuthor(Author author) {
        try {
            em.getTransaction().begin();
            var refreshPer = em.merge(author);
            em.getTransaction().commit();
            return refreshPer;
        }catch (Exception e) {
            return null;
        }

    }

    @Override
    public String deleteAuthor(int id) {
        try{
            em.getTransaction().begin();
            em.remove(em.find(Author.class, id));
            em.getTransaction().commit();
            return "¡Author "+id+" eliminada con exito!";
        }catch(Exception e) {
            return "Error al eliminar: "+e.getMessage();
        }

    }
}
