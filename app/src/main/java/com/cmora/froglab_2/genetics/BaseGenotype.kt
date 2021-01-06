package com.cmora.froglab_2.genetics

class BaseGenotype(val haplotype1: Haplotype, val haplotype2: Haplotype,
                   val editable_genes: MutableList<Int>,
                   val genome: GenomeModel) {
    var phenotype: Phenotype = mutableListOf()
    init{
        this.phenotype = GenotypePhenotypeConverter.getPhenotype(this.haplotype1, this.haplotype2, this.genome)
    }
    var is_editable = MutableList<Boolean>(genome.num_genes){false}
    init{
        for(g in editable_genes)
            is_editable[g] = true
    }
    constructor(base_gt: BaseGenotype, editable_gene: Int) : this(base_gt.haplotype1,
    base_gt.haplotype2, MutableList<Int>(1){editable_gene}, base_gt.genome) {
    }
    constructor(base_gt: BaseGenotype, editable_genes: MutableList<Int>) : this(base_gt.haplotype1,
        base_gt.haplotype2, editable_genes, base_gt.genome) {
    }
    override fun toString(): String {
        return "BaseGenotype(haplotype1=$haplotype1, haplotype2=$haplotype2, editable_genes=$editable_genes, genome=${genome.name}, phenotype=$phenotype)"
    }
}