package careers.debut.controller


import mu.KLogging
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/health")
class HealthController {
    
    @GetMapping
    fun healthCheck(): ResponseEntity<String> {
        logger.info { "Everything looks good!" }
        return ResponseEntity("OK", OK)
    }

    companion object : KLogging()
}
