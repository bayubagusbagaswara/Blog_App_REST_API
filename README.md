# Spring Boot Blog Web Application REST API

Instant.now().toEpochMilli()

# Test User dulu
- controller user
- daftarkan user dulu atau menggunakan endpoint addUser
- test user harus menyertakan userDetails artinya kita menggunaka Authentication dan Authorization

# 

@Query("SELECT t FROM Transaction t WHERE t.property IN (?1) AND t.createdAt BETWEEN ?2 AND ?3 GROUP BY t.transactionType")
List<Transaction> getAllByPropertyAndDatesBetweenGroupedByTransactionType(Set<Property> property, Date dateFrom, Date dateTo);

@Query(value = "SELECT u FROM User u WHERE u.name IN :names")
List<User> findUserByNameList(@Param("names") Collection<String> names);

Set<User> findByIdIn(Set<Long> ids);