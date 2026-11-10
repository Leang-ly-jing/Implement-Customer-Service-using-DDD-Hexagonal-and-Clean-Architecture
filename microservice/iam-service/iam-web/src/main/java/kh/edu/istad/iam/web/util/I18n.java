package kh.edu.istad.iam.web.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class I18n {

    private final MessageSource messageSource;

    public I18n(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String t(String key, Object... args) {
        try {
            return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            if (args != null && args.length > 0 && args[0] != null) {
                return args[0].toString();
            }
            return key;
        }
    }
}

