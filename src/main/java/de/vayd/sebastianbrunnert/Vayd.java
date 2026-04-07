package de.vayd.sebastianbrunnert;

import de.vayd.sebastianbrunnert.authentication.filter.AccessComponent;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;

@SpringBootApplication
@EntityScan(basePackages = "de.vayd.sebastianbrunnert")
public class Vayd {

    @Getter
    private String key = "ChangeMe!";

    @Getter
    private static Vayd instance;

    @Autowired
    @Getter
    private ServletWebServerApplicationContext server;

    @Autowired
    @Getter
    private AccessComponent accessComponent;

    public static void main(String[] args) {
        new SpringApplication(Vayd.class).run(args);
        System.out.println("PORT:" + Vayd.getInstance().getServer().getWebServer().getPort());
    }

    public Vayd() {
        instance = this;
    }
}