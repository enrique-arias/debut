package careers.debut.controller


import careers.debut.model.CreateCharacterRequest
import careers.debut.service.CharacterService
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.concurrent.CompletableFuture
import javax.validation.Valid


@RestController
@RequestMapping("/characters")
class CharacterController(private val objectMapper: ObjectMapper, private val characterService: CharacterService) {

    companion object : KLogging()

    @GetMapping
    fun getCharacters(@RequestParam(required = false, value = "name") nameOptional: Optional<String>): CompletableFuture<ResponseEntity<String>> {

        logger.info { "Requesting all characters with name: $nameOptional" }

        val characters = nameOptional.map{ name -> characterService.getCharactersByName(name)}.orElseGet{characterService.getCharacters()}

        return characters.thenApply { ResponseEntity(objectMapper.writeValueAsString(it), HttpStatus.OK) }
    }

    @GetMapping("/{id}")
    fun getCharacterById(@PathVariable(value="id") id: Long): CompletableFuture<ResponseEntity<String>> {

        logger.info { "Requesting character with id: $id" }

        return characterService.getCharactersById(id).thenApply{ characterOptional -> characterOptional.map {
            character ->  ResponseEntity(objectMapper.writeValueAsString(character), HttpStatus.OK )}
                .orElseGet { ResponseEntity(HttpStatus.NOT_FOUND)}
        }
    }

    @PostMapping
    fun addCharacter(@Valid @RequestBody createCharacterRequest: CreateCharacterRequest): CompletableFuture<ResponseEntity<String>> {

        logger.info { "Requesting create character: $createCharacterRequest" }

        return characterService.addCharacter(createCharacterRequest).thenApply{ ResponseEntity(objectMapper.writeValueAsString(it), HttpStatus.CREATED )}

    }

}
