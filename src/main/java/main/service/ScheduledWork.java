package main.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledWork {

    @Scheduled(cron = "0 0 9-17 * * 1-5") // Запускается в 9:00:00 - 17:00:00 каждый день каждого месяца с понедельника
                                        // по пятницу
    public void doWork(){
        System.out.println("Проверка");
        //todo реализовать логику и определиться, что тут будет происходить

    }
}