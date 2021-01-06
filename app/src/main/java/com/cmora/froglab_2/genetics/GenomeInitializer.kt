package com.cmora.froglab_2.genetics

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject


class GenomeInitializer {
    companion object{
        fun buildGenomeFromJSON(genomestr: String): GenomeModel{
        Log.d("GENOME", "Building genome from .json")
        //Log.d("GENOME", "String read:")
        //Log.d("GENOME", genomestr)
        val jsonobj = JSONObject(genomestr)
        var genes_added = 0
        Log.d("GENOME", "JSON object initialized")
        val jsonchroms = jsonobj.getJSONArray("chromosomes")
        Log.d("GENOME", "chromosomes obtained")
        Log.d("GENOME", jsonchroms.length().toString())
        //Aux variables
        var json_chr: JSONObject
        var json_chromgenes: JSONArray
        var json_gene: JSONObject
        var aux_json_array: JSONArray
        var alleles: MutableList<String>
        var allele_priorities: MutableList<Int>
        var extra_resources: MutableList<String> = mutableListOf()
        var epistatic_upon: MutableList<String>
        var epistatic_under: MutableList<String>
        var possible_phenotypes: MutableList<String>
        var resource_drawable: MutableList<String>
        var gene_load_order: MutableList<Int> = mutableListOf()


        aux_json_array = jsonobj.getJSONArray("gene_load_order")
        for(i in 0 until aux_json_array.length())
            gene_load_order.add(aux_json_array.getInt(i))

        aux_json_array = jsonobj.getJSONArray("extra_resources")
        for(i in 0 until aux_json_array.length())
            extra_resources.add(aux_json_array.getString(i))


            var genome : GenomeModel = GenomeModel(jsonobj.getString("name"),
            SexDeterminationSystem.valueOf(jsonobj.getString("sex_determination")),
            jsonobj.getInt("num_autosomes"),
            jsonobj.getInt("num_sex_chromosomes"),
            jsonobj.getInt("num_genes"),
            jsonobj.getInt("ploidy"),
            jsonobj.getString("about"),
            gene_load_order, extra_resources)
        Log.d("GENOME", "Genome object initialized")
        for(i in 0 until jsonchroms.length()){
            json_chr = jsonchroms.getJSONObject(i)
            Log.d("GENOME", "Chromosome init")
            //Chromosome(val name: String, val type: ChromosomeType, val num_genes: Int)
            genome.addChromosome(Chromosome(json_chr.getString("name"),
                                 ChromosomeType.valueOf(json_chr.getString("type")),
                                 json_chr.getInt("num_genes")
            ))
            Log.d("GENOME", "Chromosome added: $json_chr.getString(\"name\")")

            json_chromgenes = json_chr.getJSONArray("genes")
            Log.d("GENOME", "Got genes")
            for(g in 0 until json_chromgenes.length()){
                json_gene = json_chromgenes.getJSONObject(g)
                Log.d("GENOME", "Got gene $genes_added, $json_gene.getString(\"name\")")
                aux_json_array = json_gene.getJSONArray("alleles")
                alleles = mutableListOf<String>()
                for(j in 0 until aux_json_array.length()){alleles.add(aux_json_array.getString(j)) }
                Log.d("GENOME", "Got alleles")

                aux_json_array = json_gene.getJSONArray("allele_priority")
                allele_priorities = mutableListOf<Int>()
                for(j in 0 until aux_json_array.length()){allele_priorities.add(aux_json_array.getInt(j)) }
                Log.d("GENOME", "Got allele_priorities")

                aux_json_array = json_gene.getJSONArray("epistatic_upon")
                epistatic_upon = mutableListOf<String>()
                for(j in 0 until aux_json_array.length()){epistatic_upon.add(aux_json_array.getString(j)) }
                Log.d("GENOME", "Got epistatic_upon")

                aux_json_array = json_gene.getJSONArray("epistatic_under")
                epistatic_under = mutableListOf<String>()
                for(j in 0 until aux_json_array.length()){epistatic_under.add(aux_json_array.getString(j)) }
                Log.d("GENOME", "Got epistatic_under")

                aux_json_array = json_gene.getJSONArray("possible_phenotypes")
                possible_phenotypes = mutableListOf<String>()
                for(j in 0 until aux_json_array.length()){possible_phenotypes.add(aux_json_array.getString(j)) }
                Log.d("GENOME", "Got possible_phenotypes")

                aux_json_array = json_gene.getJSONArray("phenotype_resources")
                resource_drawable = mutableListOf<String>()
                for(j in 0 until aux_json_array.length()){resource_drawable.add(aux_json_array.getString(j)) }
                Log.d("GENOME", "Got resource_drawable")

                genome.addGene(Gene(genes_added, json_gene.getString("name"),
                    json_chr.getString("name"),
                    InheritanceType.valueOf(json_gene.getString("inheritance")),
                    alleles,
                    json_gene.getDouble("position"),
                    allele_priorities,
                    epistatic_upon,
                    epistatic_under,
                    possible_phenotypes,
                    resource_drawable
                ))
                ++genes_added
                Log.d("GENOME", "Gene added")
            }
        }
        val consistent = genome.checkValidity()
        //Log.d("GENOME", genome.toString())
        //for(g : Gene in genome.genes){Log.d("GENES:", g.toString())}
        if(consistent != "")
            Log.e("GENOME", consistent)
        return genome
    }
}}