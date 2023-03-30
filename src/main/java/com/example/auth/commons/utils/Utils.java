package com.example.auth.commons.utils;

import com.amazonaws.services.alexaforbusiness.model.NotFoundException;
import com.example.auth.commons.Access;
import com.example.auth.commons.decorator.RequestSession;
import com.example.auth.commons.enums.CustomHTTPHeaders;
import com.example.auth.commons.model.AdminConfiguration;
import com.example.auth.commons.model.EmailModel;
import com.example.auth.commons.model.RestAPI;
import com.example.auth.commons.service.AdminConfigurationService;
import com.example.auth.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class Utils {
    private static final String TOKEN_NOT_FOUND = "Token not found";
    @Autowired
    MessageSource messageSource;
    @Autowired
    AdminConfigurationService configurationService;
    @Autowired
    RequestSession requestSession;

    public static String generateVerificationToken(int length) {
        // Using random method
        Random random_method = new Random();

        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < length; i++) {

            otp.append(random_method.nextInt(10));
        }
        return otp.toString();
    }

    public static String getTokenFromHeaders(HttpServletRequest request) throws NotFoundException {

        String jwtToken = request.getHeader(CustomHTTPHeaders.TOKEN.toString());

        if (jwtToken == null) {

            throw new NotFoundException(TOKEN_NOT_FOUND);
        }
        return jwtToken;
    }

    public static String encodeBase64(String password) {

        byte[] pass = Base64.encodeBase64(password.getBytes());

        String actualString = new String(pass);

        return actualString;
    }

    public static String decodeBase64(String password) {

        byte[] actualByte = Base64.decodeBase64(password);

        String actualString = new String(actualByte);

        System.out.println(actualString);

        return actualString;
    }

    public static List<RestAPI> getAllMethodNames(Class className) {
        Method[] allMethods = className.getDeclaredMethods();

        List<RestAPI> apis = new ArrayList<>();
        for (Method method : allMethods) {
            if (Modifier.isPublic(method.getModifiers())) {
                Access a = method.getAnnotation(Access.class);
//                RequestMapping rm = method.getAnnotation(RequestMapping.class);
                String restApiName = null;
                if (method.isAnnotationPresent(GetMapping.class)) {
                    GetMapping getMapping = method.getAnnotation(GetMapping.class);
                    restApiName = getMapping.name();
                }else if (method.isAnnotationPresent(PostMapping.class)) {
                    PostMapping postMapping = method.getAnnotation(PostMapping.class);
                    restApiName = postMapping.name();
                } else if (method.isAnnotationPresent(PutMapping.class)) {
                    PutMapping putMapping = method.getAnnotation(PutMapping.class);
                    restApiName = putMapping.name();
                } else if (method.isAnnotationPresent(DeleteMapping.class)) {
                    DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
                    restApiName = deleteMapping.name();
                }
                if (a != null) {
                    List<String> authList = new ArrayList<>(Arrays.asList(a.levels()))
                            .stream()
                            .map(Enum::toString)
                            .collect(Collectors.toList());

                    RestAPI api = new RestAPI();
                    api.setName(restApiName);
                    api.setRoles(authList);
                    apis.add(api);
                }
            }
        }
        return apis;
    }

    public void sendEmailNow(EmailModel emailModel) {
        try {

            if (StringUtils.isEmpty(emailModel.getSubject())) {

                emailModel.setSubject(emailModel.getSubject());
            }
            // Recipient's email ID needs to be mentioned.
            // Sender's email ID needs to be mentioned
            AdminConfiguration adminConfiguration = configurationService.getConfiguration();

            String from = adminConfiguration.getFrom();
            Properties props = new Properties();
            props.put("mail.smtp.auth", adminConfiguration.isSmptAuth());//true
            props.put("mail.smtp.starttls.enable", adminConfiguration.isStarttls());//true
            //props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.host", adminConfiguration.getHost());//smtp.office365.com
            props.put("mail.smtp.port", adminConfiguration.getPort());//587

            // Get the Session object.
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(adminConfiguration.getUsername(), adminConfiguration.getPassword());
                }
            });
            session.setDebug(true);
            try {
                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(session);

                MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

                // Set From: header field of the header.
                helper.setFrom(new InternetAddress(adminConfiguration.getFrom()));

                // Set To: header field of the header.
                helper.setTo(emailModel.getTo());

                if (emailModel.getCc() != null && emailModel.getCc().size() != 0) {

                    String[] cc = new String[emailModel.getCc().size()];

                    emailModel.getCc().toArray(cc);

                    helper.setCc(cc);
                }
                if (emailModel.getBcc() != null && emailModel.getBcc().size() != 0) {

                    String[] bcc = new String[emailModel.getBcc().size()];

                    emailModel.getBcc().toArray(bcc);

                    helper.setBcc(bcc);
                }
                //file empty not of email model
                //
                if (emailModel.getFile() != null) {
                    try {
                        Multipart multipart = new MimeMultipart();
                        BodyPart messageBodyPart = new MimeBodyPart();
                        String file = emailModel.getFile().getPath();
                        System.out.println("file" + file);
                        DataSource source = new FileDataSource(file);
                        messageBodyPart.setDataHandler(new DataHandler(source));
                        messageBodyPart.setFileName(emailModel.getFile().getName());
                        multipart.addBodyPart(messageBodyPart);
                        message.setContent(multipart);
                        log.info("email sending start");

                    } catch (MessagingException e) {

                        throw new RuntimeException(e);
                    }
                }
                // Set Subject: header field
                helper.setSubject(emailModel.getSubject());
                // Now set the actual message
                if (!StringUtils.isEmpty(emailModel.getMessage())) {

                    helper.setText(emailModel.getMessage(), true);
                }
              /*  if(emailModel.getAttachmentList() != null && emailModel.getAttachmentList().size() != 0){
                    emailModel.getAttachmentList().forEach(attachment->{
                        try {
                            helper.addAttachment(attachment.getFileName(), new URLDataSource(new URL(attachment.getAttachmentUrl())));
                        } catch (MessagingException | IOException e) {
                            log.info("Unable to attach File");
                            e.printStackTrace();
                        }
                    });
                }
*/
                // Send message
                new Thread(() -> {

                    try {
                        Transport.send(message);

                        log.info("email sent successfully");

                    } catch (MessagingException e) {

                        e.printStackTrace();
                    }
                }).start();
            } catch (MessagingException e) {

                throw new RuntimeException(e);
            }
        } catch (Exception ignored) {
        }
    }


}