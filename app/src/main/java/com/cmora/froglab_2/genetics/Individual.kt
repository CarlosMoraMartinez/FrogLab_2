package com.cmora.froglab_2.genetics

import android.util.Log

class Individual(
    var id: Int, val family: Int, val base_genotype: BaseGenotype,
    val haplotype1: Haplotype, val haplotype2: Haplotype,
    val sex: Sex) {
    val MALE_UNICODE = "\u2642"
    val FEMALE_UNICODE = "\u2640"
    var phenotype: Phenotype = mutableListOf()
    var user_name: String = ""
    var date: Long = 0
    init{
        when(this.base_genotype.genome.sex_determination){
            SexDeterminationSystem.XY -> this.correctHaplotypesXY()
            SexDeterminationSystem.WZ -> this.correctHaplotypesWZ()
            SexDeterminationSystem.X0 -> this.correctHaplotypesX0()
        }
        this.phenotype = GenotypePhenotypeConverter.getPhenotype(this.haplotype1, this.haplotype2, this.base_genotype)
        Log.d("INDIVIDUAL", "init: fid=$id, sex=$sex, family=$family, nameAsString:${getNameAsString()}, genotype:${haplotype1}/${haplotype2}")
    }
    fun getAllPhenotypes(): String{
        var s = ""
        for(i in this.base_genotype.editable_genes)
            s += this.phenotype[i] + ", "
        return s.dropLast(2) + "."
    }
    fun getAllGenotypes(): String{
        var s = ""
        for(i in this.base_genotype.editable_genes)
            s += this.getGenotype(i) + ", "
        return s.dropLast(2) + "."
    }
    fun getNameAsString(): String{
        var s = if(sex == Sex.MALE) MALE_UNICODE else FEMALE_UNICODE;
        if(this.family == -1)
            s += "P0."
        else
            s += this.family.toString() + "."
        return s + this.id.toString()
    }
    fun getNameAsString2(): String{
        var s = if(sex == Sex.MALE) MALE_UNICODE else FEMALE_UNICODE;
        if(this.family == -1)
            s += "P0." + this.id.toString()
        else
            s += this.user_name
        return s
    }
    fun getPhenotype(ind: Int):String{
        return this.phenotype[ind]
    }
    fun getGenotype(ind: Int): String {
        var ind2: Int = -1
        if (this.base_genotype.is_editable[ind]) {
            for (i in 0 until this.base_genotype.editable_genes.size) {
                if (this.base_genotype.editable_genes[i] == ind) {
                    ind2 = i
                    break
                }
            }
        } else {
            ind2 = ind
        }
        var a = this.haplotype1[ind2]
        var b = this.haplotype2[ind2]
        if (b >= 0) {
            if (this.base_genotype.genome.genes[ind].allele_priority[a] <= this.base_genotype.genome.genes[ind].allele_priority[b])
                return this.base_genotype.genome.genes[ind].alleles[a] + this.base_genotype.genome.genes[ind].alleles[b]
            return this.base_genotype.genome.genes[ind].alleles[b] + this.base_genotype.genome.genes[ind].alleles[a]
        } else if (a >= 0){
            return this.base_genotype.genome.genes[ind].alleles[a] + "_"//Assumes that the first allele is always the non-null one
        }else {
            return "__"
        }
    }
    fun getResources():MutableList<String>{
        var res = mutableListOf<String>()
        res.addAll(base_genotype.genome.extra)
        for(gene in base_genotype.genome.gene_load_order){
            for(i:Int in 0 until base_genotype.genome.genes[gene].possible_phenotypes.size){
                if(this.phenotype[gene] == base_genotype.genome.genes[gene].possible_phenotypes[i]) {
                    res.add(base_genotype.genome.genes[gene].drawable_resources[i])
                    break
                }
            }
        }
        return res
    }
    fun getGenome(): GenomeModel{
        return this.base_genotype.genome
    }
    fun getGenomeName(): String{
        return this.base_genotype.genome.name
    }
    fun correctHaplotypesXY(){

    }
    fun correctHaplotypesWZ(){
        var gene: Int
        if(this.sex == Sex.MALE){
            for(g:Int in 0 until this.base_genotype.editable_genes.size){
                gene = this.base_genotype.editable_genes[g]
                if(this.base_genotype.genome.genes[gene].chromosome == "chrW"){
                    this.haplotype1[g] = -1
                    this.haplotype2[g] = -1
                }
            }
        }else{
            for(g:Int in 0 until this.base_genotype.editable_genes.size){
                gene = this.base_genotype.editable_genes[g]
                if(this.base_genotype.genome.genes[gene].chromosome == "chrW"){
                    this.haplotype2[g] = -1
                }else if(this.base_genotype.genome.genes[gene].chromosome == "chrZ"){
                    this.haplotype2[g] = -1
                }
            }
        }
    }
    fun correctHaplotypesX0(){

    }
    override fun toString(): String {
        return "Individual(id=$id, family=$family, base_genotype=$base_genotype, haplotype1=$haplotype1, haplotype2=$haplotype2, sex=$sex, phenotype=$phenotype)"
    }

}