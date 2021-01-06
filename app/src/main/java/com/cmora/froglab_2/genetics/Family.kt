package com.cmora.froglab_2.genetics

import android.util.Log

class Family(val id: Int, val generation: Int, val female_parent: Individual, val male_parent: Individual) {
    var offspring: MutableList<Individual> = mutableListOf()
    var name = ""
    val MALE_UNICODE = "\u2642"
    val FEMALE_UNICODE = "\u2640"
    init{
        name = "F$generation:$id"
        Log.d("FAMILY", "init: fem_id=${female_parent.id}, fem_sex=${female_parent.sex}, mal_id=${male_parent.id}, mal_sex=${male_parent.sex}")
    }
    fun makeOffspring(individual_ind: Int){
        when(female_parent.base_genotype.genome.sex_determination){
            SexDeterminationSystem.XY -> this.offspring = GeneticOperator.getXYoffspring(this.female_parent, this.male_parent, this.id, individual_ind)
            SexDeterminationSystem.WZ -> this.offspring = GeneticOperator.getWZoffspring(this.female_parent, this.male_parent, this.id, individual_ind)
            SexDeterminationSystem.X0 -> this.offspring = GeneticOperator.getX0offspring(this.female_parent, this.male_parent, this.id, individual_ind)
            SexDeterminationSystem.NO_SEX -> this.offspring = GeneticOperator.getNoSexOffspring(this.female_parent, this.male_parent, this.id)
        }
        var fam = getNameAsString()
        for(ind: Individual in offspring){
            ind.user_name = fam + '.' + ind.id.toString()
        }
    }

    fun getMalePhenotypeAsString():String{
        return MALE_UNICODE + this.male_parent.getAllPhenotypes()
    }
    fun getFemalePhenotypeAsString():String{
        return FEMALE_UNICODE + this.female_parent.getAllPhenotypes()
    }
    fun getNameAsString(): String{
        return "F" + this.generation.toString() + "." + this.id.toString()
    }
    fun getMaleNameAsString():String{
        return this.male_parent.getNameAsString2()
    }
    fun getFemaleNameAsString():String{
        return this.female_parent.getNameAsString2()
    }
    fun getMaleId():Int{
        return this.male_parent.id
    }
    fun getFemaleId():Int{
        return this.female_parent.id
    }
    fun getNumOffspring():Int{
        return this.offspring.size
    }
    override fun toString(): String {
        var s: String = "Family(id=$id,\nFemale parent:\n" + female_parent.toString() + "\n" +
                "Male parent:\n" + male_parent.toString() + "\n"
        for(i:Int in 0 until offspring.size){
            s += i.toString() + ": " + offspring[i].toString() + "\n"
        }
        return s
    }

}