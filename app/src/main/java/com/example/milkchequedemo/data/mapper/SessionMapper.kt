package com.example.milkchequedemo.data.mapper

import com.example.milkchequedemo.data.dto.SessionRequestDto
import com.example.milkchequedemo.data.dto.SessionResponseDto
import com.example.milkchequedemo.domain.model.Session
import com.example.milkchequedemo.domain.model.SessionResponse

fun Session.toDto()=
    SessionRequestDto(
        customerDOB = "2004-12-27",
        customerPassword = "122",
        firstName = userName,
        lastName = "nine",
        phone = phone,
        storeId = storeId,
        tableId = tableId

    )

fun SessionResponseDto.toDomain() = SessionResponse(
    code = code, message = message, token = token
)