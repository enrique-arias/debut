package careers.debut.service

import careers.debut.model.CreateCharacterRequest
import careers.debut.model.Gender
import careers.debut.model.RickAndMortyCharacter
import careers.debut.repository.CharacterRepository
import careers.debut.vo.CharacterVO
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors


@Service
class DefaultCharacterService(private val rickAndMortyExternalWebsite: RickAndMortyExternalWebsite,
                              private val characterRepository: CharacterRepository): CharacterService {


    override fun getCharacters(): CompletableFuture<List<RickAndMortyCharacter>> {

        val rickAndMortyExternalCharacters = rickAndMortyExternalWebsite.getRickAndMortyCharacters()
        val characters = characterRepository.getAllCharacters()

        return rickAndMortyExternalCharacters.thenCombine(characters, this::mergeCharacters)
    }

    override fun getCharactersByName(name: String): CompletableFuture<List<RickAndMortyCharacter>> {

        val rickAndMortyExternalCharacters = rickAndMortyExternalWebsite.getRickAndMortyCharactersByName(name)
        val characters = characterRepository.getCharactersByName(name)

        return rickAndMortyExternalCharacters.thenCombine(characters, this::mergeCharacters)
    }

    override fun getCharactersById(id: Long): CompletableFuture<Optional<RickAndMortyCharacter>> {

        //TODO I am guessing we search in the external website, if not found we search in our local datasource, not sure the use case

        val characterCompletableFuture = rickAndMortyExternalWebsite.getRickAndMortyCharacterById(id)


        return characterCompletableFuture.thenCompose { externalCharacterFuture ->
            externalCharacterFuture.map { CompletableFuture.completedFuture(externalCharacterFuture) }.orElseGet{getInternalCharacterRepository(id) }
        }
    }

    private fun getInternalCharacterRepository(id: Long): CompletableFuture<Optional<RickAndMortyCharacter>> {

        return characterRepository.getCharacterById(id).thenApply { internalCharacterFuture -> internalCharacterFuture.map {
            internalCharacter -> convertToRickAndMortyCharacter(internalCharacter) }
        }
    }

    override fun addCharacter(characterRequest: CreateCharacterRequest): CompletableFuture<RickAndMortyCharacter> {

        return characterRepository.addCharacter(createCharacterVO(characterRequest)).thenApply { internalCharacter ->
            convertToRickAndMortyCharacter(internalCharacter)
        }
    }


    private fun mergeCharacters(rickAndMortyCharacters: List<RickAndMortyCharacter>, characters: List<CharacterVO>) : List<RickAndMortyCharacter> {


        return rickAndMortyCharacters + convertToRickAndMortyCharacters(characters)
    }

    private fun convertToRickAndMortyCharacters(characters: List<CharacterVO>): List<RickAndMortyCharacter> {

        return characters.stream().map { convertToRickAndMortyCharacter(it)}.collect(Collectors.toList()) //Compiler complaining about characters.map (version 1.4)
    }

    private fun convertToRickAndMortyCharacter(character: CharacterVO): RickAndMortyCharacter {

        return RickAndMortyCharacter(character.id, character.name, Gender.getGender(character.gender), character.picture)
    }

    private fun createCharacterVO(characterRequest: CreateCharacterRequest) :CharacterVO {

        val character = CharacterVO()
        character.name = characterRequest.name
        character.gender = characterRequest.gender.name
        character.picture = characterRequest.picture

        return character
    }

}