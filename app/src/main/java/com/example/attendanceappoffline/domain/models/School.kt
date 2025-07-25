package com.example.attendanceappoffline.domain.models

import com.google.gson.annotations.SerializedName

data class SchoolApiResponse(
    val data: SchoolResponse
)

data class SchoolResponse(
    @SerializedName("_id") val id: String? = null,
    val name: String? = null,
    val principalName: String? = null,
    val principalPhone: String? = null,
    val email: String? = null,
    val address: String? = null,
    val state: State? = null,
    val district: String? = null,
    val block: String? = null,
    val region: String? = null,
    val city: String? = null,
    val classes: List<SchoolClass>? = null,
    val piBoxInstalled: Int = 0,
    val clickerInstalled: Int = 0,
    val recieversInstalled: Int = 0,
    val registeredNumbers: List<String>? = null,
    val packageType: String? = null,
    val installationDate: String? = null,
    val subscriptionValidTill: String? = null,
    @SerializedName("__v") val version: Int = 0
)

data class State(
    @SerializedName("_id") val id: String? = null,
    val name: String? = null,
    val districts: List<District>? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    @SerializedName("__v") val version: Int = 0
)

data class District(
    @SerializedName("_id") val id: String? = null,
    val name: String? = null,
    val blocks: List<Block>? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class Block(
    @SerializedName("_id") val id: String? = null,
    val name: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class SchoolClass(
    val name: String? = null,
    val subjects: List<Subject>? = null
)

data class Subject(
    val name: String? = null,
    val chapters: List<Chapter>? = null
)

data class Chapter(
    val name: String? = null,
    val topics: List<String>? = null,
    val status: String? = null
)
