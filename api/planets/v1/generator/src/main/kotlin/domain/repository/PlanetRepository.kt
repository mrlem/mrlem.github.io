package domain.repository

import domain.model.Planet

class PlanetRepository {

    val planets: List<Planet>
    init {
        planets = readLines()
            .map { name -> Planet(name = name) }
    }

    private fun readLines() =
        javaClass.getResource("/planets.csv")
            .readText()
            .lines()
            .filter { !it.startsWith("#") }

}