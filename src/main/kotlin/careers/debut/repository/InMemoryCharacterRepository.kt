package careers.debut.repository

import careers.debut.vo.CharacterVO
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors


@Repository
class InMemoryCharacterRepository: CharacterRepository {

    private val characters = mutableListOf<CharacterVO>()


    override fun getCharactersByName(name: String): CompletableFuture<List<CharacterVO>> {

        return CompletableFuture.completedFuture(characters.stream().filter {character -> character.name == name }.collect(Collectors.toList()))//Compiler complaining about characters.filter
    }

    override fun getAllCharacters(): CompletableFuture<List<CharacterVO>> {

        return CompletableFuture.completedFuture(characters)
    }

    override fun getCharacterById(id: Long): CompletableFuture<Optional<CharacterVO>> {

        return CompletableFuture.completedFuture(characters.stream().filter {character -> character.id == id }.findAny())

    }

    override fun addCharacter(character: CharacterVO): CompletableFuture<CharacterVO> {

        characters.add(character)
        return CompletableFuture.completedFuture(character)
    }

}