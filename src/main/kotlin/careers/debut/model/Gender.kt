package careers.debut.model

enum class Gender {
    Male,
    Female,
    Genderless,
    unknown;

    companion object {
        fun getGender(name: String): Gender {

            for(gender in Gender.values()) {
                if(gender.name == name) {
                    return gender
                }
            }
            return Gender.unknown;
        }
    }

}