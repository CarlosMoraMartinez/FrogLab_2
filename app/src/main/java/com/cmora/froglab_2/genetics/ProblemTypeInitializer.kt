package com.cmora.froglab_2.genetics

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

/*
 {
  "name": "problems of type 1",
  "genome":"simple_genome_1",
  "description": "Problems of classical Mendelian inheritance, with one dominant and one recessive allele",
  "base_haplotype1": ["A", "A", "A", "A", "A", "A", "A", "A", "A", "A"],
  "base_haplotype2": ["A", "A", "A", "A", "A", "A", "A", "A", "A", "A"],
  "problem_genes": ["gene1", "gene2", "gene3", "gene4", "gene5", "gene6"],
  "problem_father_haplotype1": ["A", "A", "A", "A", "A", "A"],
  "problem_father_haplotype2": ["a", "a", "a", "a", "a", "a"],
  "problem_mother_haplotype1": ["A", "A", "A", "A", "A", "A"],
  "problem_mother_haplotype2": ["a", "a", "a", "a", "a", "a"]
}
* */

class ProblemTypeInitializer {
    companion object {
        fun buildProblemTypeFromJSON(problemtypestr: String, genome: GenomeModel): ProblemType{
            Log.d("ProblemTypeInitializer", "1: Building problem type from .json")
            Log.d("ProblemTypeInitializer", problemtypestr)

            val jsonobj = JSONObject(problemtypestr)
            var name : String = jsonobj.getString("name")
            var description : String = jsonobj.getString("description")
            var hap1: Haplotype = mutableListOf()
            var hap2: Haplotype = mutableListOf()
            var fhap1: Haplotype = mutableListOf()
            var fhap2: Haplotype = mutableListOf()
            var mhap1: Haplotype = mutableListOf()
            var mhap2: Haplotype = mutableListOf()
            var genelist: MutableList<Int> = mutableListOf()
            Log.d("ProblemTypeInitializer", "2: Variables declared")
            var aux_array = jsonobj.getJSONArray("base_haplotype1")
            Log.d("ProblemTypeInitializer", "3: got haplotype1")
            for(i in 0 until aux_array.length()) {
                hap1.add(genome.getAlleleIdByName(i, aux_array.getString(i)))
            }
            Log.d("ProblemTypeInitializer", "4: converted haplotype 1")
            aux_array = jsonobj.getJSONArray("base_haplotype2")
            for(i in 0 until aux_array.length()) {
                hap2.add(genome.getAlleleIdByName(i, aux_array.getString(i)))
            }
            Log.d("ProblemTypeInitializer", "5")
            aux_array = jsonobj.getJSONArray("problem_genes")
            for(i in 0 until aux_array.length()) {
                genelist.add(genome.getGeneIdByName(aux_array.getString(i)))
            }
            Log.d("ProblemTypeInitializer", "6")
            aux_array = jsonobj.getJSONArray("problem_father_haplotype1")
            for(i in 0 until aux_array.length()) {
                fhap1.add(genome.getAlleleIdByName(genelist[i], aux_array.getString(i)))
            }
            Log.d("ProblemTypeInitializer", "7")
            aux_array = jsonobj.getJSONArray("problem_father_haplotype2")
            for(i in 0 until aux_array.length()) {
                fhap2.add(genome.getAlleleIdByName(genelist[i], aux_array.getString(i)))
            }
            Log.d("ProblemTypeInitializer", "8")
            aux_array = jsonobj.getJSONArray("problem_mother_haplotype1")
            for(i in 0 until aux_array.length()) {
                mhap1.add(genome.getAlleleIdByName(genelist[i], aux_array.getString(i)))
            }
            Log.d("ProblemTypeInitializer", "9")
            aux_array = jsonobj.getJSONArray("problem_mother_haplotype2")
            for(i in 0 until aux_array.length()) {
                mhap2.add(genome.getAlleleIdByName(genelist[i], aux_array.getString(i)))
            }
            Log.d("ProblemTypeInitializer", "10")

            var gene_groups: MutableList<MutableList<Int>>? = null
            if(jsonobj.has("gene_groups")){
                Log.d("ProblemTypeInitializer", "10b: has gene_groups")
                aux_array = jsonobj.getJSONArray("gene_groups")
                Log.d("ProblemTypeInitializer", "10c: aux_array_len: ${aux_array.length()}")
                gene_groups = mutableListOf()
                var group: MutableList<Int>
                var aux_array2 : JSONArray
                for(i in 0 until aux_array.length()){
                    aux_array2 = aux_array.getJSONArray(i)
                    group = mutableListOf()
                    for(j in 0 until aux_array2.length())
                        group.add(genome.getGeneIdByName(aux_array2.getString(j)))
                    Log.d("ProblemTypeInitializer", "10d: group: $group")
                    gene_groups.add(group.toMutableList())
                    Log.d("ProblemTypeInitializer", "10e: gene_groups: $gene_groups")
                }
            }
            Log.d("ProblemTypeInitializer", "11: gene_groups:${gene_groups}")
            var problem_type = ProblemType(
                    name, description, genome,
                    hap1, hap2, genelist,
                    fhap1, fhap2, mhap1, mhap2,
                    gene_groups
                )

            Log.d("ProblemTypeInitializer", "12: problem_type initialized. Returning")

            return problem_type
        }
    }
}