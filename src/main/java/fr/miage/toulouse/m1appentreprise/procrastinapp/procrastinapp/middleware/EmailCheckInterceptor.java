package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.RoleUtilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.UtilisateurRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@Component
public class EmailCheckInterceptor implements HandlerInterceptor {

    private final UtilisateurRepository utilisateurRepository;

    public EmailCheckInterceptor(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod handlerMethod) {

            AllowedRoles allowedRoles = handlerMethod.getMethodAnnotation(AllowedRoles.class);
            // Pas d'annotation = accès libre
            if (allowedRoles == null) {
                return true;
            }

            String email = request.getHeader("X-User-Email");

            if (email == null || email.isBlank()) {
                sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Email manquant dans les headers");
                return false;
            }

            Utilisateur utilisateur = utilisateurRepository.findUtilisateurByAdresseMail(email);

            if(utilisateur == null){
                sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "Utilisateur inexistant (adresse mail invalide)");
                return false;
            }

            RoleUtilisateur userRole = utilisateur.getRole();

            if (userRole == null) {
                sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Aucun rôle fourni dans l'en-tête 'X-Role'");
                return false;
            }

            for (RoleUtilisateur allowed : allowedRoles.value()) {
                if (allowed.equals(userRole)) {
                    request.setAttribute("utilisateur", utilisateur);
                    return true;
                }
            }

            sendJsonError(response, HttpServletResponse.SC_FORBIDDEN,
                    "Accès refusé : rôle '" + userRole + "' non autorisé pour cette opération.");
            return false;
        }

        return true;
    }

    private void sendJsonError(HttpServletResponse response, int status, String message) throws Exception {
        response.setStatus(status);
        response.setContentType("application/json");
        Map<String, String> error = new HashMap<>();
        error.put("status", String.valueOf(status));
        error.put("error", message);
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(error));
    }
}

