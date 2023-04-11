package domain.model

sealed class Node {

    abstract val name: String
    abstract val children: MutableList<Node>

    data class RankNode(
        override val name: String,
        override val children: MutableList<Node> = mutableListOf(),
    ) : Node()

    data class RootNode(
        override val children: MutableList<Node> = mutableListOf(),
    ) : Node() {

        override val name = "root"

    }

    data class TaxonNode(
        override val name: String,
        override val children: MutableList<Node> = mutableListOf(),
        val url: String,
    ) : Node()

}