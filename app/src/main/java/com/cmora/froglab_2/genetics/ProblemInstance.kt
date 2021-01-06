package com.cmora.froglab_2.genetics

import android.util.Log

class ProblemInstance(family: Family, val problem_gene: Int=-1) {
    val individuals: MutableList<Individual> = mutableListOf()
    val families: MutableList<Family> = mutableListOf()
    var solved = false
    var points = 0
    var genotypes_known: MutableList<Boolean> = mutableListOf()
    init {
        Log.d("PROBLEM_INSTANCE", "start init")
        family.makeOffspring(2)
        individuals.add(family.female_parent)
        individuals.add(family.male_parent)
        individuals.addAll(family.offspring)
        families.add(family)
        genotypes_known = MutableList(individuals.size){false}
        Log.d("PROBLEM_INSTANCE", "end init")
    }
    fun addGeneration(female:Int, male:Int):Boolean{
        Log.d("PROBLEM_INSTANCE", "addGeneration start. Creating family ${families.size}")
        if(male >= individuals.size || female >= individuals.size)
            return false
        if(individuals[male].sex != Sex.MALE || individuals[female].sex != Sex.FEMALE)
            return false
        if(individuals[male].family != individuals[female].family)
            return false
        val generation = if(individuals[female].family >= 0)families[individuals[female].family].generation + 1 else 1
        val newfam = Family(families.size,
            generation,
            individuals[female], individuals[male])
        newfam.makeOffspring(individuals.size)
        individuals.addAll(newfam.offspring)
        families.add(newfam)
        Log.d("PROBLEM_INSTANCE", "addGeneration end. Family ${families.size} (generation ${newfam.generation}) has ${newfam.offspring.size} offspring")
        return true
    }
    fun checkGenotype(ind: Int, test_genotype:String, test_gene:Int = problem_gene):Boolean{
        if(problem_gene == -1)
            return false
        return this.individuals[ind].getGenotype(test_gene) == test_genotype
    }

    override fun toString(): String {
        return "ProblemInstance(problem_gene=$problem_gene, individuals=$individuals, families=$families, solved=$solved, points=$points, genotypes_known=$genotypes_known)"
    }


}