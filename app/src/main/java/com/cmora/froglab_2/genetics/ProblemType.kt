package com.cmora.froglab_2.genetics

import android.util.Log
import kotlin.random.Random

class ProblemType(val name:String, val description:String,
                  val genome: GenomeModel,
                  base_haplotype1 :Haplotype,
                  base_haplotype2 :Haplotype,
                  problem_genes:MutableList<Int>,
                  var father_hap1: Haplotype,
                  var father_hap2:Haplotype,
                  var mother_hap1:Haplotype,
                  var mother_hap2:Haplotype,
                  gene_groups: MutableList<MutableList<Int>>?=null){
    val base_genotype = BaseGenotype(base_haplotype1, base_haplotype2, problem_genes, genome)
    var gene_in_problem = MutableList<Boolean>(genome.num_genes){false}
    var num_groups = 1
    var gene_groups: MutableList<MutableList<Int>>? = null//(1){problem_genes}
    init{
        for(g in problem_genes)
            gene_in_problem[g] = true
        this.gene_groups = gene_groups
        if (gene_groups != null) {
            Log.d("PROBLEM_TYPE", "init: gene groups==null")
            num_groups = gene_groups.size
        }
        Log.d("PROBLEM_TYPE", "init: end")
    }
    fun getProblemInstance():ProblemInstance{
        Log.d("PROBLEM_TYPE", "Getting problem instance")
        if(this.gene_groups != null) {
            Log.d("PROBLEM_TYPE", "Gene Groups Not Null: ${this.gene_groups}")
            return this.getProblemInstance_groups(this.gene_groups!!)
        }
        Log.d("PROBLEM_TYPE", "Gene Groups Null")
        return this.getProblemInstance_single()
    }
    fun getProblemInstance_single():ProblemInstance{
        Log.d("PROBLEM_TYPE", "single 1")
        val problem_gene_ind = Random.nextInt(0, this.base_genotype.editable_genes.size)
        val problem_gene = this.base_genotype.editable_genes[problem_gene_ind]
        Log.d("PROBLEM_TYPE", "gene:$problem_gene, gene_ind:$problem_gene_ind, ed_genes_${this.base_genotype.editable_genes}")
        val newBaseGenotype= BaseGenotype(this.base_genotype, problem_gene)
        Log.d("PROBLEM_TYPE", "newBaseGenotype: $newBaseGenotype")
        val female = Individual(0, -1, newBaseGenotype,
            MutableList(1){mother_hap1[problem_gene_ind]},
            MutableList(1){mother_hap2[problem_gene_ind]}, Sex.FEMALE)
        Log.d("PROBLEM_TYPE", "female: $female")
        val male = Individual(1, -1, newBaseGenotype,
            MutableList(1){father_hap1[problem_gene_ind]},
            MutableList(1){father_hap2[problem_gene_ind]}, Sex.MALE)
        Log.d("PROBLEM_TYPE", "male: $male")
        val family = Family(0, 1, female, male)
        Log.d("PROBLEM_TYPE", "family: $family")
        val problem = ProblemInstance(family, problem_gene)
        Log.d("PROBLEM_TYPE", "returning problem: $problem")
        return problem
    }
    fun getProblemInstance_groups(groups: MutableList<MutableList<Int>>):ProblemInstance{
        Log.d("PROBLEM_TYPE", "groups start")
        val problem_group = Random.nextInt(0, groups.size)
        Log.d("PROBLEM_TYPE", "groups: $groups")
        Log.d("PROBLEM_TYPE", "prob_group: $problem_group")
        val problem_genes = groups[problem_group]
        Log.d("PROBLEM_TYPE", "problem_genes: $problem_genes")
        Log.d("PROBLEM_TYPE", "editable_genes: ${base_genotype.editable_genes}")
        val problem_gene_inds: MutableList<Int> = mutableListOf() //Inds of editable genes in haplotypes
        for(i in 0 until problem_genes.size) {
            for (j in 0 until this.base_genotype.editable_genes.size) {
                if (problem_genes[i] == this.base_genotype.editable_genes[j]) {
                    problem_gene_inds.add(j)
                    break
                }
            }
        }
        Log.d("PROBLEM_TYPE", "problem_gene_inds: $problem_gene_inds")
        val newBaseGenotype = BaseGenotype(this.base_genotype, problem_genes)
        val hapf1: Haplotype = mutableListOf()
        val hapf2: Haplotype = mutableListOf()
        val hapm1: Haplotype = mutableListOf()
        val hapm2: Haplotype = mutableListOf()
        for(i: Int in problem_gene_inds){
            hapf1.add(mother_hap1[i])
            hapf2.add(mother_hap2[i])
            hapm1.add(father_hap1[i])
            hapm2.add(father_hap2[i])
        }
        Log.d("PROBLEM_TYPE", "hapf: ${hapf1.toString()}, ${hapf2.toString()}, ${hapm1.toString()}, ${hapm2.toString()}")
        Log.d("PROBLEM_TYPE", "newBaseGenotype: $newBaseGenotype")
        val female = Individual(0, -1, newBaseGenotype, hapf1, hapf2, Sex.FEMALE)
        Log.d("PROBLEM_TYPE", "female: $female")
        val male = Individual(1, -1, newBaseGenotype, hapm1, hapm2, Sex.MALE)
        Log.d("PROBLEM_TYPE", "male: $male")
        val family = Family(0, 1, female, male)
        Log.d("PROBLEM_TYPE", "family: $family")
        val problem = ProblemInstance(family, -1)
        Log.d("PROBLEM_TYPE", "returning problem: $problem")
        return problem
    }
    override fun toString(): String {
        return "ProblemType(name='$name', description='$description', " +
                "genome=${genome.name}, \nfather_hap1=$father_hap1, father_hap2=$father_hap2, " +
                "\nmother_hap1=$mother_hap1, mother_hap2=$mother_hap2, \nbase_genotype=$base_genotype, " +
                "gene_in_problem=$gene_in_problem, \ngroups=$gene_groups)"
    }


}