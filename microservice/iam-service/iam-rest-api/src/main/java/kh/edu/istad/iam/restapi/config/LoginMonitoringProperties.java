package kh.edu.istad.iam.restapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security.login-monitoring")
@Getter
@Setter
public class LoginMonitoringProperties {

    private int ipFloodDistinctUsernames = 5;

    private int ipFloodWindowMinutes = 10;

    private int bruteForceAttempts = 3;

    private int bruteForceWindowMinutes = 15;

    private int retentionDays = 90;
}
