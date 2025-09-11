package com.example.milkchequedemo.domain.usecase

import com.example.milkchequedemo.domain.model.MenuItem
import com.example.milkchequedemo.domain.model.Session
import com.example.milkchequedemo.domain.model.SessionResponse
import com.example.milkchequedemo.domain.repository.SessionRepository
import com.example.milkchequedemo.utils.ResponseWrapper
import jakarta.inject.Inject

class CreateSessionUseCase
@Inject constructor(
    private val sessionRepository: SessionRepository
){
    suspend operator fun invoke(session: Session): ResponseWrapper<SessionResponse> = sessionRepository.initSession(
        session
    )
}