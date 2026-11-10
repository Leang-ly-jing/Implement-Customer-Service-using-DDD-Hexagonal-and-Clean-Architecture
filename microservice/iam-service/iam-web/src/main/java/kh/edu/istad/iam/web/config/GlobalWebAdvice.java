package kh.edu.istad.iam.web.config;

import jakarta.servlet.http.HttpServletRequest;
import kh.edu.istad.iam.web.util.I18n;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalWebAdvice {

    private final MessageSource messageSource;

    @ModelAttribute("i18n")
    public I18n i18n() {
        return new I18n(messageSource);
    }

    @ModelAttribute("_csrf")
    public CsrfToken csrfToken(HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (token == null) {
            token = (CsrfToken) request.getAttribute("_csrf");
        }
        return token;
    }
}
