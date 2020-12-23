package careers.debut.service

import careers.debut.model.Gender
import careers.debut.model.RickAndMortyCharacter
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RickAndMortyExternalWebsiteAdapterTest {

    companion object {
        const val NAME = "Rick"
        const val ID : Long = 1
        const val ID_NOT_FOUND : Long = 2
        const val URL = "https://rickandmortyapi.com/api/character"
    }


    val client : HttpClient = mock()

    val objectMapper: ObjectMapper = ObjectMapper()

    val httpResponse: HttpResponse<String> = mock()


    private val rickAndMortyExternalWebsite = RickAndMortyExternalWebsiteAdapter(objectMapper, client)

    @Test
    fun getRickAndMortyCharactersShouldFailApiRequest() {

        val request = createApiRequest(URL)

        Mockito.`when`(client.sendAsync(request, HttpResponse.BodyHandlers.ofString())).thenReturn(CompletableFuture.completedFuture(httpResponse))
        Mockito.`when`(httpResponse.statusCode()).thenReturn(500)

        val characters = rickAndMortyExternalWebsite.getRickAndMortyCharacters().join()
        assertTrue(characters.isEmpty())
    }

    @Test
    fun getRickAndMortyCharacters() {

        val request = createApiRequest(URL)

        val rickAndMortyCharactes = listOf(RickAndMortyCharacter(1, "Rick Sanchez", Gender.Male, "https://rickandmortyapi.com/api/character/avatar/1.jpeg"),
                RickAndMortyCharacter(2, "Morty Smith", Gender.Male, "https://rickandmortyapi.com/api/character/avatar/2.jpeg"))

        Mockito.`when`(client.sendAsync(request, HttpResponse.BodyHandlers.ofString())).thenReturn(CompletableFuture.completedFuture(httpResponse))
        Mockito.`when`(httpResponse.statusCode()).thenReturn(200)
        Mockito.`when`(httpResponse.body()).thenReturn(getCharactersJsonResponse())

        val characters = rickAndMortyExternalWebsite.getRickAndMortyCharacters().join()
        assertEquals(rickAndMortyCharactes, characters)
    }

    @Test
    fun getRickAndMortyCharactersByNameShouldFailApiRequest() {

        val request = createApiRequest(URL + "?name=Rick")

        Mockito.`when`(client.sendAsync(request, HttpResponse.BodyHandlers.ofString())).thenReturn(CompletableFuture.completedFuture(httpResponse))
        Mockito.`when`(httpResponse.statusCode()).thenReturn(500)

        val characters = rickAndMortyExternalWebsite.getRickAndMortyCharactersByName(NAME).join()
        assertTrue(characters.isEmpty())

    }

    @Test
    fun getRickAndMortyCharactersByName() {

        val request = createApiRequest(URL + "?name=Rick")

        val rickAndMortyCharactes = listOf(RickAndMortyCharacter(1, "Rick Sanchez", Gender.Male, "https://rickandmortyapi.com/api/character/avatar/1.jpeg"))

        Mockito.`when`(client.sendAsync(request, HttpResponse.BodyHandlers.ofString())).thenReturn(CompletableFuture.completedFuture(httpResponse))
        Mockito.`when`(httpResponse.statusCode()).thenReturn(200)
        Mockito.`when`(httpResponse.body()).thenReturn(getCharactersJsonResponseWithNameRick())

        val characters = rickAndMortyExternalWebsite.getRickAndMortyCharactersByName(NAME).join()
        assertEquals(rickAndMortyCharactes, characters)

    }

    @Test
    fun getRickAndMortyCharactersByIdShouldFailApiRequest() {

        val request = createApiRequest(URL + "/1")

        Mockito.`when`(client.sendAsync(request, HttpResponse.BodyHandlers.ofString())).thenReturn(CompletableFuture.completedFuture(httpResponse))
        Mockito.`when`(httpResponse.statusCode()).thenReturn(500)

        val character = rickAndMortyExternalWebsite.getRickAndMortyCharacterById(ID).join()
        assert(character.isEmpty)

    }

    @Test
    fun getRickAndMortyCharactersByIdShouldReturnEmptyIdNotFound() {

        val request = createApiRequest(URL + "/2")

        Mockito.`when`(client.sendAsync(request, HttpResponse.BodyHandlers.ofString())).thenReturn(CompletableFuture.completedFuture(httpResponse))
        Mockito.`when`(httpResponse.statusCode()).thenReturn(404)

        val character = rickAndMortyExternalWebsite.getRickAndMortyCharacterById(ID_NOT_FOUND).join()
        assert(character.isEmpty)
    }

    @Test
    fun getRickAndMortyCharactersById() {

        val request = createApiRequest(URL + "/1")

        val rickAndMortyCharacter = RickAndMortyCharacter(1, "Rick Sanchez", Gender.Male, "https://rickandmortyapi.com/api/character/avatar/1.jpeg")

        Mockito.`when`(client.sendAsync(request, HttpResponse.BodyHandlers.ofString())).thenReturn(CompletableFuture.completedFuture(httpResponse))
        Mockito.`when`(httpResponse.statusCode()).thenReturn(200)
        Mockito.`when`(httpResponse.body()).thenReturn(getCharactersJsonResponseWithId())

        val character = rickAndMortyExternalWebsite.getRickAndMortyCharacterById(ID).join()
        assert(character.isPresent)
        assertEquals(rickAndMortyCharacter, character.get())
    }

    private fun createApiRequest(url: String): HttpRequest {

        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .GET()
                .build()
    }

    private fun getCharactersJsonResponse(): String {

        return """
            {"info":{"count":2},"results":[
            {"id":1,"name":"Rick Sanchez", "gender": "Male", "image": "https://rickandmortyapi.com/api/character/avatar/1.jpeg"},
            {"id":2,"name":"Morty Smith", "gender": "Male", "image": "https://rickandmortyapi.com/api/character/avatar/2.jpeg"}]}
            """

    }

    private fun getCharactersJsonResponseWithNameRick(): String {

        return """
            {"info":{"count":2},"results":[
            {"id":1,"name":"Rick Sanchez", "gender": "Male", "image": "https://rickandmortyapi.com/api/character/avatar/1.jpeg"}]}
            """

    }

    private fun getCharactersJsonResponseWithId(): String {

        return """
            {"id":1,"name":"Rick Sanchez", "gender": "Male", "image": "https://rickandmortyapi.com/api/character/avatar/1.jpeg"}
            """

    }


}


