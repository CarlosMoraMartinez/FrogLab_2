package com.cmora.froglab_2.genetics

import android.util.Log
import kotlin.random.Random

class GeneticOperator {
    companion object{
        val MIN_OFFSPRING = 6
        val MAX_OFFSPRING = 20
        fun getWZoffspring(female: Individual, male: Individual, fam_id: Int = 0, start_ind: Int = 0): MutableList<Individual>{
            Log.d("GENETICS", "Getting WZ offspring")
            val offspring: MutableList<Individual> = mutableListOf()
            val n =  Random.nextInt(this.MIN_OFFSPRING, this.MAX_OFFSPRING + 1)
            val sex = List<Sex>(n){Sex.values()[Random.nextInt(0, 2)]}
            var hap1: Haplotype
            var hap2: Haplotype
            var current_chromosome: String = ""
            var current_position: Double = 0.0
            var current_male_haplotype: Int
            var current_female_haplotype: Int
            var gene_ind: Int
            var allele: Int
            var auxind: Individual

            Log.d("GENETIC_OPERATOR", "WZ. Offspring size: " + n.toString() + "\nSex:\n" + sex.toString())
            for(i in 0 until n){
                hap1 = mutableListOf()
                hap2 = mutableListOf()
                current_chromosome = ""
                current_female_haplotype = Random.nextInt(0, 2)
                current_male_haplotype = Random.nextInt(0, 2)
                for(j in 0 until female.base_genotype.editable_genes.size){
                    gene_ind = female.base_genotype.editable_genes[j]
                    if(current_chromosome != female.base_genotype.genome.genes[gene_ind].chromosome){
                        current_chromosome = female.base_genotype.genome.genes[gene_ind].chromosome
                        current_position = 0.0
                        current_female_haplotype = Random.nextInt(0, 2)
                        current_male_haplotype = Random.nextInt(0, 2)
                    }
                    if(female.base_genotype.genome.chromosomes[current_chromosome]?.type == ChromosomeType.AUTOSOMAL) {
                        //Recombination in the female germ line
                        if (Random.nextDouble() < (female.base_genotype.genome.genes[gene_ind].position - current_position)) {
                            current_female_haplotype = (++current_female_haplotype).rem(2)
                        }
                        //Recombination in the male germ line
                        if (Random.nextDouble() < (male.base_genotype.genome.genes[gene_ind].position - current_position)) {
                            current_male_haplotype = (++current_male_haplotype).rem(2)
                        }
                        allele =
                            if (current_female_haplotype == 0) female.haplotype1[j] else female.haplotype2[j]
                        hap1.add(allele)
                        allele =
                            if (current_male_haplotype == 0) male.haplotype1[j] else male.haplotype2[j]
                        hap2.add(allele)
                    }else if(current_chromosome == "chrZ" && sex[i] == Sex.MALE){
                        if (Random.nextDouble() < (male.base_genotype.genome.genes[gene_ind].position - current_position)) {
                            current_male_haplotype = (++current_male_haplotype).rem(2)
                        }
                        allele =
                            if (current_male_haplotype == 0) male.haplotype1[j] else male.haplotype2[j]
                        hap1.add(allele)
                        hap2.add(female.haplotype1[j])
                    }else if(current_chromosome == "chrZ" && sex[i] == Sex.FEMALE){
                        if (Random.nextDouble() < (male.base_genotype.genome.genes[gene_ind].position - current_position)) {
                            current_male_haplotype = (++current_male_haplotype).rem(2)
                        }
                        allele =
                            if (current_male_haplotype == 0) male.haplotype1[j] else male.haplotype2[j]
                        hap1.add(allele)
                        hap2.add(-1)
                    }else if(current_chromosome == "chrW" && sex[i] == Sex.MALE){
                        hap1.add(-1)
                        hap2.add(-1)
                    }else if(current_chromosome == "chrW" && sex[i] == Sex.FEMALE){
                        hap1.add(female.haplotype1[j])
                        hap2.add(-1)
                    }
                    current_position = male.base_genotype.genome.genes[gene_ind].position
                }
                /*val id: Int, val family: Int, val base_genotype: BaseGenotype,
                 val haplotype1: Haplotype, val haplotype2: Haplotype,
                 val sex: Sex*/
                auxind = Individual(start_ind + i, fam_id, female.base_genotype, hap1, hap2, sex[i])
                offspring.add(auxind)
                Log.d("GENETIC_OPERATOR", "WZ. Individual $i added: " + offspring[i].toString())
            }
            return offspring
        }
        fun getXYoffspring(female: Individual, male: Individual, fam_id: Int = 0, start_ind: Int = 0): MutableList<Individual>{
            val offspring: MutableList<Individual> = mutableListOf()
            return offspring
        }
        fun getX0offspring(female: Individual, male: Individual, fam_id: Int = 0, start_ind: Int = 0): MutableList<Individual>{
            val offspring: MutableList<Individual> = mutableListOf()
            return offspring
        }
        fun getNoSexOffspring(female: Individual, male: Individual, fam_id: Int = 0, start_ind: Int = 0): MutableList<Individual>{
            val offspring: MutableList<Individual> = mutableListOf()
            return offspring
        }
    }
}