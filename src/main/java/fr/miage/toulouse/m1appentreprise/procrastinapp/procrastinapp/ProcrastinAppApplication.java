package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProcrastinAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcrastinAppApplication.class, args);
    }

}
