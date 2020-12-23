package careers.debut.repository

import careers.debut.model.Gender
import careers.debut.vo.CharacterVO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InMemoryCharacterRepositoryTest {


    private var characterRepository = InMemoryCharacterRepository()

    @Test
    fun testAllInMemoryDatabase() {

        val rick = CharacterVO(1, "Rick", Gender.Male.name, "location1")
        val morty = CharacterVO(2, "Morty", Gender.Male.name, "location2")

        var characters = characterRepository.getAllCharacters().join();
        Assertions.assertTrue(characters.isEmpty())

        characterRepository.addCharacter(rick)

        characters = characterRepository.getAllCharacters().join();
        Assertions.assertEquals(1, characters.size)
        Assertions.assertEquals(rick, characters.get(0))


        characterRepository.addCharacter(morty)

        characters = characterRepository.getAllCharacters().join();
        Assertions.assertEquals(2, characters.size)
        Assertions.assertEquals(rick, characters.get(0))
        Assertions.assertEquals(morty, characters.get(1))

        var characterOptional = characterRepository.getCharacterById(10).join()
        assert(characterOptional.isEmpty)

        characterOptional = characterRepository.getCharacterById(2).join()
        assert(characterOptional.isPresent)
        Assertions.assertEquals(morty, characterOptional.get())


        characters = characterRepository.getCharactersByName("Beth").join();
        Assertions.assertTrue(characters.isEmpty())

        characters = characterRepository.getCharactersByName("Rick").join();
        Assertions.assertEquals(1, characters.size)
        Assertions.assertEquals(rick, characters.get(0))
    }
}