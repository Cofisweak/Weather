package com.cofisweak.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration()
                .configure()
                .configure()
                .buildSessionFactory();
    }

}
