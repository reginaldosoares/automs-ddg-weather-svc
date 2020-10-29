package app.automs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources

@SpringBootApplication
@PropertySources(
    PropertySource(
        value = ["classpath:application-automs-sdk.properties"]),
    PropertySource(
        value = ["classpath:application-\${CONTEXT:local}.properties"],
        ignoreResourceNotFound = true)
)
class AutomationApplication

fun main(args: Array<String>) {
    val port: String? = System.getenv("PORT")

    runApplication<AutomationApplication>(*args) {
        setDefaultProperties(mapOf("server.port" to (port ?: "8080")))
    }
}
