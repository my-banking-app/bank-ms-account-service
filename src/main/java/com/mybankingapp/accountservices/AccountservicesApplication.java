package com.mybankingapp.accountservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class AccountservicesApplication {

        public static void main(String[] args) {
                log.info("Iniciando aplicación Accountservices");
                SpringApplication.run(AccountservicesApplication.class, args);
        }

}
