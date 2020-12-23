package careers.debut.vo

import javax.persistence.*

@Entity
data class CharacterVO(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1,

    @Column(nullable = false)
    var name: String = "",

    @Column(nullable = false)
    var gender: String = "",

    @Column(nullable = false)
    var picture: String = ""
)



