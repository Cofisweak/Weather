package com.cofisweak.dao;

import com.cofisweak.exception.LocationAlreadyExistsException;
import com.cofisweak.model.Location;
import com.cofisweak.model.User;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.util.List;

public class LocationRepository extends BaseRepository<Long, Location> {
    public LocationRepository() {
        super(Location.class);
    }

    @Override
    public Location save(Location object) {
        try {
            return super.save(object);
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new LocationAlreadyExistsException("Location already exists in your follow list");
            }
        }
        return object;
    }

    public List<Location> getByUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Location WHERE user = :user ORDER BY id DESC";
            Query<Location> query = session.createQuery(hql, Location.class);
            query.setParameter("user", user);
            return query.list();
        }
    }
}
