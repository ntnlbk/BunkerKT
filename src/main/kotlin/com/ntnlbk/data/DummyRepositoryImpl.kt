package com.ntnlbk.data

import com.ntnlbk.domain.DummyRepository

class DummyRepositoryImpl: DummyRepository {
    override fun dummyMethod(name: String): String {
        return "hello, $name!"
    }
}