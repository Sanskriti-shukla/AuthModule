package com.example.auth.commons.advice;

import com.example.auth.commons.decorator.GeneralHelper;
import com.example.auth.commons.decorator.RequestSession;
import com.example.auth.commons.helper.UserHelper;
import com.example.auth.commons.decorator.Response;
import com.example.auth.decorator.ImageUrl;
import com.example.auth.decorator.MainDateFilter;
import com.example.auth.model.CanvasjsChartData;
import com.example.auth.model.PurchaseLogHistory;
import com.example.auth.stockPile.model.Notification;
import com.example.auth.stockPile.model.Reaction;
import com.example.auth.stockPile.model.Subscriber;
import com.example.auth.stockPile.model.UserData;


import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
    public class GeneralBeans {



        @Bean
        public Response beanResponse() {
            return new Response();
        }
        @Bean
        ModelMapper getModelMapper(){
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper;
        }

        @Bean
        public GeneralHelper getGeneralHelper(){
            return new GeneralHelper();
        }

        @Bean
        public UserData  getUserData(){
            return  new UserData();
        }

        @Bean
        public MainDateFilter getMainDateFilter(){
            return new MainDateFilter();
        }


        @Bean
        public RequestSession getRequestSession()
        {
            return new RequestSession();
        }

        @Bean
        public NullAwareBeanUtilsBean beanUtilsBean(){
            return new NullAwareBeanUtilsBean();
        }

        @Bean
        public UserHelper getUserHelper(){
            return new UserHelper();
        }

        @Bean
        public Subscriber getSubscriber(){
            return new Subscriber();
        }

        @Bean
        public Reaction getReaction(){
            return  new Reaction();
        }

        @Bean
        public CanvasjsChartData getCanvasjsChartData()
        {
            return new CanvasjsChartData();
        }
//        @Bean
//        public RabbitListenerContainerFactory<SimpleMessageListenerContainer> prefetchTenRabbitListenerContainerFactory(ConnectionFactory rabbitConnectionFactory) {
//            SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//            factory.setConnectionFactory(rabbitConnectionFactory);
//            factory.setPrefetchCount(Integer.valueOf(prefetchCount));
//            factory.setConcurrentConsumers(Integer.valueOf(concurrentConsumers));
//            return factory;
//        }

        @Bean
        public JavaMailSender javaMailSender() {
            return new JavaMailSenderImpl();
        }
       @Bean
        public PurchaseLogHistory purchaseLogHistory(){
            return new PurchaseLogHistory();
       }


       @Bean
    public Notification getNotification(){
            return  new Notification();
       }

    @Bean
    public ImageUrl imageUrl() {
        return ImageUrl.GOOGLE_IMG_URL; // Assuming DEFAULT is one of the enum values
    }
    }



