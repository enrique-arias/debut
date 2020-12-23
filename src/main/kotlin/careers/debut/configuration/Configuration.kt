package careers.debut.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.net.http.HttpClient

@Configuration
class Configuration {

    @Bean
    fun objectMapper(builder: Jackson2ObjectMapperBuilder): ObjectMapper {
        return builder.build()
    }

    @Bean
    fun httpClient(): HttpClient {
        return HttpClient.newHttpClient()
    }
}