package domain.repository

data class TaxonDto(
    val tree: List<String>,
    val fullName: String,
    val url: String,
)