package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.middleware;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.UtilisateurRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class EmailCheckInterceptor implements HandlerInterceptor {

    private final UtilisateurRepository utilisateurRepository;

    public EmailCheckInterceptor(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String email = request.getHeader("X-User-Email");

        if (email == null || email.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("❌ Email manquant dans les headers");
            return false;
        }

        if (!utilisateurRepository.existsByAdresseMail(email)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("❌ Utilisateur non reconnu");
            return false;
        }

        return true; // ✅ Autorisé
    }
}

