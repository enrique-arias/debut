package careers.debut.service

import careers.debut.model.CreateCharacterRequest
import careers.debut.model.Gender
import careers.debut.model.RickAndMortyCharacter
import careers.debut.repository.CharacterRepository
import careers.debut.vo.CharacterVO
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito
import org.mockito.Mockito.never
import java.util.*
import java.util.concurrent.CompletableFuture

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultCharacterServiceTest {

    companion object {
        const val NAME = "Rick"
        const val ID: Long = 1

    }

    val characterRepository: CharacterRepository = mock()

    val rickAndMortyExternalWebsite: RickAndMortyExternalWebsite = mock()

    private val characterService = DefaultCharacterService(rickAndMortyExternalWebsite, characterRepository)


    @Test
    fun getAllCharacters() {


        val rick = CharacterVO(1, "Rick", Gender.Male.name, "location1")
        val morty = CharacterVO(2, "Morty", Gender.Male.name, "location2")

        val beth = RickAndMortyCharacter(3, "Beth", Gender.Female, "location3")

        val rickAndMortyCharacters = listOf(RickAndMortyCharacter(1, "Rick", Gender.Male, "location1"),
                RickAndMortyCharacter(2, "Morty", Gender.Male, "location2"), beth)

        Mockito.`when`(characterRepository.getAllCharacters()).thenReturn(CompletableFuture.completedFuture(listOf(rick, morty)))
        Mockito.`when`(rickAndMortyExternalWebsite.getRickAndMortyCharacters()).thenReturn(CompletableFuture.completedFuture(listOf(beth)))

        val characters = characterService.getCharacters().join()
        assertEquals(3, characters.size)
        assertTrue(rickAndMortyCharacters.containsAll(characters))
    }

    @Test
    fun getCharactersByName() {


        val rick = CharacterVO(1, "Rick", Gender.Male.name, "location1")
        val morty = CharacterVO(2, "Morty", Gender.Male.name, "location2")

        val beth = RickAndMortyCharacter(3, "Beth", Gender.Female, "location3")

        val rickAndMortyCharacters = listOf(RickAndMortyCharacter(1, "Rick", Gender.Male, "location1"),
                RickAndMortyCharacter(2, "Morty", Gender.Male, "location2"), beth)

        Mockito.`when`(characterRepository.getCharactersByName(NAME)).thenReturn(CompletableFuture.completedFuture(listOf(rick, morty)))
        Mockito.`when`(rickAndMortyExternalWebsite.getRickAndMortyCharactersByName(NAME)).thenReturn(CompletableFuture.completedFuture(listOf(beth)))

        val characters = characterService.getCharactersByName(NAME).join()
        assertEquals(3, characters.size)
        assertTrue(rickAndMortyCharacters.containsAll(characters))
    }

    @Test
    fun getCharactersByIdFoundInExternalWebsite() {

        val beth = RickAndMortyCharacter(3, "Beth", Gender.Female, "location3")

        Mockito.`when`(rickAndMortyExternalWebsite.getRickAndMortyCharacterById(ID)).thenReturn(CompletableFuture.completedFuture(Optional.of(beth)))

        val characterOptional = characterService.getCharactersById(ID).join()
        assertTrue(characterOptional.isPresent)
        assertEquals(beth, characterOptional.get())

        Mockito.verify(characterRepository, never()).getCharacterById(ID)

    }

    @Test
    fun getCharactersByIdNotFoundInExternalWebsite() {

        val morty = CharacterVO(2, "Morty", Gender.Male.name, "location2")

        val rickAndMortyCharacterMorty = RickAndMortyCharacter(2, "Morty", Gender.Male, "location2")


        Mockito.`when`(rickAndMortyExternalWebsite.getRickAndMortyCharacterById(ID)).thenReturn(CompletableFuture.completedFuture(Optional.empty()))
        Mockito.`when`(characterRepository.getCharacterById(ID)).thenReturn(CompletableFuture.completedFuture(Optional.of(morty)))


        val characterOptional = characterService.getCharactersById(ID).join()
        assertTrue(characterOptional.isPresent)
        assertEquals(rickAndMortyCharacterMorty, characterOptional.get())
    }

    @Test
    fun addCharacter() {

        val characterRequest = CreateCharacterRequest("Beth", Gender.Female, "location3")
        val bethVo = CharacterVO()
        bethVo.name = characterRequest.name
        bethVo.gender = characterRequest.gender.name
        bethVo.picture = characterRequest.picture
        val beth = RickAndMortyCharacter(-1, "Beth", Gender.Female, "location3")

        Mockito.`when`(characterRepository.addCharacter(bethVo)).thenReturn(CompletableFuture.completedFuture(bethVo))

        val character = characterService.addCharacter(characterRequest).join()
        assertEquals(beth, character)


    }
}


