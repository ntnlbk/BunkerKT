package com.ntnlbk.domain

class DummyUseCase (private val repository: DummyRepository) {
    operator fun invoke(name: String) = repository.dummyMethod(name)
}