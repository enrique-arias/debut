package careers.debut.controller

import careers.debut.model.CreateCharacterRequest
import careers.debut.model.Gender
import careers.debut.model.RickAndMortyCharacter
import careers.debut.service.CharacterService
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito
import org.springframework.http.HttpStatus
import java.util.*
import java.util.concurrent.CompletableFuture


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CharacterControllerTest {


    companion object {
        const val RESPONSE_JSON = "response"
        const val NAME = "RICK"
        const val ID : Long = 1
    }

    val objectMapper: ObjectMapper =  mock()

    val characterService: CharacterService = mock()

    private val characterController = CharacterController(objectMapper, characterService)


    @Test
    fun getAllCharacters() {

        val rickAndMortyCharacters = listOf(RickAndMortyCharacter(1, "Rick", Gender.Male, "location1"),
                RickAndMortyCharacter(2, "Morty", Gender.Male, "location2"))

        Mockito.`when`(characterService.getCharacters()).thenReturn(CompletableFuture.completedFuture(rickAndMortyCharacters))
        Mockito.`when`(objectMapper.writeValueAsString(rickAndMortyCharacters)).thenReturn(RESPONSE_JSON)

        val response = characterController.getCharacters(Optional.empty()).join()

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertEquals(RESPONSE_JSON, response.body)
    }

    @Test
    fun getAllCharactersByName() {

        val rickAndMortyCharacters = listOf(RickAndMortyCharacter(1, "Rick", Gender.Male, "location1"),
                RickAndMortyCharacter(2, "Morty", Gender.Male, "location2"))

        Mockito.`when`(characterService.getCharactersByName(NAME)).thenReturn(CompletableFuture.completedFuture(rickAndMortyCharacters))
        Mockito.`when`(objectMapper.writeValueAsString(rickAndMortyCharacters)).thenReturn(RESPONSE_JSON)

        val response = characterController.getCharacters(Optional.of(NAME)).join()

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertEquals(RESPONSE_JSON, response.body)
    }

    @Test
    fun getAllCharactersByIdNotFound() {

        Mockito.`when`(characterService.getCharactersById(ID)).thenReturn(CompletableFuture.completedFuture(Optional.empty()))

        val response = characterController.getCharacterById(ID).join()

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun getAllCharactersById() {

        val rickAndMortyCharacter = RickAndMortyCharacter(1, "Rick", Gender.Male, "location1")

        Mockito.`when`(characterService.getCharactersById(ID)).thenReturn(CompletableFuture.completedFuture(Optional.of(rickAndMortyCharacter)))
        Mockito.`when`(objectMapper.writeValueAsString(rickAndMortyCharacter)).thenReturn(RESPONSE_JSON)

        val response = characterController.getCharacterById(ID).join()

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertEquals(RESPONSE_JSON, response.body)
    }

    @Test
    fun addCharacter() {

        val characterRequest = CreateCharacterRequest(NAME, Gender.Male, "location3")
        val rickAndMortyCharacter = RickAndMortyCharacter(ID, NAME, Gender.Male, "location1")

        Mockito.`when`(characterService.addCharacter(characterRequest)).thenReturn(CompletableFuture.completedFuture((rickAndMortyCharacter)))
        Mockito.`when`(objectMapper.writeValueAsString(rickAndMortyCharacter)).thenReturn(RESPONSE_JSON)

        val response = characterController.addCharacter(characterRequest).join()

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
        Assertions.assertEquals(RESPONSE_JSON, response.body)
    }



}