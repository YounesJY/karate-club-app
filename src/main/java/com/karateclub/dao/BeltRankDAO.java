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

    // NEW METHOD: Get higher ranks than the current rank
    public List<BeltRank> getHigherRanks(int currentRankId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM BeltRank br WHERE br.rankID > :currentRankId ORDER BY br.rankID";
            Query<BeltRank> query = session.createQuery(hql, BeltRank.class);
            query.setParameter("currentRankId", currentRankId);
            return query.getResultList();
        }
    }

    // Alternative method: Get higher ranks by rank order (if you have a specific order field)
    public List<BeltRank> getHigherRanksByOrder(int currentRankOrder) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM BeltRank br WHERE br.rankOrder > :currentRankOrder ORDER BY br.rankOrder";
            Query<BeltRank> query = session.createQuery(hql, BeltRank.class);
            query.setParameter("currentRankOrder", currentRankOrder);
            return query.getResultList();
        }
    }

    // Method to get the next rank (immediate higher rank)
    public BeltRank getNextRank(int currentRankId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM BeltRank br WHERE br.rankID > :currentRankId ORDER BY br.rankID";
            Query<BeltRank> query = session.createQuery(hql, BeltRank.class);
            query.setParameter("currentRankId", currentRankId);
            query.setMaxResults(1);
            return query.uniqueResult();
        }
    }
}