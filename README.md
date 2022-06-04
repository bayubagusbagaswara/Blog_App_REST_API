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


CascadeType.PERSIST : cascade type presist means that save() or persist() operations cascade to related entities.
CascadeType.MERGE : cascade type merge means that related entities are merged when the owning entity is merged.
CascadeType.REFRESH : cascade type refresh does the same thing for the refresh() operation.
CascadeType.REMOVE : cascade type remove removes all related entities association with this setting when the owning entity is deleted.
CascadeType.DETACH : cascade type detach detaches all related entities if a “manual detach” occurs.
CascadeType.ALL : cascade type all is shorthand for all of the above cascade operations.

The orphanRemoval option was introduced in JPA 2.0. This provides a way to delete orphaned entities from the database.
While CascadeType.REMOVE is a way to delete a child entity or entities whenever the deletion of its parent happens.