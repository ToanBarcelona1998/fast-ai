package com.example.repository.interfaces

import com.example.domain.models.entity.Model
import com.example.domain.models.requests.ModelAddRequest

interface IModelRepository : IAddRepository<Model,ModelAddRequest> , IGetListRepository<Model,Int> , IGetRepository<Model,Int>{
}