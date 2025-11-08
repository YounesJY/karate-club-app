package com.karateclub.service;

import com.karateclub.dao.BeltRankDAO;
import com.karateclub.model.BeltRank;
import com.karateclub.service.exception.NotFoundException;
import com.karateclub.service.exception.ValidationException;

import java.util.List;

public class BeltRankServiceImpl implements BeltRankService {
    private BeltRankDAO beltRankDAO;

    public BeltRankServiceImpl() {
        this.beltRankDAO = new BeltRankDAO();
    }

    public BeltRankServiceImpl(BeltRankDAO beltRankDAO) {
        this.beltRankDAO = beltRankDAO;
    }

    @Override
    public BeltRank getBeltRankById(int rankId) {
        validateBeltRankId(rankId);

        BeltRank beltRank = beltRankDAO.getById(rankId);
        if (beltRank == null) {
            throw new NotFoundException("Belt rank not found with ID: " + rankId);
        }
        return beltRank;
    }

    @Override
    public List<BeltRank> getAllBeltRanks() {
        return beltRankDAO.getAll();
    }

    @Override
    public BeltRank createBeltRank(BeltRank beltRank) {
        validateBeltRank(beltRank);

        // Basic business rule: Belt name must be unique
        if (isBeltNameExists(beltRank.getRankName())) {
            throw new ValidationException("Belt rank name already exists: " + beltRank.getRankName());
        }

        beltRankDAO.save(beltRank);
        return beltRank;
    }

    @Override
    public BeltRank updateBeltRank(BeltRank beltRank) {
        validateBeltRank(beltRank);
        validateBeltRankExists(beltRank.getRankID());

        beltRankDAO.update(beltRank);
        return beltRank;
    }

    @Override
    public void deleteBeltRank(int rankId) {
        validateBeltRankId(rankId);

        BeltRank beltRank = beltRankDAO.getById(rankId);
        if (beltRank != null) {
            // Business rule: Prevent deletion if members have this rank
            if (hasMembersWithThisRank(rankId)) {
                throw new ValidationException("Cannot delete belt rank that is assigned to members");
            }
            beltRankDAO.delete(rankId);
        } else {
            throw new NotFoundException("Belt rank not found with ID: " + rankId);
        }
    }

    // Basic validation methods (matching your MemberServiceImpl pattern)
    private void validateBeltRankId(int rankId) {
        if (rankId <= 0) {
            throw new ValidationException("Belt rank ID must be positive");
        }
    }

    private void validateBeltRank(BeltRank beltRank) {
        if (beltRank == null) {
            throw new ValidationException("Belt rank cannot be null");
        }
        if (beltRank.getRankName() == null || beltRank.getRankName().trim().isEmpty()) {
            throw new ValidationException("Belt rank name is required");
        }
        if (beltRank.getTestFees() < 0) {
            throw new ValidationException("Test fees cannot be negative");
        }
    }

    private void validateBeltRankExists(int rankId) {
        if (beltRankDAO.getById(rankId) == null) {
            throw new NotFoundException("Belt rank not found with ID: " + rankId);
        }
    }

    // Simple helper methods for basic business rules
    private boolean isBeltNameExists(String beltName) {
        return getAllBeltRanks().stream()
                .anyMatch(rank -> rank.getRankName().equalsIgnoreCase(beltName));
    }

    private boolean hasMembersWithThisRank(int rankId) {
        // This would need MemberDAO integration - keeping it simple for now
        // For basic CRUD, we can return false or implement later
        return false;
    }
}