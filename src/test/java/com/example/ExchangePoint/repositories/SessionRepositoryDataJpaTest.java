package com.example.ExchangePoint.repositories;

import com.example.ExchangePoint.entity.Session;
import org.apache.tomcat.jni.Local;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class SessionRepositoryDataJpaTest {

    @Autowired
    private SessionRepository instance;

    @Test
    void shouldFindByDate() {
        Session session = new Session();
        session.setId(1L);
        session.setDate(LocalDate.of(2021,9,20));
        instance.save(session);

        Optional<Session> sessionByDate = instance.findByDate(LocalDate.of(2021, 9, 20));

        Assertions.assertFalse(sessionByDate.isEmpty());
    }
}
