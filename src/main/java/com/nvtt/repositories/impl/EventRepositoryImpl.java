package com.nvtt.repositories.impl;

import com.nvtt.pojo.Category;
import com.nvtt.pojo.Event;
import com.nvtt.pojo.EventStatistic;
import com.nvtt.pojo.EventStatus;
import com.nvtt.pojo.User;
import com.nvtt.pojo.dtos.admin.EventSearchCriteriaDTO;
import com.nvtt.repositories.EventRepository;
import com.nvtt.utils.DateTimeUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import jakarta.persistence.NoResultException;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@PropertySource(value = "classpath:configs.properties", ignoreResourceNotFound = true)
public class EventRepositoryImpl implements EventRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private Environment env;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public Event getEventById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Event> q = b.createQuery(Event.class);
        Root<Event> root = q.from(Event.class);
        root.fetch("eventMedias", JoinType.LEFT);
        root.fetch("category", JoinType.LEFT);
        root.fetch("status", JoinType.LEFT);
        root.fetch("organizer", JoinType.LEFT);
        q.select(root).distinct(true);
        q.where(b.equal(root.get("id"), id));
        Query<Event> hQuery = session.createQuery(q);

        Optional<Event> optionalEvent;
        try {
            Event event = hQuery.getSingleResult();
            optionalEvent = Optional.of(event);
        } catch (NoResultException e) {
            optionalEvent = Optional.empty();
        }

        if (optionalEvent.isEmpty()) {
            return null;
        }

        return optionalEvent.get();
    }

    @Override
    public Event getEventById(Long id, List<EventStatus> statuses) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Event> q = b.createQuery(Event.class);
        Root<Event> root = q.from(Event.class);
        root.fetch("eventMedias", JoinType.LEFT);
        root.fetch("category", JoinType.LEFT);
        root.fetch("organizer", JoinType.LEFT);
        q.select(root).distinct(true);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.equal(root.get("id"), id));
        if (statuses != null) {
            predicates.add(statuses.isEmpty() ? b.disjunction() : root.get("status").in(statuses));
        }

        q.where(predicates.toArray(new Predicate[0]));
        Query<Event> hQuery = session.createQuery(q);

        Optional<Event> optionalEvent;
        try {
            Event event = hQuery.getSingleResult();
            optionalEvent = Optional.of(event);
        } catch (NoResultException e) {
            optionalEvent = Optional.empty();
        }

        if (optionalEvent.isEmpty()) {
            return null;
        }

        return optionalEvent.get();
    }

    @Override
    public Event getOwnEventById(Long id, Long organizerId) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Event> q = b.createQuery(Event.class);
        Root<Event> root = q.from(Event.class);
        root.fetch("eventMedias", JoinType.LEFT);
        root.fetch("category", JoinType.LEFT);
        root.fetch("status", JoinType.LEFT);
        root.fetch("organizer", JoinType.LEFT);
        q.select(root).distinct(true);
        q.where(
                b.and(
                        b.equal(root.get("id"), id),
                        b.equal(root.get("organizer").get("id"), organizerId)
                )
        );
        Query<Event> hQuery = session.createQuery(q);

        Optional<Event> optionalEvent;
        try {
            Event event = hQuery.getSingleResult();
            optionalEvent = Optional.of(event);
        } catch (NoResultException e) {
            optionalEvent = Optional.empty();
        }

        if (optionalEvent.isEmpty()) {
            return null;
        }

        return optionalEvent.get();
    }

    @Override
    public List<Event> getEvents(Map<String, String> params, List<EventStatus> statuses) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Event> q = b.createQuery(Event.class);
        Root<Event> root = q.from(Event.class);
        root.fetch("eventMedias", JoinType.LEFT);
        root.fetch("category", JoinType.LEFT);
        root.fetch("organizer", JoinType.LEFT);
        q.select(root).distinct(true);

        List<Predicate> predicates = new ArrayList<>();

        if (statuses != null) {
            predicates.add(statuses.isEmpty() ? b.disjunction() : root.get("status").in(statuses));
        }

        if (params != null) {
            String id = params.get("id");
            if (id != null && !id.isEmpty()) {
                predicates.add(b.equal(root.get("id"), Long.parseLong(id)));
            }

            String kw = params.get("name");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            String fromPrice = params.get("fromPrice");
            if (fromPrice != null && !fromPrice.isEmpty()) {
                predicates.add(b.greaterThanOrEqualTo(root.get("ticketPrice"), new BigDecimal(fromPrice)));
            }

            String toPrice = params.get("toPrice");
            if (toPrice != null && !toPrice.isEmpty()) {
                predicates.add(b.lessThanOrEqualTo(root.get("ticketPrice"), new BigDecimal(toPrice)));
            }

            String cateId = params.get("cateId");
            if (cateId != null && !cateId.isEmpty()) {
                predicates.add(b.equal(root.get("category").get("id"), Integer.parseInt(cateId)));
            }

            String statusId = params.get("statusId");
            if (statusId != null && !statusId.isEmpty()) {
                predicates.add(b.equal(root.get("status").get("id"), Integer.parseInt(statusId)));
            }

            String startTime = params.get("startTime");
            if (startTime != null && !startTime.isEmpty()) {
                predicates.add(b.greaterThanOrEqualTo(root.get("startTime"), new Date(Long.parseLong(startTime))));
            }

            String endTime = params.get("endTime");
            if (endTime != null && !endTime.isEmpty()) {
                predicates.add(b.lessThanOrEqualTo(root.get("endTime"), new Date(Long.parseLong(endTime))));
            }

            String location = params.get("location");
            if (location != null && !location.isEmpty()) {
                predicates.add(b.like(root.get("location"), String.format("%%%s%%", location)));
            }
        }

        q.where(predicates.toArray(new Predicate[0]));

        if (params != null) {
            String sortBy = params.get("sortBy");
            String sortDir = params.getOrDefault("sortDir", "desc");

            if (sortBy != null && !sortBy.isEmpty()) {
                if ("startTime".equalsIgnoreCase(sortBy)) {
                    q.orderBy("asc".equalsIgnoreCase(sortDir) ? b.asc(root.get("startTime")) : b.desc(root.get("startTime")));
                } else if ("ticketPrice".equalsIgnoreCase(sortBy)) {
                    q.orderBy("asc".equalsIgnoreCase(sortDir) ? b.asc(root.get("ticketPrice")) : b.desc(root.get("ticketPrice")));
                } else {
                    q.orderBy(b.desc(root.get("id")));
                }
            } else {
                q.orderBy(b.desc(root.get("id")));
            }
        } else {
            q.orderBy(b.desc(root.get("id")));
        }

        Query query = session.createQuery(q);

        if (params != null) {
            int pageSize = this.env.getProperty("events.pageSize", Integer.class, 20);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;

            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }

    @Override
    public Event addEvent(Event event) {
        Session s = factory.getObject().getCurrentSession();
        if (event.getId() == null) {
            System.out.println("Media: " + event.getEventMedias().size());
            s.persist(event);
        } else {
            s.merge(event);
        }
        return event;
    }

    @Override
    public boolean deleteEvent(Event event) {
        Session s = factory.getObject().getCurrentSession();
        s.remove(event);
        return true;
    }

    @Override
    public List<Event> getOrganizerEvents(Long organizerId, Map<String, String> params) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Event> q = b.createQuery(Event.class);
        Root<Event> root = q.from(Event.class);
        root.fetch("eventMedias", JoinType.LEFT);
        root.fetch("category", JoinType.LEFT);
        root.fetch("status", JoinType.LEFT);
        root.fetch("organizer", JoinType.LEFT);
        q.select(root).distinct(true);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.equal(root.get("organizer").get("id"), organizerId));

        if (params != null) {
            String id = params.get("id");
            if (id != null && !id.isEmpty()) {
                predicates.add(b.equal(root.get("id"), Long.parseLong(id)));
            }

            String kw = params.get("name");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            String fromPrice = params.get("fromPrice");
            if (fromPrice != null && !fromPrice.isEmpty()) {
                predicates.add(b.greaterThanOrEqualTo(root.get("ticketPrice"), new BigDecimal(fromPrice)));
            }

            String toPrice = params.get("toPrice");
            if (toPrice != null && !toPrice.isEmpty()) {
                predicates.add(b.lessThanOrEqualTo(root.get("ticketPrice"), new BigDecimal(toPrice)));
            }

            String cateId = params.get("cateId");
            if (cateId != null && !cateId.isEmpty()) {
                predicates.add(b.equal(root.get("category").get("id"), Integer.parseInt(cateId)));
            }

            String status = params.get("status");
            if (status != null && !status.isEmpty()) {
                predicates.add(b.like(root.get("status").get("name"), String.format("%%%s%%", status)));
            }

            String startTime = params.get("startTime");
            if (startTime != null && !startTime.isEmpty()) {
                predicates.add(b.greaterThanOrEqualTo(root.get("startTime"), new Date(Long.parseLong(startTime))));
            }

            String endTime = params.get("endTime");
            if (endTime != null && !endTime.isEmpty()) {
                predicates.add(b.lessThanOrEqualTo(root.get("endTime"), new Date(Long.parseLong(endTime))));
            }

            String location = params.get("location");
            if (location != null && !location.isEmpty()) {
                predicates.add(b.like(root.get("location"), String.format("%%%s%%", location)));
            }
        }

        q.where(predicates.toArray(new Predicate[0]));

        if (params != null) {
            String sortBy = params.get("sortBy");
            String sortDir = params.getOrDefault("sortDir", "desc");

            if (sortBy != null && !sortBy.isEmpty()) {
                if ("startTime".equalsIgnoreCase(sortBy)) {
                    q.orderBy("asc".equalsIgnoreCase(sortDir) ? b.asc(root.get("startTime")) : b.desc(root.get("startTime")));
                } else if ("ticketPrice".equalsIgnoreCase(sortBy)) {
                    q.orderBy("asc".equalsIgnoreCase(sortDir) ? b.asc(root.get("ticketPrice")) : b.desc(root.get("ticketPrice")));
                } else {
                    q.orderBy(b.desc(root.get("id")));
                }
            } else {
                q.orderBy(b.desc(root.get("id")));
            }
        } else {
            q.orderBy(b.desc(root.get("id")));
        }

        Query query = s.createQuery(q);

        if (params != null) {
            int pageSize = this.env.getProperty("events.pageSize", Integer.class, 20);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;

            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }

    @Override
    public List<Event> searchEvents(Integer statusId, Long categoryId, Date startDate, Date endDate, String organizerName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Category> findAllCategories() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<User> findAllOrganizers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Event findById(Long id) {
        Session session = getCurrentSession();

        String hql = "SELECT e FROM Event e "
                + "LEFT JOIN FETCH e.category "
                + "LEFT JOIN FETCH e.organizer "
                + "LEFT JOIN FETCH e.status "
                + "LEFT JOIN FETCH e.eventMedias "
                + "WHERE e.id = :id";
        return session.createQuery(hql, Event.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public void update(Event event) {
        getCurrentSession().merge(event);
    }

    @Override
    public Map<String, Object> searchEvents(EventSearchCriteriaDTO criteria) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        int pageSize = this.env.getProperty("pagination.pageSize", Integer.class);
        int firstResult = (criteria.getPage() - 1) * pageSize;

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Event> countRoot = countQuery.from(Event.class);
        countQuery.select(cb.count(countRoot));

        List<Predicate> predicates = buildPredicates(criteria, cb, countRoot);
        countQuery.where(predicates.toArray(new Predicate[0]));
        Long totalElements = session.createQuery(countQuery).getSingleResult();

        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);
        root.fetch("category", JoinType.LEFT);
        root.fetch("organizer", JoinType.LEFT);
        root.fetch("status", JoinType.LEFT);

        List<Predicate> predicates_ = buildPredicates(criteria, cb, root);

        cq.where(
                predicates_.toArray(new Predicate[0])
        );
        cq.orderBy(cb.desc(root.get("createdAt")));

        List<Event> events = session.createQuery(cq)
                .setFirstResult(firstResult)
                .setMaxResults(pageSize)
                .getResultList();

        Map<String, Object> result = new HashMap<>();
        result.put("events", events);
        result.put("totalElements", totalElements);
        result.put("totalPages", (int) Math.ceil((double) totalElements / pageSize));
        result.put("currentPage", criteria.getPage());
        return result;
    }

    private List<Predicate> buildPredicates(EventSearchCriteriaDTO criteria, CriteriaBuilder cb, Root<Event> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getStatusId() != null) {
            predicates.add(cb.equal(root.get("status").get("id"), criteria.getStatusId()));
        }
        if (criteria.getCategoryId() != null) {
            predicates.add(cb.equal(root.get("category").get("id"), criteria.getCategoryId()));
        }
        if (criteria.getOrganizerId() != null) {
            predicates.add(cb.equal(root.get("organizer").get("id"), criteria.getOrganizerId()));
        }
        if (criteria.getKeyword() != null && !criteria.getKeyword().trim().isEmpty()) {
            predicates.add(cb.like(root.get("name"), "%" + criteria.getKeyword().trim() + "%"));
        }
        if (criteria.getFromDate() != null && !criteria.getFromDate().trim().isEmpty()) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("startTime"), DateTimeUtil.toDate(criteria.getFromDate())));
        }
        return predicates;
    }

    @Override
    public List<Object[]> findEventsWithDetailsByIds(List<Long> ids) {
        String hqlEvent = "SELECT DISTINCT e FROM Event e "
                + "LEFT JOIN FETCH e.category "
                + "LEFT JOIN FETCH e.organizer "
                + "LEFT JOIN FETCH e.eventMedias "
                + "LEFT JOIN FETCH e.status "
                + "WHERE e.id IN :eventIds";

        List<Event> events = getCurrentSession().createQuery(hqlEvent, Event.class)
                .setParameter("eventIds", ids)
                .getResultList();

        String hqlStat = "FROM EventStatistic s WHERE s.id IN :eventIds";
        List<EventStatistic> stats = getCurrentSession().createQuery(hqlStat, EventStatistic.class)
                .setParameter("eventIds", ids)
                .getResultList();

        Map<Long, EventStatistic> statMap = stats.stream().collect(Collectors.toMap(EventStatistic::getEventId, s -> s));

        List<Object[]> result = new ArrayList<>();
        for (Event e : events) {
            result.add(new Object[]{e, statMap.get(e.getId())});
        }

        return result;
    }

    @Override
    public List<Event> findEventsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        String hqlEvent = "SELECT DISTINCT e FROM Event e "
                + "LEFT JOIN FETCH e.category "
                + "LEFT JOIN FETCH e.organizer "
                + "LEFT JOIN FETCH e.eventMedias "
                + "LEFT JOIN FETCH e.status "
                + "WHERE e.id IN :eventIds";

        return getCurrentSession().createQuery(hqlEvent, Event.class)
                .setParameter("eventIds", ids)
                .getResultList();
    }
}
