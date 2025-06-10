package cz.jpcz.genesisresources.repository;

import cz.jpcz.genesisresources.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByPersonId(String personId);
}
