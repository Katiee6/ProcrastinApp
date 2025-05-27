package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.config;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.middleware.EmailCheckInterceptor;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.resolvers.CurrentUserArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final EmailCheckInterceptor emailCheckInterceptor;

    @Autowired
    private CurrentUserArgumentResolver currentUserArgumentResolver;

    public WebConfig(EmailCheckInterceptor emailCheckInterceptor) {
        this.emailCheckInterceptor = emailCheckInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(emailCheckInterceptor)
                .addPathPatterns("/api/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserArgumentResolver);
    }
}
