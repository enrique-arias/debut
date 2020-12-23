package careers.debut.service

import careers.debut.model.RickAndMortyCharacter
import com.fasterxml.jackson.core.type.TypeReference
import org.springframework.stereotype.Service
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KLogging
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.util.*
import java.util.concurrent.CompletableFuture


@Service
class RickAndMortyExternalWebsiteAdapter(private val objectMapper: ObjectMapper,
                                         private val client: HttpClient): RickAndMortyExternalWebsite {

    companion object : KLogging()

    private val url = "https://rickandmortyapi.com/api/character"

    override fun getRickAndMortyCharacters(): CompletableFuture<List<RickAndMortyCharacter>> {

        val request = createApiRequest(URI.create(url))

        return client.sendAsync(request, BodyHandlers.ofString())
            .thenApply { response ->
                parseCharactersResponse(response)
            }
    }


    override fun getRickAndMortyCharactersByName(name: String): CompletableFuture<List<RickAndMortyCharacter>> {

        val request = createApiRequest(URI.create(url + "?name=" + name))

        return client.sendAsync(request, BodyHandlers.ofString())
            .thenApply { response ->
                parseCharactersResponse(response)
            }
    }

    override fun getRickAndMortyCharacterById(id: Long): CompletableFuture<Optional<RickAndMortyCharacter>> {

        val request = createApiRequest(URI.create(url + "/" + id))

        return client.sendAsync(request, BodyHandlers.ofString())
            .thenApply { response ->
                parseCharacterResponse(response)
            }

    }

    private fun createApiRequest(uri: URI): HttpRequest {

        return HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .GET()
                .build()
    }

    private fun parseCharactersResponse(response: HttpResponse<String>): List<RickAndMortyCharacter> {

        return if (response.statusCode() == 200) {

            val jsonResponseBody = objectMapper.readTree(response.body())
            val results = jsonResponseBody.get("results")

            objectMapper.readValue(results.toString(), object : TypeReference<List<RickAndMortyCharacter>>(){})
        }
        else {

           logger.info { "Error making API request to Rick and Morty website: $response" }
            emptyList()
        }
    }

    private fun parseCharacterResponse(response: HttpResponse<String>): Optional<RickAndMortyCharacter> {

        return if (response.statusCode() == 200) {

            val jsonResponseBody = objectMapper.readTree(response.body())

            Optional.of(objectMapper.readValue(jsonResponseBody.toString(), RickAndMortyCharacter::class.java))
        }
        else {
            logger.info { "Error making API request to Rick and Morty website: $response" }
            Optional.empty()
        }
    }


}