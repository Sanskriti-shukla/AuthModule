package com.example.auth.commons.listener;

import com.example.auth.commons.model.AdminConfiguration;
import com.example.auth.commons.model.RestAPI;
import com.example.auth.commons.repository.AdminRepository;
import com.example.auth.commons.repository.RestAPIRepository;
import com.example.auth.commons.service.AdminConfigurationService;
import com.example.auth.commons.utils.Utils;
import com.example.auth.controller.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

@Component
@Slf4j
public class ApplicationStartUpEventListener {
    boolean skip = false;

    @Autowired
    RestAPIRepository restAPIRepository;
    @Autowired
    AdminRepository adminRepository;
    // @Autowired
    //SchedulerService schedulerService;
    @Autowired
    AdminConfigurationService adminConfigurationService;
    @Autowired
    AdminConfiguration configuration;

    @Autowired
    Utils utils;
   /* @Autowired
    EmailModel emailModel;
*/

    @EventListener()
    @Async
    public void onApplicationEvent(ContextRefreshedEvent event) throws InvocationTargetException, IllegalAccessException {
        log.debug("Landed in here");
        AdminConfiguration configuration = new AdminConfiguration();
        List<AdminConfiguration> configurations = adminRepository.findAll();
        if (!CollectionUtils.isEmpty(configurations)) {
            log.debug("Module Technical configurations exists");
            configuration = configurations.get(0);
        } else {
            configuration = new AdminConfiguration();
            configuration.setCreatedBy("SYSTEM");
            configuration.setCreated(new Date());
            configuration.setUpdatedBy("SYSTEM");
            configuration.setUpdated(new Date());
            configuration = adminRepository.insert(configuration);
            log.debug("Automatically create the module technical configurations");
        }
        // On Application Start up , create the list of authorized services for authorized data
        if (!skip) {
            saveIfNotExits(Utils.getAllMethodNames(UserController.class));
            saveIfNotExits(Utils.getAllMethodNames(CategoryController.class));
            saveIfNotExits(Utils.getAllMethodNames(PurchaseLogHistoryController.class));
            saveIfNotExits(Utils.getAllMethodNames(ItemController.class));
            saveIfNotExits(Utils.getAllMethodNames(CustomerController.class));
            saveIfNotExits(Utils.getAllMethodNames(AdminController.class));
            saveIfNotExits(Utils.getAllMethodNames(StockController.class));
            saveIfNotExits(Utils.getAllMethodNames(TopicController.class));
            saveIfNotExits(Utils.getAllMethodNames(UserDataController.class));
            saveIfNotExits(Utils.getAllMethodNames(PostController.class));
//            saveIfNotExits(Utils.getAllMethodNames(NotificationController.class));
        }

        Date currentDate = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm (z)");
        dateFormatter.setTimeZone(TimeZone.getTimeZone("CST"));
        String cstTime = dateFormatter.format(currentDate);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String gmtTime = dateFormatter.format(currentDate);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("IST"));
        String istTime = dateFormatter.format(currentDate);

        Set<String> emails = configuration.getTechAdmins();

        /*if (StringUtils.isEmpty(e.getTo())){
            emails.setTo(emails.iterator().next());
        }*/
        /*EmailModel emailModel = new EmailModel();
        AdminConfiguration adminConfiguration= adminConfigurationService.getConfiguration();
        emailModel.setTo(emails.iterator().next());
        emailModel.setCc(adminConfiguration.getTechAdmins());
        //emailModel.setBcc(emailNotificationConfig.getBcc());
        emailModel.setSubject("Auth module started");
        emailModel.setMessage("AuthModule<br/><br/>CST time : "+cstTime+"<br/>GMT time : "+gmtTime+"<br/>IST time : "+istTime);
        utils.sendEmailNow(emailModel);*/
        log.info("Module started mail sent to tech-admins");
        //scheduleCronJobs(adminRepository.findAll().get(0));*/
    }

    public void saveIfNotExits(List<RestAPI> apis) {
        apis.forEach(api -> {
            if (!restAPIRepository.existsByName(api.getName())) {
                log.info("Added API : {}", api.getName());
                restAPIRepository.insert(api);
            }
        });
    }

}

