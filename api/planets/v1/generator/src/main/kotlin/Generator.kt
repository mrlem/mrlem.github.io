import domain.model.Planet
import domain.repository.PlanetRepository
import java.io.File

class Generator {

    private val outputDir = "../planets"

    fun run() {
        File(outputDir)
            .mkdirs()

        val planets = PlanetRepository()
            .planets
        planets.generateList()
        planets.forEachIndexed { index, planet ->
            planet.generateDetail(index)
        }
    }

    private fun List<Planet>.generateList() =
        this
            .mapIndexed { index, planet ->
"""
  {
    "position": $index,
	"name": "${planet.name.capitalize()}",
	"url": "https://mrlem.org/api/planets/v1/planets/${planet.name}/",
	"imageUrl": "https://mrlem.org/api/planets/v1/planets/${planet.name}/${planet.name}.png"
  }"""
            }
            .joinToString(
                separator = ",",
                prefix = "[\n",
                postfix = "\n]",
            )
            .let { text -> File("$outputDir/index.json").writeText(text) }

    private fun Planet.generateDetail(index: Int) =
        """{
  "position": $index,
  "name": "${name.capitalize()}",
  "imageUrl": "https://mrlem.org/api/planets/v1/planets/$name/$name.png"
}"""
            .also { File("$outputDir/$name").mkdirs() }
            .let { text -> File("$outputDir/$name/index.json").writeText(text) }

}