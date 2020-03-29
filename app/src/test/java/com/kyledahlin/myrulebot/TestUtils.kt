package com.kyledahlin.myrulebot

import com.kyledahlin.myrulebot.backend.NameAndId

fun generateNameIds(size: Int): List<NameAndId> {
    return (0 until size).map { NameAndId(it.toString(), it.toString()) }
}