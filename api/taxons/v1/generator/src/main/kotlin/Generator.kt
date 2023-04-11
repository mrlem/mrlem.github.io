import domain.model.Node
import domain.repository.TaxonomyRepository
import java.io.File

class Generator {

    private val outputDir = ".."

    fun run() {
        File(outputDir)
            .mkdirs()

        println("reading")
        val taxonomy = TaxonomyRepository()
            .root
        println("printing")
        taxonomy.printTree()

        println("generating")
        taxonomy.createTree()
    }

    private fun Node.printTree(prefix: String = "") {
        println("$prefix $name")

        children.forEach { child ->
            child.printTree("$prefix  ")
        }
    }

    private fun Node.createTree(baseDir: String = outputDir, dir: String = "taxons") {
        File("$baseDir/$dir").mkdirs()
        val indexFile = File("$baseDir/$dir/index.json")
"""
{
  "name": "$name",
  "children": [
${
    children.joinToString(",") { subchild ->
        val url = when (subchild) {
            is Node.TaxonNode -> subchild.url
            is Node.RankNode -> "https://mrlem.org/api/taxons/v1/$dir/${subchild.name}/"
            else -> ""
        }
        """
    {
      "name": "${subchild.name}",
      "url": "$url"
    }
"""
    }
}
  ]
}
"""
    .let { text -> indexFile.writeText(text) }

        children
            .filterIsInstance<Node.RankNode>()
            .forEach { child ->
                child.createTree(dir = "$dir/${child.name}")
            }
    }
}