package careers.debut.service

import careers.debut.model.RickAndMortyCharacter
import java.util.*
import java.util.concurrent.CompletableFuture

interface RickAndMortyExternalWebsite {

    fun getRickAndMortyCharacters(): CompletableFuture<List<RickAndMortyCharacter>>

    fun getRickAndMortyCharactersByName(name: String): CompletableFuture<List<RickAndMortyCharacter>>

    fun getRickAndMortyCharacterById(id: Long): CompletableFuture<Optional<RickAndMortyCharacter>>

}
