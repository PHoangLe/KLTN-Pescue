package com.project.pescueshop.repository.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Properties;


@Repository
@AllArgsConstructor
@NoArgsConstructor
public class BaseDAO {
    @Autowired
    public NamedParameterJdbcTemplate jdbcTemplate;
    @PersistenceContext
    public EntityManager entityManager;
    public SessionFactory sessionFactory;

//    public BaseDAO(){
//        Configuration configuration = new Configuration();
//
//        Properties settings = new Properties();
//        settings.put(Environment.DRIVER, "org.postgresql.Driver");
//        settings.put(Environment.URL, System.getenv("DB_URL"));
//        settings.put(Environment.USER, System.getenv("DB_USERNAME"));
//        settings.put(Environment.PASS, System.getenv("DB_PASSWORD"));
//        settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
//
//        settings.put(Environment.FLUSH_BEFORE_COMPLETION, true);
//        settings.put(Environment.AUTOCOMMIT, true);
//
//        configuration.setProperties(settings);
//
//        this.sessionFactory = configuration.buildSessionFactory();
//    }
}
