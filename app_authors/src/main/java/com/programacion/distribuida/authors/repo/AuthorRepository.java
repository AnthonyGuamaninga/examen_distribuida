package com.programacion.distribuida.authors.repo;

import com.programacion.distribuida.authors.db.Author;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.transaction.Transactional;
import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public abstract class AuthorRepository extends AbstractEntityRepository<Author, Integer> {


}
