package com.cmora.froglab_2.genetics

class Gene (val id: Int, val name: String, val chromosome: String,
            val inheritanceType: InheritanceType,
            val alleles: MutableList<String>,
            val position: Double,
            val allele_priority: MutableList<Int>,
            val epistatic_upon_names: MutableList<String>,
            val epistatic_under_names: MutableList<String>,
            val possible_phenotypes: MutableList<String>,
            val drawable_resources: MutableList<String>){
    var epistatic_upon_ids: MutableList<Int> = mutableListOf<Int>()
    var epistatic_under_ids: MutableList<Int> = mutableListOf<Int>()
    override fun toString(): String {
        return "Gene(id=$id, name='$name', chromosome='$chromosome', inheritanceType=$inheritanceType, alleles=$alleles, position=$position, allele_priority=$allele_priority, epistatic_upon_names=$epistatic_upon_names, epistatic_under_names=$epistatic_under_names, epistatic_upon_ids=$epistatic_upon_ids, epistatic_under_ids=$epistatic_under_ids)"
    }
}