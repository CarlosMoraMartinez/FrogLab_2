package com.cmora.froglab_2.genetics

class GenomeModel(val name: String, val sex_determination: SexDeterminationSystem,
                    val num_autosomes:Int, val num_sexchromosomes: Int, val num_genes: Int,
                    val ploidy: Int, val about: String, val gene_load_order: MutableList<Int>,
                    val extra:MutableList<String>) {
    val chromosomes: MutableMap<String, Chromosome> = mutableMapOf<String, Chromosome>()
    val genes: MutableList<Gene> = mutableListOf<Gene>();


    fun addChromosome(chr: Chromosome){
        chromosomes[chr.name] = chr
    }
    fun addGene(gene: Gene){
        genes.add(gene)
        chromosomes[gene.chromosome]?.gene_inds?.add(gene.id)
    }
    fun checkValidity(): String{
        var auto = 0
        var sex = 0
        var this_chr = 0
        var output: String = ""
        var genes_right = (this.num_genes == genes.size)
        if(!genes_right){
            output += "Wrong number of total genes. "
        }
        for((k, v) in this.chromosomes){
            output += v.checkNumGenes()
            when(v.type){
                ChromosomeType.AUTOSOMAL -> ++auto
                ChromosomeType.SEXUAL -> ++sex
                else -> {output += "$k no valid type. "
                }
            }
        }
        if(auto != this.num_autosomes)
            output += "Wrong number of autosomes. "
        if(sex != this.num_sexchromosomes)
            output += "Wrong number of sex chromosomes. "
        return output
    }
    fun getGeneIdByName(query: String): Int{
        for(g : Gene in this.genes){
            if(g.name == query)
                return g.id
        }
        return -1
    }
    fun getAlleleIdByName(gene: String, allele: String): Int{
        val g = getGeneIdByName(gene)
        return getAlleleIdByName(g, allele)
    }
    fun getAlleleIdByName(g: Int, allele: String): Int{
        if(g == -1 || allele == "")
            return -1
        for(i:Int in 0 until genes[g].alleles.size){
            if(genes[g].alleles[i] == allele)
                return i
        }
        return -1
    }
    override fun toString(): String {
        var s = "GenomeModel(name='$name', sex_determination=$sex_determination, num_autosomes=$num_autosomes, num_sexchromosomes=$num_sexchromosomes, num_genes=$num_genes, ploidy=$ploidy, about='$about', chromosomes=$chromosomes, genes=$genes)\n"
        for(g: Gene in this.genes)
            s += g.toString() + "\n"
        return s
    }
}