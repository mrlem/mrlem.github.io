package domain.repository

import domain.model.Node
import kotlin.io.path.Path
import kotlin.io.path.forEachLine

class TaxonomyRepository {

    companion object {
        private const val maxRank = 7
    }

    val root = Node.RootNode()
    var ranks = listOf<String>()
        private set

    init {
        readLines()
    }

    private fun readLines() {
        var isHeader = true

        javaClass.getResource("/TAXREFv16.txt")
            ?.path
            ?.let { Path(it) }
            ?.forEachLine { line ->
                if (isHeader) {
                    line.readHeader()
                    isHeader = false
                } else {
                    line.readTaxon()
                }
            }
    }

    private fun String.readHeader() {
        ranks = split("\t")
            .subList(0, maxRank + 1)
            .map { it.readValue().lowercase() }
    }

    private fun String.readTaxon() {
        val values = split("\t")
        val tree = values
            .subList(0, maxRank + 1)
            .map { it.readValue().lowercase() }

        val taxon = TaxonDto(
            tree = tree,
            fullName = values[TaxonFields.NOM_COMPLET.ordinal].readValue(),
            url = values[TaxonFields.URL.ordinal].readValue(),
        )

        root.merge(taxon)
    }

    private fun Node.merge(taxon: TaxonDto, fromRank: Int = 0) {
        val name = taxon.tree.getOrNull(fromRank)
        if (name.isNullOrEmpty() || fromRank >= maxRank) {
            Node.TaxonNode(
                name = taxon.fullName,
                url = taxon.url,
            )
                .also { children.add(it) }
        } else {
            // add rank node if needed
            val subNode = children
                .firstOrNull { it.name == name }
                ?: Node.RankNode(name)
                    .also { children.add(it) }

            // continue merge
            subNode.merge(taxon, fromRank + 1)
        }
    }

    private fun String.readValue() =
        trim('"')

}