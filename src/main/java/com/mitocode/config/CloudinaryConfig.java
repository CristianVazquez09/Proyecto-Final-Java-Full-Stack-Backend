package com.mitocode.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "duh5tpqee",
                "api_key", "872899865198466",
                "api_secret", "LWCuzRQXJoxwF_t752lphpWT2us"
        ));
    }
}
