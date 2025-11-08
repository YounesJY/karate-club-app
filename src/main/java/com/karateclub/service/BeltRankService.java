package com.karateclub.service;

import com.karateclub.model.BeltRank;
import com.karateclub.service.exception.NotFoundException;
import com.karateclub.service.exception.ValidationException;
import com.karateclub.service.exception.BusinessRuleException;

import java.util.List;

public interface BeltRankService {

    // Basic CRUD operations
    BeltRank getBeltRankById(int rankId);
    List<BeltRank> getAllBeltRanks();
    BeltRank createBeltRank(BeltRank beltRank);
    BeltRank updateBeltRank(BeltRank beltRank);
    void deleteBeltRank(int rankId);
}