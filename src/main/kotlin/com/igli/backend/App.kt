package com.igli.backend

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
open class App {
}

fun main(args: Array<String>) {
    SpringApplication.run(App::class.java, *args)
}
