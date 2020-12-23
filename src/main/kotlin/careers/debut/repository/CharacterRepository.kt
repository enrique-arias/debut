package careers.debut.repository

import careers.debut.vo.CharacterVO
import java.util.*
import java.util.concurrent.CompletableFuture

interface CharacterRepository {

    fun getCharactersByName(name: String): CompletableFuture<List<CharacterVO>>

    fun getAllCharacters(): CompletableFuture<List<CharacterVO>>

    fun getCharacterById(id: Long): CompletableFuture<Optional<CharacterVO>>

    fun addCharacter(character: CharacterVO): CompletableFuture<CharacterVO>

}
