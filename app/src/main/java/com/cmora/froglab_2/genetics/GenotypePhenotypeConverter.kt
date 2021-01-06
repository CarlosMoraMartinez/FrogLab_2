package com.cmora.froglab_2.genetics

import android.util.Log

class GenotypePhenotypeConverter {
    companion object{
        fun getPhenotype(hap1: Haplotype, hap2: Haplotype, genome: GenomeModel): Phenotype{
            var phenotype = mutableListOf<String>()
            var s: String
            Log.d("GPConverter", "Called without Base Genotype")
            for(i in 0 until genome.num_genes){
                 when(genome.genes[i].inheritanceType){
                     InheritanceType.DOMINANT_RECESSIVE -> s = this.DominantRecessivePhenotype(hap1[i], hap2[i], genome.genes[i])
                     InheritanceType.SEX_DOMINANT -> s = this.SexDominant(hap1[i], hap2[i], genome.genes[i])
                     InheritanceType.SEX_RECESSIVE -> s = this.SexRecessive(hap1[i], hap2[i], genome.genes[i])
                     InheritanceType.CHROMOSOME_Y_OR_W -> s = this.ChromosomeYW(hap1[i], hap2[i], genome.genes[i])
                     InheritanceType.CODOMINANT -> s = this.Codominant(hap1[i], hap2[i], genome.genes[i])
                     InheritanceType.INTERMEDIATE -> s = this.Intermediate(hap1[i], hap2[i], genome.genes[i])
                 }
                phenotype.add(s)
            }
            Log.d("GPConverter", "Returning phenotype: $phenotype")
            return phenotype
        }
        fun getPhenotype(hap1: Haplotype, hap2: Haplotype, base_genotype: BaseGenotype): Phenotype{
            var phenotype = base_genotype.phenotype.toMutableList()
            var s: String
            var current_gene: Gene
            Log.d("GPConverter", "Called with Base Genotype")
            for(i in 0 until hap1.size){
                current_gene = base_genotype.genome.genes[base_genotype.editable_genes[i]]
                when(current_gene.inheritanceType){
                    InheritanceType.DOMINANT_RECESSIVE -> s = this.DominantRecessivePhenotype(hap1[i], hap2[i], current_gene)
                    InheritanceType.SEX_DOMINANT -> s = this.SexDominant(hap1[i], hap2[i], current_gene)
                    InheritanceType.SEX_RECESSIVE -> s = this.SexRecessive(hap1[i], hap2[i], current_gene)
                    InheritanceType.CHROMOSOME_Y_OR_W -> s = this.ChromosomeYW(hap1[i], hap2[i], current_gene)
                    InheritanceType.CODOMINANT -> s = this.Codominant(hap1[i], hap2[i], current_gene)
                    InheritanceType.INTERMEDIATE -> s = this.Intermediate(hap1[i], hap2[i], current_gene)
                }
                phenotype[current_gene.id] = s
            }
            Log.d("GPConverter", "Returning phenotype: $phenotype")
            return phenotype
        }
        fun DominantRecessivePhenotype(allele1: Int, allele2: Int, gene: Gene):String{
            Log.d("GPConverter", "allele1: $allele1, allele2: $allele2, DominantRecessivePhenotype: $gene")
            if(gene.allele_priority[allele1] < gene.allele_priority[allele2])
                return gene.possible_phenotypes[allele1]
            return gene.possible_phenotypes[allele2]
        }
        fun SexRecessive(allele1: Int, allele2: Int, gene: Gene): String{
            Log.d("GPConverter", "SexRecessive: $gene")
            if(allele1 >= 0 && allele2 >= 0)
               return DominantRecessivePhenotype(allele1, allele2, gene)
            return gene.possible_phenotypes[0]
        }
        fun SexDominant(allele1: Int, allele2: Int, gene: Gene): String{
            Log.d("GPConverter", "SexDominant: $gene, allele1: $allele1, allele2: $allele2")
            if(allele1 >= 0 && allele2 >= 0)
                return DominantRecessivePhenotype(allele1, allele2, gene)
            return gene.possible_phenotypes[allele1]
        }
        fun ChromosomeYW(allele1: Int, allele2: Int, gene: Gene): String {
            Log.d("GPConverter", "ChromosomeYW: $gene")
            if(allele1 >= 0)
                return gene.possible_phenotypes[allele1]
            return gene.possible_phenotypes[0]
        }
        fun Codominant(allele1: Int, allele2: Int, gene: Gene): String {
            Log.d("GPConverter", "Codominant: $gene")
            if(allele1 < allele2)
                return gene.possible_phenotypes[allele1] + "-" + gene.possible_phenotypes[allele2]
            return gene.possible_phenotypes[allele2] + "-" + gene.possible_phenotypes[allele1]
        }
        fun Intermediate(allele1: Int, allele2: Int, gene: Gene): String {
            Log.d("GPConverter", "Intermediate: $gene")
            if(allele1 < allele2)
                return gene.possible_phenotypes[allele1] + gene.possible_phenotypes[allele2]
            return gene.possible_phenotypes[allele2] + gene.possible_phenotypes[allele1]
        }
    }
}