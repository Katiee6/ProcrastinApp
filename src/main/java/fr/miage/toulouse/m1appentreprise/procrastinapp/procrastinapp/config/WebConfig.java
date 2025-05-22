package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.config;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.middleware.EmailCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final EmailCheckInterceptor emailCheckInterceptor;

    public WebConfig(EmailCheckInterceptor emailCheckInterceptor) {
        this.emailCheckInterceptor = emailCheckInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(emailCheckInterceptor)
                .addPathPatterns("/api/secure/**"); // adapte à tes routes protégées
    }
}
