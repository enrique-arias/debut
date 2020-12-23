package careers.debut.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class CreateCharacterRequest @JsonCreator constructor(@JsonProperty("name", required= true) val name: String,
                                                           @JsonProperty("gender", required= true) val gender: Gender,
                                                           @JsonProperty("picture", required= true) val picture: String)