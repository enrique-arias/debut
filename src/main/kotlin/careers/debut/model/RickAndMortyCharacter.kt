package careers.debut.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class RickAndMortyCharacter @JsonCreator constructor(
        @JsonProperty("id") val id:Long,
        @JsonProperty("name") val name:String,
        @JsonProperty("gender") val gender:Gender,
        @JsonProperty("image") val picture:String)