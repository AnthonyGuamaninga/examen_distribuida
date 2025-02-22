package com.programacion.distribuida.authors.service;

import com.programacion.distribuida.authors.db.Author;

import java.util.List;

public interface AuthorsService {
    public Author getById(int id);
    public List<Author> getAll();
    public String insertAuthor(Author Author);
    public Author putAuthor(Author Author);
    public String deleteAuthor(int id);
}
