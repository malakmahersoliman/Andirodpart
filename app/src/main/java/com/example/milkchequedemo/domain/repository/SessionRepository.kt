package com.example.milkchequedemo.domain.repository

import com.example.milkchequedemo.domain.model.Session
import com.example.milkchequedemo.domain.model.SessionResponse
import com.example.milkchequedemo.utils.ResponseWrapper

interface SessionRepository {

    suspend fun initSession(session: Session): ResponseWrapper<SessionResponse>
}