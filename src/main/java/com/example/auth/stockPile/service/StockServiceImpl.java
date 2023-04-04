package com.example.auth.stockPile.service;

import com.example.auth.commons.constant.MessageConstant;
import com.example.auth.commons.exception.InvalidRequestException;
import com.example.auth.commons.exception.NotFoundException;
import com.example.auth.commons.helper.UserHelper;
import com.example.auth.decorator.pagination.FilterSortRequest;
import com.example.auth.stockPile.decorator.*;
import com.example.auth.stockPile.model.Stock;
import com.example.auth.stockPile.model.Subscribe;
import com.example.auth.stockPile.model.Subscriber;
import com.example.auth.stockPile.model.UserData;
import com.example.auth.stockPile.repository.StockRepository;
import com.example.auth.stockPile.repository.SubscriberRepository;
import com.example.auth.stockPile.repository.UserDataRepository;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    private final UserHelper userHelper;
    private final ModelMapper modelMapper;
    private final UserDataServiceImpl userDataService;
    private final UserDataRepository userDataRepository;
    private final SubscriberRepository subscriberRepository;

    public StockServiceImpl(StockRepository stockRepository, UserHelper userHelper, ModelMapper modelMapper, UserDataServiceImpl userDataService, UserDataRepository userDataRepository, SubscriberRepository subscriberRepository) {
        this.stockRepository = stockRepository;

        this.userHelper = userHelper;
        this.modelMapper = modelMapper;
        this.userDataService = userDataService;
        this.userDataRepository = userDataRepository;
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public StockResponse addStock(StockAddRequest stockAddRequest) {
        Stock stock = modelMapper.map(stockAddRequest, Stock.class);
        if (stockRepository.existsBySymbolAndSoftDeleteIsFalse(stockAddRequest.getSymbol())) {
            Stock existingStock = getBySymbol(stockAddRequest.getSymbol());
            StockResponse stockResponse = new StockResponse();
            stockResponse.setId(existingStock.getId());
            return stockResponse;
        }
        stockRepository.save(stock);
        return  modelMapper.map(stock, StockResponse.class);

    }

    @Override
    public StockResponse getStockById(String id) {
        Stock stock = stockById(id);
        return modelMapper.map(stock, StockResponse.class);

    }


    @Override
    public void updateStock(String id, StockAddRequest stockAddRequest) throws NoSuchFieldException, IllegalAccessException {
        Stock stock = stockById(id);
        update(id, stockAddRequest);
        userHelper.difference(stock, stockAddRequest);
    }

    @Override
    public void deleteStockById(String id) {
        Stock stock = stockById(id);
        stock.setSoftDelete(true);
        stockRepository.save(stock);
    }

    @Override
    public List<StockResponse> getAllStock() {
        List<Stock> stocks = stockRepository.findAllBySoftDeleteFalse();
        List<StockResponse> stockResponses = new ArrayList<>();
        stocks.forEach(stock -> {
            StockResponse stockResponse = modelMapper.map(stock, StockResponse.class);
            stockResponses.add(stockResponse);
        });
        return stockResponses;
    }

    @Override
    public Page<StockResponse> getAllStockByPagination(StockFilter filter, FilterSortRequest.SortRequest<StockSortBy> sort, PageRequest pagination) {
        return stockRepository.getAllStockByPagination(filter, sort, pagination);
    }

    @Override
    public String subscribeUnsubscribeStock(String symbol, String userId, Subscribe subscribe) {
        Stock stock = getBySymbol(symbol);
        UserData user = userDataService.userById(userId);
        String subscribesId = user.getId();
        List<String> subscribers = stock.getSubscribers();

        if (subscribe == Subscribe.SUBSCRIBE) {
            if (subscribers.contains(subscribesId)) {
                throw new InvalidRequestException(MessageConstant.YOU_HAVE_ALREDAY_SUBSCRIBED);
            } else {
                subscribers.add(subscribesId);
                user.setSubscribe(true);
                Subscriber subscriber = new Subscriber();
                subscriber.setStockid(stock.getId());
                subscriber.setCreatedOn(new Date());
                subscriber.setUserId(subscribesId);
                subscriberRepository.save(subscriber);
            }
        } else if (subscribe == Subscribe.UNSUBSCRIBE) {
            if (!subscribers.contains(subscribesId)) {
                throw new InvalidRequestException(MessageConstant.YOU_HAVE_NOT_SUBSCRIBED);
            } else {
                subscribers.remove(subscribesId);
                user.setSubscribe(false);
                subscriberRepository.deleteByStockidAndUserId(stock.getId(), subscribesId);
            }
        }
        stock.setSubscribers(subscribers);
        stockRepository.save(stock);
        userDataRepository.save(user);
        return subscribesId;
    }


    @Override
    public Map<String, List<Stock>> allSubscribers() {
        return stockRepository.findAllBySoftDeleteFalse().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(stock ->
                        stock.getSymbol()));
    }

    @Override
    public StockResponse getStockBySymbol(String symbol) {
        Stock stock = getBySymbol(symbol);
        return  modelMapper.map(stock, StockResponse.class);

    }

    @Override
    public List<StockSubscribed> subscribedStocksByUserId(String userId) {
        List<StockSubscribed> list = new ArrayList<>();
        List<Subscriber> subscriber = subscriberRepository.findAllByUserId(userId);
        subscriber.forEach(subscriber1 -> {
            String stockId = subscriber1.getStockid();
            List<Stock> stock = stockRepository.findAllByIdAndSoftDeleteFalse(stockId);
            stock.forEach(stock1 -> {
                StockSubscribed stockSubscribed = new StockSubscribed();
                stockSubscribed.setStockId(stock1.getId());
                stockSubscribed.setSymbol(stock1.getSymbol());
                stockSubscribed.setName(stock1.getName());
                list.add(stockSubscribed);
            });
        });
        return list;
    }


  public   Stock stockById(String id) {
        return stockRepository.findByIdAndSoftDeleteIsFalse(id).orElseThrow(() -> new NotFoundException(MessageConstant.ID_NOT_FOUND));
    }

    Stock stockBySymbol(String symbol) {
        return stockRepository.findBySymbolAndSoftDeleteIsFalse(symbol).orElseThrow(() -> new NotFoundException(MessageConstant.SYMBOL_NOT_FOUND));
    }


    private void update(String id, StockAddRequest stockAddRequest) {
        Stock stock = stockById(id);
        if (stockAddRequest.getSymbol() != null) {
            stock.setSymbol(stockAddRequest.getSymbol());
        }
        if (stockAddRequest.getDescription() != null) {
            stock.setDescription(stockAddRequest.getDescription());
        }
        if (stockAddRequest.getName() != null) {
            stock.setName(stockAddRequest.getName());
        }
        stockRepository.save(stock);

    }

    public Stock getBySymbol(String symbol) {
        return stockRepository.findBySymbolAndSoftDeleteIsFalse(symbol).orElseThrow(() -> new NotFoundException(MessageConstant.SYMBOL_NOT_FOUND));
    }


}
