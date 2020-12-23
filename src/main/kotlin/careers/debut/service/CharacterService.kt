package careers.debut.service

import careers.debut.model.CreateCharacterRequest
import careers.debut.model.RickAndMortyCharacter
import java.util.*
import java.util.concurrent.CompletableFuture

interface CharacterService {

    fun getCharacters(): CompletableFuture<List<RickAndMortyCharacter>>

    fun getCharactersByName(name: String): CompletableFuture<List<RickAndMortyCharacter>>

    fun getCharactersById(id: Long): CompletableFuture<Optional<RickAndMortyCharacter>>

    fun addCharacter(characterRequest: CreateCharacterRequest): CompletableFuture<RickAndMortyCharacter>


}