package careers.debut.example.controller


import mu.KLogging
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/health")
class ExampleController {
    
    @GetMapping
    fun healthCheck() = ResponseEntity("OK", OK)
        .also { logger.info { "Everything looks good!" } }

    companion object : KLogging()
}
