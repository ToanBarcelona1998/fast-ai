package com.example.repository.interfaces

import com.example.domain.models.entity.Package
import com.example.domain.models.requests.PackageAddRequest
import com.example.domain.models.requests.PackageGetAllRequest

interface IPackageRepository : IAddRepository<Package,PackageAddRequest> , IGetListRepository<Package , PackageGetAllRequest>{
}