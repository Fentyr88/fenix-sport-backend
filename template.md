├── domain/
│   ├── model/
│   │   └── Product.java
│   ├── exception/
│   │   └── DomainException.java
│   └── service/ (opcional)
│
├── application/
│   ├── port/
│   │   ├── input/
│   │   │   └── ProductUseCase.java
│   │   └── output/
│   │       └── ProductRepositoryPort.java
│   │
│   ├── service/
│   │   └── ProductService.java
│   │
│   └── dto/
│       ├── request/
│       └── response/
│
├── infrastructure/
│   ├── adapter/
│   │   ├── input/
│   │   │   └── rest/
│   │   │       └── ProductController.java
│   │   │
│   │   └── output/
│   │       └── persistence/
│   │           ├── entity/
│   │           │   └── ProductEntity.java
│   │           ├── mapper/
│   │           │   └── ProductMapper.java
│   │           ├── repository/
│   │           │   └── JpaProductRepository.java
│   │           └── adapter/
│   │               └── ProductPersistenceAdapter.java
│
│   ├── config/
│   │   └── BeanConfig.java
│   │
│   └── exception/
│       └── GlobalExceptionHandler.java
│
├── test/
│   ├── unit/
│   ├── integration/