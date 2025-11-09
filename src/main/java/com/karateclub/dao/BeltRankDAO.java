package com.karateclub.dao;

import com.karateclub.config.HibernateUtil;
import com.karateclub.model.BeltRank;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class BeltRankDAO extends GenericDAO<BeltRank> {

    public BeltRankDAO() {
        super(BeltRank.class);
    }

    // Custom method: Find by rank name
    public BeltRank findByRankName(String rankName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM BeltRank br WHERE br.rankName = :rankName";
            Query<BeltRank> query = session.createQuery(hql, BeltRank.class);
            query.setParameter("rankName", rankName);
            return query.uniqueResult();
        }
    }

    // Custom method: Get all ranks ordered by rankID
    public List<BeltRank> getAllOrdered() {
        return findByHQL("FROM BeltRank br ORDER BY br.rankID");
    }

    // ADD THIS METHOD: Get all belt ranks higher than the current rank
    public List<BeltRank> getHigherRanks(int currentRankId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM BeltRank br WHERE br.rankID > :currentRankId ORDER BY br.rankID";
            Query<BeltRank> query = session.createQuery(hql, BeltRank.class);
            query.setParameter("currentRankId", currentRankId);
            return query.list();
        }
    }
}