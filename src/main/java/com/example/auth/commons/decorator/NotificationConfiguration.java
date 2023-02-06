package com.example.auth.commons.decorator;


import com.google.common.io.Resources;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URL;
import java.nio.charset.StandardCharsets;

@Data
@AllArgsConstructor
public class NotificationConfiguration {

    TemplateModel templateModel;

    public NotificationConfiguration(){

        templateModel = new TemplateModel("Result ",loadHtmlTemplateOrReturnNull("result"),false);
    }

    private String loadHtmlTemplateOrReturnNull(String name){
        try{
            URL url = Resources.getResource("templates/"+name+".html");
            return Resources.toString(url, StandardCharsets.UTF_8);
        }catch (Exception e){
            return "";
        }
    }

}
