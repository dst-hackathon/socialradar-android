package com.hackathon.hackathon2014.utility;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Krai on 11/1/14 AD.
 */
public class RestProvider {

    private static RestTemplate restTemplate;

    public static RestTemplate getInstance(){
        if( restTemplate == null ){
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        }
        return restTemplate;
    }

}
