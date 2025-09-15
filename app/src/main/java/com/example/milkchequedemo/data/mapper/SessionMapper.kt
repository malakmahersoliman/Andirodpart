package com.example.milkchequedemo.data.mapper

import com.example.milkchequedemo.data.dto.SessionRequestDto
import com.example.milkchequedemo.data.dto.SessionResponseDto
import com.example.milkchequedemo.domain.model.Session
import com.example.milkchequedemo.domain.model.SessionResponse
import java.util.UUID

fun Session.toDto()=
    SessionRequestDto(
        firstName = userName,
        customerEmail = mail,
        storeId = storeId,
        tableId = tableId
    )

fun SessionResponseDto.toDomain() = SessionResponse(
    code = code, message = message, token = UUID.randomUUID().toString(), customerId = customerId, sessionId = sessionId,
)