package com.cofisweak.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HibernateUtil {
    public static SessionFactory buildSessionFactory() {
        return new Configuration()
                .configure()
                .buildSessionFactory();
    }

}
