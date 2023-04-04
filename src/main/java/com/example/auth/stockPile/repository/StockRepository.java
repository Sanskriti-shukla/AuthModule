package com.example.auth.stockPile.repository;

import com.amazonaws.services.apigateway.model.Op;
import com.example.auth.stockPile.model.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StockRepository extends MongoRepository<Stock, String> ,StockCustomRepository {

    Optional<Stock> findByIdAndSoftDeleteIsFalse(String id);

    boolean existsBySymbolAndSoftDeleteIsFalse(String symbol);

    Optional<Stock> findBySymbolAndSoftDeleteIsFalse(String symbol);

    List<Stock> findAllBySoftDeleteFalse();

    List<Stock> findAllByIdAndSoftDeleteFalse(String stockId);
}
