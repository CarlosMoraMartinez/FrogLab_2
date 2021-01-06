package com.cmora.froglab_2.genetics

class Chromosome(val name: String, val type: ChromosomeType, val num_genes: Int) {
    var gene_inds: MutableList<Int> = mutableListOf<Int>()
    fun checkNumGenes(): String{
        if(this.num_genes == gene_inds.size)
            return ""
        return this.name + ": wrong number of genes. "
    }

    override fun toString(): String {
        return "Chromosome(name='$name', type=$type, num_genes=$num_genes, gene_inds=$gene_inds)"
    }

}